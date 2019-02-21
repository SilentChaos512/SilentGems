package net.silentchaos512.gems;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(SilentGems.MOD_ID)
public final class SilentGems {
    public static final String MOD_ID = "silentgems";
    public static final String MODID_NBT = "SilentGems"; // The original ID, used in NBT.
    public static final String MOD_NAME = "Silent's Gems";
    public static final boolean RUN_GENERATORS = false;
    public static final String RESOURCE_PREFIX = MOD_ID + ":";

//    static {
//        if (ModList.get().isLoaded("silentgear")) {
//            // Load added stat(s) before Silent Gear loads material JSONs
//            MinecraftForge.EVENT_BUS.register(new SGearStatHandler());
//        }
//    }

    public static final Random random = new Random();
//    public static final LogHelper logHelper = new LogHelper(MOD_NAME, BUILD_NUM);
//    public static final I18nHelper i18n = new I18nHelper(MOD_ID, logHelper, false);

//    public static final SRegistry registry = new SRegistry();

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static SilentGems INSTANCE;
    public static SideProxy PROXY;

    public SilentGems() {
        INSTANCE = this;
        PROXY = DistExecutor.runForDist(() -> () -> new SideProxy.Client(), () -> () -> new SideProxy.Server());
    }

    public static void safeSetRegistryName(IForgeRegistryEntry<?> obj, ResourceLocation name) {
        if (obj.getRegistryName() == null) {
            obj.setRegistryName(name);
        } else if (!obj.getRegistryName().equals(name)) {
            LOGGER.warn("Already set registry name for '{}', and new name '{}' is different!", name, obj);
        } else {
            LOGGER.warn("Already set registry name for '{}' ({})", name, obj);
        }
    }
}
