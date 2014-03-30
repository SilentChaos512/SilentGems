package silent.gems.core.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import silent.gems.configuration.Config;
import silent.gems.control.PlayerInputMap;

public class PlayerHelper {

    /**
     * Checks that the player is holding the given item. Also checks for null.
     */
    public static boolean isPlayerHoldingItem(EntityPlayer player, Item item) {

        return player.inventory.getCurrentItem() != null && item != null
                && player.inventory.getCurrentItem().getItem().itemID == item.itemID;
    }

    /**
     * Checks that the player is holding the given item. Also checks for null.
     */
    public static boolean isPlayerHoldingItem(EntityPlayer player, ItemStack stack) {

        return player.inventory.getCurrentItem() != null && stack != null
                && player.inventory.getCurrentItem().getItem().itemID == stack.itemID;
    }

    public static ItemStack getStackAfterEquipped(EntityPlayer player) {

        int i = player.inventory.currentItem + 1;

        if (i >= player.inventory.getHotbarSize()) {
            return null;
        }

        return player.inventory.getStackInSlot(i);
    }

    public static void addChatMessage(EntityPlayer player, String key, boolean fromLocalizationFile) {

        if (!player.worldObj.isRemote) {
            if (fromLocalizationFile) {
                player.addChatMessage(LocalizationHelper.getMessageText(key, ""));
            }
            else {
                player.addChatMessage(key);
            }
        }
    }

    public static void addChatMessage(EntityPlayer player, String key, EnumChatFormatting format, boolean fromLocalizationFile) {

        if (!player.worldObj.isRemote) {
            if (fromLocalizationFile) {
                player.addChatMessage(LocalizationHelper.getMessageText(key, format));
            }
            else {
                player.addChatMessage(key);
            }
        }
    }

    final static double root2 = Math.sqrt(2);

    public static double thrust(EntityPlayer player, double thrust) {

        // Mostly stolen from MachineMuse's Powersuits :)
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
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
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).playerNetServerHandler.ticksForFloatKick = 0;
        }
    }
}
