package net.silentchaos512.gems.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class ItemToolUpgrade extends ItemSG {

  public static final String[] NAMES = { Names.UPGRADE_IRON_TIP, Names.UPGRADE_DIAMOND_TIP };

  public ItemToolUpgrade() {

    icons = new IIcon[NAMES.length];
    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.TOOL_UPGRADE);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    int meta = stack.getItemDamage();
    String line;

    switch (meta) {
      case 0:
      case 1:
        // Tipped upgrades
        
        // Mining level
        line = LocalizationHelper.getItemDescription("TipUpgrade", 1);
        line = String.format(line,
            meta == 0 ? Config.MINING_LEVEL_IRON_TIP : Config.MINING_LEVEL_DIAMOND_TIP);
        list.add(EnumChatFormatting.GREEN + line);

        // Durability boost
        line = LocalizationHelper.getItemDescription("TipUpgrade", 2);
        line = String.format(line,
            meta == 0 ? Config.DURABILITY_BOOST_IRON_TIP : Config.DURABILITY_BOOST_DIAMOND_TIP);
        list.add(EnumChatFormatting.GREEN + line);

        // How to use
        line = LocalizationHelper.getItemDescription("TipUpgrade", 3);
        list.add(EnumChatFormatting.DARK_GRAY + line);
        line = LocalizationHelper.getItemDescription("TipUpgrade", 4);
        list.add(EnumChatFormatting.DARK_GRAY + line);
        break;
    }
  }

  public ItemStack applyToTool(ItemStack tool, ItemStack upgrade) {

    if (tool == null || upgrade == null) {
      return null;
    }

    int meta = upgrade.getItemDamage();
    ItemStack result = tool.copy();
    
    switch (meta) {
      case 0:
      case 1:
        // Tipped upgrades
        int currentTip = ToolHelper.getToolHeadTip(result);
        int upgradeValue = meta + 1;
        if (upgradeValue <= currentTip) {
          return null;
        }
        ToolHelper.setToolHeadTip(result, upgradeValue);
        return result;
      default:
        LogHelper.debug("Unknown tool upgrade: meta = " + meta);
        return null;
    }
  }

  @Override
  public void addRecipes() {

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 0), "ingotIron",
        Items.flint, "plankWood", "stickWood"));
    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(this, 1, 1), "gemDiamond",
        Items.flint, "plankWood", "stickWood"));
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

    for (int i = 0; i < NAMES.length; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    String name;
    int meta = stack.getItemDamage();
    if (meta >= 0 && meta < NAMES.length) {
      name = NAMES[meta];
    } else {
      name = "Unknown";
    }
    return getUnlocalizedName(name);
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    for (int i = 0; i < NAMES.length; ++i) {
      icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + NAMES[i]);
    }
  }
}
