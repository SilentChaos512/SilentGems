package silent.gems.core.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import silent.gems.configuration.Config;
import silent.gems.control.PlayerInputMap;


public class PlayerHelper {
    
    public static void addChatMessage(EntityPlayer player, String message) {
        
        player.addChatMessage(new ChatComponentText(message));
    }
    
    public static void addChatMessage(EntityPlayer player, String key, boolean fromLocalizationFile) {
        
        addChatMessage(player, fromLocalizationFile ? LocalizationHelper.getLocalizedString(key) : key);
    }
    
    public static void addItemToInventoryOrDrop(EntityPlayer player, ItemStack stack) {
        
        if (!player.inventory.addItemStackToInventory(stack)) {
            // Spawn item entity
            EntityItem entityItem = new EntityItem(player.worldObj, player.posX, player.posY + 1.5, player.posZ, stack);
            player.worldObj.spawnEntityInWorld(entityItem);
        }
    }
    
    public static boolean isPlayerHoldingItem(EntityPlayer player, Object object) {
        
        if (player.inventory.getCurrentItem() != null && object != null) {
            if (object instanceof Item) {
                return player.inventory.getCurrentItem().getItem().equals((Item) object);
            }
            else if (object instanceof ItemStack) {
                return player.inventory.getCurrentItem().getItem().equals(((ItemStack) object).getItem());
            }
            else if (object instanceof Block) {
                return InventoryHelper.isStackBlock(player.inventory.getCurrentItem(), (Block) object);
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
