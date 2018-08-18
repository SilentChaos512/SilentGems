package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.item.ItemChaosRune;
import net.silentchaos512.lib.recipe.RecipeBaseSL;

public class RecipeChaosGemUpgrade extends RecipeBaseSL {
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int countGem = 0;
        int countRune = 0;

        // Count number of chaos gems and chaos runes.
        for (ItemStack stack : getNonEmptyStacks(inv)) {
            // Chaos Gem
            if (stack.getItem() instanceof ItemChaosGem)
                ++countGem;
                // Chaos Rune
            else if (stack.getItem() instanceof ItemChaosRune)
                ++countRune;
                // Not a gem or rune.
            else
                return false;
        }

        // Must have only one chaos gem and one or more chaos runes.
        return countGem == 1 && countRune > 0;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack chaosGem = ItemStack.EMPTY;

        // Find the Chaos Gem.
        for (ItemStack stack : getNonEmptyStacks(inv)) {
            if (stack.getItem() instanceof ItemChaosGem) {
                chaosGem = stack.copy();
            }
        }

        // Found Chaos Gem?
        if (chaosGem.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemChaosGem itemGem = (ItemChaosGem) chaosGem.getItem();

        // Find runes and apply them.
        for (ItemStack stack : getNonEmptyStacks(inv)) {
            if (stack.getItem() instanceof ItemChaosRune) {
                ItemChaosRune itemRune = (ItemChaosRune) stack.getItem();
                if (!itemGem.addBuff(chaosGem, itemRune.getBuff(stack)))
                    return ItemStack.EMPTY;
            }
        }

        return chaosGem;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }
}
