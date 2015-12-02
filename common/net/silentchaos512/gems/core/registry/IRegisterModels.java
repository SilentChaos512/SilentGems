package net.silentchaos512.gems.core.registry;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRegisterModels {

  @SideOnly(Side.CLIENT)
  public void registerModels();
}
