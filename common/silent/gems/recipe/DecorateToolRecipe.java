package silent.gems.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import silent.gems.SilentGems;
import silent.gems.core.util.InventoryHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.item.Gem;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSword;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;


public class DecorateToolRecipe implements IRecipe {

    public ItemStack getStackInRowAndColumn(InventoryCrafting inventorycrafting, int row, int column, int gridWidth) {

        if (row >= 0 && row < gridWidth) {
            int slot = row + column * gridWidth;
            if (slot > 0 && slot < inventorycrafting.getSizeInventory()) {
                return inventorycrafting.getStackInSlot(slot);
            }
        }

        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world) {

        int numTools = 0;
        int numGems = 0;
        int numWool = 0;
        int toolSlot = -1;
        int i, row, column;

        final int gridWidth = inventorycrafting.getSizeInventory() == 4 ? 2 : 3;

        ItemStack stack, tool = null;

        // Count valid ingredients and look for invalid
        for (i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() instanceof Gem) {
                    // Only regular gems, not supercharged.
                    if (stack.getItemDamage() > 15) {
                        return false;
                    }
                    ++numGems;
                }
                else if (InventoryHelper.isStackBlock(stack, Blocks.wool)) {
                    ++numWool;
                }
                else if (InventoryHelper.isGemTool(stack)) {
                    ++numTools;
                    toolSlot = i;
                    tool = stack;
                }
                else {
                    // Invalid item
                    return false;
                }
            }
        }

        // Need exactly one tool.
        if (numTools != 1) {
            return false;
        }
        // No more than one wool.
        if (numWool > 1) {
            return false;
        }
        // At least one gem or wool
        if (numWool == 0 && numGems == 0) {
            return false;
        }

        // Get tool row/column
        row = toolSlot % gridWidth;
        column = toolSlot / gridWidth;
        // LogHelper.list(row, column, toolSlot);

        // Slots directly adjacent to tool may be a gem, wool, or null.
        Item[] items = new Item[4];
        stack = getStackInRowAndColumn(inventorycrafting, row - 1, column, gridWidth);
        items[0] = stack == null ? null : stack.getItem();
        stack = getStackInRowAndColumn(inventorycrafting, row + 1, column, gridWidth);
        items[1] = stack == null ? null : stack.getItem();
        stack = getStackInRowAndColumn(inventorycrafting, row, column - 1, gridWidth);
        items[2] = stack == null ? null : stack.getItem();
        stack = getStackInRowAndColumn(inventorycrafting, row, column + 1, gridWidth);
        items[3] = stack == null ? null : stack.getItem();

        // Count gems adjacent to tool.
        int adjGems = 0;
        for (i = 0; i < items.length; ++i) {
            if (items[i] != null) {
                if (items[i] instanceof Gem) {
                    ++adjGems;
                }
                else if (!InventoryHelper.isItemBlock(items[i], Blocks.wool)) {
                    return false;
                }
            }
        }

        // Make sure gems are only adjacent to tool.
        if (adjGems != numGems) {
            return false;
        }
        
        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

        ItemStack stack, tool = null;

        int i, row, column, toolSlot = 0, gemCount = 0;

        final int gridWidth = inventorycrafting.getSizeInventory() == 4 ? 2 : 3;

        // Find tool and count gems.
        for (i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null) {
                if (InventoryHelper.isGemTool(stack)) {
                    tool = stack;
                    toolSlot = i;
                }
                else if (stack.getItem() instanceof Gem) {
                    ++gemCount;
                }
            }
        }

        if (tool == null) {
            return null;
        }

        byte baseGem = getToolBaseGem(tool);
        //LogHelper.debug(baseGem);

        // Get tool row/column
        row = toolSlot % gridWidth;
        column = toolSlot / gridWidth;

        // Copy tool, we can't modify the original!
        ItemStack result = tool.copy();
        if (result.stackTagCompound == null) {
            result.stackTagCompound = new NBTTagCompound();
        }

        // Deco
        stack = getStackInRowAndColumn(inventorycrafting, row, column + 1, gridWidth);
        if (stack != null && stack.getItem() instanceof Gem) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_DECO, (byte) stack.getItemDamage());
        }
        // HeadL
        stack = getStackInRowAndColumn(inventorycrafting, row - 1, column, gridWidth);
        if (stack != null && stack.getItem() instanceof Gem) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_LEFT, (byte) stack.getItemDamage());
        }
        else if (!result.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_LEFT)
                || result.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_LEFT) == -1) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_LEFT, baseGem);
        }
        // HeadM
        stack = getStackInRowAndColumn(inventorycrafting, row, column - 1, gridWidth);
        if (stack != null && stack.getItem() instanceof Gem) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_MIDDLE, (byte) stack.getItemDamage());
        }
        else if (!result.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_MIDDLE)
                || result.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_MIDDLE) == -1) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_MIDDLE, baseGem);
        }
        // HeadR
        stack = getStackInRowAndColumn(inventorycrafting, row + 1, column, gridWidth);
        if (stack != null && stack.getItem() instanceof Gem) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_RIGHT, (byte) stack.getItemDamage());
        }
        else if (!result.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_RIGHT)
                || result.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_RIGHT) == -1) {
            result.stackTagCompound.setByte(Strings.TOOL_ICON_HEAD_RIGHT, baseGem);
        }
        // Rod
        for (i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
            stack = inventorycrafting.getStackInSlot(i);
            if (stack != null && InventoryHelper.isStackBlock(stack, Blocks.wool)) {
                result.stackTagCompound.setByte(Strings.TOOL_ICON_ROD, (byte) stack.getItemDamage());
            }
        }

        // Repair with gems.
        result.attemptDamageItem(-gemCount * result.getMaxDamage() / 8, SilentGems.instance.random);

        return result;
    }

    @Override
    public int getRecipeSize() {

        // TODO What's this?
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {

        // TODO What's this?
        return null;
    }

    private byte getToolBaseGem(ItemStack stack) {

        Item item = stack.getItem();
        Byte b = -1;

        if (item instanceof GemSword) {
            b = (byte) ((GemSword) item).getGemId();
        }
        else if (item instanceof GemPickaxe) {
            b = (byte) ((GemPickaxe) item).getGemId();
        }
        else if (item instanceof GemShovel) {
            b = (byte) ((GemShovel) item).getGemId();
        }
        else if (item instanceof GemAxe) {
            b = (byte) ((GemAxe) item).getGemId();
        }
        else if (item instanceof GemHoe) {
            b = (byte) ((GemHoe) item).getGemId();
        }

        // Eliminate "supercharged" damage difference.
        if (b != -1 && b != ModMaterials.FISH_GEM_ID) {
            b = (byte) (b & 15);
        }

        return b;
    }
}
