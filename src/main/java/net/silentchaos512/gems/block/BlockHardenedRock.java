package net.silentchaos512.gems.block;

import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockMisc.Type;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.block.BlockSL;

public class BlockHardenedRock extends BlockSL {

  public static enum Type implements IStringSerializable {

    STONE, NETHERRACK, END_STONE;

    @Override
    public String getName() {

      return name().toLowerCase();
    }
  }

  public static final PropertyEnum VARIANT = PropertyEnum.create("type", Type.class);

  public BlockHardenedRock() {

    super(Type.values().length, SilentGems.MODID, Names.HARDENED_ROCK, Material.ROCK);
    setHardness(50f);
    setResistance(2000f);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, Type.STONE));
    setHarvestLevel("pickaxe", 3);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, Type.values()[MathHelper.clamp(meta, 0, Type.values().length - 1)]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((Type) state.getValue(VARIANT)).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { VARIANT });
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    for (Type type : Type.values())
      models.put(type.ordinal(), new ModelResourceLocation(getFullName().toLowerCase(), "type=" + type.getName()));
  }
}
