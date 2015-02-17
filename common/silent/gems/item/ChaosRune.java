package silent.gems.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import silent.gems.SilentGems;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.lib.buff.ChaosBuff;

public class ChaosRune extends ItemSG {

  public final static String COST = "Cost";
  public final static String MAX_LEVEL = "MaxLevel";

  public ChaosRune() {
    
    super(ChaosBuff.all.size());

    setMaxStackSize(16);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.CHAOS_RUNE);
    rarity = EnumRarity.RARE;
  }

  @SuppressWarnings("unused")
  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    int d = stack.getItemDamage();

    if (d >= 0 && d < ChaosBuff.all.size()) {
      // Name
      list.add(EnumChatFormatting.GOLD
          + LocalizationHelper.getLocalizedString(Strings.BUFF_RESOURCE_PREFIX
              + ChaosBuff.all.get(d).name));
      // Cost
      String s = LocalizationHelper.getOtherItemKey(itemName, COST);
      list.add(EnumChatFormatting.DARK_GREEN + String.format(s, ChaosBuff.all.get(d).cost));
      // Max Level
      s = LocalizationHelper.getOtherItemKey(itemName, MAX_LEVEL);
      list.add(EnumChatFormatting.DARK_GREEN + String.format(s, ChaosBuff.all.get(d).maxLevel));
      // Buff description
      list.add(LocalizationHelper.getOtherItemKey(itemName, ChaosBuff.all.get(d).name));
      // Flight broken?
      if (ChaosBuff.all.get(d).name.equals(ChaosBuff.FLIGHT) && ChaosBuff.FLIGHT_IS_BROKEN) {
        list.add(EnumChatFormatting.RED + "Broke like a bad joke");
      }
    } else {
      list.add(EnumChatFormatting.RED + "Invalid meta value!");
    }

    // Information on how to use.
    list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 0));
  }
  
  @Override
  public String[] getVariantNames() {
    
    String[] result = new String[ChaosBuff.all.size()];
    for (int i = 0; i < ChaosBuff.all.size(); ++i) {
      result[i] = getFullName();
    }
    return result;
  }
  
  @Override
  public String getName() {
    
    return Names.CHAOS_RUNE;
  }
  
  @Override
  public String getFullName() {
    
    return SilentGems.MOD_ID + ":" + Names.CHAOS_RUNE;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tabs, List list) {

    for (int i = 0; i < ChaosBuff.all.size(); ++i) {
      list.add(new ItemStack(this, 1, i));
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(itemName);
  }
}
