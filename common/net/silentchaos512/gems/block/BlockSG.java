package net.silentchaos512.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IAddThaumcraftStuff;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Strings;

public class BlockSG extends Block implements IAddRecipe, IAddThaumcraftStuff {

  public IIcon[] icons = null;
  public boolean hasSubtypes = false;
  protected boolean gemSubtypes = false;
  protected String blockName = "null";

  public BlockSG(Material material) {

    super(material);

    setHardness(3.0f);
    setResistance(10.0f);
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(SilentGems.tabSilentGems);
    setBlockTextureName(Strings.RESOURCE_PREFIX + blockName);
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
    
    return EnumRarity.common;
  }

  @Override
  public int damageDropped(int meta) {

    return hasSubtypes ? meta : 0;
  }

  public boolean getHasGemSubtypes() {

    return gemSubtypes;
  }

  public boolean getHasSubtypes() {

    return hasSubtypes;
  }

  @Override
  public IIcon getIcon(int side, int meta) {

    if (hasSubtypes && meta < icons.length) {
      return icons[meta];
    } else {
      return blockIcon;
    }
  }

  @Override
  public void getSubBlocks(Item item, CreativeTabs tab, List subItems) {

    if (hasSubtypes) {
      for (int i = 0; i < icons.length; ++i) {
        subItems.add(new ItemStack(this, 1, i));
      }
    } else {
      super.getSubBlocks(item, tab, subItems);
    }
  }

  @Override
  public String getUnlocalizedName() {

    return "tile." + this.blockName;
  }

  @Override
  public void registerBlockIcons(IIconRegister reg) {

    if (gemSubtypes) {
      registerIconsForGemSubtypes(reg);
    } else {
      blockIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + blockName);
    }
  }

  public void registerIconsForGemSubtypes(IIconRegister reg) {

    if (icons == null || icons.length != EnumGem.all().length) {
      icons = new IIcon[EnumGem.all().length];
    }

    for (int i = 0; i < EnumGem.all().length; ++i) {
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + this.blockName + i);
    }
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
    setBlockName(blockName);
    return this;
  }
}
