package net.silentchaos512.gems.crafting.altar;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class AltarRecipe {
    private static final String NBT_CATALYST_VALUE = "SilentGems.CatalystValue";
    private static final int CATALYST_VALUE = 100;

    @Getter private final ResourceLocation id;
    @Getter private int chaosGenerated;
    @Getter private int processTime;
    @Getter private int catalystConsumed;
    @Getter private ItemStack result;
    @Getter private Ingredient input;
    @Getter private Ingredient catalyst;

    public AltarRecipe(ResourceLocation id) {
        this.id = id;
    }

    public boolean matches(IInventory inv) {
        ItemStack inputStack = inv.getStackInSlot(0);
        ItemStack catalystStack = inv.getStackInSlot(1);

        // Being a bit lenient with the catalyst and not checking remaining amount.
        // It will just "finish off" the item, no spilling over or anything.
        return this.input.test(inputStack) && this.catalyst.test(catalystStack);
    }

    public ItemStack consumeCatalyst(ItemStack stack) {
        // TODO: This method is probably too exploitable. Maybe have a "partially used catalyst" item?
        //  Only problem with that is item stacking would be weird. How to handle this?
        ItemStack result = stack.copy();
        int value = getCatalystAmount(result) - this.catalystConsumed;

        if (value > 0) {
            // Consumed part, but not all
            setCatalystAmount(result, value);
        } else {
            // Fully consumed one item
            result.shrink(1);
            if (!result.isEmpty()) {
                // Whole stack NOT consumed, reset value
                setCatalystAmount(result, CATALYST_VALUE);
            }
        }

        return result;
    }

    public static int getCatalystAmount(ItemStack stack) {
        if (!stack.hasTag() || !stack.getOrCreateTag().contains(NBT_CATALYST_VALUE)) {
            return CATALYST_VALUE;
        }
        return stack.getOrCreateTag().getInt(NBT_CATALYST_VALUE);
    }

    private static void setCatalystAmount(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(NBT_CATALYST_VALUE, amount);
    }

    public static AltarRecipe read(ResourceLocation id, JsonObject json) {
        AltarRecipe recipe = new AltarRecipe(id);

        // Chaos and processing time
        recipe.chaosGenerated = JsonUtils.getInt(json, "chaosGenerated", 200);
        recipe.processTime = JsonUtils.getInt(json, "processTime", 100);
        recipe.catalystConsumed = JsonUtils.getInt(json, "catalystConsumed", 10);

        // Ingredients
        recipe.input = Ingredient.deserialize(json.get("input"));
        recipe.catalyst = Ingredient.deserialize(json.get("catalyst"));

        // Result
        JsonObject resultJson = json.get("result").getAsJsonObject();
        recipe.result = deserializeItem(resultJson);

        return recipe;
    }

    public static AltarRecipe read(ResourceLocation id, PacketBuffer buffer) {
        AltarRecipe recipe = new AltarRecipe(id);
        recipe.result = buffer.readItemStack();
        recipe.input = Ingredient.read(buffer);
        recipe.catalyst = Ingredient.read(buffer);
        recipe.chaosGenerated = buffer.readVarInt();
        recipe.processTime = buffer.readVarInt();
        recipe.catalystConsumed = buffer.readVarInt();
        return recipe;
    }

    public static void write(AltarRecipe recipe, PacketBuffer buffer) {
        buffer.writeItemStack(recipe.result);
        recipe.input.write(buffer);
        recipe.catalyst.write(buffer);
        buffer.writeVarInt(recipe.chaosGenerated);
        buffer.writeVarInt(recipe.processTime);
        buffer.writeVarInt(recipe.catalystConsumed);
    }

    private static ItemStack deserializeItem(JsonObject json) {
        return ShapedRecipe.deserializeItem(json);
    }
}
