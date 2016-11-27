package net.silentchaos512.gems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.entity.ModEntities;
import net.silentchaos512.gems.guide.GuideSilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.part.ModParts;
import net.silentchaos512.gems.recipe.ModRecipes;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.gems.world.GemsWorldGenerator;
import net.silentchaos512.lib.SilentLib;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.LogHelper;

//@formatter:off
@Mod(modid = SilentGems.MOD_ID,
    name = SilentGems.MOD_NAME,
    version = SilentGems.VERSION,
    dependencies = SilentGems.DEPENDENCIES)
//@formatter:on
public class SilentGems {

  // public static final String MOD_ID_PHONY = "GemTest";
  private static final boolean DEV_ENV = true;
  public static final String MOD_ID = "SilentGems";
  public static final String MOD_ID_LOWER = "silentgems";
  public static final String MOD_NAME = "Silent's Gems";
  public static final String VERSION = "@VERSION@";
  public static final String DEPENDENCIES = "required-after:Forge@[12.18.1.2070,);required-after:SilentLib"
      + (DEV_ENV ? ";" : "@[1.1.0,);")
      + "after:guideapi;after:EnderIO;after:EnderZoo";
  public static final String RESOURCE_PREFIX = MOD_ID.toLowerCase() + ":";

  public static Random random = new Random();
  public static LogHelper logHelper = new LogHelper(MOD_NAME);
  public static LocalizationHelper localizationHelper;

  public static SRegistry registry = new SRegistry(MOD_ID) {

    @Override
    public Block registerBlock(Block block, String key, ItemBlock itemBlock) {

      block.setCreativeTab(GemsCreativeTabs.blocks);
      return super.registerBlock(block, key, itemBlock);
    }

    @Override
    public Item registerItem(Item item, String key) {

      if (item instanceof ITool && !(item instanceof ItemGemShield)) {
        item.setCreativeTab(GemsCreativeTabs.tools);
        ModItems.tools.add(item);
      } else if (item instanceof IArmor) {
        item.setCreativeTab(GemsCreativeTabs.tools);
      } else {
        item.setCreativeTab(GemsCreativeTabs.materials);
      }
      return super.registerItem(item, key);
    }
  };

  @Instance(MOD_ID)
  public static SilentGems instance;

  @SidedProxy(clientSide = "net.silentchaos512.gems.proxy.GemsClientProxy", serverSide = "net.silentchaos512.gems.proxy.GemsCommonProxy")
  public static net.silentchaos512.gems.proxy.GemsCommonProxy proxy;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    localizationHelper = new LocalizationHelper(MOD_ID).setReplaceAmpersand(true);
    SilentLib.instance.registerLocalizationHelperForMod(MOD_ID, localizationHelper);

    ToolHelper.init();

    GemsConfig.init(event.getSuggestedConfigurationFile());

    ModEnchantments.init();
    ModBlocks.init();
    ModItems.init();
    ModRecipes.init();
    ModParts.init();

    GemsConfig.loadModuleConfigs();

    // TODO: Achievements

    // World generation
    GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);

    // Headcrumbs
    FMLInterModComms.sendMessage("headcrumbs", "add-username", Names.SILENT_CHAOS_512);

    // Register Guide API book?
    if (Loader.isModLoaded("guideapi")) {
      GuideSilentGems.buildGuide(localizationHelper);
    }

    proxy.preInit(registry);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {

    ModEntities.init();

    GemsConfig.save();

    proxy.init(registry);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

    proxy.postInit(registry);
  }

//  @EventHandler
//  public void serverAboutToStart(FMLServerAboutToStartEvent event) {
//
//    ModItems.enchantmentToken.addModRecipes();
//  }
}
