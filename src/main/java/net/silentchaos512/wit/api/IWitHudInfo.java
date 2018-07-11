package net.silentchaos512.wit.api;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface IWitHudInfo {

  public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player, boolean advanced);
}
