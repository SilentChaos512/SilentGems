package net.silentchaos512.gems.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;


public abstract class ToolSkillDigger extends ToolSkill {

  public abstract void onGetBreakSpeed(PlayerEvent.BreakSpeed event);
}
