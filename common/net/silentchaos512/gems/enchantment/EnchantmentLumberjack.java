package net.silentchaos512.gems.enchantment;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.lib.Names;

public class EnchantmentLumberjack extends Enchantment {

  public static final float DIG_SPEED_MULTIPLIER = 0.05f;

  protected EnchantmentLumberjack(int effectId, int weight, EnumEnchantmentType type) {

    super(effectId, weight, type);
    setName(Names.LUMBERJACK);
  }

  @Override
  public boolean canApply(ItemStack stack) {

    Item item = stack.getItem();
    if (item instanceof GemAxe || item instanceof ItemBook) {
      return stack.isItemStackDamageable() ? true : super.canApply(stack);
    }
    return false;
  }
  
  @Override
  public boolean canApplyTogether(Enchantment e) {
    
    return e != ModEnchantments.aoe && super.canApplyTogether(e);
  }

  @Override
  public int getMinEnchantability(int par1) {

    return 15;
  }

  @Override
  public int getMaxEnchantability(int par1) {

    return super.getMinEnchantability(par1) + 50;
  }

  @Override
  public int getMaxLevel() {

    return 1;
  }

  @Override
  public String getTranslatedName(int par1) {

    return StatCollector.translateToLocal("enchantment." + Names.LUMBERJACK) + " "
        + StatCollector.translateToLocal("enchantment.level." + par1);
  }

  public static int tryActivate(ItemStack tool, int x, int y, int z, EntityPlayer player) {

    if (!(tool.getItem() instanceof GemAxe) || player.isSneaking()) {
      return 0;
    }

    World world = player.worldObj;
    final Block wood = world.getBlock(x, y, z);

    if (wood == null) {
      return 0;
    }

    if (wood.isWood(world, x, y, z) || wood.getMaterial() == Material.sponge) {
      if (detectTree(world, x, y, z, wood)) {
        int meta = world.getBlockMetadata(x, y, z);
        int k = breakTree(world, x, y, z, x, y, z, tool, wood, meta, player);
        return k > 0 ? k - 1 : 0;
      }
    }

    return 0;
  }

  public static boolean detectTree(World world, int x, int y, int z, Block wood) {

    int height = y;
    boolean foundTop = false;
    do {
      ++height;
      Block block = world.getBlock(x, height, z);
      if (block != wood) {
        --height;
        foundTop = true;
      }
    } while (!foundTop);

    int numLeaves = 0;
    if (height - y < 50) {
      for (int xPos = x - 1; xPos <= x + 1; xPos++) {
        for (int yPos = height - 1; yPos <= height + 1; yPos++) {
          for (int zPos = z - 1; zPos <= z + 1; zPos++) {
            Block leaves = world.getBlock(xPos, yPos, zPos);
            if (leaves != null && leaves.isLeaves(world, xPos, yPos, zPos))
              ++numLeaves;
          }
        }
      }
    }

    return numLeaves > 3;
  }

  private static int breakTree(World world, int x, int y, int z, int xStart, int yStart, int zStart,
      ItemStack tool, Block block, int meta, EntityPlayer player) {

    int blocksBroken = 0;
    GemAxe axe = (GemAxe) tool.getItem();

    for (int xPos = x - 1; xPos <= x + 1; ++xPos) {
      for (int yPos = y; yPos <= y + 1; ++yPos) {
        for (int zPos = z - 1; zPos <= z + 1; ++zPos) {
          Block localBlock = world.getBlock(xPos, yPos, zPos);
          if (block == localBlock) {
            int localMeta = world.getBlockMetadata(xPos, yPos, zPos);
            int harvestLevel = localBlock.getHarvestLevel(localMeta);
            float localHardness = localBlock == null ? Float.MAX_VALUE
                : localBlock.getBlockHardness(world, xPos, yPos, zPos);

            if (harvestLevel <= axe.getHarvestLevel(tool, "axe") && localHardness >= 0) {
              boolean cancel = false;

              // Block break event
              BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(x, y, z, world, localBlock,
                  localMeta, player);
              // event.setCanceled(cancel);
              MinecraftForge.EVENT_BUS.post(event);
              cancel = event.isCanceled();

              int xDist = xPos - xStart;
              int yDist = yPos - yStart;
              int zDist = zPos - zStart;

              if (9 * xDist * xDist + yDist * yDist + 9 * zDist * zDist < 2500) {
                if (cancel) {
                  blocksBroken += breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart, tool,
                      block, meta, player);
                } else {
                  if (localBlock == block && localMeta % 4 == meta % 4) {
                    if (!player.capabilities.isCreativeMode) {
                      localBlock.harvestBlock(world, player, x, y, z, localMeta);
                      axe.onBlockDestroyed(tool, world, localBlock, xPos, yPos, zPos, player);
                      ++blocksBroken;
                    }

                    world.setBlockToAir(xPos, yPos, zPos);
                    if (!world.isRemote) {
                      blocksBroken += breakTree(world, xPos, yPos, zPos, xStart, yStart, zStart,
                          tool, block, meta, player);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    return blocksBroken;
  }
}
