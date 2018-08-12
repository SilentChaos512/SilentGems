package net.silentchaos512.gems.init;

import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.guide.GuideBookGems;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.item.armor.ItemArmorFrame;
import net.silentchaos512.gems.item.armor.ItemGemArmor;
import net.silentchaos512.gems.item.quiver.ItemQuiver;
import net.silentchaos512.gems.item.quiver.ItemQuiverEmpty;
import net.silentchaos512.gems.item.tool.*;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemGuideBookSL;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.registry.SRegistry;

import java.util.List;

public class ModItems {
    public static final ItemGem gem = new ItemGem();
    public static final ItemSL gemSuper = new ItemSL(EnumGem.values().length, SilentGems.MODID, Names.GEM_SUPER);
    public static final ItemGemShard gemShard = new ItemGemShard();
    public static final ItemSoulGem soulGem = new ItemSoulGem();
    public static final ItemCrafting craftingMaterial = new ItemCrafting();
    public static final ItemQuiver quiver = new ItemQuiver();
    public static final ItemQuiverEmpty quiverEmpty = new ItemQuiverEmpty();
    public static final ItemTipUpgrade tipUpgrade = new ItemTipUpgrade();
    public static final ItemEnchantmentToken enchantmentToken = new ItemEnchantmentToken();
    public static final ItemToolSoul toolSoul = new ItemToolSoul();
    public static final ItemSkillOrb skillOrb = new ItemSkillOrb();
    public static final ItemChaosGem chaosGem = new ItemChaosGem();
    public static final ItemChaosRune chaosRune = new ItemChaosRune();
    public static final ItemArmorFrame armorFrame = new ItemArmorFrame();
    public static final ItemFluffyPuffSeeds fluffyPuffSeeds = new ItemFluffyPuffSeeds();
    public static final ItemFluffyPuff fluffyPuff = new ItemFluffyPuff();
    public static final ItemGlowRoseFertilizer glowRoseFertilizier = new ItemGlowRoseFertilizer();
    public static final ItemDyeSG dye = new ItemDyeSG();
    public static final ItemFoodSG food = new ItemFoodSG();
    public static final ItemHoldingGem holdingGem = new ItemHoldingGem();
    public static final ItemTorchBandolier torchBandolier = new ItemTorchBandolier();
    public static final ItemDrawingCompass drawingCompass = new ItemDrawingCompass();
    public static final ItemChaosOrb chaosOrb = new ItemChaosOrb();
    public static final ItemNodeMover nodeMover = new ItemNodeMover();
    public static final ItemTeleporterLinker teleporterLinker = new ItemTeleporterLinker();
    public static final ItemReturnHome returnHomeCharm = new ItemReturnHome();
    public static final ItemPetSummoner petSummoner = new ItemPetSummoner();
    public static final ItemDebug debugItem = new ItemDebug();

    public static final ItemGemArrow arrow = new ItemGemArrow();

    // Tools
    public static final ItemGemSword sword = new ItemGemSword();
    public static final ItemGemDagger dagger = new ItemGemDagger();
    public static final ItemGemKatana katana = new ItemGemKatana();
    public static final ItemGemMachete machete = new ItemGemMachete();
    public static final ItemGemScepter scepter = new ItemGemScepter();
    public static final ItemGemTomahawk tomahawk = new ItemGemTomahawk();
    public static final ItemGemBow bow = new ItemGemBow();
    public static final ItemGemShield shield = new ItemGemShield();
    public static final ItemGemPickaxe pickaxe = new ItemGemPickaxe();
    public static final ItemGemShovel shovel = new ItemGemShovel();
    public static final ItemGemAxe axe = new ItemGemAxe();
    public static final ItemGemPaxel paxel = new ItemGemPaxel();
    public static final ItemGemHoe hoe = new ItemGemHoe();
    public static final ItemGemSickle sickle = new ItemGemSickle();

    // Armor
    public static final ItemGemArmor gemHelmet = new ItemGemArmor(0, EntityEquipmentSlot.HEAD, Names.HELMET);
    public static final ItemGemArmor gemChestplate = new ItemGemArmor(1, EntityEquipmentSlot.CHEST, Names.CHESTPLATE);
    public static final ItemGemArmor gemLeggings = new ItemGemArmor(2, EntityEquipmentSlot.LEGS, Names.LEGGINGS);
    public static final ItemGemArmor gemBoots = new ItemGemArmor(3, EntityEquipmentSlot.FEET, Names.BOOTS);

    public static final ToolRenderHelperBase toolRenderHelper = FMLCommonHandler.instance()
            .getSide() == Side.CLIENT ? new ToolRenderHelper() : new ToolRenderHelperBase();

    // Guide Book
    public static final ItemGuideBookSL guideBook = new ItemGuideBookSL(new GuideBookGems());

    public static final List<Item> tools = Lists.newArrayList(); // Filled by SRegistry override

    public static void registerAll(SRegistry reg) {
        reg.registerItem(gem, Names.GEM).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(gemSuper, Names.GEM_SUPER).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(gemShard, Names.GEM_SHARD).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(soulGem, Names.SOUL_GEM).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(craftingMaterial, Names.CRAFTING_MATERIAL).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(quiver, Names.QUIVER_NON_EMPTY).setCreativeTab(null);
        reg.registerItem(quiverEmpty, Names.QUIVER).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(tipUpgrade, Names.UPGRADE_TIP).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(enchantmentToken, Names.ENCHANTMENT_TOKEN).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(toolSoul, Names.TOOL_SOUL).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(skillOrb, Names.SKILL_ORB).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(chaosGem, Names.CHAOS_GEM).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(chaosRune, Names.CHAOS_RUNE).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(armorFrame, Names.ARMOR_FRAME).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(fluffyPuffSeeds, Names.FLUFFY_PUFF_SEEDS);
        reg.registerItem(fluffyPuff, Names.FLUFFY_PUFF).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(glowRoseFertilizier, Names.GLOW_ROSE_FERTILIZER).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(dye, Names.DYE).setCreativeTab(GemsCreativeTabs.materials);
        reg.registerItem(food, Names.FOOD).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(holdingGem, Names.HOLDING_GEM).setCreativeTab(GemsCreativeTabs.tools);
        reg.registerItem(torchBandolier, Names.TORCH_BANDOLIER).setCreativeTab(GemsCreativeTabs.tools);
        reg.registerItem(drawingCompass, Names.DRAWING_COMPASS).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(chaosOrb, Names.CHAOS_ORB).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(nodeMover, Names.NODE_MOVER).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(teleporterLinker, Names.TELEPORTER_LINKER).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(returnHomeCharm, Names.RETURN_HOME_CHARM).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(petSummoner, Names.PET_SUMMONER).setCreativeTab(GemsCreativeTabs.utility);
        reg.registerItem(debugItem, Names.DEBUG_ITEM).setCreativeTab(GemsCreativeTabs.utility);

        reg.registerItem(arrow, Names.ARROW).setCreativeTab(GemsCreativeTabs.tools);

        // Tools
        reg.registerItem(sword, Names.SWORD);
        reg.registerItem(dagger, Names.DAGGER);
        reg.registerItem(katana, Names.KATANA);
        reg.registerItem(machete, Names.MACHETE);
        reg.registerItem(scepter, Names.SCEPTER);
        reg.registerItem(tomahawk, Names.TOMAHAWK);
        reg.registerItem(bow, Names.BOW).setCreativeTab(GemsCreativeTabs.tools);
        reg.registerItem(shield, Names.SHIELD).setCreativeTab(GemsCreativeTabs.tools);
        reg.registerItem(pickaxe, Names.PICKAXE);
        reg.registerItem(shovel, Names.SHOVEL);
        reg.registerItem(axe, Names.AXE);
        reg.registerItem(paxel, Names.PAXEL);
        reg.registerItem(hoe, Names.HOE);
        reg.registerItem(sickle, Names.SICKLE);

        // Armor
        reg.registerItem(gemHelmet, Names.HELMET);
        reg.registerItem(gemChestplate, Names.CHESTPLATE);
        reg.registerItem(gemLeggings, Names.LEGGINGS);
        reg.registerItem(gemBoots, Names.BOOTS);

        // ToolRenderHelper
        reg.registerItem(toolRenderHelper, Names.TOOL_RENDER_HELPER).setCreativeTab(null);
        toolRenderHelper.init();

        reg.registerItem(guideBook, "guide_book");

        initExtraRecipes();
    }

    private static void initExtraRecipes() {
        RecipeMaker recipes = SilentGems.registry.recipes;
        recipes.addShapeless("flint", new ItemStack(Items.FLINT), Blocks.GRAVEL, Blocks.GRAVEL);
        recipes.addShapeless("guide_book", new ItemStack(guideBook), Items.BOOK, new ItemStack(gem, 1, OreDictionary.WILDCARD_VALUE));
    }
}
