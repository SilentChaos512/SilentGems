package net.silentchaos512.gems;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.compat.ScalingHealthCompat;
import net.silentchaos512.gems.compat.VeinMinerCompat;
import net.silentchaos512.gems.compat.tconstruct.TConstructGemsCompat;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.entity.ModEntities;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.init.ModPotions;
import net.silentchaos512.gems.init.ModRecipes;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.part.ModParts;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.gems.world.GemsGeodeWorldGenerator;
import net.silentchaos512.gems.world.GemsWorldGenerator;
import net.silentchaos512.lib.SilentLib;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.LogHelper;

//@formatter:off
@Mod(modid = SilentGems.MODID,
    name = SilentGems.MOD_NAME,
    version = SilentGems.VERSION,
    dependencies = SilentGems.DEPENDENCIES,
    acceptedMinecraftVersions = SilentGems.ACCEPTED_MC_VERSIONS,
    guiFactory = "net.silentchaos512.gems.client.gui.config.GuiFactorySilentGems")
//@formatter:on
public class SilentGems {

  public static final String MODID = "silentgems";
  public static final String MODID_NBT = "SilentGems"; // The original ID, used in NBT.
  public static final String MOD_NAME = "Silent's Gems";
  public static final String VERSION = "@VERSION@";
  public static final String VERSION_SILENTLIB = "SL_VERSION";
  public static final int BUILD_NUM = 0;
  public static final String DEPENDENCIES = "required-after:silentlib@[" + VERSION_SILENTLIB + ",);"
      + "after:baubles;after:enderio;after:enderzoo;after:tconstruct;after:veinminer";
  public static final String ACCEPTED_MC_VERSIONS = "[1.12,1.12.2]";
  public static final String RESOURCE_PREFIX = MODID + ":";

  public static Random random = new Random();
  public static LogHelper logHelper = new LogHelper(MOD_NAME, BUILD_NUM);
  public static LocalizationHelper localizationHelper;

  public static SRegistry registry = new SRegistry(MODID, logHelper) {

    @Override
    public Block registerBlock(Block block, String key, ItemBlock itemBlock) {

      super.registerBlock(block, key, itemBlock);
      block.setCreativeTab(GemsCreativeTabs.blocks);
      return block;
    }

    @Override
    public Item registerItem(Item item, String key) {

      super.registerItem(item, key);
      if (item instanceof ITool) {
        // Works with repair packets.
        GemsConfig.NODE_REPAIR_WHITELIST.add(item);

        // Not adding shields to tools tab and shields don't use custom model.
        if (!(item instanceof ItemGemShield)) {
          item.setCreativeTab(GemsCreativeTabs.tools);
          ModItems.tools.add(item);
        }
      } else if (item instanceof IArmor) {
        GemsConfig.NODE_REPAIR_WHITELIST.add(item);
        item.setCreativeTab(GemsCreativeTabs.tools);
      } else {
        item.setCreativeTab(GemsCreativeTabs.materials);
      }
      return item;
    }
  };

  @Instance(MODID)
  public static SilentGems instance;

  @SidedProxy(clientSide = "net.silentchaos512.gems.proxy.GemsClientProxy", serverSide = "net.silentchaos512.gems.proxy.GemsCommonProxy")
  public static net.silentchaos512.gems.proxy.GemsCommonProxy proxy;

  public SilentGems() {

  }

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    localizationHelper = new LocalizationHelper(MODID).setReplaceAmpersand(true);
    SilentLib.instance.registerLocalizationHelperForMod(MODID, localizationHelper);

    ToolHelper.init();

    GemsConfig.INSTANCE.init(event.getSuggestedConfigurationFile());

    registry.addRegistrationHandler(new ModEnchantments(), Enchantment.class);
    registry.addRegistrationHandler(new ModBlocks(), Block.class);
    registry.addRegistrationHandler(new ModItems(), Item.class);
    registry.addRegistrationHandler(new ModPotions(), Potion.class);
    registry.addRegistrationHandler(new ModRecipes(), IRecipe.class);
    ModParts.init();
    SoulSkill.init();

    GemsConfig.INSTANCE.loadModuleConfigs();

    // TODO: Achievements

    // World generation
    GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);
    GameRegistry.registerWorldGenerator(new GemsGeodeWorldGenerator(), -10);

    // Headcrumbs
    FMLInterModComms.sendMessage("headcrumbs", "add-username", Names.SILENT_CHAOS_512);

    // Load TCon compatibility stuff?
    if (Loader.isModLoaded("tconstruct")) {
      TConstructGemsCompat.preInit();
    }

    VeinMinerCompat.init();

    proxy.preInit(registry);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {

    ModEntities.init(registry);

    GemsConfig.INSTANCE.save();

    // Scaling Health compat
    if (Loader.isModLoaded("scalinghealth")) {
      ScalingHealthCompat.init();
    }

    proxy.init(registry);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {

    proxy.postInit(registry);
  }

  @SuppressWarnings("all")
  public boolean isDevBuild() {

    return BUILD_NUM == 0;
  }
}
