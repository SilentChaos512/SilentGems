package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.ChatHelper;

public class ItemDebug extends ItemSL {

  public ItemDebug() {

    super(1, SilentGems.MODID, Names.DEBUG_ITEM);
  }

  @Override
  protected ActionResult<ItemStack> clOnItemRightClick(World worldIn, EntityPlayer playerIn,
      EnumHand hand) {

    // playerIn.attackEntityFrom(DamageSource.inWall, 1.0f);
    FoodStats food = playerIn.getFoodStats();
    food.setFoodLevel(food.getFoodLevel() - 2);
    food.setFoodSaturationLevel(food.getSaturationLevel() - 0.2f);

    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
  }

  @Override
  protected EnumActionResult clOnItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos,
      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    IBlockState state = worldIn.getBlockState(pos);
    ChatHelper.sendMessage(playerIn, "Hardness: " + state.getBlockHardness(worldIn, pos));
    return EnumActionResult.SUCCESS;
  }
}
