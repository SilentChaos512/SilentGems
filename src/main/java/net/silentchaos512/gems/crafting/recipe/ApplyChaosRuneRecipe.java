package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ChaosGem;
import net.silentchaos512.gems.item.ChaosRune;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.lib.collection.StackList;

import java.util.List;

public class ApplyChaosRuneRecipe implements IRecipe {
    private static final ResourceLocation NAME = SilentGems.getId("apply_chaos_rune");

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        StackList list = StackList.from(inv);
        // One and only one chaos gem
        ItemStack chaosGem = list.uniqueOfType(ChaosGem.class);
        // At least one rune
        int runes = list.countOfType(ChaosRune.class);
        return !chaosGem.isEmpty() && runes > 0;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        ItemStack chaosGem = ItemStack.EMPTY;
        List<ItemStack> runes = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ChaosRune) {
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
            IChaosBuff buff = ChaosRune.getBuff(rune);
            if (buff == null || !ChaosGem.addBuff(result, buff)) {
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
        return Serializer.INSTANCE;
    }

    @Override
    public boolean isDynamic() {
        // Don't show in recipe book
        return true;
    }

    public static final class Serializer implements IRecipeSerializer<ApplyChaosRuneRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private Serializer() { }

        @Override
        public ApplyChaosRuneRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new ApplyChaosRuneRecipe();
        }

        @Override
        public ApplyChaosRuneRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new ApplyChaosRuneRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, ApplyChaosRuneRecipe recipe) { }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
