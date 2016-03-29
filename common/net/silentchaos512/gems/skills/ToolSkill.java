package net.silentchaos512.gems.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public abstract class ToolSkill {

  public abstract boolean activate(ItemStack tool, EntityPlayer player, BlockPos pos);

  public abstract String getTranslatedName();
}
