package net.silentchaos512.gems.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.init.GemsRecipeInit;
import net.silentchaos512.lib.util.NameUtils;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TokenEnchantingRecipeBuilder {
    private final Item result;
    private final int count;
    private final int chaosGenerated;
    private final int processTime;
    private Ingredient token;
    private final Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();

    private TokenEnchantingRecipeBuilder(IItemProvider result, int count, int chaosGenerated, int processTime) {
        this.result = result.asItem();
        this.count = count;
        this.chaosGenerated = chaosGenerated;
        this.processTime = processTime;
    }

    public static TokenEnchantingRecipeBuilder builder(IItemProvider result, int count, int chaosGenerated, int processTime) {
        return new TokenEnchantingRecipeBuilder(result, count, chaosGenerated, processTime);
    }

    public TokenEnchantingRecipeBuilder token(IItemProvider item) {
        return token(Ingredient.fromItems(item));
    }

    public TokenEnchantingRecipeBuilder token(Tag<Item> tag) {
        return token(Ingredient.fromTag(tag));
    }

    public TokenEnchantingRecipeBuilder token(Ingredient ingredient) {
        this.token = ingredient;
        return this;
    }

    public TokenEnchantingRecipeBuilder addIngredient(IItemProvider item, int count) {
        return addIngredient(Ingredient.fromItems(item), count);
    }

    public TokenEnchantingRecipeBuilder addIngredient(Tag<Item> tag, int count) {
        return addIngredient(Ingredient.fromTag(tag), count);
    }

    public TokenEnchantingRecipeBuilder addIngredient(Ingredient ingredient, int count) {
        this.ingredients.put(ingredient, count);
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        ResourceLocation itemId = NameUtils.from(this.result);
        build(consumer, new ResourceLocation(itemId.getNamespace(), "token_enchanting/" + itemId.getPath()));
    }

    public void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, this));
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final TokenEnchantingRecipeBuilder builder;

        public Result(ResourceLocation id, TokenEnchantingRecipeBuilder builder) {
            this.id = id;
            this.builder = builder;
        }

        @Override
        public void serialize(JsonObject json) {
            // TODO: Change to camel_case?
            // TODO: Maybe rename chaos_generated to chaos_per_tick?
            json.addProperty("chaosGenerated", builder.chaosGenerated);
            json.addProperty("processTime", builder.processTime);

            JsonObject ingredients = new JsonObject();
            ingredients.add("token", builder.token.serialize());
            JsonArray others = new JsonArray();

            // FIXME: Ingredients may be arrays...
            builder.ingredients.forEach((ing, count) -> {
                JsonObject j = ing.serialize().getAsJsonObject();
                j.addProperty("count", count);
                others.add(j);
            });
            ingredients.add("others", others);
            json.add("ingredients", ingredients);

            JsonObject result = new JsonObject();
            result.addProperty("item", NameUtils.from(builder.result).toString());
            if (builder.count > 1) {
                result.addProperty("count", builder.count);
            }
            json.add("result", result);
        }

        @Override
        public ResourceLocation getID() {
            return id;
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return GemsRecipeInit.TOKEN_ENCHANTING.get();
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }
}
