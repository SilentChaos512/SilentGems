package net.silentchaos512.gems.crafting.tokenenchanter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ChaosRuneItem;
import net.silentchaos512.gems.item.EnchantmentTokenItem;
import net.silentchaos512.gems.util.InventoryUtils;
import net.silentchaos512.lib.collection.StackList;

import java.util.LinkedHashMap;
import java.util.Map;

public class TokenEnchanterRecipe implements IRecipe<IInventory> {
    public static final IRecipeType<TokenEnchanterRecipe> RECIPE_TYPE = new IRecipeType<TokenEnchanterRecipe>() {
        @Override
        public String toString() {
            return "silentgems:token_enchanting";
        }
    };
    public static final Serializer SERIALIZER = new Serializer();

    @Getter private final ResourceLocation id;
    @Getter private int chaosGenerated;
    @Getter private int processTime;
    @Getter private ItemStack result;
    @Getter private Ingredient token;
    @Getter private final Map<Ingredient, Integer> ingredientMap = new LinkedHashMap<>();
    private boolean valid = true;

    public TokenEnchanterRecipe(ResourceLocation id) {
        this.id = id;
    }

    public void consumeIngredients(IInventory inv) {
        InventoryUtils.consumeItems(inv, token, 1);
        ingredientMap.forEach(((ingredient, count) -> InventoryUtils.consumeItems(inv, ingredient, count)));
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if (!valid) return false;

        StackList list = StackList.from(inv);

        // Token?
        if (list.firstMatch(s -> token.test(s)).isEmpty()) return false;

        // Others?
        for (Map.Entry<Ingredient, Integer> entry : ingredientMap.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int count = entry.getValue();

            int countInInv = 0;
            for (ItemStack stack : list) {
                if (ingredient.test(stack)) {
                    countInInv += stack.getCount();
                }
            }

            if (countInInv < count) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<TokenEnchanterRecipe> {
        @Override
        public TokenEnchanterRecipe read(ResourceLocation recipeId, JsonObject json) {
            TokenEnchanterRecipe recipe = new TokenEnchanterRecipe(recipeId);

            // Chaos and processing time
            recipe.chaosGenerated = JSONUtils.getInt(json, "chaosGenerated", 200);
            recipe.processTime = JSONUtils.getInt(json, "processTime", 50);

            // Ingredients
            JsonObject ingredientsJson = json.get("ingredients").getAsJsonObject();
            recipe.token = Ingredient.deserialize(ingredientsJson.get("token").getAsJsonObject());
            JsonArray othersArray = ingredientsJson.get("others").getAsJsonArray();
            for (JsonElement elem : othersArray) {
                Ingredient ingredient = Ingredient.deserialize(elem);
                int count = JSONUtils.getInt(elem.getAsJsonObject(), "count", 1);
                recipe.ingredientMap.put(ingredient, count);
            }

            // Result
            JsonObject resultJson = json.get("result").getAsJsonObject();
            recipe.result = deserializeItem(resultJson);

            // Enchantments (for tokens, and maybe other things)
            JsonElement elem = resultJson.get("enchantments");
            if (elem != null) {
                for (JsonElement elem1 : elem.getAsJsonArray()) {
                    JsonObject elemObj = elem1.getAsJsonObject();
                    String name = JSONUtils.getString(elemObj, "name");
                    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(name));
                    if (enchantment == null) {
                        // Enchantment does not exist!
                        recipe.valid = false;
                    } else {
                        int level = JSONUtils.getInt(elemObj, "level", 1);
                        addEnchantment(recipe.result, enchantment, level);
                    }
                }
            }

            // Chaos rune buff
            if (recipe.result.getItem() instanceof ChaosRuneItem) {
                JsonElement elem1 = resultJson.get("buff");
                if (elem1 != null) {
                    ResourceLocation buffId = new ResourceLocation(elem1.getAsString());
                    recipe.result = ChaosRuneItem.getStack(buffId);
                } else {
                    throw new JsonSyntaxException("Chaos rune recipe is missing 'buff' string");
                }
            }

            if (!recipe.valid) {
                logInvalidRecipe(recipe);
            }

            return recipe;
        }

        @Override
        public TokenEnchanterRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            TokenEnchanterRecipe recipe = new TokenEnchanterRecipe(recipeId);
            recipe.result = buffer.readItemStack();
            recipe.token = Ingredient.read(buffer);
            int otherCount = buffer.readVarInt();
            for (int i = 0; i < otherCount; ++i) {
                Ingredient ingredient = Ingredient.read(buffer);
                int count = buffer.readVarInt();
                recipe.ingredientMap.put(ingredient, count);
            }
            return recipe;
        }

        @Override
        public void write(PacketBuffer buffer, TokenEnchanterRecipe recipe) {
            buffer.writeItemStack(recipe.result);
            recipe.token.write(buffer);
            buffer.writeVarInt(recipe.ingredientMap.size());
            recipe.ingredientMap.forEach((ingredient, count) -> {
                ingredient.write(buffer);
                buffer.writeVarInt(count);
            });
        }

        private static ItemStack deserializeItem(JsonObject json) {
            return ShapedRecipe.deserializeItem(json);
        }

        private static void addEnchantment(ItemStack stack, Enchantment enchantment, int level) {
            // Enchantment tokens are a special case
            if (stack.getItem() instanceof EnchantmentTokenItem) {
                EnchantmentTokenItem.addEnchantment(stack, enchantment, level);
            } else {
                stack.addEnchantment(enchantment, level);
            }
        }

        @SuppressWarnings("TypeMayBeWeakened")
        private static void logInvalidRecipe(TokenEnchanterRecipe recipe) {
            String msg = "Token enchanter recipe '{}' is invalid, enchantment does not exist";
            // Don't log built-in recipes for users (because they complain)
            if (SilentGems.MOD_ID.equals(recipe.getId().getNamespace())) {
                SilentGems.LOGGER.debug(msg, recipe.getId());
            } else {
                SilentGems.LOGGER.warn(msg, recipe.getId());
            }
        }
    }
}
