package net.silentchaos512.gems.guide;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.client.gui.config.GuiConfigSilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.guide.page.PageDebugTool;
import net.silentchaos512.gems.guide.page.PageOreSpawn;
import net.silentchaos512.gems.guide.page.PageSoulSkill;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.lib.guidebook.GuideBook;
import net.silentchaos512.lib.guidebook.IGuidePage;
import net.silentchaos512.lib.guidebook.chapter.GuideChapter;
import net.silentchaos512.lib.guidebook.entry.GuideEntry;
import net.silentchaos512.lib.guidebook.page.*;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuideBookGems extends GuideBook {
    public static final String TOOL_OWNER_NAME = "Guide Book";

    private GuideEntry entryGettingStarted;
    private GuideEntry entryBlocks;
    private GuideEntry entryItems;
    private GuideEntry entryTools;
    private GuideEntry entrySouls;
    private GuideEntry entryEnchantments;
    private GuideEntry entryDebug;

    public GuideBookGems() {
        super(SilentGems.MOD_ID, SilentGems.i18n);
        this.resourceGui = new ResourceLocation(SilentGems.MOD_ID, "textures/guide/gui_guide.png");
        this.resourceGadgets = new ResourceLocation(SilentGems.MOD_ID, "textures/guide/gui_guide_gadgets.png");
        edition = SilentGems.instance.getBuildNum();
    }

    @Override
    public void initEntries() {
        entryGettingStarted = new GuideEntry(this, "gettingStarted").setImportant();
        entryBlocks = new GuideEntry(this, "blocks");
        entryItems = new GuideEntry(this, "items");
        entryTools = new GuideEntry(this, "tools");
        entrySouls = new GuideEntry(this, "souls");
        entryEnchantments = new GuideEntry(this, "enchantments");
        if (edition == 0 || GemsConfig.DEBUG_MODE)
            entryDebug = new GuideEntry(this, "debug").setSpecial();
    }

    @SuppressWarnings("unused")
    @Override
    public void initChapters() {
        RecipeMaker rec = SilentGems.registry.getRecipeMaker();

        // Getting Started

        // Introduction
        new GuideChapter(this, "introduction", entryGettingStarted, new ItemStack(ModItems.gem, 1, SilentGems.random.nextInt(32)), 1000,
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2),
                new PageTextOnly(this, 3)).setSpecial();
        // Progression
        ItemStack flintPickaxe = ModItems.pickaxe.constructTool(false, new ItemStack(Items.FLINT));
        ToolHelper.setOriginalOwner(flintPickaxe, TOOL_OWNER_NAME);
        ItemStack flintPickaxeBroken = flintPickaxe.copy();
        flintPickaxeBroken.setItemDamage(ToolHelper.getMaxDamage(flintPickaxeBroken));
        ItemStack ironTipUpgrade = new ItemStack(ModItems.tipUpgrade);
        ItemStack flintPickaxeIronTips = ModItems.tipUpgrade.applyToTool(flintPickaxe, ironTipUpgrade);
        ItemStack gravel = new ItemStack(Blocks.GRAVEL);
        ItemStack gemPickaxe = ModItems.pickaxe.constructTool(new ItemStack(Items.STICK), Gems.RUBY.getItem(), Gems.SAPPHIRE.getItem(), Gems.RUBY.getItem());
        ToolHelper.setOriginalOwner(gemPickaxe, TOOL_OWNER_NAME);
        ItemStack diamondTipUpgrade = new ItemStack(ModItems.tipUpgrade, 1, 2);
        ItemStack gemPickaxeDiamondTips = ModItems.tipUpgrade.applyToTool(gemPickaxe, diamondTipUpgrade);
        ItemStack katana = ModItems.katana.constructTool(true, Gems.LEPIDOLITE.getItemSuper(), Gems.OPAL.getItemSuper(), Gems.BLACK_DIAMOND.getItemSuper());
        ToolHelper.setOriginalOwner(katana, TOOL_OWNER_NAME);
        new GuideChapter(this, "progression", entryGettingStarted, flintPickaxeIronTips, 100,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapeless(new ItemStack(Items.FLINT), gravel, gravel)),
                new PageCrafting(this, 3, rec.makeShapedOre(flintPickaxe, "fff", " s ", " s ", 'f', Items.FLINT, 's', "stickWood")),
                new PageTextOnly(this, 4),
                new PageCrafting(this, 5, rec.makeShapeless(flintPickaxe, flintPickaxeBroken, Items.FLINT, Items.FLINT)),
                new PageTextOnly(this, 6),
                new PageCrafting(this, 7, rec.makeShapelessOre(flintPickaxeIronTips, flintPickaxe, ironTipUpgrade)),
                new PageCrafting(this, 8, rec.makeShapedOre(gemPickaxe, "rsr", " t ", " t ", 'r', Gems.RUBY.getItem(), 's',
                        Gems.SAPPHIRE.getItem(), 't', "stickWood")),
                new PageTextOnly(this, 9),
                new PageCrafting(this, 10, rec.makeShapeless(gemPickaxeDiamondTips, gemPickaxe, diamondTipUpgrade)),
                new PageTextOnly(this, 11),
                new PageCrafting(this, 12, rec.makeShapedOre(katana, "lo", "d ", "r ", 'l', Gems.LEPIDOLITE.getItemSuper(), 'o',
                        Gems.OPAL.getItemSuper(), 'd', Gems.BLACK_DIAMOND.getItemSuper(), 'r', CraftingItems.ORNATE_GOLD_ROD.getStack())),
                new PageTextOnly(this, 13)).setImportant();
        // You Broke It
        new GuideChapter(this, "youBrokeIt", entryGettingStarted, CraftingItems.MAGNIFYING_GLASS.getStack(),
                new PageTextOnly(this, 1),
                new PageLinkButton(this, 2, "https://github.com/SilentChaos512/SilentGems/issues"));

        // Tools, Armor, and Parts

        // Parts
//    List<IGuidePage> pagesParts = Lists.newArrayList();
//    pagesParts.add(new PageTextOnly(this, 1));
//    for (ToolPart part : ToolPartRegistry.getMains()) {
//      pagesParts.add(new PageToolPart(this, 0, part));
//    }
//    new GuideChapter(this, "toolParts", entryTools, EnumGem.selectRandom().getItem(), 100,
//        pagesParts.toArray(new IGuidePage[pagesParts.size()]));
        // Axes
        ItemStack toolsEntryRod = SilentGems.random.nextFloat() < 0.67f ? CraftingItems.ORNATE_GOLD_ROD.getStack() : CraftingItems.ORNATE_SILVER_ROD.getStack();
        ItemStack chAxeGem = Gems.selectRandom().getItemSuper();
        ItemStack chAxe = makeTool(ModItems.axe, toolsEntryRod, chAxeGem, 3);
        new GuideChapter(this, "axe", entryTools, chAxe,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chAxe, "gg", "gr", " r", 'g', chAxeGem, 'r', toolsEntryRod)).setNoText());
        // Bows
        ItemStack chBowGem = Gems.selectRandom().getItemSuper();
        ItemStack chBow = makeTool(ModItems.bow, toolsEntryRod, chBowGem, 3);
        new GuideChapter(this, "bow", entryTools, chBow,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chBow, "rgs", "g s", "rgs", 'g', chBowGem, 'r', toolsEntryRod, 's', CraftingItems.GILDED_STRING.getStack())));
        // Daggers
        ItemStack chDaggerGem = Gems.selectRandom().getItemSuper();
        ItemStack chDagger = makeTool(ModItems.dagger, toolsEntryRod, chDaggerGem, 1);
        new GuideChapter(this, "dagger", entryTools, chDagger,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chDagger, "g", "r", "f", 'g', chDaggerGem, 'r', toolsEntryRod, 'f', "ingotGold")));
        // Hoes
        ItemStack chHoeGem = Gems.selectRandom().getItemSuper();
        ItemStack chHoe = makeTool(ModItems.hoe, toolsEntryRod, chHoeGem, 2);
        new GuideChapter(this, "hoe", entryTools, chHoe,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chHoe, "gg", " r", " r", 'g', chHoeGem, 'r', toolsEntryRod)).setNoText());
        // Katana
        ItemStack chKatanaGem = Gems.selectRandom().getItemSuper();
        ItemStack chKatana = makeTool(ModItems.katana, toolsEntryRod, chKatanaGem, 3);
        new GuideChapter(this, "katana", entryTools, chKatana,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chKatana, "gg", "g ", "r ", 'g', chKatanaGem, 'r', toolsEntryRod)).setNoText());
        // Machetes
        ItemStack chMacheteGem = Gems.selectRandom().getItemSuper();
        ItemStack chMachete = makeTool(ModItems.machete, toolsEntryRod, chMacheteGem, 3);
        new GuideChapter(this, "machete", entryTools, chMachete,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chMachete, " gg", " g ", "r  ", 'g', chMacheteGem, 'r', toolsEntryRod)).setNoText(),
                new PageTextOnly(this, 3),
                new PageTextOnly(this, 4));
        // Paxels
        ItemStack chPaxelGem = Gems.selectRandom().getItemSuper();
        ItemStack chPaxel = makeTool(ModItems.paxel, toolsEntryRod, chPaxelGem, 6);
        new GuideChapter(this, "paxel", entryTools, chPaxel,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chPaxel, "ggg", "grg", "gr ", 'g', chPaxelGem, 'r', toolsEntryRod)).setNoText());
        // Pickaxes
        ItemStack chPickaxeGem = Gems.selectRandom().getItemSuper();
        ItemStack chPickaxe = makeTool(ModItems.pickaxe, toolsEntryRod, chPickaxeGem, 3);
        new GuideChapter(this, "pickaxe", entryTools, chPickaxe,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chPickaxe, "ggg", " r ", " r ", 'g', chPickaxeGem, 'r', toolsEntryRod)).setNoText());
        // Scepters
        ItemStack chScepterGem = Gems.selectRandom().getItemSuper();
        ItemStack chScepter = makeTool(ModItems.scepter, toolsEntryRod, chScepterGem, 5);
        new GuideChapter(this, "scepter", entryTools, chScepter,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chScepter, " g ", "grg", "grg", 'g', chScepterGem, 'r', toolsEntryRod)).setNoText(),
                new PageTextOnly(this, 3));
        ItemStack chShieldGem = Gems.selectRandom().getItemSuper();
        ItemStack chShield = makeTool(ModItems.shield, toolsEntryRod, chShieldGem, 3);
        new GuideChapter(this, "shield", entryTools, chShield,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chShield, "gwg", "wrw", " g ", 'g', chShieldGem, 'r', toolsEntryRod, 'w', "plankWood")).setNoText());
        // Shovels
        ItemStack chShovelGem = Gems.selectRandom().getItemSuper();
        ItemStack chShovel = makeTool(ModItems.shovel, toolsEntryRod, chShovelGem, 1);
        new GuideChapter(this, "shovel", entryTools, chShovel,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chShovel, "g", "r", "r", 'g', chShovelGem, 'r', toolsEntryRod)).setNoText());
        // Sickles
        ItemStack chSickleGem = Gems.selectRandom().getItemSuper();
        ItemStack chSickle = makeTool(ModItems.sickle, toolsEntryRod, chSickleGem, 3);
        new GuideChapter(this, "sickle", entryTools, chSickle,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chSickle, " g", "gg", "r ", 'g', chSickleGem, 'r', toolsEntryRod)).setNoText());
        // Swords
        ItemStack chSwordGem = Gems.selectRandom().getItemSuper();
        ItemStack chSword = makeTool(ModItems.sword, toolsEntryRod, chSwordGem, 2);
        new GuideChapter(this, "sword", entryTools, chSword,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chSword, "g", "g", "r", 'g', chSwordGem, 'r', toolsEntryRod)).setNoText());
        // Tomahawks
        ItemStack chTomahawkGem = Gems.selectRandom().getItemSuper();
        ItemStack chTomahawk = makeTool(ModItems.tomahawk, toolsEntryRod, chTomahawkGem, 4);
        new GuideChapter(this, "tomahawk", entryTools, chTomahawk,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapedOre(chTomahawk, "ggg", "gr ", " r ", 'g', chTomahawkGem, 'r', toolsEntryRod)).setNoText());
        // Armor
        ItemStack chHelmetGem = Gems.selectRandom().getItemSuper();
        ItemStack chHelmet = ModItems.gemHelmet.constructArmor(EnumMaterialTier.SUPER, chHelmetGem);
        ItemStack chHelmetFrame = ModItems.armorFrame.getFrameForArmorPiece(ModItems.gemHelmet, EnumMaterialTier.SUPER);
        ArmorHelper.setOriginalOwner(chHelmet, TOOL_OWNER_NAME);
        new GuideChapter(this, "armor", entryTools, chHelmet, -10,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, CraftingItems.ItemCrafting.recipeLatticeMundane).setNoText(),
                new PageCrafting(this, 3, CraftingItems.ItemCrafting.recipeLatticeRegular).setNoText(),
                new PageCrafting(this, 4, CraftingItems.ItemCrafting.recipeLatticeSuper).setNoText(),
                new PageCrafting(this, 5, rec.makeShaped(chHelmetFrame, "lll", "l l", 'l', CraftingItems.ARMOR_LATTICE_SUPER.getStack())),
                new PageCrafting(this, 6, rec.makeShapedOre(chHelmet, " g ", "gfg", " g ", 'g', chHelmetGem, 'f', chHelmetFrame)));

        // Blocks

        // Ores
        new GuideChapter(this, "ores", entryBlocks, new ItemStack(ModBlocks.gemOre, 1, SilentGems.random.nextInt(16)), 10,
                new PageTextOnly(this, 1),
                new PageOreSpawn(this, 2, GemsConfig.WORLD_GEN_GEMS),
                new PageOreSpawn(this, 3, GemsConfig.WORLD_GEN_GEMS_DARK),
                new PageOreSpawn(this, 8, GemsConfig.WORLD_GEN_GEMS_LIGHT),
                new PageOreSpawn(this, 4, GemsConfig.WORLD_GEN_CHAOS),
                new PageFurnace(this, 5, CraftingItems.CHAOS_ESSENCE.getStack()),
                new PageOreSpawn(this, 6, GemsConfig.WORLD_GEN_ENDER),
                new PageFurnace(this, 7, CraftingItems.ENDER_ESSENCE.getStack())).setImportant();
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
                new PagePicture(this, 3, new ResourceLocation(SilentGems.MOD_ID, "textures/guide/chaosnode.png"), 125),
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Chaos Pylons
        new GuideChapter(this, "chaosPylon", entryBlocks, new ItemStack(ModBlocks.chaosPylon),
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Fluffy Blocks
        new GuideChapter(this, "fluffyBlocks", entryBlocks, new ItemStack(ModBlocks.fluffyBlock),
                new PageCrafting(this, 1, rec.makeShaped(new ItemStack(ModBlocks.fluffyBlock), "ff", "ff", 'f', CraftingItems.FLUFFY_FABRIC.getStack())),
                new PageTextOnly(this, 2));
        // Glow Rose
        new GuideChapter(this, "glowRose", entryBlocks, new ItemStack(ModBlocks.glowRose),
                new PageTextOnly(this, 1));
        // Material Grader
        new GuideChapter(this, "materialGrader", entryBlocks, new ItemStack(ModBlocks.materialGrader),
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Decorative Gem Blocks
        new GuideChapter(this, "gemDecoBlocks", entryBlocks, new ItemStack(ModBlocks.gemBrickCoated, 1, SilentGems.random.nextInt(16)), -10,
                new PageTextOnly(this, 1));

        // Items

        // Crafting Materials
        List<IGuidePage> pages = Lists.newArrayList();
        pages.add(new PageTextOnly(this, 1));
        for (CraftingItems item : CraftingItems.values()) {
            ItemStack stack = item.getStack();
            IRecipe recipe = CraftingItems.ItemCrafting.guideRecipeMap.get(stack.getItemDamage());

            if (stack.isItemEqual(CraftingItems.CHAOS_ESSENCE.getStack()) || stack.isItemEqual(CraftingItems.ENDER_ESSENCE.getStack()) || stack.isItemEqual(CraftingItems.CHAOS_IRON.getStack()))
                pages.add(new PageFurnace(this, 100 + stack.getItemDamage(), stack));
            else if (recipe != null)
                pages.add(new PageCrafting(this, 100 + stack.getItemDamage(), recipe));
            else
                pages.add(new PageTextOnly(this, 100 + stack.getItemDamage()));
        }
        new GuideChapter(this, "craftingMaterial", entryItems, CraftingItems.CHAOS_ESSENCE.getStack(),
                pages.toArray(new IGuidePage[0]));
        // Chaos Gems
        ItemStack chChaosGem = new ItemStack(ModItems.chaosGem, 1, Gems.selectRandom().ordinal());
        ModItems.chaosGem.receiveCharge(chChaosGem, ModItems.chaosGem.getMaxCharge(chChaosGem), false);
        ItemStack chChaosGemWithBuffs = chChaosGem.copy();
        ItemStack chChaosGemRuneStrength = new ItemStack(ModItems.chaosRune);
        ModItems.chaosRune.setBuff(chChaosGemRuneStrength, ChaosBuff.STRENGTH);
        ItemStack chChaosGemRuneResistance = new ItemStack(ModItems.chaosRune);
        ModItems.chaosRune.setBuff(chChaosGemRuneResistance, ChaosBuff.RESISTANCE);
        ModItems.chaosGem.addBuff(chChaosGemWithBuffs, ChaosBuff.STRENGTH);
        ModItems.chaosGem.addBuff(chChaosGemWithBuffs, ChaosBuff.STRENGTH);
        ModItems.chaosGem.addBuff(chChaosGemWithBuffs, ChaosBuff.RESISTANCE);
        ModItems.chaosGem.addBuff(chChaosGemWithBuffs, ChaosBuff.RESISTANCE);
        new GuideChapter(this, "chaosGem", entryItems, chChaosGem,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapeless(chChaosGemWithBuffs, chChaosGem, chChaosGemRuneStrength, chChaosGemRuneStrength, chChaosGemRuneResistance, chChaosGemRuneResistance)),
                new PageTextOnly(this, 3));
        // Chaos Orbs
        ItemStack chChaosOrb = new ItemStack(ModItems.chaosOrb, 1, ItemChaosOrb.Type.SUPREME.ordinal());
        ModItems.chaosOrb.receiveCharge(chChaosOrb, ModItems.chaosOrb.getMaxCharge(chChaosOrb), false);
        new GuideChapter(this, "chaosOrb", entryItems, chChaosOrb,
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Drawing Compass
        ItemStack chDrawingCompass = new ItemStack(ModItems.drawingCompass);
        new GuideChapter(this, "drawingCompass", entryItems, chDrawingCompass,
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Enchantment Tokens
        ItemStack chEnchantmentToken = new ItemStack(ModItems.enchantmentToken, 1, EnchantmentToken.BLANK_META);
        ItemStack tokenSharpness = ModItems.enchantmentToken.constructToken(Enchantments.SHARPNESS);
        ItemStack chEnchantmentTokenPickaxe = ToolRandomizer.INSTANCE.randomize(new ItemStack(ModItems.pickaxe), 0.75f);
        ItemStack chEnchantmentTokenPickaxeEnchanted = chEnchantmentTokenPickaxe.copy();
        ItemStack tokenUnbreaking = ModItems.enchantmentToken.constructToken(Enchantments.UNBREAKING);
        ItemStack tokenFortune = ModItems.enchantmentToken.constructToken(Enchantments.FORTUNE);
        for (int i = 0; i < 3; ++i) {
            ModItems.enchantmentToken.applyTokenToTool(tokenFortune, chEnchantmentTokenPickaxeEnchanted);
            ModItems.enchantmentToken.applyTokenToTool(tokenUnbreaking, chEnchantmentTokenPickaxeEnchanted);
        }
        new GuideChapter(this, "enchantmentToken", entryItems, chEnchantmentToken,
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2),
                new PageCrafting(this, 3, rec.makeShapedOre(new ItemStack(ModItems.enchantmentToken, 12, EnchantmentToken.BLANK_META), "ggg", "lcl", "ggg", 'g', "ingotGold", 'l', "gemLapis", 'c', "gemChaos")),
                new PageCrafting(this, 4, rec.makeShapedOre(tokenSharpness, "r r", "fbf", "fff", 'r', "gemRuby", 'f', Items.FLINT, 'b', chEnchantmentToken)),
                new PageTextOnly(this, 5),
                new PageCrafting(this, 6, rec.makeShapeless(chEnchantmentTokenPickaxeEnchanted, chEnchantmentTokenPickaxe, tokenUnbreaking, tokenUnbreaking, tokenUnbreaking, tokenFortune, tokenFortune, tokenFortune)));
        // Fluffy Puffs
        new GuideChapter(this, "fluffyPuff", entryItems, new ItemStack(ModItems.fluffyPuff),
                new PageTextOnly(this, 1));
        // Gems
        Gems chGem = Gems.selectRandom();
        ItemStack craftedShards = chGem.getShard();
        craftedShards.setCount(9);
        new GuideChapter(this, "gem", entryItems, chGem.getItem(),
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapelessOre(craftedShards, chGem.getItem())),
                new PageCrafting(this, 3, rec.makeShapedOre(chGem.getItemSuper(), "cgc", "cdc", "cgc", 'c', CraftingItems.CHAOS_ESSENCE.getStack(), 'g', chGem.getItem(), 'd', "dustGlowstone")));
        // Holding Gem
        ItemStack chHoldingGem = ModItems.holdingGem.construct(Gems.selectRandom());
        ItemStack chHoldingGemIcon = chHoldingGem.copy();
        chHoldingGemIcon.setItemDamage(0);
        ItemStack chHoldingGemSet = chHoldingGem.copy();
        ModItems.holdingGem.setBlockPlaced(chHoldingGemSet, Blocks.COBBLESTONE.getDefaultState());
        chHoldingGemSet.setItemDamage(chHoldingGemSet.getMaxDamage() - 1);
        new GuideChapter(this, "holdingGem", entryItems, chHoldingGemIcon,
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShapeless(chHoldingGemSet, chHoldingGem, new ItemStack(Blocks.COBBLESTONE))),
                new PageTextOnly(this, 3));
        // Tip upgrades
        ItemStack chTipUpgrade = new ItemStack(ModItems.tipUpgrade, 1, 2);
        pages = Lists.newArrayList();
        pages.add(new PageTextOnly(this, 1));
        pages.add(new PageTextOnly(this, 2));
        pages.add(new PageCrafting(this, 3, rec.makeShapelessOre(CraftingItems.UPGRADE_BASE.getStack(), Items.FLINT, Items.FLINT, "stickWood", "plankWood")));
        for (IRecipe recipe : ItemTipUpgrade.RECIPES) {
            pages.add(new PageCrafting(this, 0, recipe).setNoText());
        }
        new GuideChapter(this, "tipUpgrade", entryItems, chTipUpgrade, pages.toArray(new IGuidePage[0]));
        // Torch Bandolier
        ItemStack chTorchBandolier = new ItemStack(ModItems.torchBandolier);
        new GuideChapter(this, "torchBandolier", entryItems, chTorchBandolier,
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Legacy items
        ItemStack chLegacyItems = Gems.selectRandom().getItemSuper();
        new GuideChapter(this, "legacyItems", entryItems, chLegacyItems,
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));

        /*
         * Souls and Skills
         */

        // Important Notice
//    new GuideChapter(this, "soulsImportant", entrySouls, new ItemStack(Items.DIAMOND),
//        new PageTextOnly(this, 1)).setImportant();

        // Tool Souls
        new GuideChapter(this, "toolSoul", entrySouls, new ItemStack(ModItems.toolSoul),
                new PageTextOnly(this, 1),
                new PageCrafting(this, 2, rec.makeShaped(new ItemStack(ModItems.toolSoul), " s ", "sds", " s ", 's', ModItems.soulGem, 'd', CraftingItems.SOUL_SHELL.getStack())));

        // Soul Skills (includes a page for each skill!)
        pages = new ArrayList<>();
        pages.add(new PageTextOnly(this, 1));
        pages.add(new PageTextOnly(this, 2));
        for (SoulSkill skill : SoulSkill.getSkillList()) {
            pages.add(new PageSoulSkill(this, skill));
        }
        new GuideChapter(this, "soulSkills", entrySouls, new ItemStack(ModItems.skillOrb),
                pages.toArray(new IGuidePage[0]));

        // Enchantments

        // Gravity
        new GuideChapter(this, "enchantmentGravity", entryEnchantments, ModItems.enchantmentToken.constructToken(ModEnchantments.gravity),
                new PageTextOnly(this, 1));
        // Ice Aspect
        new GuideChapter(this, "enchantmentIceAspect", entryEnchantments, ModItems.enchantmentToken.constructToken(ModEnchantments.iceAspect),
                new PageTextOnly(this, 1));
        // Life Steal
        new GuideChapter(this, "enchantmentLifeSteal", entryEnchantments, ModItems.enchantmentToken.constructToken(ModEnchantments.lifeSteal),
                new PageTextOnly(this, 1),
                new PageTextOnly(this, 2));
        // Lightning Aspect
        new GuideChapter(this, "enchantmentLightningAspect", entryEnchantments, ModItems.enchantmentToken.constructToken(ModEnchantments.lightningAspect),
                new PageTextOnly(this, 1));
        // Magic Damage (Concentration)
        new GuideChapter(this, "enchantmentMagicDamage", entryEnchantments, ModItems.enchantmentToken.constructToken(ModEnchantments.magicDamage),
                new PageTextOnly(this, 1));

        // Debug

        if (entryDebug != null) {
            // Tool test
            new GuideChapter(this, "toolTest", entryDebug, CraftingItems.IRON_POTATO.getStack(),
                    new PageDebugTool(this, 1),
                    new PageDebugTool(this, 2));
        }
    }

    public static final String[] QUOTES = {
            "The flowers probably won't kill you.",
            "Try the donuts!",
            "May contain unintended &cR&6a&ei&an&9b&do&5w&0s!".replaceAll("&", "\u00a7"),
            "Shake well and refrigerate after opening.",
            "Drowning in [slightly fewer] JSON files...",
            "Download only from CurseForge!",
            "Rabbit poop coffee!",
            "It stares into your soul.",
            "Pot now included... flower pot, that is.",
            "Did you know Chaos Gems are finally back?",
            "Also try Extra Parts!",
            "Your wish has been granted!",
            "Voted most unnecessarily complicated mod in high school.",
            "I like your gems!@HockeyStick",
            "Also try JEI! Seriously, learn to look up the recipes... How do you play without mods like this?",
            "How do you craft the upgrades?@Everyone Ever",
            "Scathing comments since 2017!",
            "Muffin button not included.",
            "Now with more enchantments!",
            "Send help.@SilentChaos512",
            "Good lookin' mainframes!",
            "Looking for something fresh and new? Try Silent Gear!"
    };

    @Override
    public String[] getQuotes() {
        return QUOTES;
    }

    @Override
    public @Nonnull
    String selectQuote(Random rand) {
        if (rand.nextInt(100) < 40) return "Join my Discord server: https://discord.gg/gh84eWK";
        if (rand.nextInt(100) == 0) return "Lolis are love, lolis are life!";
        return super.selectQuote(rand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getConfigScreen(GuiScreen parent) {
        return new GuiConfigSilentGems(parent);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiScreen getAchievementScreen(GuiScreen parent) {
        return null;
    }

    private ItemStack makeTool(ITool tool, ItemStack rod, ItemStack gem, int gemCount) {
        ItemStack[] array = new ItemStack[gemCount];
        for (int i = 0; i < array.length; ++i)
            array[i] = gem;
        ItemStack ret = tool.constructTool(rod, array);
        ToolHelper.setOriginalOwner(ret, TOOL_OWNER_NAME);
        return ret;
    }
}
