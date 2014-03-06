package silent.gems.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import silent.gems.SilentGems;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities {

    public static void init() {

        // TODO
    }

    private static void registerMob(Class<? extends EntityLiving> mobClass, String name, int modID, boolean addSpawn,
            EnumCreatureType creatureType, int spawnWeight, int spawnMin, int spawnMax, BiomeGenBase[] biomes, int foregroundColor,
            int backgroundColor) {

        EntityRegistry.registerGlobalEntityID(mobClass, name, EntityRegistry.findGlobalUniqueEntityId(), foregroundColor, backgroundColor);
        EntityRegistry.registerModEntity(mobClass, name, modID, SilentGems.instance, 80, 3, true);

        if (addSpawn) {
            EntityRegistry.addSpawn(mobClass, spawnWeight, spawnMin, spawnMax, creatureType, biomes);
        }
    }
}
