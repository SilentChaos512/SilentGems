package net.silentchaos512.gems.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class ItemToolUpgrade extends ItemSG {

  /**
   * The names of each sub-item. Order determines metadata, so this list cannot be rearranged.
   */
  public static final String[] NAMES = { Names.UPGRADE_IRON_TIP, Names.UPGRADE_DIAMOND_TIP,
      Names.UPGRADE_NO_GLINT, Names.UPGRADE_EMERALD_TIP, Names.UPGRADE_GOLD_TIP };
  /**
   * The order in which items will appear in NEI. This can be resorted freely with no ill effects.
   */
  public static final String[] SORTED_NAMES = { Names.UPGRADE_IRON_TIP, Names.UPGRADE_DIAMOND_TIP,
      Names.UPGRADE_EMERALD_TIP, Names.UPGRADE_GOLD_TIP, Names.UPGRADE_NO_GLINT };

  public static final int META_METALLURGY_START = EnumTipUpgrade.COPPER.id;

  public static final int META_IRON_TIPPED = 0;
  public static final int META_DIAMOND_TIPPED = 1;
  public static final int META_EMERALD_TIPPED = 3;
  public static final int META_GOLD_TIPPED = 4;
  public static final int META_NO_GLINT = 2;

  private Map<Integer, IIcon> iconMap = new HashMap<Integer, IIcon>();
  private IIcon iconDefault;

  public ItemToolUpgrade() {

    icons = new IIcon[NAMES.length];
    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.TOOL_UPGRADE);

    if (NAMES.length != SORTED_NAMES.length) {
      LogHelper.warning("ItemToolUpgrade: NAMES and SORTED_NAMES contain a different number of "
          + "items! This may cause some items to not show up!");
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    int meta = stack.getItemDamage();
    String line;
    String item;

    EnumTipUpgrade tip = getTipForUpgrade(meta);
    if (tip != EnumTipUpgrade.NONE) {

      /*
       * Tipped Upgrades
       */

      item = "TipUpgrade";
      // Mining level
      line = LocalizationHelper.getItemDescription(item, 1);
      line = String.format(line, tip.getMiningLevel());
      list.add(EnumChatFormatting.GREEN + line);

      // Durability boost
      line = LocalizationHelper.getItemDescription(item, 2);
      line = String.format(line, tip.getDurabilityBoost());
      list.add(EnumChatFormatting.GREEN + line);

      // Speed boost
      line = LocalizationHelper.getItemDescription(item, 3);
      line = String.format(line, tip.getSpeedBoost());
      list.add(EnumChatFormatting.GREEN + line);
    } else if (meta == META_NO_GLINT) {

      /*
       * No Glint
       */

      item = Names.UPGRADE_NO_GLINT;
      // Effect
      line = LocalizationHelper.getItemDescription(item, 1);
      list.add(EnumChatFormatting.GREEN + line);
      line = LocalizationHelper.getItemDescription(item, 2);
      list.add(EnumChatFormatting.GREEN + line);
    } else {

      /*
       * Unknown upgrade?
       */

      list.add(EnumChatFormatting.RED + "Error!");
      return;
    }

    // How to use
    line = LocalizationHelper.getOtherItemKey("ToolUpgrade", "HowToUse");
    list.add(EnumChatFormatting.DARK_GRAY + line);
  }

  public EnumTipUpgrade getTipForUpgrade(int meta) {

    int id = meta;
    switch (meta) {
      case META_IRON_TIPPED:
        id = 1;
        break;
      case META_DIAMOND_TIPPED:
        id = 2;
        break;
      case META_EMERALD_TIPPED:
        id = 3;
        break;
      case META_GOLD_TIPPED:
        id = 4;
        break;
    }
    return EnumTipUpgrade.getById(id);
  }

  public ItemStack applyToTool(ItemStack tool, ItemStack upgrade) {

    if (tool == null || upgrade == null) {
      return null;
    }

    int meta = upgrade.getItemDamage();
    ItemStack result = tool.copy();
    EnumTipUpgrade tip = EnumTipUpgrade.getById(meta);

    if (tip != EnumTipUpgrade.NONE) {
      // Tipped upgrades
      int newTip = tip.id;
      ToolHelper.setToolHeadTip(result, newTip);
      // Make sure durability would be OK.
      if (result.getItemDamage() > result.getItem().getMaxDamage(result)) {
        return null;
      }
      return result;
    } else if (meta == META_NO_GLINT) {
      // No glint
      boolean currentValue = ToolHelper.getToolNoGlint(result);
      ToolHelper.setToolNoGlint(result, !currentValue);
      return result;
    } else {
      // Unknown?
      LogHelper.debug("Unknown tool upgrade: meta = " + meta);
      return null;
    }
  }

  @Override
  public void addRecipes() {

    ItemStack base = CraftingMaterial.getStack(Names.UPGRADE_BASE);

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 0), "ingotIron", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), "gemDiamond", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 2), "dyeBlack", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 3), "gemEmerald", base));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 4), "ingotGold", base));
  }

  public void addMetallurgyTipRecipes() {

    ItemStack base = CraftingMaterial.getStack(Names.UPGRADE_BASE);

    int id = META_METALLURGY_START;
    EnumTipUpgrade tip = EnumTipUpgrade.getById(id);
    while (tip != EnumTipUpgrade.NONE) {
      GameRegistry.addRecipe(
          new ShapelessOreRecipe(new ItemStack(this, 1, id), tip.getMaterialName(), base));
      tip = EnumTipUpgrade.getById(++id);
    }
  }

  public ItemStack getStack(String name) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(this, 1, i);
      }
    }

    return null;
  }

  public static ItemStack getStack(String name, int count) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(ModItems.craftingMaterial, count, i);
      }
    }

    return null;
  }

  public static int getMetaFor(String name) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    int i = 0;
    for (; i < SORTED_NAMES.length; ++i) {
      list.add(getStack(SORTED_NAMES[i]));
    }
    for (; i < NAMES.length; ++i) {
      list.add(getStack(NAMES[i]));
    }

    if (SilentGems.instance.foundMetallurgy) {
      // Metallurgy tips
      int id = META_METALLURGY_START;
      EnumTipUpgrade tip = EnumTipUpgrade.getById(id);
      while (tip != EnumTipUpgrade.NONE) {
        list.add(new ItemStack(this, 1, id));
        tip = EnumTipUpgrade.getById(++id);
      }
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    String name;
    int meta = stack.getItemDamage();
    if (meta >= 0 && meta < NAMES.length) {
      name = NAMES[meta];
    } else {
      // Metallurgy tip?
      EnumTipUpgrade tip = EnumTipUpgrade.getById(meta);
      if (tip != EnumTipUpgrade.NONE) {
        name = "UpgradeTip." + tip.name().toLowerCase().replaceAll("_", "");
      } else {
        // No idea
        name = "Unknown";
      }
    }
    return getUnlocalizedName(name);
  }

  @Override
  public IIcon getIconFromDamage(int meta) {

    return iconMap.containsKey(meta) ? iconMap.get(meta) : iconDefault;
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    iconDefault = reg.registerIcon(Strings.RESOURCE_PREFIX + Names.UPGRADE_BASE);
    for (int i = 0; i < NAMES.length; ++i) {
      iconMap.put(i, reg.registerIcon(Strings.RESOURCE_PREFIX + NAMES[i]));
    }

    // Metallurgy tips
//    int id = META_METALLURGY_START;
//    EnumTipUpgrade tip = EnumTipUpgrade.getById(id);
//    while (tip != EnumTipUpgrade.NONE) {
//      iconMap.put(tip.id, reg.registerIcon(Strings.RESOURCE_PREFIX + tip.name().toLowerCase()));
//      tip = EnumTipUpgrade.getById(++id);
//    }
  }
}
