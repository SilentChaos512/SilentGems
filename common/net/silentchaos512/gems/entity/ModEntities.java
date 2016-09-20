package net.silentchaos512.gems.entity;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;

public class ModEntities {

  public static void init() {

    int id = -1;
    EntityRegistry.registerModEntity(EntityChaosNodePacket.class, "ChaosNodePacket", ++id,
        SilentGems.instance, 64, 10, true);
    EntityRegistry.registerModEntity(EntityChaosProjectile.class, "ChaosProjectile", ++id,
        SilentGems.instance, 64, 20, true);
    EntityRegistry.registerModEntity(EntityChaosProjectileHoming.class, "ChaosProjectileHoming",
        ++id, SilentGems.instance, 64, 20, true);
    EntityRegistry.registerModEntity(EntityChaosProjectileSweep.class, "ChaosProjectileSweep", ++id,
        SilentGems.instance, 64, 20, true);
    EntityRegistry.registerModEntity(EntityThrownTomahawk.class, "ThrownTomahawk", ++id,
        SilentGems.instance, 64, 20, true);
  }
}
