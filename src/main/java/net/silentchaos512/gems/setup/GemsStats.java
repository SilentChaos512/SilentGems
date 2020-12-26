package net.silentchaos512.gems.setup;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.ItemStats;

public final class GemsStats {
    public static final DeferredRegister<ItemStat> REGISTER = Registration.create(ItemStats.REGISTRY.get());

    public static final RegistryObject<ItemStat> CHARGEABILITY = REGISTER.register("chargeability", () ->
            new ItemStat(1f, 0f, 100f, TextFormatting.GOLD, new ItemStat.Properties().hidden()));

    private GemsStats() {}

    static void register() {}
}
