package silent.gems.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHelper {

    /**
     * Adds one recipe consisting of a center item with 1-4 different items (2-8
     * of each) surrounding it.
     * 
     * @param output
     *            The item being crafted.
     * @param middleStack
     *            The item in the middle of the crafting grid.
     * @param surroundingObjects
     *            The item(s) surrounding the middle item. Order affects the
     *            recipe.
     */
    public static void addSurround(ItemStack output, ItemStack middleStack, Object... surroundingObjects) {

        ItemStack[] stacks = new ItemStack[surroundingObjects.length];

        int i = -1;
        for (Object obj : surroundingObjects) {
            ++i;
            if (obj instanceof Block) {
                stacks[i] = new ItemStack((Block) obj);
            }
            else if (obj instanceof Item) {
                stacks[i] = new ItemStack((Item) obj);
            }
            else if (obj instanceof ItemStack) {
                stacks[i] = (ItemStack) obj;
            }
        }

        switch (surroundingObjects.length) {
            case 0: {
                // No surrounding stacks?
                LogHelper.warning("Failed to add a weird recipe for " + output.toString());
                break;
            }
            case 1: {
                GameRegistry.addShapedRecipe(output, "xxx", "xcx", "xxx", 'c', middleStack, 'x', stacks[0]);
                break;
            }
            case 2: {
                GameRegistry.addShapedRecipe(output, "xyx", "ycy", "xyx", 'c', middleStack, 'x', stacks[0], 'y', stacks[1]);
                break;
            }
            case 3: {
                GameRegistry.addShapedRecipe(output, " xy", "zcz", "yx ", 'c', middleStack, 'x', stacks[0], 'y', stacks[1], 'z', stacks[2]);
                break;
            }
            case 4: {
                GameRegistry.addShapedRecipe(output, "xyz", "dcd", "zyx", 'c', middleStack, 'x', stacks[0], 'y', stacks[1], 'z', stacks[2],
                        'd', stacks[3]);
                break;
            }
            default: {
                // Too many things!
                LogHelper.warning("Failed to add a weird recipe for " + output.toString());
            }
        }
    }
}