package net.silentchaos512.gems.init;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.potion.PotionBase;
import net.silentchaos512.gems.potion.PotionFreezing;
import net.silentchaos512.gems.potion.PotionShocking;
import net.silentchaos512.lib.util.TimeUtils;

public final class ModPotions {
    public static PotionFreezing freezing;
    public static PotionShocking shocking;
    public static Potion insulated;
    public static Potion grounded;

    private ModPotions() {}

    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        if (!event.getRegistry().getRegistryName().equals(ForgeRegistries.POTIONS.getRegistryName())) {
            return;
        }

        freezing = registerPotion("freezing", new PotionFreezing());
        shocking = registerPotion("shocking", new PotionShocking());
        insulated = registerPotion("insulated", new PotionBase(false, 0x009499));
        grounded = registerPotion("grounded", new PotionBase(false, 0x919900));
    }

    public static void registerPotionTypes(RegistryEvent.Register<PotionType> event) {
        if (!event.getRegistry().getRegistryName().equals(ForgeRegistries.POTION_TYPES.getRegistryName())) {
            return;
        }

        registerPotionType("insulating", new PotionType(new PotionEffect(insulated, TimeUtils.ticksFromMinutes(3))));
        registerPotionType("grounding", new PotionType(new PotionEffect(grounded, TimeUtils.ticksFromMinutes(3))));
    }

    private static <T extends Potion> T registerPotion(String name, T potion) {
        ResourceLocation id = SilentGems.getId(name);
        potion.setRegistryName(id);
        ForgeRegistries.POTIONS.register(potion);
        return potion;
    }

    private static <T extends PotionType> void registerPotionType(String name, T potionType) {
        ResourceLocation id = SilentGems.getId(name);
        potionType.setRegistryName(id);
        ForgeRegistries.POTION_TYPES.register(potionType);
    }
}
