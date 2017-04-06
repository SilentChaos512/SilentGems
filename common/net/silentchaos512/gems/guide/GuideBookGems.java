package net.silentchaos512.gems.guide;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.guide.page.PageOreSpawn;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.chapter.GuideChapter;
import net.silentchaos512.lib.guidebook.entry.GuideEntry;
import net.silentchaos512.lib.guidebook.page.PageCrafting;
import net.silentchaos512.lib.guidebook.page.PageFurnace;
import net.silentchaos512.lib.guidebook.page.PagePicture;
import net.silentchaos512.lib.guidebook.page.PageTextOnly;
import net.silentchaos512.lib.util.StackHelper;

public class GuideBookGems extends GuideBook {

  public static final String TOOL_OWNER_NAME = "Guide Book";

  private GuideEntry entryGettingStarted;
  private GuideEntry entryBlocks;
  private GuideEntry entryItems;
  private GuideEntry entryRecipes;

  public GuideBookGems() {

    super(SilentGems.MODID);
    this.resourceGui = new ResourceLocation(SilentGems.MODID, "textures/guide/gui_guide.png");
    this.resourceGadgets = new ResourceLocation(SilentGems.MODID, "textures/guide/gui_guide_gadgets.png");

    edition = SilentGems.BUILD_NUM;
  }

  @Override
  public void initEntries() {

    entryGettingStarted = new GuideEntry(this, "gettingStarted").setImportant();
    entryBlocks = new GuideEntry(this, "blocks");
    entryItems = new GuideEntry(this, "items");
    entryRecipes = new GuideEntry(this, "recipes");
  }

  @SuppressWarnings("unused")
  @Override
  public void initChapters() {

    //@formatter:off

    // Getting Started

    // Introduction
    new GuideChapter(this, "introduction", entryGettingStarted, new ItemStack(ModItems.gem, 1, SilentGems.random.nextInt(32)), 1000,
        new PageTextOnly(this, 1),
        new PageTextOnly(this, 2),
        new PageTextOnly(this, 3)).setSpecial();
    // Progression
    ItemStack flintPickaxe = ModItems.pickaxe.constructTool(false, new ItemStack(Items.FLINT));
    ToolHelper.setOriginalOwner(flintPickaxe, TOOL_OWNER_NAME);
    ItemStack flintPickaxeBroken = StackHelper.safeCopy(flintPickaxe);
    flintPickaxeBroken.setItemDamage(ToolHelper.getMaxDamage(flintPickaxeBroken));
    ItemStack ironTipUpgrade = new ItemStack(ModItems.tipUpgrade);
    ItemStack flintPickaxeIronTips = ModItems.tipUpgrade.applyToTool(flintPickaxe, ironTipUpgrade);
    ItemStack gravel = new ItemStack(Blocks.GRAVEL);
    ItemStack gemPickaxe = ModItems.pickaxe.constructTool(new ItemStack(Items.STICK), EnumGem.RUBY.getItem(), EnumGem.SAPPHIRE.getItem(), EnumGem.RUBY.getItem());
    ToolHelper.setOriginalOwner(gemPickaxe, TOOL_OWNER_NAME);
    ItemStack diamondTipUpgrade = new ItemStack(ModItems.tipUpgrade, 1, 2);
    ItemStack gemPickaxeDiamondTips = ModItems.tipUpgrade.applyToTool(gemPickaxe, diamondTipUpgrade);
    ItemStack katana = ModItems.katana.constructTool(true, EnumGem.LEPIDOLITE.getItemSuper(), EnumGem.OPAL.getItemSuper(), EnumGem.BLACK_DIAMOND.getItemSuper());
    ToolHelper.setOriginalOwner(katana, TOOL_OWNER_NAME);
    new GuideChapter(this, "progression", entryGettingStarted, flintPickaxeIronTips, 100,
        new PageTextOnly(this, 1),
        new PageCrafting(this, 2, new ShapelessOreRecipe(new ItemStack(Items.FLINT), gravel, gravel)),
        new PageCrafting(this, 3, new ShapedOreRecipe(flintPickaxe, "fff", " s ", " s ", 'f', Items.FLINT, 's', "stickWood")),
        new PageTextOnly(this, 4),
        new PageCrafting(this, 5, new ShapelessOreRecipe(flintPickaxe, flintPickaxeBroken, Items.FLINT, Items.FLINT)),
        new PageTextOnly(this, 6),
        new PageCrafting(this, 7, new ShapelessOreRecipe(flintPickaxeIronTips, flintPickaxe, ironTipUpgrade)),
        new PageCrafting(this, 8, new ShapedOreRecipe(gemPickaxe, "rsr", " t ", " t ", 'r', EnumGem.RUBY.getItem(), 's',
            EnumGem.SAPPHIRE.getItem(), 't', "stickWood")),
        new PageTextOnly(this, 9),
        new PageCrafting(this, 10, new ShapelessOreRecipe(gemPickaxeDiamondTips, gemPickaxe, diamondTipUpgrade)),
        new PageTextOnly(this, 11),
        new PageCrafting(this, 12, new ShapedOreRecipe(katana, "lo", "d ", "r ", 'l', EnumGem.LEPIDOLITE.getItemSuper(), 'o',
            EnumGem.OPAL.getItemSuper(), 'd', EnumGem.BLACK_DIAMOND.getItemSuper(), 'r', ModItems.craftingMaterial.toolRodGold)),
        new PageTextOnly(this, 13)).setImportant();

    // Blocks

    // Ores
    new GuideChapter(this, "ores", entryBlocks, new ItemStack(ModBlocks.gemOre, 1, SilentGems.random.nextInt(16)), 10,
        new PageTextOnly(this, 1),
        new PageOreSpawn(this, 2, GemsConfig.WORLD_GEN_GEMS),
        new PageOreSpawn(this, 3, GemsConfig.WORLD_GEN_GEMS_DARK),
        new PageOreSpawn(this, 4, GemsConfig.WORLD_GEN_CHAOS),
        new PageFurnace(this, 5, ModItems.craftingMaterial.chaosEssence),
        new PageOreSpawn(this, 6, GemsConfig.WORLD_GEN_ENDER),
        new PageFurnace(this, 7, ModItems.craftingMaterial.enderEssence)).setImportant();
    // Chaos Altar
    new GuideChapter(this, "chaosAltar", entryBlocks, new ItemStack(ModBlocks.chaosAltar),
        new PageCrafting(this, 1, ModBlocks.chaosAltar.recipe),
        new PageTextOnly(this, 2),
        new PageTextOnly(this, 3),
        new PageTextOnly(this, 4));
    // Chaos Flower Pot
    new GuideChapter(this, "chaosFlowerPot", entryBlocks, new ItemStack(ModBlocks.chaosFlowerPot),
        new PageCrafting(this, 1, ModBlocks.chaosFlowerPot.recipe),
        new PageTextOnly(this, 2));
    // Chaos Node
    new GuideChapter(this, "chaosNode", entryBlocks, new ItemStack(ModBlocks.chaosNode),
        new PagePicture(this, 1, new ResourceLocation(SilentGems.MODID, "textures/guide/chaosnode.png"), 125),
        new PageTextOnly(this, 2));
    // Fluffy Blocks
    new GuideChapter(this, "fluffyBlocks", entryBlocks, new ItemStack(ModBlocks.fluffyBlock),
        new PageCrafting(this, 1, new ShapedOreRecipe(new ItemStack(ModBlocks.fluffyBlock), "ff", "ff", 'f', ModItems.craftingMaterial.fluffyFabric)),
        new PageTextOnly(this, 2));
    // Glow Rose
    new GuideChapter(this, "glowRose", entryBlocks, new ItemStack(ModBlocks.glowRose),
        new PageTextOnly(this, 1));
    // Material Grader
    new GuideChapter(this, "materialGrader", entryBlocks, new ItemStack(ModBlocks.materialGrader),
        new PageTextOnly(this, 1));
    // Decorative Gem Blocks
    new GuideChapter(this, "gemDecoBlocks", entryBlocks, new ItemStack(ModBlocks.gemBrickCoated, 1, SilentGems.random.nextInt(16)), -10,
        new PageTextOnly(this, 1));

    // @formatter:on
  }
}
