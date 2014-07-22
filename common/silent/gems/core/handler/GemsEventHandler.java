package silent.gems.core.handler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import silent.gems.SilentGems;
import silent.gems.block.GlowRose;
import silent.gems.configuration.Config;
import silent.gems.control.PlayerInputMap;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.item.ChaosGem;
import silent.gems.item.TorchBandolier;
import silent.gems.item.tool.GemSickle;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.buff.ChaosBuff;
import silent.gems.network.MessagePlayerUpdate;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GemsEventHandler {

    private Random random = new Random();
    private int tickPlayer = 0;

    @SubscribeEvent
    public void onHarvestDropsEvent(HarvestDropsEvent event) {

        if (event.harvester != null && event.harvester.inventory.getCurrentItem() != null
                && event.harvester.inventory.getCurrentItem().getItem() instanceof GemSickle) {
            ItemStack sickle = event.harvester.inventory.getCurrentItem();

            // Check a 3x3x3 cube.
            for (int z = event.z - 1; z < event.z + 2; ++z) {
                for (int y = event.y - 1; y < event.y + 2; ++y) {
                    for (int x = event.x - 1; x < event.x + 2; ++x) {
                        Block block = event.world.getBlock(x, y, z);
                        // Is the block a material the sickle will harvest?
                        for (Material material : GemSickle.effectiveMaterials) {
                            if (block.getMaterial() == material) {
                                // Get drops from block, considering silk touch.
                                for (ItemStack stack : getSickleDropsForBlock(sickle, block, event.world.getBlockMetadata(x, y, z),
                                        event.world, x, y, z, event.isSilkTouching, event.fortuneLevel)) {
                                    event.drops.add(stack);
                                }
                                
                                // Break block
                                event.world.setBlockToAir(x, y, z);
                                break;
                            }
                        }
                    }
                }
            }
            
            if (sickle.attemptDamageItem(1, random)) {
//                sickle.stackSize = 0;
//                sickle = null;
                event.harvester.inventory.setInventorySlotContents(event.harvester.inventory.currentItem, null);
            }
        }
    }

    private ArrayList<ItemStack> getSickleDropsForBlock(ItemStack sickle, Block block, int meta, World world, int x, int y, int z,
            boolean isSilkTouching, int fortuneLevel) {

        // Debug
//        if (block instanceof IShearable) {
//            LogHelper.list(((IShearable) block).isShearable(sickle, world, x, y, z), isSilkTouching);
//        }
//        LogHelper.list(block.getUnlocalizedName(), meta);

        // For some reason, silk touch is set to false for things like vines.
        if (!isSilkTouching && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, sickle) > 0) {
            isSilkTouching = true;
        }

        if (block instanceof IShearable && ((IShearable) block).isShearable(sickle, world, x, y, z) && isSilkTouching) {
            return ((IShearable) block).onSheared(sickle, world, x, y, z, fortuneLevel);
        }
        else if (isSilkTouching) {
            ArrayList<ItemStack> result = new ArrayList<ItemStack>();
            result.add(new ItemStack(block, 1, meta));
            return result;
        }
        else {
            return block.getDrops(world, x, y, z, meta, fortuneLevel);
        }
    }

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

        if (event.side == Side.CLIENT) {
            // Input map update
            handlePlayerInput();
        }
        else {
            // Every tick:
            tickFlight(event.player);

            ++tickPlayer;
            if (tickPlayer >= 40) { // This ticks once per second. Why is it not 20?
                tickPlayer = 0;
                // Every second:
                tickInventory(event.player);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void handlePlayerInput() {

        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            PlayerInputMap inputMap = PlayerInputMap.getInputMapFor(player.getCommandSenderName());
            inputMap.forwardKey = Math.signum(player.movementInput.moveForward);
            inputMap.strafeKey = Math.signum(player.movementInput.moveStrafe);
            inputMap.jumpKey = player.movementInput.jump;
            inputMap.sneakKey = player.movementInput.sneak;
            inputMap.motionX = player.motionX;
            inputMap.motionY = player.motionY;
            inputMap.motionZ = player.motionZ;

            if (inputMap.hasChanged()) {
                inputMap.refresh();
//                MessagePlayerUpdate message = new MessagePlayerUpdate((EntityPlayer) player, inputMap);
//                SilentGems.network.sendToAllAround(message, new TargetPoint(player.dimension, player.posX, player.posY, player.posZ, 160));
            }
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
                    player.fallDistance = (float) computeFallHeightFromVelocity(MathHelper.clamp_float((float) player.motionY, -1000.0f, 0.0f));
                    return;
                }
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
        // if (jumpkey) {
        // LogHelper.derpRand();
        // }
        // Get thrust level
        int flightLevel = ChaosGem.getBuffLevel(chaosGem, ChaosBuff.getBuffByName(ChaosBuff.FLIGHT));
        double t = Config.CHAOS_GEM_FLIGHT_THRUST.value;
        double thrust = t + t * (flightLevel - 1) / 2;

        if (jumpkey && player.motionY < 0.5) {
            // LogHelper.derpRand();
            thrust = PlayerHelper.thrust(player, thrust);
            // LogHelper.debug(thrust);
        }
    }
}
