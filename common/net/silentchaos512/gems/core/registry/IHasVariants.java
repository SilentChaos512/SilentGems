package net.silentchaos512.gems.core.registry;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IHasVariants {

  @SideOnly(Side.CLIENT)
  public String[] getVariantNames();

  @SideOnly(Side.CLIENT)
  public String getName();

  @SideOnly(Side.CLIENT)
  public String getFullName();
}
