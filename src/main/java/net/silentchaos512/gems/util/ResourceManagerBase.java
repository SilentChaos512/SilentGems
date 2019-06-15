package net.silentchaos512.gems.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public abstract class ResourceManagerBase<T> implements IResourceManagerReloadListener {
    private final String dataPath;
    private final Marker logMarker;
    private final Logger logger;

    protected final Map<ResourceLocation, T> resources = new LinkedHashMap<>();

    protected ResourceManagerBase(String dataPath, String logMarker, Logger logger) {
        this.dataPath = dataPath;
        this.logMarker = MarkerManager.getMarker(logMarker);
        this.logger = logger;
    }

    public abstract T deserialize(ResourceLocation id, JsonObject json);

    @Nullable
    public T get(ResourceLocation id) {
        return this.resources.get(id);
    }

    public Collection<T> getValues() {
        return this.resources.values();
    }

    //region Resource reloading

    protected Gson getGson() {
        return (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    }

    protected Collection<ResourceLocation> getAllResources(IResourceManager resourceManager) {
        return resourceManager.getAllResourceLocations(this.dataPath, s -> s.endsWith(".json"));
    }

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {
        Gson gson = getGson();
        Collection<ResourceLocation> resourceLocations = getAllResources(resourceManager);
        if (resourceLocations.isEmpty()) return;

        this.resources.clear();

        for (ResourceLocation id : resourceLocations) {
            try (IResource iResource = resourceManager.getResource(id)) {
                int substringStart = this.dataPath.length();
                int substringEnd = id.getPath().length() - ".json".length();
                String path = id.getPath().substring(substringStart, substringEnd);
                ResourceLocation name = new ResourceLocation(id.getNamespace(), path);
                this.logger.debug(this.logMarker, "Found resource file '{}', reading as '{}'", id, name);

                JsonObject json = JSONUtils.fromJson(gson, IOUtils.toString(iResource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
                if (json == null) {
                    this.logger.error(this.logMarker, "Could not load resource '{}' as it's null or empty", name);
                } else {
                    T obj = this.deserialize(name, json);
                    this.addObject(name, obj);
                }
            } catch (IllegalArgumentException | JsonParseException ex) {
                this.logger.error(this.logMarker, "Parsing error loading '{}'", id);
                this.logger.catching(ex);
            } catch (IOException ex) {
                this.logger.error(this.logMarker, "Could not read file '{}'", id);
                this.logger.catching(ex);
            }
        }

        this.logger.info(this.logMarker, "Registered {} objects", this.resources.size());
    }

    private void addObject(ResourceLocation id, T obj) {
        if (this.resources.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate object with ID " + id);
        } else {
            this.resources.put(id, obj);
        }
    }

    //endregion
}
