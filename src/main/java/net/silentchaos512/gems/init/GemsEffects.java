package net.silentchaos512.gems.init;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.potion.BaseEffect;
import net.silentchaos512.gems.potion.FreezingEffect;
import net.silentchaos512.gems.potion.ShockingEffect;
import net.silentchaos512.lib.util.TimeUtils;

public final class GemsEffects {
    public static FreezingEffect freezing;
    public static ShockingEffect shocking;
    public static Effect insulated;
    public static Effect grounded;

    private GemsEffects() {}

    public static void registerEffects(RegistryEvent.Register<Effect> event) {
        freezing = registerEffect("freezing", new FreezingEffect());
        shocking = registerEffect("shocking", new ShockingEffect());
        insulated = registerEffect("insulated", new BaseEffect(EffectType.BENEFICIAL, 0x009499));
        grounded = registerEffect("grounded", new BaseEffect(EffectType.BENEFICIAL, 0x919900));
    }

    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        if (!event.getRegistry().getRegistryName().equals(ForgeRegistries.POTION_TYPES.getRegistryName())) {
            return;
        }

        registerPotion("insulating", new Potion(new EffectInstance(insulated, TimeUtils.ticksFromMinutes(3))));
        registerPotion("grounding", new Potion(new EffectInstance(grounded, TimeUtils.ticksFromMinutes(3))));
    }

    private static <T extends Effect> T registerEffect(String name, T potion) {
        ResourceLocation id = SilentGems.getId(name);
        potion.setRegistryName(id);
        ForgeRegistries.POTIONS.register(potion);
        return potion;
    }

    private static <T extends Potion> void registerPotion(String name, T potionType) {
        ResourceLocation id = SilentGems.getId(name);
        potionType.setRegistryName(id);
        ForgeRegistries.POTION_TYPES.register(potionType);
    }
}
