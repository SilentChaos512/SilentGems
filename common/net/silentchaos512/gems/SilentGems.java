package net.silentchaos512.gems;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.handler.GemsEventHandler;
import net.silentchaos512.gems.core.proxy.CommonProxy;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.enchantment.ModEnchantments;
import net.silentchaos512.gems.gui.GuiHandlerSilentGems;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.buff.ChaosBuff;
import net.silentchaos512.gems.network.MessageChaosGemToggle;
import net.silentchaos512.gems.world.GemsWorldGenerator;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = SilentGems.MOD_ID, name = SilentGems.MOD_NAME, version = SilentGems.VERSION_NUMBER)
public class SilentGems {

  public final static String MOD_ID = "SilentGems";
  public final static String MOD_NAME = "Silent's Gems";
  public final static String VERSION_NUMBER = "@VERSION@";
  public final static String CHANNEL_NAME = MOD_ID;

  public Random random = new Random();

  @Instance(SilentGems.MOD_ID)
  public static SilentGems instance;

  @SidedProxy(clientSide = "net.silentchaos512.gems.core.proxy.ClientProxy", serverSide = "net.silentchaos512.gems.core.proxy.CommonProxy")
  public static CommonProxy proxy;

  public static SimpleNetworkWrapper network;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    Config.init(event.getSuggestedConfigurationFile());

    ModBlocks.init();
    ModItems.init();
    ModEnchantments.init();

    Config.save();
    
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandlerSilentGems());

    network = NetworkRegistry.INSTANCE.newSimpleChannel(SilentGems.MOD_ID);
    int discriminator = -1;
    network.registerMessage(MessageChaosGemToggle.Handler.class, MessageChaosGemToggle.class,
        ++discriminator, Side.SERVER);
  }

  @EventHandler
  public void load(FMLInitializationEvent event) {

    // Proxies
    proxy.registerTileEntities();
    proxy.registerRenderers();
    proxy.registerKeyHandlers();
    
    // Event handler
    FMLCommonHandler.instance().bus().register(new GemsEventHandler());
    
    // Recipes and ore dictionary.
    SRegistry.addRecipesAndOreDictEntries();
    ModItems.initItemRecipes();
    ChaosBuff.initRecipes();
    
    ModItems.addRandomChestGenLoot();

    // World generators
    GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

    // Is this the right place for this?
    SRegistry.addThaumcraftStuff();
  }

  // @EventHandler
  // public void serverLoad(FMLServerStartingEvent event) {
  //
  // NetworkRegistry.INSTANCE.newChannel(SilentGems.CHANNEL_NAME, new PacketHandler());
  // }

  public static CreativeTabs tabSilentGems = new CreativeTabs("tabSilentGems") {

    @Override
    public Item getTabIconItem() {

      return SRegistry.getItem(Names.GEM_ITEM);
    }
  };
}
