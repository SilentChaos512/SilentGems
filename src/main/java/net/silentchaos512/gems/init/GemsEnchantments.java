package net.silentchaos512.gems.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
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
        if (event.getRegistry().getRegistrySuperType() != Enchantment.class) return;
        IForgeRegistry<Enchantment> reg = ForgeRegistries.ENCHANTMENTS;

        lifeSteal = register(reg, "life_steal", new EnchantmentLifeSteal());
        gravity = register(reg, "gravity", new EnchantmentGravity());
        iceAspect = register(reg, "ice_aspect", new EnchantmentIceAspect());
        lightningAspect = register(reg, "lightning_aspect", new EnchantmentLightningAspect());
        supercharged = register(reg, "supercharged", new EnchantmentSupercharged());
    }

    private static <T extends Enchantment> T register(IForgeRegistry<Enchantment> reg, String name, T enchantment) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        enchantment.setRegistryName(id);
        reg.register(enchantment);

        return enchantment;
    }
}
