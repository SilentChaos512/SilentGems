package net.silentchaos512.gems.crafting.tokenenchanter;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.item.ChaosRuneItem;
import net.silentchaos512.gems.item.EnchantmentTokenItem;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.lib.collection.StackList;

import java.util.LinkedHashMap;
import java.util.Map;

public class TokenEnchanterRecipe {
    @Getter private final ResourceLocation id;
    @Getter private int chaosGenerated;
    @Getter private int processTime;
    @Getter private ItemStack result;
    @Getter private Ingredient token;
    @Getter private final Map<Ingredient, Integer> ingredients = new LinkedHashMap<>();
    private boolean valid = true;

    public TokenEnchanterRecipe(ResourceLocation id) {
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean matches(IInventory inv) {
        if (!valid) return false;

        StackList list = StackList.from(inv);

        // Token?
        if (list.firstMatch(s -> token.test(s)).isEmpty()) return false;

        // Others?
        for (Map.Entry<Ingredient, Integer> entry : ingredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            int count = entry.getValue();

            int countInInv = 0;
            for (ItemStack stack : list) {
                if (ingredient.test(stack) && stack.getCount() >= count) {
                    countInInv += stack.getCount();
                }
            }

            if (countInInv < count) {
                return false;
            }
        }

        return true;
    }

    public static TokenEnchanterRecipe read(ResourceLocation id, JsonObject json) {
        TokenEnchanterRecipe recipe = new TokenEnchanterRecipe(id);

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
            recipe.ingredients.put(ingredient, count);
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
                String str = elem1.getAsString();
                IChaosBuff buff = ChaosBuffManager.get(str);
                if (buff != null) {
                    recipe.result = ChaosRuneItem.getStack(buff);
                }
            }
        }

        return recipe;
    }

    public static TokenEnchanterRecipe read(ResourceLocation id, PacketBuffer buffer) {
        TokenEnchanterRecipe recipe = new TokenEnchanterRecipe(id);
        recipe.result = buffer.readItemStack();
        recipe.token = Ingredient.read(buffer);
        int otherCount = buffer.readVarInt();
        for (int i = 0; i < otherCount; ++i) {
            Ingredient ingredient = Ingredient.read(buffer);
            int count = buffer.readVarInt();
            recipe.ingredients.put(ingredient, count);
        }
        return recipe;
    }

    public static void write(TokenEnchanterRecipe recipe, PacketBuffer buffer) {
        buffer.writeItemStack(recipe.result);
        recipe.token.write(buffer);
        buffer.writeVarInt(recipe.ingredients.size());
        recipe.ingredients.forEach((ingredient, count) -> {
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
}
