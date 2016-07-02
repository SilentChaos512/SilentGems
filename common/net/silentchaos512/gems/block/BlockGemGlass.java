package net.silentchaos512.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.block.BlockSL;

public class BlockGemGlass extends BlockGemSubtypes {

  public BlockGemGlass(boolean dark) {

    super(dark, "GemGlass" + (dark ? "Dark" : ""), Material.GLASS);
    setHardness(0.3f);
  }

  @Override
  public void addRecipes() {

  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.TRANSLUCENT;
  }
}
