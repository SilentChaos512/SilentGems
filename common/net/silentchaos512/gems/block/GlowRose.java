package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IHasSubtypes;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class GlowRose extends BlockBush implements IAddRecipe, IHasVariants, IHasSubtypes {

  public static final PropertyEnum VARIANT = PropertyEnum.create("variant", EnumGem.class);

  public GlowRose() {

    super(Material.plants);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumGem.RUBY));

    lightValue = Config.GLOW_ROSE_LIGHT_LEVEL;
    setUnlocalizedName(Names.GLOW_ROSE);
    setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addRecipes() {

    Item dye = Items.dye;
    Item dyeSG = SRegistry.getItem(Names.DYE);

    // Flowers to dye.
    // 0=black
    addFlowerToDyeRecipe(new ItemStack(dyeSG, 1, 0), new ItemStack(this, 1, EnumGem.ONYX.id));
    // 1=red
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 1), new ItemStack(this, 1, EnumGem.RUBY.id));
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 1), new ItemStack(this, 1, EnumGem.GARNET.id));
    // 2=green
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 2), new ItemStack(this, 1, EnumGem.EMERALD.id));
    // 3=brown
    // 4=blue
    addFlowerToDyeRecipe(new ItemStack(dyeSG, 1, 4), new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
    addFlowerToDyeRecipe(new ItemStack(dyeSG, 1, 4), new ItemStack(this, 1, EnumGem.IOLITE.id));
    // 5=purple
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 5), new ItemStack(this, 1, EnumGem.AMETHYST.id));
    // 6=cyan
    // 7=light gray
    // 8=gray
    // 9=pink
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 9), new ItemStack(this, 1, EnumGem.MORGANITE.id));
    // 10=lime
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 10), new ItemStack(this, 1, EnumGem.PERIDOT.id));
    // 11=yellow
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 11), new ItemStack(this, 1, EnumGem.HELIODOR.id));
    // 12=light blue
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 12), new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
    // 13-magenta
    // 14-orange
    addFlowerToDyeRecipe(new ItemStack(dye, 1, 14), new ItemStack(this, 1, EnumGem.TOPAZ.id));
  }

  private void addFlowerToDyeRecipe(ItemStack dye, ItemStack flower) {

    GameRegistry.addShapelessRecipe(new ItemStack(dye.getItem(), 2, dye.getItemDamage()), flower);
    // Pulverizer
//    ThermalExpansionHelper.addPulverizerRecipe(1600, flower,
//        new ItemStack(dye.getItem(), 4, dye.getItemDamage()));
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

    return SilentGems.MOD_ID + ":" + getName();
  }

  @Override
  public String[] getVariantNames() {

    String[] names = new String[EnumGem.values().length];
    for (int i = 0; i < names.length; ++i) {
      names[i] = getFullName() + i;
    }
    return names;
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + Names.GLOW_ROSE;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

    for (EnumGem gem : EnumGem.values()) {
      subItems.add(new ItemStack(item, 1, gem.id));
    }
  }

  @Override
  public boolean hasSubtypes() {

    return true;
  }

  @Override
  public boolean hasGemSubtypes() {

    return true;
  }

  @Override
  public int damageDropped(IBlockState state) {

    return this.getMetaFromState(state);
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
