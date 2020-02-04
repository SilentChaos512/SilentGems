package net.silentchaos512.gems.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.render.entity.CorruptedSlimeRenderer;
import net.silentchaos512.gems.client.render.entity.EnderSlimeRenderer;
import net.silentchaos512.gems.client.render.entity.WispRenderer;
import net.silentchaos512.gems.client.render.entity.WispShotRenderer;
import net.silentchaos512.gems.entity.CorruptedSlimeEntity;
import net.silentchaos512.gems.entity.EnderSlimeEntity;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.utils.Lazy;

public final class GemsEntities {
    public static final Lazy<EntityType<EnderSlimeEntity>> ENDER_SLIME = makeType("ender_slime", EnderSlimeEntity::new);
    public static final Lazy<EntityType<CorruptedSlimeEntity>> CORRUPTED_SLIME = makeType("corrupted_slime", CorruptedSlimeEntity::new);

    private GemsEntities() {}

    public static void registerTypes(RegistryEvent.Register<EntityType<?>> event) {
        registerType("ender_slime", ENDER_SLIME.get());
        registerType("corrupted_slime", CORRUPTED_SLIME.get());

        for (WispTypes wispType : WispTypes.values()) {
            registerType(wispType.getName(), wispType.getEntityType());
            registerType(wispType.getName() + "_shot", wispType.getShotType());
        }

        EntitySpawnPlacementRegistry.register(ENDER_SLIME.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EnderSlimeEntity::canSpawnAt);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(ENDER_SLIME.get(), EnderSlimeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CORRUPTED_SLIME.get(), CorruptedSlimeRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(WispTypes.CHAOS.getEntityType(), WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.FIRE.getEntityType(), WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.ICE.getEntityType(), WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.LIGHTNING.getEntityType(), WispRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.WATER.getEntityType(), WispRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(WispTypes.CHAOS.getShotType(), WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.FIRE.getShotType(), WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.ICE.getShotType(), WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.LIGHTNING.getShotType(), WispShotRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(WispTypes.WATER.getShotType(), WispShotRenderer::new);
    }

    private static <T extends Entity> Lazy<EntityType<T>> makeType(String name, EntityType.IFactory<T> factory) {
        return Lazy.of(() -> EntityType.Builder.create(factory, EntityClassification.MONSTER).build(SilentGems.getId(name).toString()));
    }

    private static void registerType(String name, EntityType<?> type) {
        ResourceLocation id = SilentGems.getId(name);
        type.setRegistryName(id);
        ForgeRegistries.ENTITIES.register(type);
    }
}
