package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GemBlock extends BlockSG {

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GemBlock() {

    super(EnumGem.values().length, Material.iron);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    setHardness(3.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeStone);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_BLOCK);
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

  @Override
  public void addRecipes() {

    for (EnumGem gem : EnumGem.values()) {
      RecipeHelper.addCompressionRecipe(gem.getItem(), gem.getBlock(), 9);
    }
  }

  @Override
  public void addOreDict() {

    for (EnumGem gem : EnumGem.values()) {
      OreDictionary.registerOre(gem.getBlockOreName(), new ItemStack(this, 1, gem.id));
    }
  }
}
