package net.silentchaos512.gems.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class BlockTeleporterAnchor extends BlockTeleporter {

  public BlockTeleporterAnchor() {

    setDefaultState(blockState.getBaseState());
    subBlockCount = 1;

    this.isAnchor = true;
    this.setHasSubtypes(false);
    this.setHasGemSubtypes(false);
    this.setUnlocalizedName(Names.TELEPORTER_ANCHOR);
  }

  @Override
  public void addRecipes() {

    if (!Config.RECIPE_TELEPORTER_ANCHOR_DISABLED) {
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
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[0]);
  }
}
