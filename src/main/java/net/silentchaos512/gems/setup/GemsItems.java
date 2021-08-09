package net.silentchaos512.gems.setup;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.ItemLike;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.item.GemsFoodItem;
import net.silentchaos512.gems.item.PetSummonerItem;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.item.container.FlowerBasketItem;
import net.silentchaos512.gems.item.container.GemBagItem;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.registry.ItemRegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public final class GemsItems {
    private static final Collection<ItemRegistryObject<? extends Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    static {
        Gems.registerItems();
    }

    public static final ItemRegistryObject<Item> RAW_SILVER = registerCraftingItem("raw_silver");
    public static final ItemRegistryObject<Item> SILVER_INGOT = registerCraftingItem("silver_ingot");
    public static final ItemRegistryObject<Item> SILVER_NUGGET = registerCraftingItem("silver_nugget");
    public static final ItemRegistryObject<Item> LOLINOMICON = registerCraftingItem("lolinomicon");

    public static final ItemRegistryObject<SoulGemItem> SOUL_GEM = register("soul_gem", () ->
            new SoulGemItem(unstackableProps()));

    public static final ItemRegistryObject<GemBagItem> GEM_BAG = register("gem_bag", () ->
            new GemBagItem(unstackableProps()));
    public static final ItemRegistryObject<FlowerBasketItem> FLOWER_BASKET = register("flower_basket", () ->
            new FlowerBasketItem(unstackableProps()));

    public static final ItemRegistryObject<PetSummonerItem> SUMMON_KITTY = registerSimpleModel("summon_kitty", () ->
            new PetSummonerItem(PetSummonerItem::getCat, baseProps()));
    public static final ItemRegistryObject<PetSummonerItem> SUMMON_PUPPY = registerSimpleModel("summon_puppy", () ->
            new PetSummonerItem(PetSummonerItem::getDog, baseProps()));

    public static final ItemRegistryObject<Item> POTATO_ON_A_STICK = registerFood("potato_on_a_stick", Items.STICK, new FoodProperties.Builder()
            .nutrition(6)
            .saturationMod(0.7f));
    public static final ItemRegistryObject<Item> SUGAR_COOKIE = registerFood("sugar_cookie", null, new FoodProperties.Builder()
            .nutrition(2)
            .saturationMod(0.4f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 400), 1f));
    public static final ItemRegistryObject<Item> UNCOOKED_MEATY_STEW = registerFood("uncooked_meaty_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.6f));
    public static final ItemRegistryObject<Item> MEATY_STEW = registerFood("meaty_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(12)
            .saturationMod(1.6f));
    public static final ItemRegistryObject<Item> UNCOOKED_FISHY_STEW = registerFood("uncooked_fishy_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(0.5f));
    public static final ItemRegistryObject<Item> FISHY_STEW = registerFood("fishy_stew", Items.BOWL, new FoodProperties.Builder()
            .nutrition(10)
            .saturationMod(1.2f));
    public static final ItemRegistryObject<Item> IRON_POTATO = registerFood("iron_potato", null, new FoodProperties.Builder()
            .nutrition(9)
            .saturationMod(0.9f)
            .alwaysEat()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10 * 60 * 20, 4), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5 * 60 * 20, 0), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5 * 60 * 20, 1), 1f)
    );

    private GemsItems() {}

    static void register() {}

    public static Collection<ItemRegistryObject<? extends Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static <T extends Item> ItemRegistryObject<T> registerSimpleModel(String name, Supplier<T> item) {
        ItemRegistryObject<T> ret = register(name, item);
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static ItemRegistryObject<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        return registerSimpleModel(name, () -> new Item(baseProps()));
    }

    private static ItemRegistryObject<Item> registerFood(String name, @Nullable ItemLike returnItem, FoodProperties.Builder foodBuilder) {
        ItemRegistryObject<Item> ret = register(name, () -> new GemsFoodItem(foodBuilder, returnItem, baseProps()));
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static Item.Properties baseProps() {
        return new Item.Properties().tab(GemsBase.ITEM_GROUP);
    }

    private static Item.Properties unstackableProps() {
        return baseProps().stacksTo(1);
    }
}
