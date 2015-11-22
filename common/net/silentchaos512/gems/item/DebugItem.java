package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
      LogHelper.debug("Debug Item used by " + player.getName());
    }

    return stack;
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (world.isRemote) {
      return true;
    }

    // Get and display block hardness.
    IBlockState state = world.getBlockState(pos);
    float hardness = state.getBlock().getBlockHardness(world, pos);

    PlayerHelper.addChatMessage(player, "Block hardness: " + hardness);

    return true;
  }
}
