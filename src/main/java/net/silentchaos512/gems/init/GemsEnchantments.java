package net.silentchaos512.gems.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fml.RegistryObject;
import net.silentchaos512.gems.enchantment.*;

import java.util.function.Supplier;

public final class GemsEnchantments {
    public static final RegistryObject<EnchantmentGravity> GRAVITY = register("gravity", EnchantmentGravity::new);
    public static final RegistryObject<EnchantmentLifeSteal> LIFE_STEAL = register("life_steal", EnchantmentLifeSteal::new);
    public static final RegistryObject<EnchantmentIceAspect> ICE_ASPECT = register("ice_aspect", EnchantmentIceAspect::new);
    public static final RegistryObject<EnchantmentLightningAspect> LIGHTNING_ASPECT = register("lightning_aspect", EnchantmentLightningAspect::new);
    public static final RegistryObject<EnchantmentSupercharged> SUPERCHARGED = register("supercharged", EnchantmentSupercharged::new);

    private GemsEnchantments() {}

    static void register() {}

    private static <T extends Enchantment> RegistryObject<T> register(String name, Supplier<T> enchantment) {
        return Registration.ENCHANTMENTS.register(name, enchantment);
    }
}
