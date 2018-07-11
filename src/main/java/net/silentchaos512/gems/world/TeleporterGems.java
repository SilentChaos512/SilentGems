package net.silentchaos512.gems.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterGems extends Teleporter {

  public TeleporterGems(WorldServer worldIn) {

    super(worldIn);
  }

  @Override
  public void placeInPortal(Entity entity, float yaw) {

  }
}
