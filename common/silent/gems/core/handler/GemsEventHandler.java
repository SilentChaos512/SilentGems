package silent.gems.core.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import silent.gems.block.GlowRose;
import silent.gems.configuration.Config;
import silent.gems.control.PlayerInputMap;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.PlayerHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.item.ChaosGem;
import silent.gems.item.TorchBandolier;
import silent.gems.item.tool.GemSickle;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.lib.buff.ChaosBuff;

public class GemsEventHandler {

  private Random random = new Random();
  private int tickPlayer = 0;

  @SubscribeEvent
  public void onItemCraftedEvent(ItemCraftedEvent event) {

    if (event.craftMatrix instanceof InventoryCrafting) {
      if (TorchBandolier
          .matchesRecipe((InventoryCrafting) event.craftMatrix, event.player.worldObj)) {
        ItemStack gem = ((InventoryCrafting) event.craftMatrix).getStackInRowAndColumn(1, 1);
        if (gem != null) {
          int k = gem.getItemDamage();
          NBTTagCompound tags = event.crafting.getTagCompound();
          if (tags == null) {
            tags = new NBTTagCompound();
          }
          tags.setByte(Strings.TORCH_BANDOLIER_GEM, (byte) k);
          tags.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
          event.crafting.setTagCompound(tags);
        }
      }
    }
  }

  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {

    if (event.harvester != null && event.harvester.inventory.getCurrentItem() != null
        && event.harvester.inventory.getCurrentItem().getItem() instanceof GemSickle) {
      ItemStack sickle = event.harvester.inventory.getCurrentItem();

      // Check a 3x3x3 cube.
      int evtX = event.pos.getX();
      int evtY = event.pos.getY();
      int evtZ = event.pos.getZ();
      for (int z = evtZ - 1; z < evtZ + 2; ++z) {
        for (int y = evtY - 1; y < evtY + 2; ++y) {
          for (int x = evtX - 1; x < evtX + 2; ++x) {
            Block block = event.state.getBlock();
            // Is the block a material the sickle will harvest?
            for (Material material : GemSickle.effectiveMaterials) {
              if (block.getMaterial() == material) {
                // Get drops from block, considering silk touch.
                for (ItemStack stack : getSickleDropsForBlock(event.world, event.pos, sickle,
                    event.isSilkTouching, event.fortuneLevel)) {
                  event.drops.add(stack);
                }

                // Break block
                event.world.setBlockToAir(event.pos);
                break;
              }
            }
          }
        }
      }

      if (sickle.attemptDamageItem(1, random)) {
        // sickle.stackSize = 0;
        // sickle = null;
        event.harvester.inventory.setInventorySlotContents(event.harvester.inventory.currentItem,
            null);
      }
    }
  }

  private List<ItemStack> getSickleDropsForBlock(World world, BlockPos pos, ItemStack sickle,
      boolean silkTouch, int fortune) {

    // For some reason, silk touch is set to false for things like vines.
    if (!silkTouch
        && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, sickle) > 0) {
      silkTouch = true;
    }

    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    if (block instanceof IShearable && ((IShearable) block).isShearable(sickle, world, pos)
        && silkTouch) {
      return ((IShearable) block).onSheared(sickle, world, pos, fortune);
    } else if (silkTouch) {
      ArrayList<ItemStack> result = new ArrayList<ItemStack>();
      result.add(new ItemStack(block, 1, block.getMetaFromState(state)));
      return result;
    } else {
      return block.getDrops(world, pos, state, fortune);
    }
  }

  @SubscribeEvent
  public void onUseBonemeal(BonemealEvent event) {

    if (event.block == Blocks.grass) {
      if (!event.world.isRemote) {
        // Spawn some Glow Roses?
        int k = random.nextInt(6) - 1;
        int x, y, z, meta;
        GlowRose flower = (GlowRose) SRegistry.getBlock(Names.GLOW_ROSE);
        for (int i = 0; i < k; ++i) {
          x = event.pos.getX() + random.nextInt(9) - 4;
          y = event.pos.getY() + 1;
          z = event.pos.getZ() + random.nextInt(9) - 4;
          // Get rid of tall grass, it seems to spawn first.
          if (event.block.getBlock() == Blocks.tallgrass) {
            event.world.setBlockToAir(event.pos);
          }
          if (event.block.getBlock() == Blocks.air
              && flower.canBlockStay(event.world, event.pos, event.block)) {
            meta = random.nextInt(EnumGem.all().length);
            IBlockState newState = flower.getDefaultState(); // TODO: metadata?
            event.world.setBlockState(event.pos, newState, 2);
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
    } else {
      // Every tick:
//      tickFlight(event.player);

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

    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (player != null) {
      PlayerInputMap inputMap = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity()
          .getName());
      inputMap.forwardKey = Math.signum(player.movementInput.moveForward);
      inputMap.strafeKey = Math.signum(player.movementInput.moveStrafe);
      inputMap.jumpKey = player.movementInput.jump;
      inputMap.sneakKey = player.movementInput.sneak;
      inputMap.motionX = player.motionX;
      inputMap.motionY = player.motionY;
      inputMap.motionZ = player.motionZ;

      if (inputMap.hasChanged()) {
        inputMap.refresh();
        // MessagePlayerUpdate message = new MessagePlayerUpdate((EntityPlayer) player, inputMap);
        // SilentGems.network.sendToAllAround(message, new TargetPoint(player.dimension, player.posX, player.posY,
        // player.posZ, 160));
      }
    }
  }

  private void tickInventory(EntityPlayer player) {

    for (ItemStack stack : player.inventory.mainInventory) {
      if (stack != null) {
        if (stack.getItem() instanceof TorchBandolier) {
          ((TorchBandolier) stack.getItem()).absorbTorches(stack, player);
        } else if (stack.getItem() instanceof ChaosGem) {
          ((ChaosGem) stack.getItem()).doTick(stack, player);
        }
        ModEnchantments.mending.tryActivate(player, stack);
      }
    }
  }
}
