package net.silentchaos512.gems.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.enchantment.*;

public final class GemsEnchantments {
    public static EnchantmentGravity gravity;
    public static EnchantmentLifeSteal lifeSteal;
    public static EnchantmentIceAspect iceAspect;
    public static EnchantmentLightningAspect lightningAspect;
    public static EnchantmentSupercharged supercharged;

    private GemsEnchantments() {}

    public static void registerAll(RegistryEvent.Register<Enchantment> event) {
        lifeSteal = register("life_steal", new EnchantmentLifeSteal());
        gravity = register("gravity", new EnchantmentGravity());
        iceAspect = register("ice_aspect", new EnchantmentIceAspect());
        lightningAspect = register("lightning_aspect", new EnchantmentLightningAspect());
        supercharged = register("supercharged", new EnchantmentSupercharged());
    }

    private static <T extends Enchantment> T register(String name, T enchantment) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        enchantment.setRegistryName(id);
        ForgeRegistries.ENCHANTMENTS.register(enchantment);

        return enchantment;
    }
}
