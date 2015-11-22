package net.silentchaos512.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GemOre extends BlockSG {

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GemOre() {

    super(EnumGem.values().length, Material.rock);
    setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    setHardness(3.0f);
    setResistance(15.0f);
    setStepSound(Block.soundTypeStone);
    setHarvestLevel("pickaxe", 2);

    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setUnlocalizedName(Names.GEM_ORE);
  }

  @Override
  public void addOreDict() {

    for (EnumGem gem : EnumGem.values()) {
      OreDictionary.registerOre(gem.getOreBlockOreName(), new ItemStack(this, 1, gem.id));
    }
  }

  @Override
  public void addRecipes() {

    for (int i = 0; i < EnumGem.values().length; ++i) {
      ItemStack ore = new ItemStack(this, 1, i);
      ItemStack gem = new ItemStack(ModItems.gem, 1, i);
      ItemStack gemBonus = new ItemStack(ModItems.gem, 2, i);

      // Vanilla smelting
      GameRegistry.addSmelting(ore, gem, 0.5f);
      // Redstone furnace
//      ThermalExpansionHelper.addFurnaceRecipe(1600, ore, gem);
      // Pulverizer
//      ThermalExpansionHelper.addPulverizerRecipe(4000, ore, gemBonus);
      // Sag mill
      RecipeHelper.addSagMillRecipe(EnumGem.get(i).name, 3000, "");
    }
  }

  @Override
  public void addThaumcraftStuff() {

//    for (EnumGem gem : EnumGem.values()) {
//      ItemStack out = new ItemStack(gem.getShard().getItem(), 0, gem.id);
//      ThaumcraftApi.addSmeltingBonus(gem.getOre(), out);
//    }
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
  public int damageDropped(IBlockState state) {

    return ((EnumGem) state.getValue(VARIANT)).id;
  }

  @Override
  public int getExpDrop(IBlockAccess world, BlockPos pos, int fortune) {

    if (getItemDropped(world.getBlockState(pos), RANDOM, fortune) != Item.getItemFromBlock(this)) {
      return 1 + SilentGems.instance.random.nextInt(5);
    }
    return 0;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {

    return ModItems.gem;
  }

  @Override
  public int quantityDropped(Random random) {

    return 1;
  }

  @Override
  public int quantityDroppedWithBonus(int fortune, Random random) {

    if (fortune > 0) {
      int j = random.nextInt(fortune + 2) - 1;

      if (j < 0) {
        j = 0;
      }

      return quantityDropped(random) * (j + 1);
    } else {
      return quantityDropped(random);
    }
  }
}
