package net.silentchaos512.gems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.entity.ModEntities;
import net.silentchaos512.gems.item.ModItems;
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
    dependencies = "required-after:SilentLib")
//@formatter:on
public class SilentGems {

  // public static final String MOD_ID_PHONY = "GemTest";
  public static final String MOD_ID = "SilentGems";
  public static final String MOD_NAME = "Silent's Gems";
  public static final String VERSION = "@VERSION@";

  public Random random = new Random();
  public LogHelper logHelper = new LogHelper(MOD_NAME);
  public LocalizationHelper localizationHelper;

  public SRegistry registry = new SRegistry(MOD_ID) {

    @Override
    public Block registerBlock(Block block, String key, Class<? extends ItemBlock> itemClass) {

      block.setCreativeTab(creativeTab);
      return super.registerBlock(block, key, itemClass);
    }

    @Override
    public Item registerItem(Item item, String key) {

      item.setCreativeTab(creativeTab);
      if (item instanceof ITool)
        ModItems.tools.add(item);
      return super.registerItem(item, key);
    }
  };

  public CreativeTabs creativeTab = new CreativeTabs("tabSilentGems") {

    @Override
    public Item getTabIconItem() {

      return ModItems.gem;
    }
  };

  @Instance(MOD_ID)
  public static SilentGems instance;

  @SidedProxy(clientSide = "net.silentchaos512.gems.proxy.ClientProxy", serverSide = "net.silentchaos512.gems.proxy.CommonProxy")
  public static net.silentchaos512.gems.proxy.CommonProxy proxy;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    localizationHelper = new LocalizationHelper(MOD_ID).setReplaceAmpersand(true);
    SilentLib.instance.registerLocalizationHelperForMod(MOD_ID, localizationHelper);

    ToolHelper.init();

    Config.init(event.getSuggestedConfigurationFile());
    ModBlocks.init();
    ModItems.init();
    ModRecipes.init();
    ModParts.init();

    // TODO: Achievements

    // World generation
    GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);

    proxy.preInit(registry);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {

    ModEntities.init();

    Config.save();

    proxy.init(registry);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

    proxy.postInit(registry);
  }
}
