package net.silentchaos512.gems.skills;

import net.minecraftforge.event.entity.player.PlayerEvent;


public abstract class ToolSkillDigger extends ToolSkill {
    public abstract void onGetBreakSpeed(PlayerEvent.BreakSpeed event);
}
