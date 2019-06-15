package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.GearSoulItem;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.lib.collection.StackList;

public class ApplyGearSoulRecipe implements ICraftingRecipe {
    public static final ResourceLocation NAME = SilentGems.getId("apply_gear_soul");
    public static final Serializer SERIALIZER = new Serializer();

    public ApplyGearSoulRecipe() {}

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        StackList list = StackList.from(inv);
        ItemStack gear = list.uniqueOfType(ICoreItem.class);
        ItemStack gearSoul = list.uniqueOfType(GearSoulItem.class);
        GearSoul soul = SoulManager.getSoul(gearSoul);
        return !gear.isEmpty() && !gearSoul.isEmpty() && soul != null;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        StackList list = StackList.from(inv);
        ItemStack result = list.uniqueOfType(ICoreItem.class).copy();
        ItemStack gearSoul = list.uniqueOfType(GearSoulItem.class);

        // Got the gear soul
        GearSoul soul = SoulManager.getSoul(gearSoul);
        if (soul == null) return ItemStack.EMPTY;
        // Apply soul part to result
        // The part does not contain the soul data, but it needs to be attached regardless
        GearData.addUpgradePart(result, gearSoul);
        // Set the soul's actual data to the gear
        SoulManager.setSoul(result, soul);

        GearData.recalculateStats(result, ForgeHooks.getCraftingPlayer());
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ApplyGearSoulRecipe> {
        private Serializer() {}

        @Override
        public ApplyGearSoulRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new ApplyGearSoulRecipe();
        }

        @Override
        public ApplyGearSoulRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new ApplyGearSoulRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, ApplyGearSoulRecipe recipe) {}
    }
}
