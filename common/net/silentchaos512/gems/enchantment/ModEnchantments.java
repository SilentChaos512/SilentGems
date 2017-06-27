package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;

public class ModEnchantments {

  public static EnchantmentGravity gravity = new EnchantmentGravity();
  public static EnchantmentLifeSteal lifeSteal = new EnchantmentLifeSteal();
  public static EnchantmentMagicDamage magicDamage = new EnchantmentMagicDamage();
  public static EnchantmentIceAspect iceAspect = new EnchantmentIceAspect();
  public static EnchantmentLightningAspect lightningAspect = new EnchantmentLightningAspect();

  public static void init() {

    register(EnchantmentLifeSteal.NAME, lifeSteal);
    register(EnchantmentMagicDamage.NAME, magicDamage);
    register(EnchantmentGravity.NAME, gravity);
    register(EnchantmentIceAspect.NAME, iceAspect);
    register(EnchantmentLightningAspect.NAME, lightningAspect);
  }

  private static void register(String name, Enchantment ench) {

    GameRegistry.register(ench);
  }
}
