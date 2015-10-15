package net.silentchaos512.gems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.item.armor.ArmorSG;
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

  /**
   * An internal helper class. This is needed because each enchantment requires some extra data to go with it. Maybe
   * there was another way to handle this, but it works.
   */
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

  private static final ArrayList<Integer> metas = new ArrayList<Integer>(); // Used to sort tokens in getSubItems.
  public static final int META_BLANK = 256; // Previously, the blank tokens were 0, but that's the ID of Protection.

  // I store which items an enchantment token can be applied to in a single integer. Each bit represents one item class,
  // these constants are the positions of the respective bit.
  public static final int T_BOOTS = 1024;
  public static final int T_LEGGINGS = 512;
  public static final int T_CHESTPLATE = 256;
  public static final int T_HELMET = 128;
  public static final int T_BOW = 64;
  public static final int T_SICKLE = 32;
  public static final int T_SWORD = 16;
  public static final int T_PICKAXE = 8;
  public static final int T_SHOVEL = 4;
  public static final int T_AXE = 2;
  public static final int T_HOE = 1;

  private IIcon iconAny;
  private IIcon iconArmor;
  private IIcon iconBow;
  private IIcon iconEmpty;
  private IIcon iconSword;
  private IIcon iconTool;

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

    // All
    addEnchantment(Enchantment.unbreaking, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_HOE | T_SICKLE
        | T_BOW | T_HELMET | T_CHESTPLATE | T_LEGGINGS | T_BOOTS);

    // Melee weapons
    addEnchantment(Enchantment.baneOfArthropods, T_SWORD | T_PICKAXE | T_SHOVEL);
    addEnchantment(Enchantment.fireAspect, T_SWORD);
    addEnchantment(Enchantment.knockback, T_SWORD | T_AXE | T_HOE);
    addEnchantment(Enchantment.looting, T_SWORD);
    addEnchantment(Enchantment.sharpness, T_SWORD | T_AXE | T_SICKLE);
    addEnchantment(Enchantment.smite, T_SWORD | T_AXE | T_HOE);

    // Ranged weapons
    addEnchantment(Enchantment.flame, T_BOW);
    addEnchantment(Enchantment.infinity, T_BOW);
    addEnchantment(Enchantment.power, T_BOW);
    addEnchantment(Enchantment.punch, T_BOW);

    // Digging tools
    addEnchantment(Enchantment.efficiency, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_SICKLE);
    addEnchantment(Enchantment.fortune, T_PICKAXE | T_SHOVEL | T_AXE | T_HOE | T_SICKLE);
    addEnchantment(Enchantment.silkTouch, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_SICKLE);

    // Armor
    final int allArmor = T_HELMET | T_CHESTPLATE | T_LEGGINGS | T_BOOTS;
    addEnchantment(Enchantment.aquaAffinity, T_HELMET);
    addEnchantment(Enchantment.blastProtection, allArmor);
    addEnchantment(Enchantment.featherFalling, T_BOOTS);
    addEnchantment(Enchantment.fireProtection, allArmor);
    addEnchantment(Enchantment.projectileProtection, allArmor);
    addEnchantment(Enchantment.protection, allArmor);
    addEnchantment(Enchantment.respiration, T_HELMET);
    addEnchantment(Enchantment.thorns, allArmor);

    // This mod
    addEnchantment(ModEnchantments.mending, T_SWORD | T_PICKAXE | T_SHOVEL | T_AXE | T_HOE
        | T_SICKLE | T_BOW | T_HELMET | T_CHESTPLATE | T_LEGGINGS | T_BOOTS);
    addEnchantment(ModEnchantments.aoe, T_PICKAXE | T_SHOVEL | T_AXE);
  }

  /**
   * Adds an enchantment to the hash map.
   * 
   * @param e
   * @param validTools
   */
  private static void addEnchantment(Enchantment e, int validTools) {

    metas.add(e.effectId);
    EnchData data = new EnchantToken.EnchData();
    data.enchantment = e;
    data.validTools = validTools;
    data.gemToolsOnly = e.effectId == ModEnchantments.aoe.effectId
        || !Config.ENCHANTMENT_TOKENS_ON_ANY_TOOL;
    enchants.put(e.effectId, data);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    final int meta = stack.getItemDamage();
    final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
    String s;

    if (meta == META_BLANK) {
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
      // Max level
      s = LocalizationHelper.getOtherItemKey(itemName, "MaxLevel");
      s += " " + enchants.get(meta).getMaxLevel();
      list.add(s);
      // How to use.
      list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 3));
    } else {
      list.add(EnumChatFormatting.ITALIC + LocalizationHelper.getMiscText(Strings.PRESS_SHIFT));
    }
  }

  @Override
  public void addRecipes() {

    ItemStack baseToken = new ItemStack(this, 1, META_BLANK);
    ItemStack chaosEssence = CraftingMaterial.getStack(Names.CHAOS_ESSENCE);

    // Base token recipe
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 12, META_BLANK), "ggg", "rer",
        "ggg", 'g', "ingotGold", 'r', "dustRedstone", 'e', "gemChaos"));
    // Uncrafting token
    GameRegistry.addShapelessRecipe(new ItemStack(this, 1, META_BLANK),
        new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE));

    int gemCount = 2;

    // All
    addTokenRecipe(Enchantment.unbreaking.effectId, EnumGem.SAPPHIRE.getItemOreName(), gemCount,
        "ingotIron", 5, baseToken);

    // Digging tools
    addTokenRecipe(Enchantment.efficiency.effectId, EnumGem.EMERALD.getItemOreName(), gemCount,
        "dustGlowstone", 4, baseToken);
    addTokenRecipe(Enchantment.fortune.effectId, EnumGem.HELIODOR.getItemOreName(), gemCount,
        "gemDiamond", 3, baseToken);
    addTokenRecipe(Enchantment.silkTouch.effectId, EnumGem.IOLITE.getItemOreName(), gemCount,
        "gemEmerald", 3, baseToken);

    // Melee weapons
    addTokenRecipe(Enchantment.baneOfArthropods.effectId, EnumGem.AMETHYST.getItemOreName(),
        gemCount, Items.spider_eye, 4, baseToken);
    addTokenRecipe(Enchantment.fireAspect.effectId, EnumGem.GARNET.getItemOreName(), gemCount,
        Items.blaze_powder, 4, baseToken);
    addTokenRecipe(Enchantment.knockback.effectId, EnumGem.AQUAMARINE.getItemOreName(), gemCount,
        Items.feather, 5, baseToken);
    addTokenRecipe(Enchantment.looting.effectId, EnumGem.TOPAZ.getItemOreName(), gemCount,
        "gemEmerald", 2, baseToken);
    addTokenRecipe(Enchantment.sharpness.effectId, EnumGem.RUBY.getItemOreName(), gemCount,
        Items.flint, 5, baseToken);
    addTokenRecipe(Enchantment.smite.effectId, EnumGem.PERIDOT.getItemOreName(), gemCount,
        Items.rotten_flesh, 5, baseToken);

    // Ranged weapons
    addTokenRecipe(Enchantment.flame.effectId, EnumGem.GARNET.getItemOreName(), gemCount,
        Items.blaze_rod, 2, baseToken);
    addTokenRecipe(Enchantment.infinity.effectId, EnumGem.AMETHYST.getItemOreName(), gemCount,
        Items.ender_eye, 4, baseToken);
    addTokenRecipe(Enchantment.power.effectId, EnumGem.RUBY.getItemOreName(), gemCount, Items.arrow,
        5, baseToken);
    addTokenRecipe(Enchantment.punch.effectId, EnumGem.AQUAMARINE.getItemOreName(), gemCount,
        Blocks.piston, 2, baseToken);

    // Armor
    addTokenRecipe(Enchantment.aquaAffinity.effectId, EnumGem.SAPPHIRE.getItemOreName(), gemCount,
        "blockLapis", 2, baseToken);
    addTokenRecipe(Enchantment.blastProtection.effectId, EnumGem.TOPAZ.getItemOreName(), gemCount,
        Items.gunpowder, 5, baseToken);
    addTokenRecipe(Enchantment.featherFalling.effectId, EnumGem.AQUAMARINE.getItemOreName(),
        gemCount, CraftingMaterial.getStack(Names.PLUME), 1, baseToken);
    addTokenRecipe(Enchantment.fireProtection.effectId, EnumGem.GARNET.getItemOreName(), gemCount,
        Items.magma_cream, 2, baseToken);
    addTokenRecipe(Enchantment.projectileProtection.effectId, EnumGem.HELIODOR.getItemOreName(),
        gemCount, Items.arrow, 4, baseToken);
    addTokenRecipe(Enchantment.protection.effectId, EnumGem.AMETHYST.getItemOreName(), 2,
        Blocks.iron_bars, 4, baseToken);
    addTokenRecipe(Enchantment.respiration.effectId, EnumGem.IOLITE.getItemOreName(), gemCount,
        new ItemStack(Items.fish, 1, 3), 1, baseToken);
    addTokenRecipe(Enchantment.thorns.effectId, EnumGem.EMERALD.getItemOreName(), gemCount,
        new ItemStack(Blocks.double_plant, 1, 4), 2, baseToken);

    // This mod
    addTokenRecipe(ModEnchantments.mending.effectId, EnumGem.MORGANITE.getItemOreName(), gemCount,
        CraftingMaterial.getStack(Names.MYSTERY_GOO), 2, baseToken);
    addTokenRecipe(ModEnchantments.aoe.effectId, EnumGem.ONYX.getItemOreName(), gemCount,
        Blocks.tnt, 3, baseToken);
  }

  private void addTokenRecipe(int key, String gem, int gemCount, Object otherMaterial,
      int otherCount, ItemStack baseToken) {

    String row1 = gemCount == 1 ? " g " : (gemCount == 2 ? "g g" : "ggg");
    String row2 = otherCount < 4 ? " t " : "mtm";
    String row3 = otherCount == 1 ? " m " : (otherCount == 2 || otherCount == 4 ? "m m" : "mmm");

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(this, 1, key), row1, row2, row3, 'g',
        gem, 'm', otherMaterial, 't', baseToken));
  }

  private void addTokenRecipe(int key, String gem, int gemCount, Block otherMaterial,
      int otherCount, ItemStack baseToken) {

    addTokenRecipe(key, gem, gemCount, new ItemStack(otherMaterial), otherCount, baseToken);
  }

  private void addTokenRecipe(int key, String gem, int gemCount, Item otherMaterial, int otherCount,
      ItemStack baseToken) {

    addTokenRecipe(key, gem, gemCount, new ItemStack(otherMaterial), otherCount, baseToken);
  }

  /**
   * Determine if token can be applied to the tool. Checks that the tool is the right type, the enchantments don't
   * conflict, and the enchantment can be "leveled up".
   * 
   * @param token
   *          The Enchantment Token
   * @param tool
   *          Not necessarily a tool, but that's what enchantment tokens were originally for!
   * @return
   */
  public static boolean capApplyTokenToTool(ItemStack token, ItemStack tool) {

    if (token == null || tool == null) {
      // This has never happened, but...
      return false;
    }

    int tokenMeta = token.getItemDamage();
    if (!enchants.containsKey(tokenMeta)) {
      // Bad token, maybe an enchantment ID was changed, or the token was spawned in with 'give'.
      return false;
    }

    EnchData e = enchants.get(tokenMeta);
    int validTools = enchants.get(tokenMeta).validTools;

    // Check that tool type matches token.
    boolean flag = false; // Just to avoid gigantic 'if' statements.
    Item itemTool = tool.getItem();
    if (e.gemToolsOnly) {
      // This enchantment can only be applied to gem tools (respects config, I think).
      flag |= itemTool instanceof GemSword && (validTools & T_SWORD) != 0;
      flag |= itemTool instanceof GemPickaxe && (validTools & T_PICKAXE) != 0;
      flag |= itemTool instanceof GemShovel && (validTools & T_SHOVEL) != 0;
      flag |= itemTool instanceof GemAxe && (validTools & T_AXE) != 0;
      flag |= itemTool instanceof GemHoe && (validTools & T_HOE) != 0;
      flag |= itemTool instanceof GemSickle && (validTools & T_SICKLE) != 0;

      if (itemTool instanceof ArmorSG) {
        ArmorSG armor = (ArmorSG) itemTool;
        flag |= armor.armorType == 0 && (validTools & T_HELMET) != 0;
        flag |= armor.armorType == 1 && (validTools & T_CHESTPLATE) != 0;
        flag |= armor.armorType == 2 && (validTools & T_LEGGINGS) != 0;
        flag |= armor.armorType == 3 && (validTools & T_BOOTS) != 0;
      }
    } else {
      // This enchantment can be applied to items not from this mod! Sickles don't count :(
      flag |= itemTool instanceof ItemSword && (validTools & T_SWORD) != 0;
      flag |= itemTool instanceof ItemPickaxe && (validTools & T_PICKAXE) != 0;
      flag |= itemTool instanceof ItemSpade && (validTools & T_SHOVEL) != 0;
      flag |= itemTool instanceof ItemAxe && (validTools & T_AXE) != 0;
      flag |= itemTool instanceof ItemHoe && (validTools & T_HOE) != 0;
      flag |= itemTool instanceof GemSickle && (validTools & T_SICKLE) != 0;
      flag |= itemTool instanceof ItemBow && (validTools & T_BOW) != 0;

      if (itemTool instanceof ItemArmor) {
        ItemArmor armor = (ItemArmor) itemTool;
        flag |= armor.armorType == 0 && (validTools & T_HELMET) != 0;
        flag |= armor.armorType == 1 && (validTools & T_CHESTPLATE) != 0;
        flag |= armor.armorType == 2 && (validTools & T_LEGGINGS) != 0;
        flag |= armor.armorType == 3 && (validTools & T_BOOTS) != 0;
      }
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
      int level = EnchantmentHelper.getEnchantmentLevel(e.enchantment.effectId, tool);
      if (level == 0) {
        // Tool does not have this enchantment. Does it conflict with existing enchants?
        for (int i = 0; i < tool.getEnchantmentTagList().tagCount(); ++i) {
          int id = ((NBTTagCompound) tool.getEnchantmentTagList().getCompoundTagAt(i))
              .getShort("id");
          if (!e.enchantment.canApplyTogether(Enchantment.enchantmentsList[id])
              || !Enchantment.enchantmentsList[id].canApplyTogether(e.enchantment)) {
            return false;
          }
        }
        return true;
      } else if (level < e.getMaxLevel()) {
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

    int meta = token.getItemDamage();
    EnchData e = enchants.get(meta);
    int level = EnchantmentHelper.getEnchantmentLevel(e.enchantment.effectId, tool);

    // Adding enchantment is easy, leveling it up is a bit harder.
    if (level == 0) {
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
      int id = t.getShort("id");
      if (id == e.enchantment.effectId) {
        level = t.getShort("lvl");
        t.setShort("lvl", (short) (level + 1));
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

    return stack.getItemDamage() == META_BLANK ? EnumRarity.common : EnumRarity.rare;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tabs, List list) {

    list.add(new ItemStack(this, 1, META_BLANK));

    for (int k : metas) {
      list.add(new ItemStack(this, 1, k));
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return getUnlocalizedName(itemName);
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return stack.getItemDamage() != META_BLANK;
  }

  /**
   * Creates a String listing the tools this enchantment can be applied to. This is for display purposes only.
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
      if ((k & T_HELMET) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_HELMET));
      }
      if ((k & T_CHESTPLATE) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_CHESTPLATE));
      }
      if ((k & T_LEGGINGS) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_LEGGINGS));
      }
      if ((k & T_BOOTS) != 0) {
        list.add(LocalizationHelper.getMiscText(Strings.TOOL_BOOTS));
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

  @Override
  public void registerIcons(IIconRegister reg) {

    String str = Strings.RESOURCE_PREFIX + Names.ENCHANT_TOKEN + "_";
    iconAny = reg.registerIcon(str + "Any");
    iconArmor = reg.registerIcon(str + "Armor");
    iconBow = reg.registerIcon(str + "Bow");
    iconEmpty = reg.registerIcon(str + "Empty");
    iconSword = reg.registerIcon(str + "Sword");
    iconTool = reg.registerIcon(str + "Tool");
  }

  @Override
  public IIcon getIconFromDamage(int meta) {

    if (enchants.containsKey(meta)) {
      EnumEnchantmentType type = enchants.get(meta).enchantment.type;
      switch (type) {
        case weapon:
          return iconSword;
        case digger:
          return iconTool;
        case bow:
          return iconBow;
        case armor:
        case armor_feet:
        case armor_legs:
        case armor_torso:
        case armor_head:
          return iconArmor;
        default:
          return iconAny;
      }
    }

    return iconEmpty;
  }
}
