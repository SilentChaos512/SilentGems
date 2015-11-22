package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class Gem extends ItemSG {

  public Gem() {

    setMaxStackSize(64);
    setHasSubtypes(true);
    setHasGemSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.GEM_ITEM);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    if (shifted) {
      int id = stack.getItemDamage() & 0xF;
      boolean supercharged = stack.getItemDamage() > 15;
      ToolMaterial material = EnumGem.get(id).getToolMaterial(supercharged);

      list.add(EnumChatFormatting.ITALIC
          + LocalizationHelper.getOtherItemKey(itemName, "ToolProperties"));
      String separator = EnumChatFormatting.DARK_GRAY
          + LocalizationHelper.getOtherItemKey(itemName, "Separator");
      list.add(separator);

      String formatInt = EnumChatFormatting.GOLD + "%s:" + EnumChatFormatting.RESET + " %d";
      String formatFloat = EnumChatFormatting.GOLD + "%s:" + EnumChatFormatting.RESET + " %.1f";

      // Durability
      String s = LocalizationHelper.getOtherItemKey(itemName, "MaxUses");
      s = String.format(formatInt, s, material.getMaxUses());
      list.add(s);

      // Efficiency
      s = LocalizationHelper.getOtherItemKey(itemName, "Efficiency");
      s = String.format(formatFloat, s, material.getEfficiencyOnProperMaterial());
      list.add(s);

      // Damage
      s = LocalizationHelper.getOtherItemKey(itemName, "Damage");
      s = String.format(formatInt, s, (int) material.getDamageVsEntity());
      list.add(s);

      // Enchantability
      s = LocalizationHelper.getOtherItemKey(itemName, "Enchantability");
      s = String.format(formatInt, s, material.getEnchantability());
      list.add(s);

      // Spawn weight
      if (stack.getItemDamage() < EnumGem.values().length) {
        s = LocalizationHelper.getOtherItemKey(itemName, "SpawnWeight");
        s = String.format(formatInt, s, Config.GEM_WEIGHTS.get(id).itemWeight);
        list.add(s);
      }

      list.add(separator);

      // Decorate tool hint.
      s = LocalizationHelper.getOtherItemKey(itemName, "Decorate1");
      list.add(EnumChatFormatting.DARK_AQUA + s);
      s = LocalizationHelper.getOtherItemKey(itemName, "Decorate2");
      list.add(EnumChatFormatting.DARK_AQUA + s);
    } else {
      list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getMiscText(Strings.PRESS_SHIFT));
    }
  }

  @Override
  public void addOreDict() {

    for (EnumGem gem : EnumGem.values()) {
      ItemStack item = gem.getItem();
      OreDictionary.registerOre(gem.getItemOreName(), item);
      OreDictionary.registerOre(Strings.ORE_DICT_GEM_BASIC, item);
    }
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);
    for (EnumGem gem : EnumGem.values()) {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, gem.id | 16), "ere", "ege",
          "ere", 'e', "gemChaos", 'r', "dustRedstone", 'g', gem.getItemOreName()));
    }
  }

  @Override
  public void addThaumcraftStuff() {

//    ThaumcraftApi.registerObjectTag(EnumGem.RUBY.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.TOOL, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.GARNET.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.FIRE, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.TOPAZ.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.EARTH, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.HELIODOR.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.DESIRE, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.PERIDOT.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.DEATH, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.EMERALD.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.MOTION, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.AQUAMARINE.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.AURA, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.SAPPHIRE.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.PROTECT, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.IOLITE.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.MECHANISM, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.AMETHYST.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.AVERSION, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.MORGANITE.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.MAN, 2));
//    ThaumcraftApi.registerObjectTag(EnumGem.ONYX.getItem(),
//        (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.DARKNESS, 2));
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    return (stack.getItemDamage() & 16) == 16 ? EnumRarity.RARE : EnumRarity.COMMON;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubItems(Item item, CreativeTabs tabs, List list) {

    int i;
    for (i = 0; i < EnumGem.values().length; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
    for (i = 16; i < 16 + EnumGem.values().length; ++i) {
      list.add(new ItemStack(item, 1, i));
    }
  }
  
  @Override
  public String[] getVariantNames() {

    int gemCount = EnumGem.values().length;
    String[] result = new String[28];

    int i = 0;
    // Regular gems
    for (; i < gemCount; ++i) {
      result[i] = getFullName() + i;
    }
    // Blanks
    for (; i < 16; ++i) {
      result[i] = null;
    }
    // Supercharged gems
    for (; i < 28; ++i) {
      result[i] = getFullName() + i;
    }

    return result;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.GEMS_PREFIX + stack.getItemDamage();
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return (stack.getItemDamage() & 16) == 16;
  }
}
