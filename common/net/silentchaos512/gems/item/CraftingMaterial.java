package net.silentchaos512.gems.item;

import java.util.List;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.RecipeHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class CraftingMaterial extends ItemSG implements IFuelHandler {

  /**
   * Hide items in NEI with a meta greater than this. Not really used at this time.
   */
  public static final int HIDE_AFTER_META = 99;
  /**
   * The NAMES of each sub-item. This list cannot be rearranged, as the index determines the meta.
   */
  public static final String[] NAMES = { Names.ORNATE_STICK, Names.MYSTERY_GOO, Names.YARN_BALL,
      Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS, Names.PLUME, Names.GOLDEN_PLUME,
      Names.NETHER_SHARD, Names.CHAOS_CAPACITOR, Names.CHAOS_BOOSTER, Names.RAWHIDE_BONE,
      Names.CHAOS_ESSENCE_SHARD, Names.CHAOS_COAL, Names.CHAOS_ESSENCE_PLUS_2, Names.NETHER_CLUSTER,
      Names.MINI_PYLON, Names.CHAOS_CORE, Names.GILDED_STRING, Names.IRON_POTATO,
      Names.UPGRADE_BASE, Names.FLUFFY_FABRIC, Names.LIFE_ESSENCE };
  /**
   * The order that items appear in NEI.
   */
  public static final String[] SORTED_NAMES = { Names.CHAOS_ESSENCE, Names.CHAOS_ESSENCE_PLUS,
      Names.CHAOS_ESSENCE_PLUS_2, Names.CHAOS_ESSENCE_SHARD, Names.LIFE_ESSENCE, Names.NETHER_SHARD,
      Names.NETHER_CLUSTER, Names.CHAOS_CORE, Names.CHAOS_COAL, Names.ORNATE_STICK,
      Names.FLUFFY_FABRIC, Names.GILDED_STRING, Names.UPGRADE_BASE, Names.MINI_PYLON,
      Names.MYSTERY_GOO, Names.PLUME, Names.GOLDEN_PLUME, Names.YARN_BALL, Names.RAWHIDE_BONE,
      Names.IRON_POTATO, Names.CHAOS_CAPACITOR, Names.CHAOS_BOOSTER };

  // public static final int[] HAS_EFFECT_META = { 4, 13 };

  public CraftingMaterial() {

    super();

    icons = new IIcon[NAMES.length];
    setMaxStackSize(64);
    setHasSubtypes(true);
    setMaxDamage(0);
    setUnlocalizedName(Names.CRAFTING_MATERIALS);

    // Derp check.
    if (NAMES.length != SORTED_NAMES.length) {
      LogHelper.warning("CraftingMaterial: NAMES and SORTED_NAMES contain a different number of "
          + "items! This may cause some items to not show up!");
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    String str = "";
    if (stack.getItemDamage() == getMetaFor(Names.CHAOS_COAL)) {
      str = LocalizationHelper.getOtherItemKey(this.itemName, "fuel");
    } else {
      str = LocalizationHelper.getItemDescription(this.itemName, 0);
    }
    list.add(EnumChatFormatting.DARK_GRAY + str);

    if (this.showFlavorText()) {
      String name;
      int meta = stack.getItemDamage();
      if (meta >= 0 && meta < NAMES.length) {
        name = NAMES[meta];
      } else {
        name = "Unknown";
      }
      str = LocalizationHelper.getItemDescription(name, 0);
      list.add(EnumChatFormatting.ITALIC + str);
    }
  }

  @Override
  public void addOreDict() {

    OreDictionary.registerOre("gemChaos", getStack(Names.CHAOS_ESSENCE));
    OreDictionary.registerOre("nuggetChaos", getStack(Names.CHAOS_ESSENCE_SHARD));
  }

  @Override
  public void addRecipes() {

    GameRegistry.registerFuelHandler(this);

    ItemStack chaosEssence = getStack(Names.CHAOS_ESSENCE);
    ItemStack refinedEssence = getStack(Names.CHAOS_ESSENCE_PLUS);
    ItemStack anyGem = new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE);
    ItemStack anyShard = new ItemStack(ModItems.gemShard, 1, OreDictionary.WILDCARD_VALUE);

    // Ornate stick
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.ORNATE_STICK, 8), "gig", "geg", "gig",
        'g', "ingotGold", 'i', "ingotIron", 'e', "gemChaos"));
    // Mystery goo
    GameRegistry.addRecipe(getStack(Names.MYSTERY_GOO, 1), "mmm", "mam", "mmm", 'm',
        Blocks.mossy_cobblestone, 'a', Items.apple);
    // Yarn ball
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.YARN_BALL, 1), "sss", "sgs", "sss",
        's', Items.string, 'g', anyShard));
    // Refined chaos essence
    RecipeHelper.addSurroundOre(refinedEssence, "dustGlowstone", "dustRedstone", "gemChaos");
    // Plume
    GameRegistry.addRecipe(new ShapedOreRecipe(getStack(Names.PLUME, 1), "fff", "fsf", "fff", 'f',
        Items.feather, 's', anyGem));
    // Golden plume
    RecipeHelper.addSurroundOre(getStack(Names.GOLDEN_PLUME, 1), getStack(Names.PLUME), "gemChaos",
        "ingotGold");
    // Nether Shard
    GameRegistry.addShapedRecipe(getStack(Names.NETHER_SHARD, 24), " c ", "cnc", " c ", 'c',
        refinedEssence, 'n', Items.nether_star);
    // Nether Shard -> Cluster
    ItemStack netherCluster = getStack(Names.NETHER_CLUSTER);
    GameRegistry.addShapedRecipe(netherCluster, "sss", "s s", "sss", 's',
        getStack(Names.NETHER_SHARD));
    // Nether Cluster -> Star
    GameRegistry.addShapelessRecipe(new ItemStack(Items.nether_star), netherCluster, netherCluster,
        netherCluster);
    // Chaos Capacitor
    // GameRegistry.addShapedRecipe(getStack(Names.CHAOS_CAPACITOR, 3), "srs", "ses", "srs", 's',
    // getStack(Names.NETHER_SHARD), 'r', Items.redstone, 'e', Items.emerald);
    // Chaos Booster
    // GameRegistry.addShapedRecipe(getStack(Names.CHAOS_BOOSTER, 3), "sgs", "ses", "sgs", 's',
    // getStack(Names.NETHER_SHARD), 'g', Items.glowstone_dust, 'e', Items.emerald);
    // Rawhide bone
    GameRegistry.addShapedRecipe(getStack(Names.RAWHIDE_BONE, 1), " l ", "lbl", " l ", 'l',
        Items.leather, 'b', Items.bone);
    // Chaos Essence Shard
    RecipeHelper.addCompressionRecipe(getStack(Names.CHAOS_ESSENCE_SHARD), chaosEssence, 9);
    // Chaos Coal
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 8), "gemChaos", Items.coal);
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_COAL, 4), "gemChaos",
        new ItemStack(Items.coal, 1, 1));
    // Chaos Coal -> Torches
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.torch, 16), "c", "s", 'c',
        getStack(Names.CHAOS_COAL), 's', "stickWood"));
    // Mini Pylon
    GameRegistry.addShapedRecipe(getStack(Names.MINI_PYLON), " e ", "epe", " e ", 'e',
        refinedEssence, 'p', new ItemStack(ModBlocks.chaosPylon, 1, 0));
    // Crystallized chaos essence (tier 3)
    RecipeHelper.addSurroundOre(getStack(Names.CHAOS_ESSENCE_PLUS_2), anyGem,
        getStack(Names.NETHER_SHARD), refinedEssence);
    // Chaos Core
    GameRegistry.addShapedRecipe(getStack(Names.CHAOS_CORE), " e ", "eqe", " e ", 'e',
        refinedEssence, 'q', new ItemStack(Blocks.quartz_block, 1, OreDictionary.WILDCARD_VALUE));
    // Gilded String
    GameRegistry.addShapedRecipe(getStack(Names.GILDED_STRING, 3), "gsg", "gsg", "gsg", 's',
        Items.string, 'g', Items.gold_nugget);
    // Upgrade Base
    GameRegistry.addRecipe(new ShapelessOreRecipe(getStack(Names.UPGRADE_BASE, 4), Items.flint,
        Items.flint, "plankWood", "stickWood"));
  }

  @Override
  public void addThaumcraftStuff() {

    ThaumcraftApi.registerObjectTag(getStack(Names.CHAOS_ESSENCE),
        (new AspectList()).add(Aspect.GREED, 4).add(Aspect.ENTROPY, 2));
  }

  @Override
  public EnumRarity getRarity(ItemStack stack) {

    int meta = stack.getItemDamage();
    if (meta == getMetaFor(Names.CHAOS_ESSENCE_PLUS)) {
      return EnumRarity.rare;
    } else if (meta == getMetaFor(Names.CHAOS_ESSENCE_PLUS_2)) {
      return EnumRarity.epic;
    } else {
      return super.getRarity(stack);
    }
  }

  public static ItemStack getStack(String name) {

    for (int i = 0; i < NAMES.length; ++i) {
      if (NAMES[i].equals(name)) {
        return new ItemStack(ModItems.craftingMaterial, 1, i);
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

  public static boolean doesStackMatch(ItemStack stack, String name) {

    return stack.getItem() instanceof CraftingMaterial && stack.getItemDamage() == getMetaFor(name);
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
  }

  @Override
  public int getBurnTime(ItemStack stack) {

    if (stack != null && stack.getItem() == this
        && stack.getItemDamage() == getStack(Names.CHAOS_COAL).getItemDamage()) {
      return Config.CHAOS_COAL_BURN_TIME;
    }
    return 0;
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

  // @Override
  // public boolean hasEffect(ItemStack stack, int pass) {
  //
  // for (int k : HAS_EFFECT_META) {
  // if (stack.getItemDamage() == k) {
  // return true;
  // }
  // }
  // return false;
  // }

  @Override
  public void registerIcons(IIconRegister iconRegister) {

    for (int i = 0; i < NAMES.length; ++i) {
      icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + NAMES[i]);
    }
  }
}
