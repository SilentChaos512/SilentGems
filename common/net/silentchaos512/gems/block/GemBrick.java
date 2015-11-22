package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GemBrick extends BlockSG {

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GemBrick(String name) {

    super(EnumGem.values().length, Material.rock);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    setHardness(2.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeStone);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(name);
  }

  @Override
  public void addRecipes() {

    if (blockName.equals(Names.GEM_BRICK_COATED)) {
      for (EnumGem gem : EnumGem.values()) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 8, gem.id), "sss", "sgs",
            "sss", 's', Blocks.stonebrick, 'g', gem.getItemOreName()));
      }
    } else {
      for (EnumGem gem : EnumGem.values()) {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 8, gem.id), "sss", "sgs",
            "sss", 's', Blocks.stonebrick, 'g', gem.getShardOreName()));
      }
    }
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(VARIANT, EnumGem.get(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumGem) state.getValue(VARIANT)).id;
  }

  @Override
  protected BlockState createBlockState() {

    return new BlockState(this, new IProperty[] { VARIANT });
  }
}
