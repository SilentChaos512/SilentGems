package net.silentchaos512.gems.init;

import net.minecraft.potion.Potion;
import net.silentchaos512.gems.potion.PotionFreezing;
import net.silentchaos512.gems.potion.PotionShocking;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.SRegistry;

public class ModPotions implements IRegistrationHandler<Potion> {

  public static final PotionFreezing freezing = new PotionFreezing();
  public static final PotionShocking shocking = new PotionShocking();

  @Override
  public void registerAll(SRegistry reg) {

    reg.registerPotion(freezing);
    reg.registerPotion(shocking);
  }
}
