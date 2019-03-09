package net.silentchaos512.gems.crafting.tokenenchanter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.IInventory;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class TokenEnchanterRecipeManager implements IResourceManagerReloadListener {
    public static final TokenEnchanterRecipeManager INSTANCE = new TokenEnchanterRecipeManager();
    private static final String DATA_PATH = "silentgems/token_recipes/";
    private static final Marker MARKER = MarkerManager.getMarker("TokenEnchanterRecipeManager");
    private static final Map<ResourceLocation, TokenEnchanterRecipe> MAP = new HashMap<>();

    private TokenEnchanterRecipeManager() {}

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
        Collection<ResourceLocation> resources = resourceManager.getAllResourceLocations(
                DATA_PATH, s -> s.endsWith(".json"));
        if (resources.isEmpty()) return;

        MAP.clear();

        for (ResourceLocation id : resources) {
            try (IResource iresource = resourceManager.getResource(id)) {
                String path = id.getPath().substring(DATA_PATH.length(), id.getPath().length() - ".json".length());
                ResourceLocation name = new ResourceLocation(id.getNamespace(), path);
                SilentGems.LOGGER.debug(MARKER, "Found likely token enchanter recipe: {}, trying to read as {}", id, name);

                JsonObject json = JsonUtils.fromJson(gson, IOUtils.toString(iresource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
                if (json == null) {
                    SilentGems.LOGGER.error(MARKER, "Could not load token enchanter recipe {} as it's null or empty", name);
                } else {
                    TokenEnchanterRecipe recipe = TokenEnchanterRecipe.read(name, json);
                    if (recipe.isValid()) {
                        addRecipe(recipe);
                    } else {
                        logInvalidRecipe(recipe);
                    }
                }
            } catch (IllegalArgumentException | JsonParseException ex) {
                SilentGems.LOGGER.error(MARKER, "Parsing error loading token enchanter recipe {}", id, ex);
            } catch (IOException ex) {
                SilentGems.LOGGER.error(MARKER, "Could not read token enchanter recipe {}", id, ex);
            }
        }
    }

    @Nullable
    public static TokenEnchanterRecipe getMatch(IInventory inv) {
        for (TokenEnchanterRecipe recipe : MAP.values()) {
            if (recipe.matches(inv)) {
                return recipe;
            }
        }
        return null;
    }

    public static Collection<TokenEnchanterRecipe> getValues() {
        return MAP.values();
    }

    private static void addRecipe(TokenEnchanterRecipe recipe) {
        if (MAP.containsKey(recipe.getId())) {
            throw new IllegalStateException("Duplicate token enchanter recipe " + recipe.getId());
        }
        MAP.put(recipe.getId(), recipe);
    }

    private static void logInvalidRecipe(TokenEnchanterRecipe recipe) {
        String msg = "Token enchanter recipe '{}' is invalid, enchantment does not exist";
        // Don't log built-in recipes for users (because they complain)
        if (SilentGems.MOD_ID.equals(recipe.getId().getNamespace())) {
            SilentGems.LOGGER.debug(MARKER, msg, recipe.getId());
        } else {
            SilentGems.LOGGER.info(MARKER, msg, recipe.getId());
        }
    }
}
