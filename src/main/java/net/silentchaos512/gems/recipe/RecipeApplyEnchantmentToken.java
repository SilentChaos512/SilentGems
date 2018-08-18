package net.silentchaos512.gems.recipe;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ItemEnchantmentToken;
import net.silentchaos512.lib.recipe.RecipeBaseSL;

import java.util.List;

public class RecipeApplyEnchantmentToken extends RecipeBaseSL {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return !getCraftingResult(inventoryCrafting).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack tool = ItemStack.EMPTY;
        List<ItemStack> tokens = Lists.newArrayList();

        for (ItemStack stack : getNonEmptyStacks(inv)) {
            if (stack.getItem() instanceof ItemEnchantmentToken) {
                tokens.add(stack);
            } else {
                if (!tool.isEmpty()) {
                    return ItemStack.EMPTY;
                }
                tool = stack;
            }
        }

        // Found correct items?
        if (tool.isEmpty() || tokens.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Apply tokens to tools.
        ItemStack result = tool.copy();
        for (ItemStack token : tokens) {
            if (!ModItems.enchantmentToken.applyTokenToTool(token, result)) {
                return ItemStack.EMPTY;
            }
        }

        return result;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.enchantmentToken);
    }
}
