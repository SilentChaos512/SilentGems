package net.silentchaos512.gems.block;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.block.BlockSL;

public class BlockGemSubtypes extends BlockSL {

  public final boolean isDark;

  public BlockGemSubtypes(String name) {

    super(1, SilentGems.MODID, name, Material.ROCK);
    isDark = false;
  }

  public BlockGemSubtypes(boolean isDark, String name) {

    this(16, isDark, name, Material.ROCK);
  }

  public BlockGemSubtypes(boolean isDark, String name, Material material) {

    this(16, isDark, name, material);
  }

  public BlockGemSubtypes(int subtypeCount, boolean isDark, String name, Material material) {

    super(subtypeCount, SilentGems.MODID, name, material);
    this.isDark = isDark;

    setDefaultState(blockState.getBaseState().withProperty(EnumGem.VARIANT_GEM, EnumGem.RUBY));
    setUnlocalizedName(name);
  }

  public EnumGem getGem(int meta) {

    if (meta < 0 || meta > 15) {
      return EnumGem.RUBY;
    }
    return EnumGem.values()[meta + (isDark ? 16 : 0)];
  }

  @Override
  public boolean hasSubtypes() {

    return true;
  }

  @Override
  public int damageDropped(IBlockState state) {

    return getGem(getMetaFromState(state)).ordinal() & 0xF;
  }

  @Override
  public int quantityDropped(Random random) {

    return 1;
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return getDefaultState().withProperty(EnumGem.VARIANT_GEM, EnumGem.values()[meta]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumGem) state.getValue(EnumGem.VARIANT_GEM)).ordinal() & 0xF;
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, EnumGem.VARIANT_GEM);
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    List list = Lists.newArrayList();
    for (int i = 0; i < 16; ++i) {
      EnumGem gem = EnumGem.values()[i];
      list.add(new ModelResourceLocation(getFullName(), "gem=" + gem.getName()));
    }
    return list;
  }
}
