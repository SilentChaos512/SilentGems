package net.silentchaos512.gems;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.achievement.GemsAchievement;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.client.render.tool.ToolRenderHelper;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.handler.GemsEventHandler;
import net.silentchaos512.gems.core.handler.GemsForgeEventHandler;
import net.silentchaos512.gems.core.proxy.CommonProxy;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.entity.ModEntities;
import net.silentchaos512.gems.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.buff.ChaosBuff;
import net.silentchaos512.gems.network.MessageChaosGemToggle;
import net.silentchaos512.gems.network.MessageSetFlight;
import net.silentchaos512.gems.world.GemsWorldGenerator;

@Mod(modid = SilentGems.MOD_ID, name = SilentGems.MOD_NAME, version = SilentGems.VERSION_NUMBER)
public class SilentGems {

  public final static String MOD_ID = "SilentGems";
  public final static String MOD_NAME = "Silent's Gems";
  public final static String VERSION_NUMBER = "@VERSION@";
  public final static String CHANNEL_NAME = MOD_ID;

  public Random random = new Random();
  public static Logger logger = LogManager.getLogger(MOD_NAME);

  @Instance(SilentGems.MOD_ID)
  public static SilentGems instance;

  @SidedProxy(clientSide = "net.silentchaos512.gems.core.proxy.ClientProxy", serverSide = "net.silentchaos512.gems.core.proxy.CommonProxy")
  public static CommonProxy proxy;

  public static SimpleNetworkWrapper network;

  public static boolean foundMetallurgy = false;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    Config.init(event.getSuggestedConfigurationFile());

    ModBlocks.init();
    ModItems.init();
    ModEnchantments.init();
    ModEntities.init();

    Config.save();

    proxy.preInit();

    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerSilentGems());

    network = NetworkRegistry.INSTANCE.newSimpleChannel(SilentGems.MOD_ID);
    int discriminator = -1;
    network.registerMessage(MessageChaosGemToggle.Handler.class, MessageChaosGemToggle.class,
        ++discriminator, Side.SERVER);
    network.registerMessage(MessageSetFlight.Handler.class, MessageSetFlight.class, ++discriminator,
        Side.CLIENT);
    LogHelper.info("Pre init done.");
  }

  @EventHandler
  public void load(FMLInitializationEvent event) {

    // Event handler
    FMLCommonHandler.instance().bus().register(new GemsEventHandler());
    MinecraftForge.EVENT_BUS.register(new GemsForgeEventHandler());

    // Recipes and ore dictionary.
    SRegistry.addRecipesAndOreDictEntries();
    ModItems.initItemRecipes();
    ChaosBuff.initRecipes();
    ModItems.addRandomChestGenLoot();
    proxy.init();

    // Achievements
    AchievementPage.registerAchievementPage(GemsAchievement.createPage());

    // World generators
    GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);

    // Look for other mods...
    foundMetallurgy = Loader.isModLoaded("Metallurgy");
    if (foundMetallurgy) {
      ModItems.toolUpgrade.addMetallurgyTipRecipes();
    }

    LogHelper.info("Init done.");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

    // Is this the right place for this?
    SRegistry.addThaumcraftStuff();
    proxy.postInit();
    LogHelper.info("Post init done.");

    // Calculate possible tool combinations
    if (event.getSide() == Side.CLIENT) {
      int toolsPerClass = ToolRenderHelper.instance.getPossibleToolCombinations();
      LogHelper.info("Tools per class: " + toolsPerClass);
      LogHelper.info("Total possible tools: " + ToolHelper.TOOL_CLASSES.length * toolsPerClass);
      LogHelper.info("Note I can't guarantee that these numbers are correct.");
    }
  }

  public static CreativeTabs tabSilentGems = new CreativeTabs("tabSilentGems") {

    @Override
    public Item getTabIconItem() {

      return SRegistry.getItem(Names.GEM_ITEM);
    }
  };
}
