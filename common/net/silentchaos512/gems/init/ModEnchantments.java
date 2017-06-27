package net.silentchaos512.gems.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.enchantment.EnchantmentGravity;
import net.silentchaos512.gems.enchantment.EnchantmentIceAspect;
import net.silentchaos512.gems.enchantment.EnchantmentLifeSteal;
import net.silentchaos512.gems.enchantment.EnchantmentLightningAspect;
import net.silentchaos512.gems.enchantment.EnchantmentMagicDamage;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.SRegistry;

public class ModEnchantments implements IRegistrationHandler<Enchantment> {

  public static EnchantmentGravity gravity = new EnchantmentGravity();
  public static EnchantmentLifeSteal lifeSteal = new EnchantmentLifeSteal();
  public static EnchantmentMagicDamage magicDamage = new EnchantmentMagicDamage();
  public static EnchantmentIceAspect iceAspect = new EnchantmentIceAspect();
  public static EnchantmentLightningAspect lightningAspect = new EnchantmentLightningAspect();

  @Override
  public void registerAll(SRegistry reg) {

    reg.registerEnchantment(lifeSteal, EnchantmentLifeSteal.NAME);
    reg.registerEnchantment(magicDamage, EnchantmentMagicDamage.NAME);
    reg.registerEnchantment(gravity, EnchantmentGravity.NAME);
    reg.registerEnchantment(iceAspect, EnchantmentIceAspect.NAME);
    reg.registerEnchantment(lightningAspect, EnchantmentLightningAspect.NAME);
  }
}
