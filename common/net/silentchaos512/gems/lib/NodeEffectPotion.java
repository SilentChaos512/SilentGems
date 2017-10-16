package net.silentchaos512.gems.lib;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.silentchaos512.gems.SilentGems;

public class NodeEffectPotion extends NodeEffect {

  protected final Potion potion;
  protected final int durationMin, durationMax, amplifier;

  public NodeEffectPotion(String key, float delayInSeconds, float successChance, int color,
      boolean targetPlayers, boolean targetPassives, boolean targetHostiles, Potion potion,
      int durationMin, int durationMax, int amplifier) {

    super(key, delayInSeconds, successChance, color, targetPlayers, targetPassives, targetHostiles);

    this.potion = potion;
    this.durationMin = durationMin;
    this.durationMax = durationMax;
    this.amplifier = amplifier;
  }
}
