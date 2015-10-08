package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.lib.Names;

public class DebugItem extends ItemSG {

  public DebugItem() {

    setMaxStackSize(1);
    setUnlocalizedName(Names.DEBUG_ITEM);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    list.add("Current functions:");
    list.add("Right-click in air: Reduce food level and cure potion effects.");
    list.add("Right-click on block: Get block hardness.");
    
    super.addInformation(stack, player, list, advanced);
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    // Make player hungry.
    int foodLevel = player.getFoodStats().getFoodLevel();
    player.getFoodStats().setFoodLevel(foodLevel > 2 ? foodLevel - 2 : foodLevel);

    // Remove potion effect.
    player.curePotionEffects(new ItemStack(Items.milk_bucket));

    // Log usage
    if (!world.isRemote) {
      LogHelper.debug("Debug Item used by " + player.getCommandSenderName());
    }

    return stack;
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
      int par7, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    // Get and display block hardness.
    Block block = world.getBlock(x, y, z);
    float hardness = block.getBlockHardness(world, x, y, z);

    PlayerHelper.addChatMessage(player, "Block hardness: " + hardness);

    return true;
  }
}
