package net.silentchaos512.gems.setup;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.GemsFoodItem;
import net.silentchaos512.gems.item.PetSummonerItem;
import net.silentchaos512.gems.util.Gems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public final class GemsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SilentGems.MOD_ID);
    private static final Collection<DeferredItem<? extends Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    static {
        Gems.registerItems();
    }

    public static final DeferredItem<Item> RAW_SILVER = registerCraftingItem("raw_silver");
    public static final DeferredItem<Item> SILVER_INGOT = registerCraftingItem("silver_ingot");
    public static final DeferredItem<Item> SILVER_NUGGET = registerCraftingItem("silver_nugget");

    public static final DeferredItem<Item> CHAOS_ESSENCE = registerCraftingItem("chaos_essence");

    public static final DeferredItem<PetSummonerItem> SUMMON_KITTY = registerSimpleModel("summon_kitty", () ->
            new PetSummonerItem(PetSummonerItem::getCat, baseProps()));
    public static final DeferredItem<PetSummonerItem> SUMMON_PUPPY = registerSimpleModel("summon_puppy", () ->
            new PetSummonerItem(PetSummonerItem::getDog, baseProps()));

    public static final DeferredItem<Item> POTATO_ON_A_STICK = registerFood("potato_on_a_stick", Items.STICK, new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.7f)
    );
    public static final DeferredItem<Item> SUGAR_COOKIE = registerFood("sugar_cookie", null, new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4f)
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 400), 1f)
    );
    public static final DeferredItem<Item> CUP_OF_COFFEE = registerFood("cup_of_coffee", null, new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.2f)
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1800), 1f)
    );
    public static final DeferredItem<Item> UNCOOKED_MEATY_STEW = registerFood("uncooked_meaty_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.6f)
    );
    public static final DeferredItem<Item> MEATY_STEW = registerFood("meaty_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(12)
            .saturationModifier(1.6f)
    );
    public static final DeferredItem<Item> UNCOOKED_FISHY_STEW = registerFood("uncooked_fishy_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.5f)
    );
    public static final DeferredItem<Item> FISHY_STEW = registerFood("fishy_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(10)
            .saturationModifier(1.2f)
    );
    public static final DeferredItem<Item> IRON_POTATO = registerFood("iron_potato", null, new FoodProperties.Builder()
            .nutrition(9)
            .saturationModifier(0.9f)
            .alwaysEdible()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10 * 60 * 20, 4), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5 * 60 * 20, 0), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5 * 60 * 20, 1), 1f)
    );

    private GemsItems() {
    }

    public static Collection<DeferredItem<? extends Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    private static <T extends Item> DeferredItem<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    private static <T extends Item> DeferredItem<T> registerSimpleModel(String name, Supplier<T> item) {
        DeferredItem<T> ret = register(name, item);
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static DeferredItem<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        return registerSimpleModel(name, () -> new Item(baseProps()));
    }

    private static DeferredItem<Item> registerFood(String name, @Nullable ItemLike returnItem, FoodProperties.Builder foodBuilder) {
        DeferredItem<Item> ret = register(name, () -> new GemsFoodItem(foodBuilder, returnItem, baseProps()));
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static Item.Properties baseProps() {
        return new Item.Properties();
    }

    private static Item.Properties unstackableProps() {
        return baseProps().stacksTo(1);
    }
}
