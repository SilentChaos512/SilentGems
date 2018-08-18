package net.silentchaos512.gems;

import mcp.MethodsReturnNonnullByDefault;
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
import net.silentchaos512.gems.api.stats.CommonItemStats;
import net.silentchaos512.gems.compat.VeinMinerCompat;
import net.silentchaos512.gems.compat.gear.SGearStatHandler;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.entity.ModEntities;
import net.silentchaos512.gems.init.*;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.lib.GemsCreativeTabs;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.part.ModParts;
import net.silentchaos512.gems.lib.soul.SoulSkill;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.gems.world.GemsGeodeWorldGenerator;
import net.silentchaos512.gems.world.GemsWorldGenerator;
import net.silentchaos512.lib.base.IModBase;
import net.silentchaos512.lib.registry.SRegistry;
import net.silentchaos512.lib.util.I18nHelper;
import net.silentchaos512.lib.util.LogHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Consumer;

@Mod(modid = SilentGems.MODID,
        name = SilentGems.MOD_NAME,
        version = SilentGems.VERSION,
        dependencies = SilentGems.DEPENDENCIES,
        acceptedMinecraftVersions = SilentGems.ACCEPTED_MC_VERSIONS,
        guiFactory = "net.silentchaos512.gems.client.gui.config.GuiFactorySilentGems")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"unused", "WeakerAccess"})
public class SilentGems implements IModBase {
    public static final String MODID = "silentgems";
    public static final String MODID_NBT = "SilentGems"; // The original ID, used in NBT.
    public static final String MOD_NAME = "Silent's Gems";
    public static final String VERSION = "2.8.1";
    public static final String VERSION_SILENTLIB = "3.0.0";
    public static final int BUILD_NUM = 0;
    public static final String DEPENDENCIES = "required-after:silentlib@[" + VERSION_SILENTLIB + ",);"
            + "after:baubles;after:enderio;after:enderzoo;after:veinminer;after:silentgear";
    public static final String ACCEPTED_MC_VERSIONS = "[1.12,1.12.2]";
    public static final String RESOURCE_PREFIX = MODID + ":";

    static {
        if (Loader.isModLoaded("silentgear")) {
            // Load added stat(s) before Silent Gear loads material JSONs
            SGearStatHandler.init();
        }
    }

    public static final Random random = new Random();
    public static final LogHelper logHelper = new LogHelper(MOD_NAME, BUILD_NUM);
    public static final I18nHelper i18n = new I18nHelper(MODID, logHelper, true);

    public static final SRegistry registry = new SRegistry() {
        @Override
        public <T extends Block> T registerBlock(T block, String key, ItemBlock itemBlock) {
            super.registerBlock(block, key, itemBlock);
            block.setCreativeTab(GemsCreativeTabs.BLOCKS);
            return block;
        }

        @Override
        public <T extends Item> T registerItem(T item, String key) {
            super.registerItem(item, key);
            if (item instanceof ITool) {
                // Works with repair packets.
                GemsConfig.NODE_REPAIR_WHITELIST.add(item);

                // Not adding shields to tools tab and shields don't use custom model.
                if (!(item instanceof ItemGemShield)) {
                    item.setCreativeTab(GemsCreativeTabs.TOOLS);
                    ModItems.tools.add(item);
                }
            } else if (item instanceof IArmor) {
                GemsConfig.NODE_REPAIR_WHITELIST.add(item);
                item.setCreativeTab(GemsCreativeTabs.TOOLS);
            } else {
                item.setCreativeTab(GemsCreativeTabs.MATERIALS);
            }
            return item;
        }
    };

    @Instance(MODID)
    public static SilentGems instance;

    @SidedProxy(clientSide = "net.silentchaos512.gems.proxy.GemsClientProxy", serverSide = "net.silentchaos512.gems.proxy.GemsCommonProxy")
    public static net.silentchaos512.gems.proxy.GemsCommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        registry.setMod(this);
        registry.getRecipeMaker().setJsonHellMode(isDevBuild());

        CommonItemStats.init();
        ToolHelper.init();

        GemsConfig.INSTANCE.init(event.getSuggestedConfigurationFile());

        registry.addRegistrationHandler((Consumer<SRegistry>) ModEnchantments::registerAll, Enchantment.class);
        registry.addRegistrationHandler((Consumer<SRegistry>) ModBlocks::registerAll, Block.class);
        registry.addRegistrationHandler((Consumer<SRegistry>) ModItems::registerAll, Item.class);
        registry.addRegistrationHandler((Consumer<SRegistry>) ModPotions::registerAll, Potion.class);
        registry.addRegistrationHandler((Consumer<SRegistry>) ModRecipes::registerAll, IRecipe.class);
        ModParts.init();
        SoulSkill.init();

        GemsConfig.INSTANCE.loadModuleConfigs();

        // World generation
        GameRegistry.registerWorldGenerator(new GemsWorldGenerator(), 0);
        GameRegistry.registerWorldGenerator(new GemsGeodeWorldGenerator(), -10);

        // Headcrumbs
        FMLInterModComms.sendMessage("headcrumbs", "add-username", Names.SILENT_CHAOS_512);

        VeinMinerCompat.init();

        proxy.preInit(registry, event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModEntities.init(registry);
        GemsConfig.INSTANCE.save();
        proxy.init(registry, event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(registry, event);
    }

    @Override
    public String getModId() {
        return MODID;
    }

    @Override
    public String getModName() {
        return MOD_NAME;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public int getBuildNum() {
        return BUILD_NUM;
    }

    @Override
    public LogHelper getLog() {
        return logHelper;
    }
}




