package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.lib.util.ChatHelper;

public class ItemDebug extends Item {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        FoodStats food = playerIn.getFoodStats();
        food.setFoodLevel(food.getFoodLevel() - 2);
        food.setFoodSaturationLevel(food.getSaturationLevel() - 0.2f);

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = worldIn.getBlockState(pos);
        ChatHelper.sendMessage(playerIn, "Hardness: " + state.getBlockHardness(worldIn, pos));
        return EnumActionResult.SUCCESS;
    }
}
