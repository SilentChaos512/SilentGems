package net.silentchaos512.gems.guide;

import java.util.List;

import com.google.common.collect.Lists;

import amerifrance.guideapi.api.GuideAPIItems;
import amerifrance.guideapi.api.GuideRegistry;
import amerifrance.guideapi.api.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.abstraction.EntryAbstract;
import amerifrance.guideapi.api.abstraction.IPage;
import amerifrance.guideapi.api.base.Book;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageFurnaceRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
import amerifrance.guideapi.page.PageTextImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentFishingSpeed;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ItemChaosOrb;
import net.silentchaos512.gems.item.ItemEnchantmentToken;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.LocalizationHelper;

public class GuideSilentGems {

  public static final String GETTING_STARTED = "gettingStarted";
  public static final String BLOCKS = "blocks";
  public static final String ITEMS = "items";
  public static final String TERMS = "terms";

  public static Book book;
  private static LocalizationHelper localizationHelper;

  public static void buildGuide(LocalizationHelper loc) {

    localizationHelper = loc;
    int i;
    ItemStack stack;
    String prefix;

    List<CategoryAbstract> categories = Lists.newArrayList();
    List<EntryAbstract> entries;
    List<IPage> pages;

    // =========================
    // Category: Getting Started
    // =========================

    entries = Lists.newArrayList();

    // Entry: Introduction
    pages = Lists.newArrayList();
    prefix = GETTING_STARTED + ".introduction";
    for (i = 0; i < 2; ++i)
      pages.add(new PageText(getString(prefix + i)));
    entries.add(new EntryItemStack(pages, getString(prefix), EnumGem.RUBY.getItem()));

    // Entry: Flint Tools
    pages = Lists.newArrayList();
    prefix = GETTING_STARTED + ".flint";
    for (i = 0; i < 2; ++i)
      pages.add(new PageText(getString(prefix + i)));
    ItemStack flintPick = ModItems.pickaxe.constructTool(new ItemStack(Items.BONE),
        new ItemStack(Items.FLINT));
    pages.add(new PageIRecipe(
        new ShapedOreRecipe(flintPick, "fff", " s ", " s ", 'f', Items.FLINT, 's', Items.BONE)));
    for (; i < 4; ++i)
      pages.add(new PageText(getString(prefix + i)));
    entries.add(new EntryItemStack(pages, getString(prefix), new ItemStack(Items.FLINT)));

    // Entry: Iron-Tipped
    pages = Lists.newArrayList();
    prefix = GETTING_STARTED + ".ironTipped";
    for (i = 0; i < 2; ++i)
      pages.add(new PageText(getString(prefix + i)));
    ItemStack ironUpgrade = new ItemStack(ModItems.tipUpgrade);
    ItemStack ironTippedFlintPick = ModItems.tipUpgrade.applyToTool(flintPick, ironUpgrade);
    ToolHelper.recalculateStats(ironTippedFlintPick);
    pages.add(new PageIRecipe(new ShapelessOreRecipe(ironTippedFlintPick, flintPick, ironUpgrade)));
    pages.add(new PageText(getString(prefix + i)));
    entries.add(new EntryItemStack(pages, getString(prefix), new ItemStack(ModItems.tipUpgrade)));

    // Entry: Gem Tools
    pages = Lists.newArrayList();
    prefix = GETTING_STARTED + ".gemTools";
    for (i = 0; i < 3; ++i)
      pages.add(new PageText(getString(prefix + i)));
    ItemStack rubySapphirePick = ModItems.pickaxe.constructTool(
        ModItems.craftingMaterial.toolRodIron, EnumGem.RUBY.getItem(), EnumGem.SAPPHIRE.getItem(),
        EnumGem.RUBY.getItem());
    pages.add(new PageIRecipe(
        new ShapedOreRecipe(rubySapphirePick, "rsr", " i ", " i ", 'r', EnumGem.RUBY.getItem(), 's',
            EnumGem.SAPPHIRE.getItem(), 'i', ModItems.craftingMaterial.toolRodIron)));
    for (; i < 5; ++i)
      pages.add(new PageText(getString(prefix + i)));
    entries.add(new EntryItemStack(pages, getString(prefix), rubySapphirePick));

    // Entry: Chaos Ore
    pages = Lists.newArrayList();
    prefix = GETTING_STARTED + ".chaosOre";
    pages.add(new PageText(getString(prefix + 0)));
    ItemStack diamondUpgrade = new ItemStack(ModItems.tipUpgrade, 1, 2);
    ItemStack diamondTippedPick = ModItems.tipUpgrade.applyToTool(rubySapphirePick, diamondUpgrade);
    ToolHelper.recalculateStats(diamondTippedPick);
    pages.add(new PageIRecipe(
        new ShapelessOreRecipe(diamondTippedPick, rubySapphirePick, diamondUpgrade)));
    pages.add(new PageText(getString(prefix + 1)));
    pages.add(new PageFurnaceRecipe(new ItemStack(ModBlocks.essenceOre)));
    pages.add(new PageText(getString(prefix + 2)));
    entries.add(new EntryItemStack(pages, getString(prefix), new ItemStack(ModBlocks.essenceOre)));

    // Entry: Super Tools
    pages = Lists.newArrayList();
    prefix = GETTING_STARTED + ".superTools";
    for (i = 0; i < 2; ++i)
      pages.add(new PageText(getString(prefix + i)));
    ItemStack superPick = ModItems.pickaxe.constructTool(true, EnumGem.BLUE_TOPAZ.getItemSuper(),
        EnumGem.HELIODOR.getItemSuper(), EnumGem.BLACK_DIAMOND.getItemSuper());
    pages.add(new PageIRecipe(new ShapedOreRecipe(superPick, "thd", " r ", " r ", 'r',
        ModItems.craftingMaterial.toolRodGold, 't', EnumGem.BLUE_TOPAZ.getItemSuper(), 'h',
        EnumGem.HELIODOR.getItemSuper(), 'd', EnumGem.BLACK_DIAMOND.getItemSuper())));
    for (; i < 4; ++i)
      pages.add(new PageText(getString(prefix + i)));
    entries.add(new EntryItemStack(pages, getString(prefix), superPick));

    // Add category
    String catGettingStarted = getString("category." + GETTING_STARTED);
    categories.add(new CategoryItemStack(entries, catGettingStarted, flintPick));

    // ================
    // Category: Blocks
    // ================

    entries = Lists.newArrayList();

    // Entry: Chaos Flower Pot
    pages = Lists.newArrayList();
    prefix = BLOCKS + ".chaosFlowerPot";
    pages.addAll(getPages(prefix));
    stack = new ItemStack(ModBlocks.chaosFlowerPot);
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Chaos Node
    pages = Lists.newArrayList();
    prefix = BLOCKS + ".chaosNode";
    pages.add(new PageText(getString(prefix + 0)));
    ResourceLocation imageChaosNode = new ResourceLocation(SilentGems.MOD_ID.toLowerCase(),
        "textures/guide/ChaosNode.png");
    pages.add(new PageTextImage(getString(prefix + 1), imageChaosNode, false));
    pages.add(new PageText(getString(prefix + 2)));
    entries.add(new EntryItemStack(pages, getString(prefix), new ItemStack(ModBlocks.chaosNode)));

    // Entry: Fluffy Blocks
    prefix = BLOCKS + ".fluffyBlock";
    pages = getPages(prefix);
    stack = new ItemStack(ModBlocks.fluffyBlock);
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Gem Lamps
    prefix = BLOCKS + ".gemLamp";
    pages = getPages(prefix);
    stack = new ItemStack(ModBlocks.gemLamp, 1, EnumGem.SAPPHIRE.ordinal());
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Glow Roses
    prefix = BLOCKS + ".glowRose";
    pages = getPages(prefix);
    stack = new ItemStack(ModBlocks.glowRose, 1, EnumGem.AGATE.ordinal());
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Material Grader
    pages = Lists.newArrayList();
    prefix = BLOCKS + ".materialGrader";
    pages.addAll(getPages(prefix));
    stack = new ItemStack(ModBlocks.materialGrader);
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Add category
    String catBlocks = getString("category." + BLOCKS);
    categories.add(new CategoryItemStack(entries, catBlocks, new ItemStack(ModBlocks.chaosPylon)));

    // ===============
    // Category: Items
    // ===============

    entries = Lists.newArrayList();

    // Entry: Chaos Coal
    prefix = ITEMS + ".chaosCoal";
    pages = getPages(prefix);
    stack = ModItems.craftingMaterial.chaosCoal;
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Chaos Orbs
    prefix = ITEMS + ".chaosOrb";
    pages = getPages(prefix);
    stack = new ItemStack(ModItems.chaosOrb, 1, ItemChaosOrb.Type.SUPREME.ordinal());
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Enchantment Tokens
    prefix = ITEMS + ".enchantmentToken";
    pages = Lists.newArrayList();
    pages.add(new PageText(getString(prefix + 0)));
    ItemStack katanaEnchPre = ModItems.katana.constructTool(true, EnumGem.ONYX.getItemSuper());
    ItemStack tokenUnbreaking = ModItems.enchantmentToken.constructToken(Enchantments.UNBREAKING);
    ItemStack tokenLooting = ModItems.enchantmentToken.constructToken(Enchantments.LOOTING);
    ItemStack katanaEnchPost = katanaEnchPre.copy();
    katanaEnchPost.addEnchantment(Enchantments.UNBREAKING, 3);
    katanaEnchPost.addEnchantment(Enchantments.LOOTING, 3);
    pages.add(
        new PageIRecipe(new ShapelessOreRecipe(katanaEnchPost, tokenUnbreaking, tokenUnbreaking,
            tokenUnbreaking, tokenLooting, tokenLooting, tokenLooting, katanaEnchPre)));
    for (i = 1; i < 3; ++i)
      pages.add(new PageText(getString(prefix + i)));
    entries.add(new EntryItemStack(pages, getString(prefix), tokenUnbreaking));

    // Entry: Fluffy Puffs
    prefix = ITEMS + ".fluffyPuff";
    pages = getPages(prefix);
    entries.add(new EntryItemStack(pages, getString(prefix), new ItemStack(ModItems.fluffyPuff)));

    // Entry: Iron Potato
    prefix = ITEMS + ".ironPotato";
    pages = getPages(prefix);
    stack = ModItems.craftingMaterial.ironPotato;
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Torch Bandolier
    prefix = ITEMS + ".torchBandolier";
    pages = getPages(prefix);
    stack = new ItemStack(ModItems.torchBandolier);
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Add category
    String catItems = getString("category." + ITEMS);
    categories.add(new CategoryItemStack(entries, catItems, tokenLooting));

    // =====================
    // Category: Terminology
    // =====================

    entries = Lists.newArrayList();

    // Entry: Chaos
    prefix = TERMS + ".chaos";
    pages = getPages(prefix);
    entries.add(new EntryItemStack(pages, getString(prefix),
        ModItems.craftingMaterial.chaosEssenceEnriched));

    // Entry: Decorating
    prefix = TERMS + ".decorating";
    pages = getPages(prefix);
    ItemStack pickaxeDecoPre = ModItems.pickaxe.constructTool(true,
        EnumGem.HELIODOR.getItemSuper());
    ItemStack[] decoParts = { EnumGem.RUBY.getItem(), EnumGem.MALACHITE.getItem(),
        EnumGem.SAPPHIRE.getItem(), EnumGem.AMETRINE.getItem() };
    ItemStack pickaxeDecoPost = ToolHelper.decorateTool(pickaxeDecoPre, decoParts[0], decoParts[1],
        decoParts[2], decoParts[3]);
    ToolHelper.recalculateStats(pickaxeDecoPost);
    pages.add(2,
        new PageIRecipe(
            new ShapedOreRecipe(pickaxeDecoPost, " n ", "wbe", " s ", 'b', pickaxeDecoPre, 'w',
                decoParts[0], 'n', decoParts[1], 'e', decoParts[2], 's', decoParts[3])));
    entries.add(new EntryItemStack(pages, getString(prefix), pickaxeDecoPost));

    // Entry: Grade
    prefix = TERMS + ".grade";
    pages = getPages(prefix);
    stack = ModItems.craftingMaterial.magnifyingGlass;
    entries.add(new EntryItemStack(pages, getString(prefix), stack));

    // Entry: Super Skills
    prefix = TERMS + ".superSkills";
    pages = getPages(prefix);
    entries.add(new EntryItemStack(pages, getString(prefix), superPick));

    // Entry: Tier
    prefix = TERMS + ".tier";
    pages = getPages(prefix);
    entries.add(new EntryItemStack(pages, getString(prefix), new ItemStack(Items.FLINT)));

    // Add category
    String catTerminology = getString("category." + TERMS);
    categories.add(new CategoryItemStack(entries, catTerminology, new ItemStack(ModItems.dye)));

    // =============
    // Register Book
    // =============

    String title = getString("title");
    String welcome = getString("welcome");
    java.awt.Color color = new java.awt.Color(255, 32, 47); // FF202F
    book = new Book();
    book.setCategoryList(categories);
    book.setTitle(title);
    book.setWelcomeMessage(welcome);
    book.setDisplayName(title);
    book.setAuthor("SilentChaos512");
    book.setCustomModel(false);
    book.setColor(color);
    book.setSpawnWithBook(true);
    GuideRegistry.registerBook(book, false);

    // Register model (default registration fails)
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
      Item itemGuideBook = GuideAPIItems.guideBook;
      int meta = GuideRegistry.getIndexOf(book);
      ModelResourceLocation model = new ModelResourceLocation("guideapi:ItemGuideBook",
          "inventory");
      ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
      ModelLoader.registerItemVariants(itemGuideBook, model);
      mesher.register(itemGuideBook, meta, model);
      // ModelLoader.setCustomModelResourceLocation(itemGuideBook, meta, model);
    }
  }

  private static String getString(String key) {

    return localizationHelper.getLocalizedString("guide", key).replaceAll("\\\\n", "\n");
  }

  private static List<IPage> getPages(String prefixKey) {

    List<IPage> list = Lists.newArrayList();
    int i = 0;
    String key = prefixKey + i;
    String str = getString(key);

    while (!str.equals("guide.silentgems:" + key)) {
      list.add(new PageText(str));
      key = prefixKey + ++i;
      str = getString(key);
    }

    return list;
  }
}
