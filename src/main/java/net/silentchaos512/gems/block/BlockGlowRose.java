package net.silentchaos512.gems.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IHasSubtypes;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;
import java.util.Map;

public class BlockGlowRose extends BlockBush implements IRegistryObject, IHasSubtypes {

  public BlockGlowRose() {

    super();
    setDefaultState(blockState.getBaseState().withProperty(EnumGem.VARIANT_GEM, EnumGem.RUBY));
    setSoundType(SoundType.PLANT);

    lightValue = GemsConfig.GLOW_ROSE_LIGHT_LEVEL;
    setTranslationKey(Names.GLOW_ROSE);
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    Item dye = Items.DYE;
    Item dyeSG = ModItems.dye;

    // Flowers to dye.
    // 0=black
    addFlowerToDyeRecipe(recipes, EnumDyeColor.BLACK, EnumGem.ONYX);
    // 1=red
    addFlowerToDyeRecipe(recipes, EnumDyeColor.RED, EnumGem.RUBY);
    addFlowerToDyeRecipe(recipes, EnumDyeColor.RED, EnumGem.GARNET);
    // 2=green
    addFlowerToDyeRecipe(recipes, EnumDyeColor.GREEN, EnumGem.BERYL);
    // 3=brown
    // 4=blue
    addFlowerToDyeRecipe(recipes, EnumDyeColor.BLUE, EnumGem.SAPPHIRE);
    addFlowerToDyeRecipe(recipes, EnumDyeColor.BLUE, EnumGem.IOLITE);
    // 5=purple
    addFlowerToDyeRecipe(recipes, EnumDyeColor.PURPLE, EnumGem.AMETHYST);
    // 6=cyan
    addFlowerToDyeRecipe(recipes, EnumDyeColor.CYAN, EnumGem.INDICOLITE);
    // 7=light gray
    // 8=gray
    // 9=pink
    addFlowerToDyeRecipe(recipes, EnumDyeColor.PINK, EnumGem.MORGANITE);
    // 10=lime
    addFlowerToDyeRecipe(recipes, EnumDyeColor.LIME, EnumGem.PERIDOT);
    // 11=yellow
    addFlowerToDyeRecipe(recipes, EnumDyeColor.YELLOW, EnumGem.HELIODOR);
    // 12=light blue
    addFlowerToDyeRecipe(recipes, EnumDyeColor.LIGHT_BLUE, EnumGem.AQUAMARINE);
    // 13-magenta
    addFlowerToDyeRecipe(recipes, EnumDyeColor.MAGENTA, EnumGem.AGATE);
    // 14-orange
    addFlowerToDyeRecipe(recipes, EnumDyeColor.ORANGE, EnumGem.TOPAZ);
    addFlowerToDyeRecipe(recipes, EnumDyeColor.ORANGE, EnumGem.AMBER);
    // 15-white
    addFlowerToDyeRecipe(recipes, EnumDyeColor.WHITE, EnumGem.OPAL);
  }

  private void addFlowerToDyeRecipe(RecipeMaker recipes, EnumDyeColor dye, EnumGem gem) {

    ItemStack dyeStack;
    ItemStack glowRose = new ItemStack(this, 1, gem.ordinal() & 0xF);

    if (dye == EnumDyeColor.BLACK || dye == EnumDyeColor.BLUE) {
      dyeStack = new ItemStack(ModItems.dye, 2, dye.getDyeDamage());
    } else {
      dyeStack = new ItemStack(Items.DYE, 2, dye.getDyeDamage());
    }

    recipes.addShapeless(dye.name() + "_" + gem.name(), dyeStack, glowRose);
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return Names.GLOW_ROSE;
  }

  @Override
  public String getFullName() {

    return getModId() + ":" + getName();
  }

  @Override
  public String getModId() {

    return SilentGems.MODID;
  }

  @Override
  public void getModels(Map<Integer, ModelResourceLocation> models) {

    String name = getFullName().toLowerCase();
    for (int i = 0; i < 16; ++i) {
      models.put(i, new ModelResourceLocation(name + i, "inventory"));
    }
  }

  @Override
  public boolean registerModels() {

    return false;
  }

  // 1.10.2 compat - getSubBlocks
  public void func_149666_a(Item item, CreativeTabs tab, List<ItemStack> list) {

    for (int i = 0; i < 16; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  // 1.11 compat - geSubBlocks
  public void func_149666_a(Item item, CreativeTabs tab, NonNullList<ItemStack> list) {

    for (int i = 0; i < 16; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {

    for (int i = 0; i < 16; ++i) {
      list.add(new ItemStack(this, 1, i));
    }
  }

  @Override
  public String getTranslationKey() {

    return "tile." + Names.GLOW_ROSE;
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(EnumGem.VARIANT_GEM, EnumGem.values()[meta & 0xF]);
  }

  @Override
  public int getMetaFromState(IBlockState state) {

    return ((EnumGem) state.getValue(EnumGem.VARIANT_GEM)).ordinal();
  }

  @Override
  protected BlockStateContainer createBlockState() {

    return new BlockStateContainer(this, new IProperty[] { EnumGem.VARIANT_GEM });
  }

  @Override
  public boolean hasSubtypes() {

    return true;
  }
}
