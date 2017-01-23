package net.silentchaos512.gems.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.armor.ItemArmorFrame;
import net.silentchaos512.gems.item.armor.ItemGemArmor;
import net.silentchaos512.gems.item.tool.ItemGemAxe;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemHoe;
import net.silentchaos512.gems.item.tool.ItemGemKatana;
import net.silentchaos512.gems.item.tool.ItemGemPickaxe;
import net.silentchaos512.gems.item.tool.ItemGemScepter;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.item.tool.ItemGemShovel;
import net.silentchaos512.gems.item.tool.ItemGemSickle;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.item.tool.ItemGemTomahawk;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.SRegistry;

public class ModItems {

  public static final ItemGem gem = new ItemGem();
  public static final ItemGemShard gemShard = new ItemGemShard();
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
  public static final ItemGemKatana katana = new ItemGemKatana();
  public static final ItemGemScepter scepter = new ItemGemScepter();
  public static final ItemGemTomahawk tomahawk = new ItemGemTomahawk();
  public static final ItemGemBow bow = new ItemGemBow();
  public static final ItemGemShield shield = new ItemGemShield();
  public static final ItemGemPickaxe pickaxe = new ItemGemPickaxe();
  public static final ItemGemShovel shovel = new ItemGemShovel();
  public static final ItemGemAxe axe = new ItemGemAxe();
  public static final ItemGemHoe hoe = new ItemGemHoe();
  public static final ItemGemSickle sickle = new ItemGemSickle();

  // Armor
  public static final ItemGemArmor gemHelmet = new ItemGemArmor(0, EntityEquipmentSlot.HEAD, Names.HELMET);
  public static final ItemGemArmor gemChestplate = new ItemGemArmor(1, EntityEquipmentSlot.CHEST, Names.CHESTPLATE);
  public static final ItemGemArmor gemLeggings = new ItemGemArmor(2, EntityEquipmentSlot.LEGS, Names.LEGGINGS);
  public static final ItemGemArmor gemBoots = new ItemGemArmor(3, EntityEquipmentSlot.FEET, Names.BOOTS);

  public static final ToolRenderHelperBase toolRenderHelper = FMLCommonHandler.instance()
      .getSide() == Side.CLIENT ? new ToolRenderHelper() : new ToolRenderHelperBase();

  public static final List<Item> tools = Lists.newArrayList(); // Filled by SRegistry override

  public static void init() {

    SRegistry reg = SilentGems.instance.registry;

    reg.registerItem(gem).setCreativeTab(GemsCreativeTabs.materials);
    reg.registerItem(gemShard).setCreativeTab(GemsCreativeTabs.materials);
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
    reg.registerItem(katana, Names.KATANA);
    reg.registerItem(scepter, Names.SCEPTER);
    reg.registerItem(tomahawk, Names.TOMAHAWK);
    reg.registerItem(bow, Names.BOW).setCreativeTab(GemsCreativeTabs.tools);
    reg.registerItem(shield, Names.SHIELD).setCreativeTab(GemsCreativeTabs.tools);
    reg.registerItem(pickaxe, Names.PICKAXE);
    reg.registerItem(shovel, Names.SHOVEL);
    reg.registerItem(axe, Names.AXE);
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

    initExtraRecipes();
  }

  public static void initExtraRecipes() {

    GameRegistry.addShapelessRecipe(new ItemStack(Items.FLINT), Blocks.GRAVEL, Blocks.GRAVEL);
  }
}
