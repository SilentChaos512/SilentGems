package silent.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IAddThaumcraftStuff;
import silent.gems.core.registry.IHasVariants;
import silent.gems.lib.Strings;

public class BlockSG extends Block implements IAddRecipe, IHasVariants, IAddThaumcraftStuff {

  protected int subBlockCount = 1; // TODO: Is this really used?
  public boolean hasSubtypes = false;
  protected boolean gemSubtypes = false;
  protected String blockName = "null";

  public BlockSG(int subBlockCount, Material material) {

    super(material);

    this.subBlockCount = subBlockCount;
    setHardness(3.0f);
    setResistance(30.0f);
    setStepSound(Block.soundTypeMetal);
    setCreativeTab(SilentGems.tabSilentGems);
    setUnlocalizedName(Strings.RESOURCE_PREFIX + blockName);
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

  @Override
  public int damageDropped(IBlockState state) {

    return hasSubtypes ? this.getMetaFromState(state) : 0;
  }

  public boolean getHasGemSubtypes() {

    return gemSubtypes;
  }
  
  public Block setHasGemSubtypes(boolean value) {

    gemSubtypes = value;
    return this;
  }

  public boolean getHasSubtypes() {

    return hasSubtypes;
  }

  public Block setHasSubtypes(boolean value) {

    hasSubtypes = value;
    return this;
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

    return SilentGems.MOD_ID + ":" + blockName;
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

  @Override
  public Block setUnlocalizedName(String blockName) {

    this.blockName = blockName;
    return this;
  }
}
