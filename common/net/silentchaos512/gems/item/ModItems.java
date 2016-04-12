package net.silentchaos512.gems.item;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
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

  public static ItemGem gem = new ItemGem();
  public static ItemGemShard gemShard = new ItemGemShard();
  public static ItemCrafting craftingMaterial = new ItemCrafting();
  public static ItemTipUpgrade tipUpgrade = new ItemTipUpgrade();
  public static ItemEnchantmentToken enchantmentToken = new ItemEnchantmentToken();
  public static ItemFluffyPuffSeeds fluffyPuffSeeds = new ItemFluffyPuffSeeds();
  public static ItemFluffyPuff fluffyPuff = new ItemFluffyPuff();
  public static ItemDyeSG dye = new ItemDyeSG();
  public static ItemFoodSG food = new ItemFoodSG();
  public static ItemTorchBandolier torchBandolier = new ItemTorchBandolier();
  public static ItemChaosOrb chaosOrb = new ItemChaosOrb();
  public static ItemNodeMover nodeMover = new ItemNodeMover();
  public static ItemTeleporterLinker teleporterLinker = new ItemTeleporterLinker();

  public static ItemGemSword sword = new ItemGemSword();
  public static ItemGemKatana katana = new ItemGemKatana();
  public static ItemGemScepter scepter = new ItemGemScepter();
  public static ItemSL bow = new ItemSL(1, SilentGems.MOD_ID, Names.BOW); // temp
  public static ItemSL shield = new ItemSL(1, SilentGems.MOD_ID, Names.SHIELD); // temp
  public static ItemGemPickaxe pickaxe = new ItemGemPickaxe();
  public static ItemGemShovel shovel = new ItemGemShovel();
  public static ItemGemAxe axe = new ItemGemAxe();
  public static ItemGemHoe hoe = new ItemGemHoe();
  public static ItemGemSickle sickle = new ItemGemSickle();
  public static ItemSL fishingRod = new ItemSL(1, SilentGems.MOD_ID, Names.FISHING_ROD); // temp

  public static ToolRenderHelperBase toolRenderHelper;

  public static final List<Item> tools = Lists.newArrayList();

  public static void init() {

    SRegistry reg = SilentGems.instance.registry;

    reg.registerItem(gem);
    reg.registerItem(gemShard);
    reg.registerItem(craftingMaterial);
    reg.registerItem(tipUpgrade);
    reg.registerItem(enchantmentToken);
    reg.registerItem(fluffyPuffSeeds, Names.FLUFFY_PUFF_SEEDS);
    reg.registerItem(fluffyPuff);
    reg.registerItem(dye);
    reg.registerItem(food, Names.FOOD);
    reg.registerItem(torchBandolier);
    reg.registerItem(chaosOrb);
    reg.registerItem(nodeMover);
    reg.registerItem(teleporterLinker);

    // Tools
    reg.registerItem(sword, Names.SWORD);
    reg.registerItem(katana, Names.KATANA);
    reg.registerItem(scepter, Names.SCEPTER);
    reg.registerItem(bow, Names.BOW);
    reg.registerItem(shield, Names.SHIELD);
    reg.registerItem(pickaxe, Names.PICKAXE);
    reg.registerItem(shovel, Names.SHOVEL);
    reg.registerItem(axe, Names.AXE);
    reg.registerItem(hoe, Names.HOE);
    reg.registerItem(sickle, Names.SICKLE);
    reg.registerItem(fishingRod, Names.FISHING_ROD);

    // ToolRenderHelper
    if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
      toolRenderHelper = (ToolRenderHelperBase) reg.registerItem(new ToolRenderHelper());
    } else {
      toolRenderHelper = (ToolRenderHelperBase) reg.registerItem(new ToolRenderHelperBase());
    }
    toolRenderHelper.init();

    initExtraRecipes();
    initLoot();
  }

  public static void initExtraRecipes() {

    GameRegistry.addShapelessRecipe(new ItemStack(Items.flint), Blocks.gravel, Blocks.gravel);
  }

  public static void initLoot() {

    // TODO
  }
}
