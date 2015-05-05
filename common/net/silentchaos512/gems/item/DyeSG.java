package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class DyeSG extends ItemSG {

  IIcon iconBlack;
  IIcon iconBlue;
  
  public DyeSG() {

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
  public IIcon getIconFromDamage(int meta) {
    
    if (meta == 0) {
      return iconBlack;
    } else if (meta == 4) {
      return iconBlue;
    } else {
      return null;
    }
  }
  
  @Override
  public void registerIcons(IIconRegister reg) {
    
    iconBlack = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName + "Black");
    iconBlue = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName + "Blue");
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
