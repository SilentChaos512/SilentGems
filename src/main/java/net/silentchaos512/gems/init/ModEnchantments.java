package net.silentchaos512.gems.init;

import net.silentchaos512.gems.enchantment.*;
import net.silentchaos512.lib.registry.SRegistry;

public class ModEnchantments {
    public static EnchantmentGravity gravity = new EnchantmentGravity();
    public static EnchantmentLifeSteal lifeSteal = new EnchantmentLifeSteal();
    public static EnchantmentMagicDamage magicDamage = new EnchantmentMagicDamage();
    public static EnchantmentIceAspect iceAspect = new EnchantmentIceAspect();
    public static EnchantmentLightningAspect lightningAspect = new EnchantmentLightningAspect();
    public static EnchantmentSupercharged supercharged = new EnchantmentSupercharged();

    public static void registerAll(SRegistry reg) {
        reg.registerEnchantment(lifeSteal, "lifesteal");
        reg.registerEnchantment(magicDamage, "magicdamage");
        reg.registerEnchantment(gravity, "gravity");
        reg.registerEnchantment(iceAspect, "iceaspect");
        reg.registerEnchantment(lightningAspect, "lightningaspect");
        reg.registerEnchantment(supercharged, "supercharged");
    }
}
