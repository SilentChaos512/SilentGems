package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.lib.buff.ChaosBuff;

public class ChaosRune extends ItemSG {

  public final static String COST = "Cost";
  public final static String MAX_LEVEL = "MaxLevel";

  public ChaosRune() {

    setMaxStackSize(16);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.CHAOS_RUNE);
    rarity = EnumRarity.RARE;
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    int meta = stack.getItemDamage();

    if (meta >= 0 && meta < ChaosBuff.values().length) {
      ChaosBuff buff = ChaosBuff.values()[meta];
      // Name
      list.add(EnumChatFormatting.GOLD
          + LocalizationHelper.getLocalizedString(Strings.BUFF_RESOURCE_PREFIX + buff.name));
      if (shifted) {
        // Cost
        String s = LocalizationHelper.getOtherItemKey(itemName, COST);
        list.add(EnumChatFormatting.DARK_GREEN + String.format(s, buff.cost));
        // Max Level
        s = LocalizationHelper.getOtherItemKey(itemName, MAX_LEVEL);
        list.add(EnumChatFormatting.DARK_GREEN + String.format(s, buff.maxLevel));
        // Buff description
//        if (this.showFlavorText()) {
//          list.add(LocalizationHelper.getOtherItemKey(itemName, buff.name));
//        }
        // Information on how to use.
        list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 0));
      } else {
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getMiscText(Strings.PRESS_SHIFT));
      }
    } else {
      list.add(EnumChatFormatting.RED + "Invalid meta value!");
    }
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tabs, List list) {

    for (int i = 0; i < ChaosBuff.values().length; ++i) {
      list.add(new ItemStack(this, 1, i));
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(itemName);
  }
  
  @Override
  public String[] getVariantNames() {
    
    String[] result = new String[ChaosBuff.values().length];
    for (int i = 0; i < result.length; ++i) {
      result[i] = getFullName();
    }
    return result;
  }
}
