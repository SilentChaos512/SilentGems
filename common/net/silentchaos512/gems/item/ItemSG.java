package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IAddThaumcraftStuff;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class ItemSG extends Item implements IAddRecipe, IAddThaumcraftStuff, IHasVariants {

  protected int subItemCount = 1;
  protected boolean gemSubtypes = false;
  protected String itemName = "null";
  protected boolean isGlowing = false;
  protected EnumRarity rarity = EnumRarity.COMMON;

  public ItemSG() {
    
    this(1);
  }

  public ItemSG(int subItemCount) {

    this.subItemCount = subItemCount;
    setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    int i = 1;
    String s = LocalizationHelper.getItemDescription(itemName, i);
    while (!s.equals(LocalizationHelper.getItemDescriptionKey(itemName, i)) && i < 8) {
      list.add(EnumChatFormatting.ITALIC + s);
      s = LocalizationHelper.getItemDescription(itemName, ++i);
    }

    if (i == 1) {
      s = LocalizationHelper.getItemDescription(itemName, 0);
      if (!s.equals(LocalizationHelper.getItemDescriptionKey(itemName, 0))) {
        list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getItemDescription(itemName, 0));
      }
    }
  }

  public static boolean showFlavorText() {

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    return !Config.HIDE_FLAVOR_TEXT_ALWAYS && ((Config.HIDE_FLAVOR_TEXT_UNTIL_SHIFT && shifted)
        || !Config.HIDE_FLAVOR_TEXT_UNTIL_SHIFT);
  }

  /**
   * Should be overridden if the deriving class needs ore dictionary entries.
   */
  @Override
  public void addOreDict() {

  }

  /**
   * Adds all recipes to make this item to the GameRegistry. Used to separate out recipe code so that ModItems is easier
   * to read.
   */
  @Override
  public void addRecipes() {

  }

  @Override
  public void addThaumcraftStuff() {

  }

  public String getLocalizedName(ItemStack stack) {

    return StatCollector.translateToLocal(getUnlocalizedName(stack) + ".name");
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    return rarity;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    if (hasSubtypes) {
      for (int i = 0; i < subItemCount; ++i) {
        list.add(new ItemStack(item, 1, i));
      }
    } else {
      list.add(new ItemStack(item, 1, 0));
    }
  }

  public int getSubItemCount() {

    return subItemCount;
  }

  @Override
  public String[] getVariantNames() {

    if (hasSubtypes) {
      String[] names = new String[subItemCount];
      for (int i = 0; i < names.length; ++i) {
        names[i] = getFullName() + i;
      }
      return names;
    }
    return new String[] { getFullName() };
  }

  public String getName() {

    return itemName;
  }

  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + itemName);
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    int meta = stack.getItemDamage();
    String str = LocalizationHelper.ITEM_PREFIX + itemName;

    if ((gemSubtypes && meta < EnumGem.values().length) || hasSubtypes) {
      str += meta;
    }

    return str;
  }

  public String getUnlocalizedName(String itemName) {

    return LocalizationHelper.ITEM_PREFIX + itemName;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return isGlowing;
  }

  public void setHasGemSubtypes(boolean value) {

    gemSubtypes = value;
    subItemCount = EnumGem.values().length;
  }

  @Override
  public Item setUnlocalizedName(String itemName) {

    this.itemName = itemName;
    return this;
  }
}
