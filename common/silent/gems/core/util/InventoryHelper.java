package silent.gems.core.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSword;


public class InventoryHelper {

    public static boolean isGemTool(ItemStack stack) {

        if (stack != null) {
            Item item = stack.getItem();
            return item instanceof GemSword || item instanceof GemPickaxe || item instanceof GemShovel || item instanceof GemAxe
                    || item instanceof GemHoe;
        }
        return false;
    }
    
    public static boolean placeBlockInWorld(World world, int x, int y, int z, int side, int id, int meta) {
        
        if (Block.blocksList[id] != null) {
            ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];
            return world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, id, meta, 2);
        }
        else {
            return false;
        }
    }
    
    public static boolean placeTorchOnBlockAt(World world, int x, int y, int z, int side) {
        
        if (canPlaceTorchOn(world, x, y, z)) {
            ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];
            int meta = side == 0 ? 0 : 6 - side;
            world.setBlock(x + d.offsetX, y + d.offsetY, z + d.offsetZ, Block.torchWood.blockID, meta, 2);
            
            return true;
        }
        else {
            return false;
        }
    }
    
    private static boolean canPlaceTorchOn(World world, int x, int y, int z) {
        
        if (world.doesBlockHaveSolidTopSurface(x, y, z)) {
            return true;
        }
        else {
            int l = world.getBlockId(x, y, z);
            return (Block.blocksList[l] != null && Block.blocksList[l].canPlaceTorchOnTop(world, x, y, z));
        }
    }
    
    public static ItemStack getItemOfTypeFromPlayer(EntityPlayer player, int itemID) {
        
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.itemID == itemID) {
                return stack;
            }
        }
        
        return null;
    }
    
    public static ItemStack[] getAllItemsOfType(EntityPlayer player, int itemID) {
        
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.itemID == itemID) {
                stacks.add(stack);
            }
        }
        
        ItemStack[] result = new ItemStack[stacks.size()];
        for (int i = 0; i < stacks.size(); ++i) {
            result[i] = stacks.get(i);
        }
        return result;
    }
}
