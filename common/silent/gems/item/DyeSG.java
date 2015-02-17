package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.SilentGems;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;

public class DyeSG extends ItemSG {

  public DyeSG() {

    super(16);

    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.DYE);
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("dyeBlack", new ItemStack(this, 1, 0));
    OreDictionary.registerOre("dyeBlue", new ItemStack(this, 1, 4));
  }

  @Override
  public String[] getVariantNames() {

    String s = SilentGems.MOD_ID + ":";
    return new String[] { s + "DyeBlack", null, null, null, s + "DyeBlue" };
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    list.add(new ItemStack(this, 1, 0));
    list.add(new ItemStack(this, 1, 4));
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    String s = LocalizationHelper.ITEM_PREFIX + itemName;
    if (stack.getItemDamage() == 0) {
      return s + "Black";
    } else if (stack.getItemDamage() == 4) {
      return s + "Blue";
    } else {
      return s + "Unknown";
    }
  }
}
