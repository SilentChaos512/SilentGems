package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;

public class ModEnchantments {

  public static EnchantmentLifeSteal lifeSteal = new EnchantmentLifeSteal();
  public static EnchantmentMagicDamage magicDamage = new EnchantmentMagicDamage();

  public static void init() {

    register(EnchantmentLifeSteal.NAME, lifeSteal);
    register(EnchantmentMagicDamage.NAME, magicDamage);
  }

  private static void register(String name, Enchantment ench) {

    ench.setRegistryName(new ResourceLocation(SilentGems.MOD_ID, name));
    GameRegistry.register(ench);
  }
}
