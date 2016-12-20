package net.silentchaos512.gems.compat.tconstruct;

import net.silentchaos512.gems.lib.EnumGem;
import slimeknights.tconstruct.library.materials.Material;

public class TConstructMaterialGem extends Material {

  public TConstructMaterialGem(EnumGem gem) {

    super("silentgems:" + gem.name().toLowerCase(), gem.getColor());
    // TODO Auto-generated constructor stub
  }

}
