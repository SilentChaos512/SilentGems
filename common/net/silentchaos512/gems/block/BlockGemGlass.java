package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.registry.RecipeMaker;

public class BlockGemGlass extends BlockGemSubtypes {

  public BlockGemGlass(EnumGem.Set set) {

    super(set, nameForSet(set, "GemGlass"), Material.GLASS);
    setHardness(0.3f);
    setSoundType(SoundType.GLASS);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    for (int i = 0; i < 16; ++i) {
      EnumGem gem = getGem(i);
      recipes.addSurroundOre(blockName + i, new ItemStack(this, 8, i), gem.getShardOreName(), "blockGlass");
    }
  }

  @Override
  public void addOreDict() {

    for (int i = 0; i < 16; ++i) {
      OreDictionary.registerOre("blockGlass", new ItemStack(this, 1, i));
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {

    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) {

    return false;
  }

  @Override
  public boolean isFullCube(IBlockState state) {

    return false;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess,
      BlockPos pos, EnumFacing side) {

    IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
    Block block = iblockstate.getBlock();

    if (this == block && blockState != iblockstate)
      return true;

    return block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
  }
}
