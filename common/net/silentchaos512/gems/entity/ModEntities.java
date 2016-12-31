package net.silentchaos512.gems.entity;

import java.util.List;

import com.google.common.collect.Lists;

import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket;
import net.silentchaos512.gems.entity.packet.EntityPacketAttack;
import net.silentchaos512.gems.entity.packet.EntityPacketChaos;
import net.silentchaos512.gems.entity.packet.EntityPacketLevitation;
import net.silentchaos512.gems.entity.packet.EntityPacketRegen;
import net.silentchaos512.gems.entity.packet.EntityPacketRepair;
import net.silentchaos512.gems.entity.packet.EntityPacketSaturation;
import net.silentchaos512.lib.registry.SRegistry;

public class ModEntities {

  public static List<Class> nodePacketClasses = Lists.newArrayList();

  public static void init(SRegistry reg) {

    reg.registerEntity(EntityChaosProjectile.class, "ChaosProjectile");
    reg.registerEntity(EntityChaosProjectileHoming.class, "ChaosProjectileHoming");
    reg.registerEntity(EntityChaosProjectileSweep.class, "ChaosProjectileSweep");

    reg.registerEntity(EntityThrownTomahawk.class, "ThrownTomahawk");

    registerNodePacket(EntityPacketChaos.class, "Chaos");
    registerNodePacket(EntityPacketRepair.class, "Repair");
    registerNodePacket(EntityPacketRegen.class, "Regen");
    registerNodePacket(EntityPacketSaturation.class, "Saturation");
    registerNodePacket(EntityPacketAttack.class, "Attack");
    registerNodePacket(EntityPacketLevitation.class, "Leviation");
  }

  private static void registerNodePacket(Class<? extends EntityChaosNodePacket> clazz, String name) {

    nodePacketClasses.add(clazz);
    SilentGems.registry.registerEntity(clazz, "NodePacket" + name);
  }
}
