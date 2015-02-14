package silent.gems;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
import silent.gems.block.ModBlocks;
import silent.gems.configuration.Config;
import silent.gems.core.handler.GemsEventHandler;
import silent.gems.core.proxy.CommonProxy;
import silent.gems.core.registry.SRegistry;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.item.ModItems;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.lib.buff.ChaosBuff;
import silent.gems.network.MessageChaosGemToggle;
import silent.gems.network.MessagePlayerUpdate;
import silent.gems.world.GemsWorldGenerator;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
public class SilentGems {

  public Random random = new Random();

  @Instance(Reference.MOD_ID)
  public static SilentGems instance;

  @SidedProxy(clientSide = "silent.gems.core.proxy.ClientProxy", serverSide = "silent.gems.core.proxy.CommonProxy")
  public static CommonProxy proxy;

  public static SimpleNetworkWrapper network;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    // LogHelper.init();

    Config.init(event.getSuggestedConfigurationFile());

    ModEnchantments.init();
    ModBlocks.init();
    ModItems.init();
    ChaosBuff.init();

    Config.save();
    
    proxy.preInit();

    MinecraftForge.EVENT_BUS.register(new GemsEventHandler());
    FMLCommonHandler.instance().bus().register(new GemsEventHandler());

    network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
    int discriminator = -1;
    network.registerMessage(MessagePlayerUpdate.Handler.class, MessagePlayerUpdate.class,
        ++discriminator, Side.SERVER);
    network.registerMessage(MessageChaosGemToggle.Handler.class, MessageChaosGemToggle.class,
        ++discriminator, Side.SERVER);
  }

  @EventHandler
  public void load(FMLInitializationEvent event) {

    SRegistry.addRecipesAndOreDictEntries();
    ModItems.addRandomChestGenLoot();
    ModItems.initItemRecipes();

    proxy.init();

    GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    
    proxy.postInit();

    // Is this the right place for this?
    SRegistry.addThaumcraftStuff();
  }

  // @EventHandler
  // public void serverLoad(FMLServerStartingEvent event) {
  //
  // NetworkRegistry.INSTANCE.newChannel(Reference.CHANNEL_NAME, new PacketHandler());
  // }

  public static CreativeTabs tabSilentGems = new CreativeTabs("tabSilentGems") {

    @Override
    public Item getTabIconItem() {

      return SRegistry.getItem(Names.GEM_ITEM);
    }
  };
}
