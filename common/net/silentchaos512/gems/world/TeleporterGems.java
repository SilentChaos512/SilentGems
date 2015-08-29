package net.silentchaos512.gems.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;


public class TeleporterGems extends Teleporter {

  public TeleporterGems(WorldServer worldServer) {
    
    super(worldServer);
  }

  @Override
  public void placeInPortal(Entity entity, double par2, double par4, double par6, float par8) {

  }
}
