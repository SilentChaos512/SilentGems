package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.lib.Names;

public class BlockTeleporterAnchor extends BlockTeleporter {

  public BlockTeleporterAnchor() {

    super(Names.TELEPORTER_ANCHOR);
    setDefaultState(blockState.getBaseState());
  }

  @Override
  public boolean hasSubtypes() {

    return false;
  }

  @Override
  public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player,
      boolean advanced) {

    return null;
  }

  @Override
  public void addRecipes() {

    if (!Config.RECIPE_TELEPORTER_ANCHOR_DISABLE) {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 4), "cec", " i ", "cec", 'c',
          "gemChaos", 'e', Items.ender_pearl, 'i', Blocks.iron_block));
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return getDefaultState();
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return 0;
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[0]);
  }
}
