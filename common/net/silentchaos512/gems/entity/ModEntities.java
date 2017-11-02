package net.silentchaos512.gems.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.silentchaos512.lib.registry.SRegistry;

public class ModEntities {

  public static List<Class> nodePacketClasses = Lists.newArrayList();

  public static void init(SRegistry reg) {

    reg.registerEntity(EntityChaosProjectile.class, "ChaosProjectile", 64, 4, true);
    reg.registerEntity(EntityChaosProjectileHoming.class, "ChaosProjectileHoming", 64, 4, true);
    reg.registerEntity(EntityChaosProjectileSweep.class, "ChaosProjectileSweep", 64, 4, true);

    reg.registerEntity(EntityThrownTomahawk.class, "ThrownTomahawk");

    reg.registerEntity(EntityEnderSlime.class, "EnderSlime", 64, 4, false, 0x003333, 0xAA00AA);
    EntityRegistry.addSpawn(EntityEnderSlime.class, 10, 1, 4, EnumCreatureType.MONSTER, Biomes.SKY);
  }
}
