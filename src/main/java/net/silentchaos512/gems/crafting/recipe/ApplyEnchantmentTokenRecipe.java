package net.silentchaos512.gems.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.GemsRecipeInit;
import net.silentchaos512.gems.item.EnchantmentTokenItem;

import java.util.List;

public class ApplyEnchantmentTokenRecipe extends SpecialRecipe {
    public ApplyEnchantmentTokenRecipe(ResourceLocation idIn) {
        super(idIn);
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {
        return !getCraftingResult(inv).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        ItemStack tool = ItemStack.EMPTY;
        List<ItemStack> tokens = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof EnchantmentTokenItem) {
                    // Any number of tokens
                    tokens.add(stack);
                } else {
                    // Only one tool
                    if (!tool.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    tool = stack;
                }
            }
        }

        if (tool.isEmpty() || tokens.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = tool.copy();
        result.setCount(1);
        for (ItemStack token : tokens) {
            if (!EnchantmentTokenItem.applyTokenToTool(token, result)) {
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
    public IRecipeSerializer<?> getSerializer() {
        return GemsRecipeInit.APPLY_ENCHANTMENT_TOKEN.get();
    }
}
