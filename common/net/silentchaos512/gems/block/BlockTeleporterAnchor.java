package net.silentchaos512.gems.block;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.RecipeMaker;

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
  public void addRecipes(RecipeMaker recipes) {

    if (!GemsConfig.RECIPE_TELEPORTER_ANCHOR_DISABLE) {
      recipes.addShapedOre(blockName, new ItemStack(this, 4), "cec", " i ", "cec", 'c',
          "gemChaos", 'e', Items.ENDER_PEARL, 'i', Blocks.IRON_BLOCK);
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

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    models.put(0, new ModelResourceLocation(getFullName().toLowerCase(), "inventory"));
  }
}
