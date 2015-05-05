package net.silentchaos512.gems.core.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.control.PlayerInputMap;


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
    
    // Flight stuff
    
    final static double root2 = Math.sqrt(2);

    public static double thrust(EntityPlayer player, double thrust) {

        // Mostly stolen from MachineMuse's Powersuits :)
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderName());
        boolean jumpkey = movementInput.jumpKey;
        float forwardkey = movementInput.forwardKey;
        float strafekey = movementInput.strafeKey;
        boolean downkey = movementInput.downKey;
        boolean sneakkey = movementInput.sneakKey;
        double thrustUsed = 0;

        Vec3 playerHorzFacing = player.getLookVec();
        playerHorzFacing.yCoord = 0;
        playerHorzFacing.normalize();
        if (forwardkey == 0) {
            player.motionY += thrust;
        }
        else {
            player.motionY += thrust / root2;
            player.motionX += playerHorzFacing.xCoord * thrust / root2 * Math.signum(forwardkey);
            player.motionZ += playerHorzFacing.zCoord * thrust / root2 * Math.signum(forwardkey);
        }
        thrustUsed += thrust;

        // Slow the player if they are going too fast
        double horzm2 = player.motionX * player.motionX + player.motionZ * player.motionZ;
        double horzmlim = Config.CHAOS_GEM_FLIGHT_MAX_SPEED.value * Config.CHAOS_GEM_FLIGHT_MAX_SPEED.value / 400;
        if (sneakkey && horzmlim > 0.05) {
            horzmlim = 0.05;
        }

        if (horzm2 > horzmlim) {
            double ratio = Math.sqrt(horzmlim / horzm2);
            player.motionX *= ratio;
            player.motionZ *= ratio;
        }
        resetFloatKickTicks(player);
        return thrustUsed;
    }

    public static void resetFloatKickTicks(EntityPlayer player) {

        // I'm guessing this prevents a player from being kicked for flying?
        // TODO: Fix this!
        if (player instanceof EntityPlayerMP) {
//            ((EntityPlayerMP) player).playerNetServerHandler.ticksForFloatKick = 0;
        }
    }
}
