package net.silentchaos512.gems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;

public class EnchantToken extends ItemSG {

  public static class EnchData {

    public int validTools;
    public Enchantment enchantment;
    public boolean gemToolsOnly;

    public String getName() {

      return enchantment.getName();
    }

    public int getMaxLevel() {

      return enchantment.getMaxLevel();
    }
  }

  // These constants are used to store which tools an enchantment is valid for. See init().
  public final static int T_ARMOR = 128;
  public final static int T_BOW = 64;
  public final static int T_SICKLE = 32;
  public final static int T_SWORD = 16;
  public final static int T_PICKAXE = 8;
  public final static int T_SHOVEL = 4;
  public final static int T_AXE = 2;
  public final static int T_HOE = 1;

  /**
   * Stores the enchantments that there are tokens for.
   */
  public static HashMap<Integer, EnchData> enchants = new HashMap<Integer, EnchData>();

  public EnchantToken() {

    super();
    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.ENCHANT_TOKEN);
  }

  /**
   * Populates the enchants hash map.
   */
  public static void init() {

    if (!enchants.isEmpty()) {
      return;
    }

    addEnchantment(Enchantment.baneOfArthropods, T_SWORD | T_PICKAXE | T_SHOVEL);
    addEnchantment(Enchantment.efficiency, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_SICKLE);
    addEnchantment(Enchantment.flame, T_BOW);
    addEnchantment(Enchantment.fireAspect, T_SWORD);
    addEnchantment(Enchantment.fortune, T_PICKAXE | T_SHOVEL | T_AXE | T_HOE | T_SICKLE);
    addEnchantment(Enchantment.infinity, T_BOW);
    addEnchantment(Enchantment.knockback, T_SWORD | T_AXE | T_HOE);
    addEnchantment(Enchantment.looting, T_SWORD);
    addEnchantment(Enchantment.power, T_BOW);
    addEnchantment(Enchantment.punch, T_BOW);
    addEnchantment(Enchantment.sharpness, T_SWORD | T_AXE | T_SICKLE);
    addEnchantment(Enchantment.silkTouch, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_SICKLE);
    addEnchantment(Enchantment.smite, T_SWORD | T_AXE | T_HOE);
    addEnchantment(Enchantment.unbreaking, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_HOE
        | T_SICKLE | T_BOW);

    addEnchantment(ModEnchantments.mending, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_HOE
        | T_SICKLE);
    addEnchantment(ModEnchantments.aoe, T_PICKAXE | T_SHOVEL | T_AXE);
  }

  /**
   * Adds an enchantment to the hash map. validTools is appended to the enchantment name after VALID_TOOL_SEP.
   * 
   * @param e
   * @param validTools
   */
  private static void addEnchantment(Enchantment e, int validTools) {

    EnchData data = new EnchantToken.EnchData();
    data.enchantment = e;
    data.validTools = validTools;
    data.gemToolsOnly = e.effectId == ModEnchantments.aoe.effectId
        || !Config.ENCHANTMENT_TOKENS_ON_ANY_TOOL.value;
    enchants.put(e.effectId, data);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    final int meta = stack.getItemDamage();
    final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    String s;

    if (meta == 0) {
      list.add(EnumChatFormatting.GOLD + LocalizationHelper.getOtherItemKey(itemName, "Empty"));
      list.add(LocalizationHelper.getItemDescription(itemName, 1));
      return;
    } else if (enchants.containsKey(meta)) {
      // Enchantment name.
      list.add(EnumChatFormatting.GOLD + getEnchantmentName(meta));
    } else {
      // Bad token!
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, "BadToken"));
      return;
    }

    // Check for null values?
    if (enchants.get(meta) == null || enchants.get(meta).enchantment == null) {
      return;
    }

    if (shifted) {
      // Enchantment description.
      s = LocalizationHelper.getOtherItemKey(itemName, enchants.get(meta).enchantment.getName());
      if (!s.equals(LocalizationHelper.ITEM_PREFIX + itemName + "."
          + enchants.get(meta).enchantment.getName())) {
        list.add(EnumChatFormatting.DARK_GRAY + s);
      }
      // For gem tools only?
      if (enchants.get(meta).gemToolsOnly) {
        list.add(EnumChatFormatting.YELLOW
            + LocalizationHelper.getOtherItemKey(itemName, "GemToolsOnly"));
      }
      // List of valid tools.
      list.add(LocalizationHelper.getItemDescription(itemName, 2));
      list.add(validToolsFor(meta));
      // How to use.
      list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 3));
    } else {
      list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getMiscText(Strings.PRESS_SHIFT));
    }
  }

  @Override
  public void addRecipes() {

    ItemStack baseToken = new ItemStack(this, 1, 0);
    ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);

    GameRegistry.addShapedRecipe(new ItemStack(this, 8, 0), "ggg", "rer", "ggg", 'g',
        Items.gold_ingot, 'r', Items.redstone, 'e', chaosEssence);

    int gemCount = 2;
    addTokenRecipe(Enchantment.baneOfArthropods.effectId, EnumGem.AMETHYST.getItem(), gemCount,
        Items.spider_eye, 4, baseToken);
    addTokenRecipe(Enchantment.efficiency.effectId, EnumGem.EMERALD.getItem(), gemCount,
        Items.gold_ingot, 2, baseToken);
    addTokenRecipe(Enchantment.flame.effectId, EnumGem.GARNET.getItem(), gemCount, Items.blaze_rod,
        2, baseToken);
    addTokenRecipe(Enchantment.fireAspect.effectId, EnumGem.GARNET.getItem(), gemCount,
        Items.blaze_powder, 4, baseToken);
    addTokenRecipe(Enchantment.fortune.effectId, EnumGem.HELIODOR.getItem(), gemCount,
        Items.diamond, 3, baseToken);
    addTokenRecipe(Enchantment.infinity.effectId, EnumGem.AMETHYST.getItem(), gemCount,
        Items.ender_eye, 4, baseToken);
    addTokenRecipe(Enchantment.knockback.effectId, EnumGem.AQUAMARINE.getItem(), gemCount,
        Items.feather, 5, baseToken);
    addTokenRecipe(Enchantment.looting.effectId, EnumGem.TOPAZ.getItem(), gemCount, Items.emerald,
        2, baseToken);
    addTokenRecipe(Enchantment.power.effectId, EnumGem.RUBY.getItem(), gemCount, Items.arrow, 5,
        baseToken);
    addTokenRecipe(Enchantment.punch.effectId, EnumGem.AQUAMARINE.getItem(), gemCount,
        Blocks.piston, 2, baseToken);
    addTokenRecipe(Enchantment.sharpness.effectId, EnumGem.RUBY.getItem(), gemCount, Items.flint,
        5, baseToken);
    addTokenRecipe(Enchantment.silkTouch.effectId, EnumGem.IOLITE.getItem(), gemCount,
        Items.emerald, 3, baseToken);
    addTokenRecipe(Enchantment.smite.effectId, EnumGem.PERIDOT.getItem(), gemCount,
        Items.rotten_flesh, 5, baseToken);
    addTokenRecipe(Enchantment.unbreaking.effectId, EnumGem.SAPPHIRE.getItem(), gemCount,
        Items.iron_ingot, 5, baseToken);

    addTokenRecipe(ModEnchantments.mending.effectId, EnumGem.MORGANITE.getItem(), gemCount,
        CraftingMaterial.getStack(Names.MYSTERY_GOO), 2, baseToken);
    addTokenRecipe(ModEnchantments.aoe.effectId, EnumGem.ONYX.getItem(), gemCount, Blocks.tnt, 3,
        baseToken);
  }

  private void addTokenRecipe(int key, ItemStack gem, int gemCount, ItemStack otherMaterial,
      int otherCount, ItemStack baseToken) {

    String row1 = gemCount == 1 ? " g " : (gemCount == 2 ? "g g" : "ggg");
    String row2 = otherCount < 4 ? " t " : "mtm";
    String row3 = otherCount == 1 ? " m " : (otherCount == 2 || otherCount == 4 ? "m m" : "mmm");

    GameRegistry.addShapedRecipe(new ItemStack(this, 1, key), row1, row2, row3, 'g', gem, 'm',
        otherMaterial, 't', baseToken);
  }

  private void addTokenRecipe(int key, ItemStack gem, int gemCount, Block otherMaterial,
      int otherCount, ItemStack baseToken) {

    addTokenRecipe(key, gem, gemCount, new ItemStack(otherMaterial), otherCount, baseToken);
  }

  private void addTokenRecipe(int key, ItemStack gem, int gemCount, Item otherMaterial,
      int otherCount, ItemStack baseToken) {

    addTokenRecipe(key, gem, gemCount, new ItemStack(otherMaterial), otherCount, baseToken);
  }

  /**
   * Determine if token can be applied to the tool. Checks that the tool is the right type, the enchantments don't
   * conflict, and the enchantment can be "leveled up".
   * 
   * @param token
   * @param tool
   * @return
   */
  public static boolean capApplyTokenToTool(ItemStack token, ItemStack tool) {

    int k = token.getItemDamage();
    if (!enchants.containsKey(k)) {
      return false;
    }

    EnchData e = enchants.get(k);
    k = enchants.get(k).validTools;

    // Check that tool type matches token.
    boolean flag = false;
    if (e.gemToolsOnly) {
      flag |= tool.getItem() instanceof GemSword && (k & T_SWORD) != 0;
      flag |= tool.getItem() instanceof GemPickaxe && (k & T_PICKAXE) != 0;
      flag |= tool.getItem() instanceof GemShovel && (k & T_SHOVEL) != 0;
      flag |= tool.getItem() instanceof GemAxe && (k & T_AXE) != 0;
      flag |= tool.getItem() instanceof GemHoe && (k & T_HOE) != 0;
      flag |= tool.getItem() instanceof GemSickle && (k & T_SICKLE) != 0;
    } else {
      flag |= tool.getItem() instanceof ItemSword && (k & T_SWORD) != 0;
      flag |= tool.getItem() instanceof ItemPickaxe && (k & T_PICKAXE) != 0;
      flag |= tool.getItem() instanceof ItemSpade && (k & T_SHOVEL) != 0;
      flag |= tool.getItem() instanceof ItemAxe && (k & T_AXE) != 0;
      flag |= tool.getItem() instanceof ItemHoe && (k & T_HOE) != 0;
    }

    if (flag) {
      // Token and tool type match. Does tool have any enchantments?
      if (tool.hasTagCompound()) {
        if (!tool.stackTagCompound.hasKey("ench")) {
          return true;
        }
      } else if (!tool.hasTagCompound()) {
        return true;
      }

      // Does tool already have this enchantment? If so, can it be upgraded?
      k = EnchantmentHelper.getEnchantmentLevel(e.enchantment.effectId, tool);
      if (k == 0) {
        // Tool does not have this enchantment. Does it conflict with existing enchants?
        for (int i = 0; i < tool.getEnchantmentTagList().tagCount(); ++i) {
          k = ((NBTTagCompound) tool.getEnchantmentTagList().getCompoundTagAt(i)).getShort("id");
          if (!e.enchantment.canApplyTogether(Enchantment.enchantmentsList[k])
              || !Enchantment.enchantmentsList[k].canApplyTogether(e.enchantment)) {
            return false;
          }
        }
        return true;
      } else if (k < e.getMaxLevel()) {
        // Tool has enchantment, but it can be leveled up.
        return true;
      }
    }

    return false;
  }

  /**
   * Applies the token's enchantment to the tool. Need to check canApplyTokenToTool before calling.
   * 
   * @param token
   * @param tool
   */
  public static void enchantTool(ItemStack token, ItemStack tool) {

    int k = token.getItemDamage();
    EnchData e = enchants.get(k);
    k = EnchantmentHelper.getEnchantmentLevel(e.enchantment.effectId, tool);

    // Adding enchantment is easy, leveling it up is a bit harder.
    if (k == 0) {
      tool.addEnchantment(e.enchantment, 0);
    }

    if (tool.stackTagCompound == null) {
      tool.setTagCompound(new NBTTagCompound());
    }
    if (!tool.stackTagCompound.hasKey("ench")) {
      tool.setTagInfo("ench", new NBTTagList());
    }

    NBTTagCompound t;
    for (int i = 0; i < tool.getEnchantmentTagList().tagCount(); ++i) {
      t = (NBTTagCompound) tool.getEnchantmentTagList().getCompoundTagAt(i);
      k = t.getShort("id");
      if (k == e.enchantment.effectId) {
        k = t.getShort("lvl");
        t.setShort("lvl", (short) (k + 1));
      }
    }
  }

  public static String getEnchantmentName(int key) {

    if (enchants.containsKey(key)) {
      return LocalizationHelper.getLocalizedString(enchants.get(key).getName());
    } else {
      return "";
    }
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    return stack.getItemDamage() == 0 ? EnumRarity.common : EnumRarity.rare;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tabs, List list) {

    list.add(new ItemStack(this, 1, 0));

    for (int k : enchants.keySet()) {
      list.add(new ItemStack(this, 1, k));
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(itemName);
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return stack.getItemDamage() != 0;
  }

  /**
   * Creates a String listing the tools this enchantment can be applied to.
   * 
   * @param key
   * @return
   */
  public static String validToolsFor(int key) {

    List list = new ArrayList<String>();
    int k = enchants.get(key).validTools;

    try {
      if ((k & T_SWORD) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_SWORD));
      }
      if ((k & T_PICKAXE) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_PICKAXE));
      }
      if ((k & T_SHOVEL) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_SHOVEL));
      }
      if ((k & T_AXE) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_AXE));
      }
      if ((k & T_HOE) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_HOE));
      }
      if ((k & T_SICKLE) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_SICKLE));
      }
      if ((k & T_BOW) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_BOW));
      }
      if ((k & T_ARMOR) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_ARMOR));
      }

      String s = "";

      // Separate each item with commas.
      for (Object o : list) {
        if (s.length() > 0) {
          s += ", ";
        }
        s += o;
      }

      return s;
    } catch (Exception ex) {
    }

    return "";
  }
}
