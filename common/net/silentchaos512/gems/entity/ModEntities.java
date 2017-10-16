package net.silentchaos512.gems.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.silentchaos512.lib.registry.SRegistry;

public class ModEntities {

  public static List<Class> nodePacketClasses = Lists.newArrayList();

  public static void init(SRegistry reg) {

    reg.registerEntity(EntityChaosProjectile.class, "ChaosProjectile", 64, 4, true);
    reg.registerEntity(EntityChaosProjectileHoming.class, "ChaosProjectileHoming", 64, 4, true);
    reg.registerEntity(EntityChaosProjectileSweep.class, "ChaosProjectileSweep", 64, 4, true);

    reg.registerEntity(EntityThrownTomahawk.class, "ThrownTomahawk");
  }
}
