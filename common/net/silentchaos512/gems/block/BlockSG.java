package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IAddThaumcraftStuff;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.lib.Names;

public class BlockSG extends Block implements IAddRecipe, IHasVariants, IAddThaumcraftStuff {

  protected int subBlockCount;
  public boolean hasSubtypes = false;
  protected boolean gemSubtypes = false;
  protected String blockName = "null";

  public BlockSG(int subBlockCount, Material material) {

    super(material);

    this.subBlockCount = subBlockCount;
    setHardness(3.0f);
    setResistance(10.0f);
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public void addRecipes() {

  }

  @Override
  public void addThaumcraftStuff() {

  }

  public EnumRarity getRarity(ItemStack stack) {

    return EnumRarity.COMMON;
  }

  @Override
  public int damageDropped(IBlockState state) {

    return hasSubtypes ? this.getMetaFromState(state) : 0;
  }

  public boolean getHasGemSubtypes() {

    return gemSubtypes;
  }

  public boolean getHasSubtypes() {

    return hasSubtypes;
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

    if (hasSubtypes) {
      for (int i = 0; i < subBlockCount; ++i) {
        subItems.add(new ItemStack(this, 1, i));
      }
    } else {
      super.getSubBlocks(item, tab, subItems);
    }
  }

  public int getSubBlockCount() {

    return subBlockCount;
  }

  @Override
  public String getName() {

    return blockName;
  }

  @Override
  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + blockName);
  }

  @Override
  public String[] getVariantNames() {

    if (hasSubtypes) {
      String[] names = new String[subBlockCount];
      for (int i = 0; i < names.length; ++i) {
        names[i] = getFullName() + i;
      }
      return names;
    }
    return new String[] { getFullName() };
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + this.blockName;
  }

  public Block setHasGemSubtypes(boolean value) {

    this.gemSubtypes = value;
    return this;
  }

  public Block setHasSubtypes(boolean value) {

    this.hasSubtypes = value;
    return this;
  }

  public Block setUnlocalizedName(String blockName) {

    this.blockName = blockName;
    return this;
  }
}
