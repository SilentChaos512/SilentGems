package silent.gems.core.handler;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.player.BonemealEvent;
import silent.gems.block.GlowRose;
import silent.gems.configuration.Config;
import silent.gems.control.PlayerInputMap;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.item.ChaosGem;
import silent.gems.item.TorchBandolier;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.buff.ChaosBuff;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;


public class GemsEventHandler {

    private Random random = new Random();
    private int tickPlayer = 0;
    
    @SubscribeEvent
    public void onUseBonemeal(BonemealEvent event) {
        
        if (event.block == Blocks.grass) {
            if (!event.world.isRemote) {
                // Spawn some Glow Roses?
                int k = random.nextInt(6) - 1;
                int x, y, z, m;
                GlowRose flower = (GlowRose) SRegistry.getBlock(Names.GLOW_ROSE);
                for (int i = 0; i < k; ++i) {
                    x = event.x + random.nextInt(9) - 4;
                    y = event.y + 1;
                    z = event.z + random.nextInt(9) - 4;
                    // Get rid of tall grass, it seems to spawn first.
                    if (event.world.getBlock(x, y, z) == Blocks.tallgrass) {
                        event.world.setBlockToAir(x, y, z);
                    }
                    if (event.world.isAirBlock(x, y, z) && flower.canBlockStay(event.world, x, y, z)) {
                        m = random.nextInt(EnumGem.all().length);
                        event.world.setBlock(x, y, z, flower, m, 2);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.player.worldObj.isRemote) {
            return;
        }
        
        // Every tick:
        tickFlight(event.player);
        
        ++tickPlayer;
        if (tickPlayer >= 40) { // This ticks once per second. Why is it not 20?
            tickPlayer = 0;
            // Every second:
            tickInventory(event.player);
        }
    }
    
    private void tickFlight(EntityPlayer player) {
        
        // Look for a Chaos gem with Flight.
        int level;
        ChaosBuff flight = ChaosBuff.getBuffByName(ChaosBuff.FLIGHT);
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null && stack.getItem() instanceof ChaosGem) {
                level = ChaosGem.getBuffLevel(stack, flight);
                if (level > 0 && ChaosGem.isEnabled(stack)) {
                    handleFlight(player, stack);
                    return;
                }
                player.fallDistance = (float) computeFallHeightFromVelocity(MathHelper.clamp_float((float) player.motionY, -1000.0f, 0.0f));
            }
        }
    }
    
    private void tickInventory(EntityPlayer player) {
        
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack != null) {
                if (stack.getItem() instanceof TorchBandolier) {
                    ((TorchBandolier) stack.getItem()).absorbTorches(stack, player);
                }
                else if (stack.getItem() instanceof ChaosGem) {
                    ((ChaosGem) stack.getItem()).doTick(stack, player);
                }
                ModEnchantments.mending.tryActivate(player, stack);
            }
        }
    }
    
    // Flight stuff
    
    // Stolen from MachineMuses' Powersuits :)
    public static final double DEFAULT_GRAVITY = -0.0784000015258789;
    
    public static double computeFallHeightFromVelocity(double velocity) {
        
        double ticks = velocity / DEFAULT_GRAVITY;
        double distance = -0.5 * DEFAULT_GRAVITY * ticks * ticks;
        return distance;
    }
    
    private void handleFlight(EntityPlayer player, ItemStack chaosGem) {
        
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderName());
        boolean jumpkey = movementInput.jumpKey;
        if (jumpkey) {
            LogHelper.derpRand();
        }
        // Get thrust level
        int flightLevel = ChaosGem.getBuffLevel(chaosGem, ChaosBuff.getBuffByName(ChaosBuff.FLIGHT));
        double t = Config.CHAOS_GEM_FLIGHT_THRUST.value;
        double thrust = t + t * (flightLevel - 1) / 2;
        
        if (jumpkey && player.motionY < 0.5) {
            thrust = PlayerHelper.thrust(player, thrust);
        }
    }
}
