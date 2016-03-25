package net.silentchaos512.gems.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.tool.ItemGemAxe;
import net.silentchaos512.gems.item.tool.ItemGemHoe;
import net.silentchaos512.gems.item.tool.ItemGemKatana;
import net.silentchaos512.gems.item.tool.ItemGemPickaxe;
import net.silentchaos512.gems.item.tool.ItemGemScepter;
import net.silentchaos512.gems.item.tool.ItemGemShovel;
import net.silentchaos512.gems.item.tool.ItemGemSickle;
import net.silentchaos512.gems.item.tool.ItemGemSword;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.SRegistry;

public class ModItems {

  public static ItemGem gem;
  public static ItemGemShard gemShard;
  public static ItemCrafting craftingMaterial;
  public static ItemTipUpgrade tipUpgrade;
  public static ItemFluffyPuffSeeds fluffyPuffSeeds;
  public static ItemFluffyPuff fluffyPuff;
  public static ItemDyeSG dye;
  public static ItemFoodSG food;
  public static ItemTorchBandolier torchBandolier;
  public static ItemChaosOrb chaosOrb;

  public static ItemGemSword sword;
  public static ItemGemKatana katana;
  public static ItemGemScepter scepter;
  public static ItemSL bow;             // temp
  public static ItemSL shield;          // temp
  public static ItemGemPickaxe pickaxe;
  public static ItemGemShovel shovel;
  public static ItemGemAxe axe;
  public static ItemGemHoe hoe;
  public static ItemGemSickle sickle;
  public static ItemSL fishingRod;      // temp

  public static ToolRenderHelperBase toolRenderHelper;

  public static final List<Item> tools = Lists.newArrayList();

  public static void init() {

    SRegistry reg = SilentGems.instance.registry;

    gem = (ItemGem) reg.registerItem(new ItemGem());
    gemShard = (ItemGemShard) reg.registerItem(new ItemGemShard());
    craftingMaterial = (ItemCrafting) reg.registerItem(new ItemCrafting());
    tipUpgrade = (ItemTipUpgrade) reg.registerItem(new ItemTipUpgrade());
    fluffyPuffSeeds = (ItemFluffyPuffSeeds) reg.registerItem(new ItemFluffyPuffSeeds(),
        Names.FLUFFY_PUFF_SEEDS);
    fluffyPuff = (ItemFluffyPuff) reg.registerItem(new ItemFluffyPuff());
    dye = (ItemDyeSG) reg.registerItem(new ItemDyeSG());
    food = (ItemFoodSG) reg.registerItem(new ItemFoodSG(), Names.FOOD);
    torchBandolier = (ItemTorchBandolier) reg.registerItem(new ItemTorchBandolier());
    chaosOrb = (ItemChaosOrb) reg.registerItem(new ItemChaosOrb());

    // Tools
    sword = (ItemGemSword) reg.registerItem(new ItemGemSword(), Names.SWORD);
    katana = (ItemGemKatana) reg.registerItem(new ItemGemKatana(), Names.KATANA);
    scepter = (ItemGemScepter) reg.registerItem(new ItemGemScepter(), Names.SCEPTER);
    bow = (ItemSL) reg.registerItem(new ItemSL(1, SilentGems.MOD_ID, Names.BOW));
    shield = (ItemSL) reg.registerItem(new ItemSL(1, SilentGems.MOD_ID, Names.SHIELD));
    pickaxe = (ItemGemPickaxe) reg.registerItem(new ItemGemPickaxe(), Names.PICKAXE);
    shovel = (ItemGemShovel) reg.registerItem(new ItemGemShovel(), Names.SHOVEL);
    axe = (ItemGemAxe) reg.registerItem(new ItemGemAxe(), Names.AXE);
    hoe = (ItemGemHoe) reg.registerItem(new ItemGemHoe(), Names.HOE);
    sickle = (ItemGemSickle) reg.registerItem(new ItemGemSickle(), Names.SICKLE);
    fishingRod = (ItemSL) reg.registerItem(new ItemSL(1, SilentGems.MOD_ID, Names.FISHING_ROD));

    // ToolRenderHelper
    if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
      toolRenderHelper = (ToolRenderHelperBase) reg.registerItem(new ToolRenderHelper());
    } else {
      toolRenderHelper = (ToolRenderHelperBase) reg.registerItem(new ToolRenderHelperBase());
    }
    toolRenderHelper.init();

    initLoot();
  }

  public static void initLoot() {

    // TODO
  }
}
