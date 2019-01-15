package net.silentchaos512.gems.init;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.potion.PotionFreezing;
import net.silentchaos512.gems.potion.PotionShocking;

public class ModPotions {
    public static PotionFreezing freezing;
    public static PotionShocking shocking;

    public static void registerAll(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> reg = event.getRegistry();

        freezing = register(reg, "freezing", new PotionFreezing());
        shocking = register(reg, "shocking", new PotionShocking());
    }

    private static <T extends Potion> T register(IForgeRegistry<Potion> reg, String name, T potion) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        potion.setRegistryName(id);
        reg.register(potion);

        return potion;
    }
}
