package net.silentchaos512.gems.init;

import net.silentchaos512.gems.potion.PotionFreezing;
import net.silentchaos512.gems.potion.PotionShocking;
import net.silentchaos512.lib.registry.SRegistry;

public class ModPotions {
    public static final PotionFreezing freezing = new PotionFreezing();
    public static final PotionShocking shocking = new PotionShocking();

    public static void registerAll(SRegistry reg) {
        reg.registerPotion(freezing, "freezing");
        reg.registerPotion(shocking, "shocking");
    }
}
