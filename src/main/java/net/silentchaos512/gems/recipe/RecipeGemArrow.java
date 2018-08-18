package net.silentchaos512.gems.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.recipe.RecipeBaseSL;

public class RecipeGemArrow extends RecipeBaseSL {
    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.arrow);
    }

    private boolean partTiersMatch(InventoryCrafting inv) {
        EnumMaterialTier tier = null;

        // Check mains
        for (ItemStack stack : getMaterials(inv)) {
            ToolPart part = ToolPartRegistry.fromStack(stack);
            if (tier == null) {
                tier = part.getTier();
            } else if (tier != part.getTier()) {
                return false;
            }
        }

        // No mains found?
        if (tier == null) {
            return false;
        }

        // Check rod
        ItemStack rod = getRod(inv);
        if (!rod.isEmpty())
            return ToolPartRegistry.fromStack(rod).validForToolOfTier(tier);

        return true;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        if (inv.getSizeInventory() < 9) {
            return false;
        }

        for (int col = 0; col < 3; ++col) {
            ItemStack stack = inv.getStackInRowAndColumn(col, 0);
            if (stack.getItem() == Items.FLINT) {
                // No flint arrows for now, it would conflict with vanilla recipe.
                return false;
            }

            if (!stack.isEmpty()) {
                ItemStack rod = inv.getStackInRowAndColumn(col, 1);
                ItemStack fletching = inv.getStackInRowAndColumn(col, 2);
                ToolPart part1 = ToolPartRegistry.fromStack(stack);
                ToolPart part2 = ToolPartRegistry.fromStack(rod);
                // ToolPart part3 = ToolPartRegistry.fromStack(fletching);

                return part1 instanceof ToolPartMain && part2 instanceof ToolPartRod
                        && fletching.getItem() == Items.FEATHER; // TODO: fletching
            }
        }

        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        if (!partTiersMatch(inv)) {
            return ItemStack.EMPTY;
        }

        ItemStack rod = getRod(inv);
        NonNullList<ItemStack> materials = getMaterials(inv);
        ItemStack[] array = materials.toArray(new ItemStack[0]);

        return ModItems.arrow.construct(rod, array[0]);
    }

    private NonNullList<ItemStack> getMaterials(InventoryCrafting inv) {
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            ToolPart part = ToolPartRegistry.fromStack(stack);
            if (part != null && !part.isBlacklisted(stack) && part instanceof ToolPartMain) {
                list.add(stack);
            }
        }
        return list;
    }

    private ItemStack getRod(InventoryCrafting inv) {
        ItemStack rod = ItemStack.EMPTY;
        for (ItemStack stack : getNonEmptyStacks(inv)) {
            ToolPart part = ToolPartRegistry.fromStack(stack);
            if (part != null && !part.isBlacklisted(stack) && part instanceof ToolPartRod) {
                if (rod.isEmpty()) {
                    rod = stack;
                } else if (!rod.isItemEqual(stack)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return rod;
    }
}
