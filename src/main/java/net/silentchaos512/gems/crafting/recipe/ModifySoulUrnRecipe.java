package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.urn.IUrnUpgradeItem;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.gems.lib.urn.UrnHelper;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;
import net.silentchaos512.lib.collection.StackList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ModifySoulUrnRecipe implements ICraftingRecipe {
    public static final ResourceLocation NAME = SilentGems.getId("modify_soul_urn");
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        StackList list = StackList.from(inv);
        ItemStack urn = list.uniqueMatch(ModifySoulUrnRecipe::isSoulUrn);
        Collection<ItemStack> mods = list.allMatches(ModifySoulUrnRecipe::isModifierItem);
        Collection<ItemStack> dyes = list.allMatches(s -> getDyeColor(s).isPresent());

        // For upgrade items, make sure the urn doesn't have it already
        for (ItemStack mod : mods) {
            if (mod.getItem() instanceof IUrnUpgradeItem) {
                IUrnUpgradeItem upgradeItem = (IUrnUpgradeItem) mod.getItem();

                List<UrnUpgrade> currentUpgrades = UrnUpgrade.ListHelper.load(urn);
                if (UrnUpgrade.ListHelper.contains(currentUpgrades, upgradeItem.getSerializer())) {
                    return false;
                }
            }
        }

        return !urn.isEmpty() && (list.size() == 1 || !mods.isEmpty() || !dyes.isEmpty());
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        StackList list = StackList.from(inv);
        ItemStack urn = list.uniqueMatch(ModifySoulUrnRecipe::isSoulUrn).copy();
        Collection<ItemStack> mods = list.allMatches(ModifySoulUrnRecipe::isModifierItem);
        Collection<ItemStack> dyes = list.allMatches(s -> getDyeColor(s).isPresent());

        // urn is a copy, so modify that directly
        if (mods.isEmpty() && dyes.isEmpty()) {
            // No modifier items, toggle between lidded and lidless version
            UrnHelper.toggleHasLid(urn);
        } else {
            mods.forEach(mod -> applyModifierItem(urn, mod));
            applyDyes(urn, dyes);
        }

        return urn;
    }

    private static boolean isSoulUrn(ItemStack stack) {
        return stack.getItem() == ModBlocks.soulUrn.asItem();
    }

    private static boolean isModifierItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof GemItem || item instanceof IUrnUpgradeItem;
    }

    private static void applyModifierItem(ItemStack urn, ItemStack mod) {
        SilentGems.LOGGER.debug("ModifySoulUrnRecipe#applyModifierItem: {}, {}", urn, mod);

        //noinspection ChainOfInstanceofChecks
        if (mod.getItem() instanceof GemItem) {
            Gems gem = Gems.from(mod);
            assert gem != null; // GemItem should not produce null
            UrnHelper.setGem(urn, gem);
        } else if (mod.getItem() instanceof IUrnUpgradeItem) {
            IUrnUpgradeItem upgradeItem = (IUrnUpgradeItem) mod.getItem();
            CompoundNBT urnSubcompound = urn.getOrCreateChildTag(UrnConst.NBT_ROOT);

            List<UrnUpgrade> list = UrnUpgrade.ListHelper.load(urnSubcompound);
            list.add(upgradeItem.getSerializer().deserialize(new CompoundNBT()));
            UrnUpgrade.ListHelper.save(list, urnSubcompound);
        }
    }

    // Largely copied from RecipesArmorDyes
    private static void applyDyes(ItemStack urn, Collection<ItemStack> dyes) {
        int[] componentSums = new int[3];
        int maxColorSum = 0;
        int colorCount = 0;

        int clayColor = UrnHelper.getClayColor(urn);
        if (clayColor != UrnConst.UNDYED_COLOR) {
            float r = (float) (clayColor >> 16 & 255) / 255.0F;
            float g = (float) (clayColor >> 8 & 255) / 255.0F;
            float b = (float) (clayColor & 255) / 255.0F;
            maxColorSum = (int) ((float) maxColorSum + Math.max(r, Math.max(g, b)) * 255.0F);
            componentSums[0] = (int) ((float) componentSums[0] + r * 255.0F);
            componentSums[1] = (int) ((float) componentSums[1] + g * 255.0F);
            componentSums[2] = (int) ((float) componentSums[2] + b * 255.0F);
            ++colorCount;
        }

        for (ItemStack dye : dyes) {
            float[] componentValues = getDyeColor(dye)
                    .orElse(DyeColor.WHITE)
                    .getColorComponentValues();
            int r = (int) (componentValues[0] * 255.0F);
            int g = (int) (componentValues[1] * 255.0F);
            int b = (int) (componentValues[2] * 255.0F);
            maxColorSum += Math.max(r, Math.max(g, b));
            componentSums[0] += r;
            componentSums[1] += g;
            componentSums[2] += b;
            ++colorCount;
        }

        if (colorCount > 0) {
            int r = componentSums[0] / colorCount;
            int g = componentSums[1] / colorCount;
            int b = componentSums[2] / colorCount;
            float maxAverage = (float) maxColorSum / (float) colorCount;
            float max = (float) Math.max(r, Math.max(g, b));
            r = (int) ((float) r * maxAverage / max);
            g = (int) ((float) g * maxAverage / max);
            b = (int) ((float) b * maxAverage / max);
            int finalColor = (r << 8) + g;
            finalColor = (finalColor << 8) + b;
            UrnHelper.setClayColor(urn, finalColor);
        }
    }

    private static Optional<DyeColor> getDyeColor(ItemStack dye) {
        return Optional.ofNullable(DyeColor.getColor(dye));
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModBlocks.soulUrn);
    }

    @Override
    public boolean isDynamic() {
        // Don't show in recipe book
        return true;
    }

    @Override
    public String getGroup() {
        return SilentGems.MOD_ID;
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ModifySoulUrnRecipe> {

        private Serializer() {}

        @Override
        public ModifySoulUrnRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new ModifySoulUrnRecipe();
        }

        @Override
        public ModifySoulUrnRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new ModifySoulUrnRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, ModifySoulUrnRecipe recipe) {}
    }
}
