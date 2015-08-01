package net.silentchaos512.gems.core.handler;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.silentchaos512.gems.block.GlowRose;
import net.silentchaos512.gems.core.registry.SRegistry;
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

    // Some things I only want to do once per second, not every tick.
    boolean isSecond = event.player.worldObj.getTotalWorldTime() % 20 == 0;

    if (event.side == Side.CLIENT) {
    } else {
      for (ItemStack stack : event.player.inventory.mainInventory) {
        if (stack != null) {
          Item item = stack.getItem();
          if (item instanceof ChaosGem) {
            ChaosGem chaosGem = (ChaosGem) item;
            chaosGem.doTick(stack, event.player);
          } else if (item instanceof TorchBandolier && isSecond) {
            TorchBandolier torchBandolier = (TorchBandolier) item;
            torchBandolier.absorbTorches(stack, event.player);
          } else if (isSecond) {
            ModEnchantments.mending.tryActivate(event.player, stack);
          }
        }
      }

      if (isSecond) {
        for (ItemStack stack : event.player.inventory.armorInventory) {
          if (stack != null) {
            ModEnchantments.mending.tryActivate(event.player, stack);
          }
        }
      }
    }
  }
}
