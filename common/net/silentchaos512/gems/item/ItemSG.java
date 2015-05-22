package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IAddThaumcraftStuff;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Strings;

public class ItemSG extends Item implements IAddRecipe, IAddThaumcraftStuff {

  public IIcon[] icons = null;
  protected boolean gemSubtypes = false;
  protected String itemName = "null";
  protected boolean isGlowing = false;
  protected EnumRarity rarity = EnumRarity.common;

  public ItemSG() {

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
    return !Config.hideFlavorTextAlways
        && ((Config.hideFlavorTextUntilShift && shifted) || !Config.hideFlavorTextUntilShift);
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

  @Override
  public IIcon getIconFromDamage(int meta) {

    if (hasSubtypes && icons != null && meta < icons.length) {
      return icons[meta];
    } else {
      return super.getIconFromDamage(meta);
    }
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
      for (int i = 0; i < icons.length; ++i) {
        list.add(new ItemStack(this, 1, i));
      }
    } else {
      list.add(new ItemStack(this, 1, 0));
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    int d = stack.getItemDamage();
    String s = LocalizationHelper.ITEM_PREFIX + itemName;

    if ((gemSubtypes && d < EnumGem.all().length) || hasSubtypes) {
      s += d;
    }

    return s;
  }

  public String getUnlocalizedName(String itemName) {

    return LocalizationHelper.ITEM_PREFIX + itemName;
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return isGlowing;
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    if (gemSubtypes) {
      registerIconsForGemSubtypes(reg);
    } else {
      itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName);
    }
  }

  public void registerIconsForGemSubtypes(IIconRegister reg) {

    if (icons == null || icons.length != EnumGem.all().length) {
      icons = new IIcon[EnumGem.all().length];
    }

    for (int i = 0; i < EnumGem.all().length; ++i) {
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + this.itemName + EnumGem.all()[i].id);
    }
  }

  public void setHasGemSubtypes(boolean value) {

    gemSubtypes = value;
  }

  @Override
  public Item setUnlocalizedName(String itemName) {

    this.itemName = itemName;
    return this;
  }
}