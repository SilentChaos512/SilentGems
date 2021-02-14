package net.silentchaos512.gemschaos;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.gemschaos.chaos.ChaosSourceCapability;
import net.silentchaos512.gemschaos.config.ChaosConfig;
import net.silentchaos512.gemschaos.setup.ChaosRegistration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mod(ChaosMod.MOD_ID)
@Mod.EventBusSubscriber(modid = ChaosMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChaosMod {
    public static final String MOD_ID = "silentgems_chaos";

    public static final Random RANDOM = new Random();
    public static final Logger LOGGER = LogManager.getLogger("Silent's Gems: Chaos");
    public static final TextUtil TEXT = new TextUtil(MOD_ID);

    public ChaosMod() {
        ChaosConfig.init();
        ChaosRegistration.register();
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        ChaosSourceCapability.register();
    }

    public static String getVersion() {
        Optional<? extends ModContainer> o = ModList.get().getModContainerById(MOD_ID);
        if (o.isPresent()) {
            return o.get().getModInfo().getVersion().toString();
        }
        return "0.0.0";
    }

    public static boolean isDevBuild() {
        return "NONE".equals(getVersion());
    }

    public static ResourceLocation getId(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    @Nullable
    public static ResourceLocation getIdWithDefaultNamespace(String name) {
        if (name.contains(":"))
            return ResourceLocation.tryCreate(name);
        return ResourceLocation.tryCreate(MOD_ID + ":" + name);
    }

    public static String shortenId(@Nullable ResourceLocation id) {
        if (id == null)
            return "null";
        if (MOD_ID.equals(id.getNamespace()))
            return id.getPath();
        return id.toString();
    }

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Gems.RUBY.getItem());
        }
    };
}
