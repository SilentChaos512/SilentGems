package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Names;

public class DyeSG extends ItemSG {

  public DyeSG() {

    super(2);
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

    String prefix = SilentGems.MOD_ID + ":";
    String[] array = { prefix + "DyeBlack", null, null, null, prefix + "DyeBlue" };
    for (int i = 0; i < array.length; ++i) {
      if (array[i] != null) {
        array[i] = Names.convert(array[i]);
      }
    }
    return array;
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
