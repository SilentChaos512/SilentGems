package net.silentchaos512.gems.entity;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityPacketAttack;
import net.silentchaos512.gems.entity.packet.EntityPacketChaos;
import net.silentchaos512.gems.entity.packet.EntityPacketLevitation;
import net.silentchaos512.gems.entity.packet.EntityPacketRegen;
import net.silentchaos512.gems.entity.packet.EntityPacketRepair;
import net.silentchaos512.gems.entity.packet.EntityPacketSaturation;

public class ModEntities {

  private static int ID;

  public static void init() {

    ID = -1;

    register(EntityChaosProjectile.class, "ChaosProjectile");
    register(EntityChaosProjectileHoming.class, "ChaosProjectileHoming");
    register(EntityChaosProjectileSweep.class, "ChaosProjectileSweep");

    register(EntityThrownTomahawk.class, "ThrownTomahawk");

    register(EntityPacketChaos.class, "NodePacketChaos");
    register(EntityPacketRepair.class, "NodePacketRepair");
    register(EntityPacketRegen.class, "NodePacketRegen");
    register(EntityPacketSaturation.class, "NodePacketSaturation");
    register(EntityPacketAttack.class, "NodePacketAttack");
    register(EntityPacketLevitation.class, "NodePacketLeviation");
  }

  private static void register(Class<? extends Entity> clazz, String name) {

    EntityRegistry.registerModEntity(clazz, name, ++ID, SilentGems.instance, 64, 20, true);
  }
}
