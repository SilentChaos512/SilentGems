package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.PlayerHelper;

public class ItemDebug extends ItemSL {

  public ItemDebug() {

    super(1, SilentGems.MOD_ID, Names.DEBUG_ITEM);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn,
      EntityPlayer playerIn, EnumHand hand) {

    playerIn.attackEntityFrom(DamageSource.inWall, 1.0f);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }

  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn,
      BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    IBlockState state = worldIn.getBlockState(pos);
    PlayerHelper.addChatMessage(playerIn, "Hardness: " + state.getBlockHardness(worldIn, pos));
    return EnumActionResult.SUCCESS;
  }
}
