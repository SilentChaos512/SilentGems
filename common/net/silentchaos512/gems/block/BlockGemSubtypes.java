package net.silentchaos512.gems.block;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockSL;

public class BlockGemSubtypes extends BlockSL {

  public final EnumGem.Set gemSet;

  public BlockGemSubtypes(String name) {

    super(1, SilentGems.MODID, name, Material.ROCK);
    this.gemSet = EnumGem.Set.CLASSIC;
  }

  public BlockGemSubtypes(EnumGem.Set set, String name) {

    this(16, set, name, Material.ROCK);
  }

  public BlockGemSubtypes(EnumGem.Set set, String name, Material material) {

    this(16, set, name, material);
  }

  public BlockGemSubtypes(int subtypeCount, EnumGem.Set set, String name, Material material) {

    super(subtypeCount, SilentGems.MODID, name, material);
    this.gemSet = set;

    setDefaultState(blockState.getBaseState().withProperty(EnumGem.VARIANT_GEM, EnumGem.RUBY));
    setUnlocalizedName(name);
  }

  protected static String nameForSet(EnumGem.Set set, String baseName) {

    switch (set) {
      case CLASSIC:
        return baseName;
      case DARK:
        return baseName + "Dark";
      case LIGHT:
        return baseName + "Light";
      default:
        return baseName + "Unknown";
    }
  }

  public EnumGem getGem(int meta) {

    if (meta < 0 || meta > 15) {
      return EnumGem.RUBY;
    }
    return EnumGem.values()[meta + gemSet.startMeta];
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

    return getDefaultState().withProperty(EnumGem.VARIANT_GEM, EnumGem.values()[meta & 0xF]);
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
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = EnumGem.values()[i];
      models.put(i, new ModelResourceLocation(getFullName().toLowerCase(), "gem=" + gem.getName()));
    }
  }
}
