package silent.gems.core.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.item.tool.GemAxe;
import silent.gems.item.tool.GemHoe;
import silent.gems.item.tool.GemPickaxe;
import silent.gems.item.tool.GemShovel;
import silent.gems.item.tool.GemSickle;
import silent.gems.item.tool.GemSword;

public class InventoryHelper {

//  public static void breakExtraBlock(ItemStack stack, World world, int x, int y, int z, int side,
//      EntityPlayer player) {
//
//    if (world.isAirBlock(x, y, z)) {
//      return;
//    }
//
//    Block block = world.getBlock(x, y, z);
//    int meta = world.getBlockMetadata(x, y, z);
//
//    // TODO: Check for effective material?
//
//    if (!ForgeHooks.canHarvestBlock(block, player, meta)
//        || ForgeHooks.blockStrength(block, player, world, x, y, z) <= 0.0001f) {
//      return;
//    }
//
//    if (player.capabilities.isCreativeMode) {
//      block.onBlockHarvested(world, x, y, z, meta, player);
//      if (block.removedByPlayer(world, player, x, y, z, false)) {
//        block.onBlockDestroyedByPlayer(world, x, y, z, meta);
//      }
//
//      if (!world.isRemote) {
//        // TODO: send update to client?
//      }
//    }
//  }

  /*
   * Returns true if the ItemStack is a gem sword/pickaxe/shovel/axe/hoe.
   */
  public static boolean isGemTool(ItemStack stack) {

    if (stack != null) {
      Item item = stack.getItem();
      return (item instanceof GemSword) || item instanceof GemPickaxe || item instanceof GemShovel
          || item instanceof GemAxe || item instanceof GemHoe || item instanceof GemSickle;
    }
    return false;
  }

  public static boolean isItemBlock(Item item, Block block) {

    return Block.getIdFromBlock(block) == Item.getIdFromItem(item);
  }

  public static boolean isStackBlock(ItemStack stack, Block block) {

    return Block.getIdFromBlock(block) == Item.getIdFromItem(stack.getItem());
  }

  public static boolean matchesOreDict(ItemStack stack, String oreName) {

    int[] ids = OreDictionary.getOreIDs(stack);
    for (int i = 0; i < ids.length; ++i) {
      if (OreDictionary.getOreName(ids[i]).equals(oreName)) {
        return true;
      }
    }
    return false;
  }
}
