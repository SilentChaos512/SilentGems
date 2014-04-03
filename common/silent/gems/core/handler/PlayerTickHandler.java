package silent.gems.core.handler;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import silent.gems.configuration.Config;
import silent.gems.control.PlayerInputMap;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.InventoryHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.item.ChaosGem;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.lib.buff.ChaosBuff;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;


public class PlayerTickHandler implements ITickHandler {

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {

        EntityPlayer player = null;
        
        try {
            player = (EntityPlayer) tickData[0];
        }
        catch (Exception ex) {
            LogHelper.warning("PlayerTickHandler received invalid Player object.");
            return;
        }
        
        // Chaos gem with flight
        //ItemStack chaosGem = InventoryHelper.getItemOfTypeFromPlayer(player, SRegistry.getItem(Names.CHAOS_GEM).itemID);
        for (ItemStack chaosGem : InventoryHelper.getAllItemsOfType(player, SRegistry.getItem(Names.CHAOS_GEM).itemID)) {
            boolean flightGemFound = false;
            if (chaosGem != null && !flightGemFound) {
                int level = ChaosGem.getBuffLevel(chaosGem, ChaosBuff.getBuffByName("flight"));
                if (level > 0) {
                    if (ChaosGem.isEnabled(chaosGem)) {
                        //LogHelper.derp();
                        handleFlight(player, chaosGem);
                        flightGemFound = true;
                    }
                    player.fallDistance = (float) computeFallHeightFromVelocity(MathHelper.clamp_float((float) player.motionY, -1000.0f, 0.0f));
                }
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

    }
    
    private void handleFlight(EntityPlayer player, ItemStack chaosGem) {
        
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
        boolean jumpkey = movementInput.jumpKey;
        // Get thrust level
        int flightLevel = ChaosGem.getBuffLevel(chaosGem, ChaosBuff.getBuffByName("flight"));
        double t = Config.CHAOS_GEM_FLIGHT_THRUST.value;
        double thrust = t + t * (flightLevel - 1) / 2;
        
        if (jumpkey && player.motionY < 0.5) {
            //LogHelper.derp();
            thrust = PlayerHelper.thrust(player, thrust);
        }
    }

    @Override
    public EnumSet<TickType> ticks() {

        return EnumSet.of(TickType.PLAYER);
    }

    @Override
    public String getLabel() {

        return Reference.MOD_ID + ":PlayerTick";
    }

    // Stolen from MachineMuses' Powersuits :)
    public static final double DEFAULT_GRAVITY = -0.0784000015258789;
    
    public static double computeFallHeightFromVelocity(double velocity) {
        
        double ticks = velocity / DEFAULT_GRAVITY;
        double distance = -0.5 * DEFAULT_GRAVITY * ticks * ticks;
        return distance;
    }
}
