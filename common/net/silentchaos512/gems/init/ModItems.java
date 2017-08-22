package net.silentchaos512.gems.init;

import java.util.List;

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
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.item.ItemChaosOrb;
import net.silentchaos512.gems.item.ItemChaosRune;
import net.silentchaos512.gems.item.ItemCrafting;
import net.silentchaos512.gems.item.ItemDebug;
import net.silentchaos512.gems.item.ItemDrawingCompass;
import net.silentchaos512.gems.item.ItemDyeSG;
import net.silentchaos512.gems.item.ItemEnchantmentToken;
import net.silentchaos512.gems.item.ItemFluffyPuff;
import net.silentchaos512.gems.item.ItemFluffyPuffSeeds;
import net.silentchaos512.gems.item.ItemFoodSG;
import net.silentchaos512.gems.item.ItemGem;
import net.silentchaos512.gems.item.ItemGemShard;
import net.silentchaos512.gems.item.ItemGlowRoseFertilizer;
import net.silentchaos512.gems.item.ItemHoldingGem;
import net.silentchaos512.gems.item.ItemNodeMover;
import net.silentchaos512.gems.item.ItemPetSummoner;
import net.silentchaos512.gems.item.ItemReturnHome;
import net.silentchaos512.gems.item.ItemSoulGem;
import net.silentchaos512.gems.item.ItemTeleporterLinker;
import net.silentchaos512.gems.item.ItemTipUpgrade;
import net.silentchaos512.gems.item.ItemTorchBandolier;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.ToolRenderHelperBase;
import net.silentchaos512.gems.item.armor.ItemArmorFrame;
import net.silentchaos512.gems.item.armor.ItemGemArmor;
import net.silentchaos512.gems.item.tool.ItemGemAxe;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemDagger;
import net.silentchaos512.gems.item.tool.ItemGemHoe;
import net.silentchaos512.gems.item.tool.ItemGemKatana;
import net.silentchaos512.gems.item.tool.ItemGemPaxel;
import net.silentchaos512.gems.item.tool.ItemGemPickaxe;
import net.silentchaos512.gems.item.tool.ItemGemScepter;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.item.tool.ItemGemShovel;
import net.silentchaos512.gems.item.tool.ItemGemSickle;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.item.tool.ItemGemTomahawk;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemGuideBookSL;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.registry.SRegistry;

public class ModItems implements IRegistrationHandler<Item> {

  public static final ItemGem gem = new ItemGem();
  public static final ItemGemShard gemShard = new ItemGemShard();
  public static final ItemSoulGem soulGem = new ItemSoulGem();
  public static final ItemCrafting craftingMaterial = new ItemCrafting();
  public static final ItemTipUpgrade tipUpgrade = new ItemTipUpgrade();
  public static final ItemEnchantmentToken enchantmentToken = new ItemEnchantmentToken();
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

  // Tools
  public static final ItemGemSword sword = new ItemGemSword();
  public static final ItemGemDagger dagger = new ItemGemDagger();
  public static final ItemGemKatana katana = new ItemGemKatana();
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
  public static final ItemGemArmor gemHelmet = new ItemGemArmor(0, EntityEquipmentSlot.HEAD,
      Names.HELMET);
  public static final ItemGemArmor gemChestplate = new ItemGemArmor(1, EntityEquipmentSlot.CHEST,
      Names.CHESTPLATE);
  public static final ItemGemArmor gemLeggings = new ItemGemArmor(2, EntityEquipmentSlot.LEGS,
      Names.LEGGINGS);
  public static final ItemGemArmor gemBoots = new ItemGemArmor(3, EntityEquipmentSlot.FEET,
      Names.BOOTS);

  public static final ToolRenderHelperBase toolRenderHelper = FMLCommonHandler.instance()
      .getSide() == Side.CLIENT ? new ToolRenderHelper() : new ToolRenderHelperBase();

  // Guide Book
  public static final ItemGuideBookSL guideBook = new ItemGuideBookSL(new GuideBookGems());

  public static final List<Item> tools = Lists.newArrayList(); // Filled by SRegistry override

  @Override
  public void registerAll(SRegistry reg) {

    reg.registerItem(gem).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(gemShard).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(soulGem).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(craftingMaterial).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(tipUpgrade).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(enchantmentToken).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(chaosGem).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(chaosRune).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(armorFrame).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(fluffyPuffSeeds, Names.FLUFFY_PUFF_SEEDS);
    reg.registerItem(fluffyPuff).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(glowRoseFertilizier).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(dye).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(food, Names.FOOD).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(holdingGem).setCreativeTab(GemsCreativeTabs.tools);
    reg.registerItem(torchBandolier).setCreativeTab(GemsCreativeTabs.tools);
    reg.registerItem(drawingCompass).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(chaosOrb).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(nodeMover).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(teleporterLinker).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(returnHomeCharm).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(petSummoner).setCreativeTab(GemsCreativeTabs.utility);
    reg.registerItem(debugItem).setCreativeTab(GemsCreativeTabs.utility);

    // Tools
    reg.registerItem(sword, Names.SWORD);
    reg.registerItem(dagger);
    reg.registerItem(katana, Names.KATANA);
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
    reg.registerItem(toolRenderHelper).setCreativeTab(null);
    toolRenderHelper.init();

    reg.registerItem(guideBook);

    initExtraRecipes();
  }

  public static void initExtraRecipes() {

    RecipeMaker recipes = SilentGems.registry.recipes;
    recipes.addShapeless("flint", new ItemStack(Items.FLINT), Blocks.GRAVEL, Blocks.GRAVEL);
    recipes.addShapeless("guide_book", new ItemStack(guideBook), Items.BOOK,
        new ItemStack(gem, 1, OreDictionary.WILDCARD_VALUE));
  }
}
