package silent.gems;

import net.minecraftforge.common.MinecraftForge;
import silent.gems.block.ModBlocks;
import silent.gems.configuration.Config;
import silent.gems.core.proxy.CommonProxy;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.enchantment.ModEnchantments;
import silent.gems.entity.ModEntities;
import silent.gems.item.ModItems;
import silent.gems.lib.Reference;
import silent.gems.lib.buff.ChaosBuff;
import silent.gems.world.GemsWorldGenerator;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION_NUMBER)
public class SilentGems {

    @Instance(Reference.MOD_ID)
    public static SilentGems instance;

    @SidedProxy(clientSide = "silent.gems.core.proxy.ClientProxy", serverSide = "silent.gems.core.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        LogHelper.init();

        Config.init(event.getSuggestedConfigurationFile());

        ModBlocks.init();
        ModItems.init();
        ModEnchantments.init();
        ChaosBuff.init();
        
        ModItems.initItemRecipes();

        // ModBlocks.initBlockRecipes();
        // ModItems.initItemRecipes();
        SRegistry.addRecipesAndOreDictEntries();
        ModItems.addRandomChestGenLoot();

        // LocalizationHelper.init();

        Config.save();
        
        MinecraftForge.EVENT_BUS.register(new GemsEventHandler());
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {

        ModEntities.init();

        proxy.registerTickHandlers();
        proxy.registerTileEntities();
        proxy.registerRenderers();
        proxy.registerKeyHandlers();
        
        GameRegistry.registerWorldGenerator(new GemsWorldGenerator());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        // TODO
    }
}
