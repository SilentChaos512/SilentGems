package net.silentchaos512.gems.block;

import java.util.List;

import com.google.common.collect.Lists;

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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IHasSubtypes;
import net.silentchaos512.lib.registry.IRegistryObject;

public class BlockGlowRose extends BlockBush implements IRegistryObject, IHasSubtypes {

  public BlockGlowRose() {

    super();
    setDefaultState(
        blockState.getBaseState().withProperty(EnumGem.VARIANT_GEM, EnumGem.RUBY));
    setSoundType(SoundType.PLANT);

    lightValue = GemsConfig.GLOW_ROSE_LIGHT_LEVEL;
    setUnlocalizedName(Names.GLOW_ROSE);
    setCreativeTab(SilentGems.instance.creativeTab);
  }

  @Override
  public void addRecipes() {

    Item dye = Items.DYE;
    Item dyeSG = ModItems.dye;

    // Flowers to dye.
    // 0=black
    addFlowerToDyeRecipe(EnumDyeColor.BLACK, EnumGem.ONYX);
    // 1=red
    addFlowerToDyeRecipe(EnumDyeColor.RED, EnumGem.RUBY);
    addFlowerToDyeRecipe(EnumDyeColor.RED, EnumGem.GARNET);
    // 2=green
    addFlowerToDyeRecipe(EnumDyeColor.GREEN, EnumGem.BERYL);
    // 3=brown
    // 4=blue
    addFlowerToDyeRecipe(EnumDyeColor.BLUE, EnumGem.SAPPHIRE);
    addFlowerToDyeRecipe(EnumDyeColor.BLUE, EnumGem.IOLITE);
    // 5=purple
    addFlowerToDyeRecipe(EnumDyeColor.PURPLE, EnumGem.AMETHYST);
    // 6=cyan
    addFlowerToDyeRecipe(EnumDyeColor.CYAN, EnumGem.INDICOLITE);
    // 7=light gray
    // 8=gray
    // 9=pink
    addFlowerToDyeRecipe(EnumDyeColor.PINK, EnumGem.MORGANITE);
    // 10=lime
    addFlowerToDyeRecipe(EnumDyeColor.LIME, EnumGem.PERIDOT);
    // 11=yellow
    addFlowerToDyeRecipe(EnumDyeColor.YELLOW, EnumGem.HELIODOR);
    // 12=light blue
    addFlowerToDyeRecipe(EnumDyeColor.LIGHT_BLUE, EnumGem.AQUAMARINE);
    // 13-magenta
    addFlowerToDyeRecipe(EnumDyeColor.MAGENTA, EnumGem.AGATE);
    // 14-orange
    addFlowerToDyeRecipe(EnumDyeColor.ORANGE, EnumGem.TOPAZ);
    addFlowerToDyeRecipe(EnumDyeColor.ORANGE, EnumGem.AMBER);
    // 15-white
    addFlowerToDyeRecipe(EnumDyeColor.WHITE, EnumGem.OPAL);
  }

  private void addFlowerToDyeRecipe(EnumDyeColor dye, EnumGem gem) {

    ItemStack dyeStack;
    ItemStack glowRose = new ItemStack(this, 1, gem.ordinal() & 0xF);

    if (dye == EnumDyeColor.BLACK || dye == EnumDyeColor.BLUE) {
      dyeStack = new ItemStack(ModItems.dye, 2, dye.getDyeDamage());
    } else {
      dyeStack = new ItemStack(Items.DYE, 2, dye.getDyeDamage());
    }

    GameRegistry.addShapelessRecipe(dyeStack, glowRose);
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

    return SilentGems.MOD_ID.toLowerCase();
  }

  @Override
  public List<ModelResourceLocation> getVariants() {

    List<ModelResourceLocation> list = Lists.newArrayList();
    for (int i = 0; i < 16; ++i) {
      list.add(new ModelResourceLocation(getFullName() + i, "inventory"));
    }
    return list;
  }

  @Override
  public boolean registerModels() {

    return false;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List list) {

    for (int i = 0; i < 16; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.GLOW_ROSE;
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {

    return this.getDefaultState().withProperty(EnumGem.VARIANT_GEM,
        EnumGem.values()[meta & 0xF]);
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
