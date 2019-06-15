package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ChaosGemItem;
import net.silentchaos512.gems.item.ChaosRuneItem;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.lib.collection.StackList;

import java.util.List;

public class ApplyChaosRuneRecipe implements ICraftingRecipe {
    public static final ResourceLocation NAME = SilentGems.getId("apply_chaos_rune");
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        StackList list = StackList.from(inv);
        // One and only one chaos gem
        ItemStack chaosGem = list.uniqueOfType(ChaosGemItem.class);
        // At least one rune
        int runes = list.countOfType(ChaosRuneItem.class);
        return !chaosGem.isEmpty() && runes > 0;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack chaosGem = ItemStack.EMPTY;
        List<ItemStack> runes = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ChaosRuneItem) {
                    // Any number of runes
                    runes.add(stack);
                } else {
                    // Only one chaos gem
                    if (!chaosGem.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    chaosGem = stack;
                }
            }
        }

        if (chaosGem.isEmpty() || runes.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = chaosGem.copy();
        for (ItemStack rune : runes) {
            IChaosBuff buff = ChaosRuneItem.getBuff(rune);
            if (buff == null || !ChaosGemItem.addBuff(result, buff)) {
                return ItemStack.EMPTY;
            }
        }
        return result;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        // Cannot determine
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public boolean isDynamic() {
        // Don't show in recipe book
        return true;
    }

    public static final class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ApplyChaosRuneRecipe> {
        private Serializer() {}

        @Override
        public ApplyChaosRuneRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new ApplyChaosRuneRecipe();
        }

        @Override
        public ApplyChaosRuneRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new ApplyChaosRuneRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, ApplyChaosRuneRecipe recipe) {}
    }
}
