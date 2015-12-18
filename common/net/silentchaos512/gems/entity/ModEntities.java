package net.silentchaos512.gems.entity;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.projectile.EntityProjectileChaosOrb;

public class ModEntities {

  public static void init() {

    int id = -1;
    EntityRegistry.registerModEntity(EntityProjectileChaosOrb.class, "ChaosOrb", ++id,
        SilentGems.instance, 64, 10, true);
  }
}
