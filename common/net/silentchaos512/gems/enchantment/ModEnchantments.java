package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;

public class ModEnchantments {

  public static EnchantmentMagicDamage magicDamage = new EnchantmentMagicDamage();

  public static void init() {

    register(new ResourceLocation(SilentGems.MOD_ID, EnchantmentMagicDamage.NAME), magicDamage);
  }

  private static void register(ResourceLocation name, Enchantment ench) {

    ench.setRegistryName(name);
    GameRegistry.register(ench);
  }
}
