package net.silentchaos512.gems;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Random;

@Mod(SilentGems.MOD_ID)
public final class SilentGems {
    public static final String MOD_ID = "silentgems";
    public static final String MOD_NAME = "Silent's Gems";
    public static final String VERSION = "3.1.0";
    public static final boolean RUN_GENERATORS = false;
    public static final String RESOURCE_PREFIX = MOD_ID + ":";

    public static final Random random = new Random();
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static SilentGems INSTANCE;
    public static IProxy PROXY;

    public SilentGems() {
        INSTANCE = this;
        PROXY = DistExecutor.runForDist(() -> () -> new SideProxy.Client(), () -> () -> new SideProxy.Server());
    }

    public static String getVersion() {
        return getVersion(false);
    }

    public static String getVersion(boolean correctInDev) {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            String str = o.get().getModInfo().getVersion().toString();
            if (correctInDev && "NONE".equals(str))
                return VERSION;
            return str;
        }
        return "0.0.0";
    }

    public static boolean isDevBuild() {
        String version = getVersion(false);
        return "NONE".equals(version);
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
