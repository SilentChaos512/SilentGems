package silent.gems.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSickle;
import silent.gems.item.tool.GemSword;

public class InventoryHelper {

    /*
     * Returns true if the ItemStack is a gem sword/pickaxe/shovel/axe/hoe.
     */
    public static boolean isGemTool(ItemStack stack) {

        if (stack != null) {
            Item item = stack.getItem();
            return (item instanceof GemSword) || item instanceof GemPickaxe || item instanceof GemShovel || item instanceof GemAxe
                    || item instanceof GemHoe || item instanceof GemSickle;
        }
        return false;
    }
    
    public static boolean isItemBlock(Item item, Block block) {
        
        return Block.getIdFromBlock(block) == Item.getIdFromItem(item);
    }
    
    public static boolean isStackBlock(ItemStack stack, Block block) {
        
        return Block.getIdFromBlock(block) == Item.getIdFromItem(stack.getItem());
    }
}
