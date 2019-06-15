package net.silentchaos512.gems.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.render.entity.EnderSlimeRenderer;
import net.silentchaos512.gems.entity.EnderSlimeEntity;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModEntities {
    ENDER_SLIME(() -> EntityType.Builder.create(EnderSlimeEntity::new, EntityClassification.MONSTER), 0x003333, 0xAA00AA);

    private final Lazy<EntityType<?>> entityType;
    private final Lazy<SpawnEggItem> spawnEgg;

    ModEntities(Supplier<EntityType.Builder<?>> factory, int eggPrimaryColor, int eggSecondaryColor) {
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
        if (!event.getRegistry().getRegistryName().equals(ForgeRegistries.ENTITIES.getRegistryName())) return;

        for (ModEntities entity : values()) {
            EntityType<?> type = entity.type();
            type.setRegistryName(SilentGems.getId(entity.getName()));
            ForgeRegistries.ENTITIES.register(type);

            EntitySpawnPlacementRegistry.register(
                    type,
                    EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(EnderSlimeEntity.class, new EnderSlimeRenderer.Factory());
    }
}
