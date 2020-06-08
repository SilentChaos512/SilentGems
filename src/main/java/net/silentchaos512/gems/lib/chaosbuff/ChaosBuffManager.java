package net.silentchaos512.gems.lib.chaosbuff;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.network.NetworkEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.network.SyncChaosBuffsPacket;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public final class ChaosBuffManager implements IResourceManagerReloadListener {
    public static final ChaosBuffManager INSTANCE = new ChaosBuffManager();

    public static final Marker MARKER = MarkerManager.getMarker("ChaosBuffManager");

    private static final String DATA_PATH = "silentgems_chaos_buffs";
    private static final Map<ResourceLocation, IChaosBuff> MAP = Collections.synchronizedMap(new LinkedHashMap<>());

    private ChaosBuffManager() { }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        Collection<ResourceLocation> resources = resourceManager.getAllResourceLocations(
                DATA_PATH, s -> s.endsWith(".json"));
        if (resources.isEmpty()) return;

        synchronized (MAP) {
            MAP.clear();
            SilentGems.LOGGER.info(MARKER, "Reloading chaos buff files");

            for (ResourceLocation id : resources) {
                try (IResource iresource = resourceManager.getResource(id)) {
                    String path = id.getPath().substring(DATA_PATH.length() + 1, id.getPath().length() - ".json".length());
                    ResourceLocation name = new ResourceLocation(id.getNamespace(), path);
                    if (SilentGems.LOGGER.isTraceEnabled()) {
                        SilentGems.LOGGER.trace(MARKER, "Found likely chaos buff file: {}, trying to read as {}", id, name);
                    }

                    JsonObject json = JSONUtils.fromJson(gson, IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
                    if (json == null) {
                        SilentGems.LOGGER.error(MARKER, "could not load chaos buff {} as it's null or empty", name);
                    } else if (!CraftingHelper.processConditions(json, "conditions")) {
                        SilentGems.LOGGER.info("Skipping loading chaos buff {} as it's conditions were not met", name);
                    } else {
                        addBuff(ChaosBuffSerializers.deserialize(name, json));
                    }
                } catch (IllegalArgumentException | JsonParseException ex) {
                    SilentGems.LOGGER.error(MARKER, "Parsing error loading chaos buff {}", id, ex);
                } catch (IOException ex) {
                    SilentGems.LOGGER.error(MARKER, "Could not read chaos buff {}", id, ex);
                }
            }

            SilentGems.LOGGER.info(MARKER, "Finished! Registered {} chaos buffs", MAP.size());
        }
    }

    private static void addBuff(IChaosBuff buff) {
        if (MAP.containsKey(buff.getId())) {
            throw new IllegalArgumentException("Duplicate chaos buff " + buff.getId());
        }
        MAP.put(buff.getId(), buff);
    }

    @Nullable
    public static IChaosBuff get(ResourceLocation id) {
        synchronized (MAP) {
            return MAP.get(id);
        }
    }

    @Nullable
    public static IChaosBuff get(String id) {
        return get(new ResourceLocation(id));
    }

    public static Collection<IChaosBuff> getValues() {
        synchronized (MAP) {
            return MAP.values();
        }
    }

    public static void handlePacket(SyncChaosBuffsPacket packet, Supplier<NetworkEvent.Context> context) {
        synchronized (MAP) {
            MAP.clear();
            packet.getBuffs().forEach(buff -> MAP.put(buff.getId(), buff));
            SilentGems.LOGGER.info("Received {} chaos buffs from server", MAP.size());
            context.get().setPacketHandled(true);
        }
    }

    @Nullable
    public static ITextComponent getGreetingErrorMessage(PlayerEntity player) {
        synchronized (MAP) {
            if (MAP.isEmpty()) {
                SilentGems.LOGGER.error("Something went wrong with chaos buff loading! This may be caused by another broken mod, even those not related to Silent's Gems.");
                return new StringTextComponent("[Silent's Gems] No chaos buffs are loaded! This means chaos gems and runes will not work. This can be caused by a broken mod.");
            }
            return null;
        }
    }
}
