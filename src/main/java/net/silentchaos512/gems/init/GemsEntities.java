package net.silentchaos512.gems.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.render.entity.EnderSlimeRenderer;
import net.silentchaos512.gems.client.render.entity.WispRenderer;
import net.silentchaos512.gems.client.render.entity.WispShotRenderer;
import net.silentchaos512.gems.entity.*;
import net.silentchaos512.gems.entity.projectile.*;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.utils.Lazy;

import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;

public enum GemsEntities {
    ENDER_SLIME(() -> EntityType.Builder.create(EnderSlimeEntity::new, EntityClassification.MONSTER), 0x003333, 0xAA00AA);

    private final Lazy<EntityType<?>> entityType;
    private final Lazy<SpawnEggItem> spawnEgg;

    GemsEntities(Supplier<EntityType.Builder<?>> factory, int eggPrimaryColor, int eggSecondaryColor) {
        this.entityType = Lazy.of(() -> {
            ResourceLocation id = SilentGems.getId(this.getName());
            return factory.get().build(id.toString());
        });
        this.spawnEgg = Lazy.of(() -> {
            Item.Properties props = new Item.Properties().group(ItemGroup.MISC);
            return new SpawnEggItem(this.type(), eggPrimaryColor, eggSecondaryColor, props);
        });
    }

    public EntityType<?> type() {
        return this.entityType.get();
    }

    public SpawnEggItem getSpawnEgg() {
        return this.spawnEgg.get();
    }

    public String getName() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public static void registerAll(RegistryEvent.Register<EntityType<?>> event) {
        Arrays.stream(values()).forEach(e -> registerType(e.getName(), e.type()));

        for (WispTypes wispType : WispTypes.values()) {
            registerType(wispType.getName(), wispType.getEntityType());
            registerType(wispType.getName() + "_shot", wispType.getShotType());
        }
    }

    private static void registerType(String name, EntityType<?> type) {
        ResourceLocation id = SilentGems.getId(name);
        type.setRegistryName(id);
        ForgeRegistries.ENTITIES.register(type);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EnderSlimeEntity.class, EnderSlimeRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ChaosWispEntity.class, WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(FireWispEntity.class, WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IceWispEntity.class, WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(LightningWispEntity.class, WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WaterWispEntity.class, WispRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ChaosWispShotEntity.class, WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(FireWispShotEntity.class, WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IceWispShotEntity.class, WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(LightningWispShotEntity.class, WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WaterWispShotEntity.class, WispShotRenderer::new);
    }
}
