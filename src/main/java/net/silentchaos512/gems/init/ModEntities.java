package net.silentchaos512.gems.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.EntityEnderSlime;

public class ModEntities {
    public static EntityType<EntityEnderSlime> ENDER_SLIME;

    public static void registerAll(RegistryEvent.Register<EntityType<?>> event) {
        IForgeRegistry<EntityType<?>> reg = event.getRegistry();

        ENDER_SLIME = register(reg, "ender_slime", EntityType.Builder.create(EntityEnderSlime.class, EntityEnderSlime::new));
        // TODO: How to add spawn eggs? ItemSpawnEgg
//        reg.registerEntity(EntityEnderSlime.class, "EnderSlime", 64, 4, false, 0x003333, 0xAA00AA);
        if (3 > 0) { // TODO: config
//            EntityRegistry.addSpawn(EntityEnderSlime.class, GemsConfig.ENDER_SLIME_SPAWN_WEIGHT,
//                    GemsConfig.ENDER_SLIME_SPAWN_MIN, GemsConfig.ENDER_SLIME_SPAWN_MAX,
//                    EnumCreatureType.MONSTER, Biomes.SKY);
            // TODO: Wat?
            EntitySpawnPlacementRegistry.register(ENDER_SLIME,
                    EntitySpawnPlacementRegistry.SpawnPlacementType.ON_GROUND,
                    Heightmap.Type.WORLD_SURFACE,
                    null);
        }
    }

    private static <T extends Entity> EntityType<T> register(IForgeRegistry<EntityType<?>> reg, String name, EntityType.Builder<T> builder) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        EntityType<T> entityType = builder.build(id.toString());
        entityType.setRegistryName(id);
        reg.register(entityType);
        return entityType;
    }
}
