package net.silentchaos512.gems.core.handler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.silentchaos512.gems.block.GlowRose;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.ChaosGem;
import net.silentchaos512.gems.item.TorchBandolier;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.lib.buff.ChaosBuff;
import net.silentchaos512.gems.recipe.TorchBandolierExtractRecipe;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class GemsEventHandler {

  private Random random = new Random();
  private int tickPlayer = 0;

  @SubscribeEvent
  public void onItemCraftedEvent(ItemCraftedEvent event) {

    if (event.craftMatrix instanceof InventoryCrafting) {
      InventoryCrafting inv = (InventoryCrafting) event.craftMatrix;
      EntityPlayer player = event.player;
      World world = player.worldObj;
      if (TorchBandolier.matchesRecipe(inv, world)) {
        // Decorate a newly crafted Torch Bandolier with the gem used.
        ItemStack gem = inv.getStackInRowAndColumn(1, 1);
        if (gem != null) {
          int k = gem.getItemDamage();
          if (event.crafting.stackTagCompound == null) {
            event.crafting.stackTagCompound = new NBTTagCompound();
          }
          event.crafting.stackTagCompound.setByte(Strings.TORCH_BANDOLIER_GEM, (byte) k);
          event.crafting.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
        }
      } else if ((new TorchBandolierExtractRecipe()).matches(inv, world)) {
        // Return the Torch Bandolier that torches were extracted from.
        ItemStack bandolier = null;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
          bandolier = inv.getStackInSlot(i);
          if (bandolier != null && bandolier.getItem() instanceof TorchBandolier) {
            break;
          }
        }
        
        bandolier.attemptDamageItem(event.crafting.stackSize, world.rand);
        PlayerHelper.addItemToInventoryOrDrop(player, bandolier);
      }
    }
  }

//  @SubscribeEvent
//  public void onHarvestDropsEvent(HarvestDropsEvent event) {
//
//    if (event.harvester != null && event.harvester.inventory.getCurrentItem() != null
//        && event.harvester.inventory.getCurrentItem().getItem() instanceof GemSickle) {
//      ItemStack sickle = event.harvester.inventory.getCurrentItem();
//
//      // Check a 3x3x3 cube.
//      for (int z = event.z - 1; z < event.z + 2; ++z) {
//        for (int y = event.y - 1; y < event.y + 2; ++y) {
//          for (int x = event.x - 1; x < event.x + 2; ++x) {
//            Block block = event.world.getBlock(x, y, z);
//            // Is the block a material the sickle will harvest?
//            for (Material material : GemSickle.effectiveMaterials) {
//              if (block.getMaterial() == material) {
//                // Get drops from block, considering silk touch.
//                for (ItemStack stack : getSickleDropsForBlock(sickle, block,
//                    event.world.getBlockMetadata(x, y, z), event.world, x, y, z,
//                    event.isSilkTouching, event.fortuneLevel)) {
//                  event.drops.add(stack);
//                }
//
//                // Break block
//                event.world.setBlockToAir(x, y, z);
//                break;
//              }
//            }
//          }
//        }
//      }
//
//      if (sickle.attemptDamageItem(1, random)) {
//        event.harvester.inventory.setInventorySlotContents(event.harvester.inventory.currentItem,
//            null);
//      }
//    }
//  }

  private ArrayList<ItemStack> getSickleDropsForBlock(ItemStack sickle, Block block, int meta,
      World world, int x, int y, int z, boolean isSilkTouching, int fortuneLevel) {

    // For some reason, silk touch is set to false for things like vines.
    if (!isSilkTouching
        && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, sickle) > 0) {
      isSilkTouching = true;
    }

    if (block instanceof IShearable && ((IShearable) block).isShearable(sickle, world, x, y, z)
        && isSilkTouching) {
      return ((IShearable) block).onSheared(sickle, world, x, y, z, fortuneLevel);
    } else if (isSilkTouching) {
      ArrayList<ItemStack> result = new ArrayList<ItemStack>();
      result.add(new ItemStack(block, 1, meta));
      return result;
    } else {
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
    } else {
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

  private void tickFlight(EntityPlayer player) {

    // Look for a Chaos gem with Flight.
    int level;
    ChaosBuff flight = ChaosBuff.getBuffByName(ChaosBuff.FLIGHT);
    for (ItemStack stack : player.inventory.mainInventory) {
      if (stack != null && stack.getItem() instanceof ChaosGem) {
        level = ChaosGem.getBuffLevel(stack, flight);
        if (level > 0) {
          if (ChaosGem.isEnabled(stack)) {
            player.fallDistance = 0.0f;
          }
        }
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
    
    for (ItemStack stack : player.inventory.armorInventory) {
      if (stack != null) {
        ModEnchantments.mending.tryActivate(player, stack);
      }
    }
  }
}
