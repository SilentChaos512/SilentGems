package net.silentchaos512.gems.setup;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.PetSummonerItem;
import net.silentchaos512.gems.item.SparklingBoneMealItem;
import net.silentchaos512.gems.item.TeleporterLinker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class GemsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SilentGems.MOD_ID);
    private static final Collection<DeferredItem<? extends Item>> SIMPLE_MODEL_ITEMS = new ArrayList<>();

    static {
        Gems.registerItems();
    }

    public static final DeferredItem<Item> RAW_SILVER = registerCraftingItem("raw_silver");
    public static final DeferredItem<Item> SILVER_INGOT = registerCraftingItem("silver_ingot");
    public static final DeferredItem<Item> SILVER_NUGGET = registerCraftingItem("silver_nugget");
    public static final DeferredItem<Item> SILVER_ROD = registerCraftingItem("silver_rod");

    public static final DeferredItem<Item> CHAOS_ESSENCE = registerCraftingItem("chaos_essence");

    public static final DeferredItem<Item> REINFORCED_GOLD_ROD = registerCraftingItem("reinforced_gold_rod");
    public static final DeferredItem<Item> REINFORCED_SILVER_ROD = registerCraftingItem("reinforced_silver_rod");

    public static final DeferredItem<TeleporterLinker> TELEPORTER_LINKER = registerSimpleModel("teleporter_linker",
            TeleporterLinker::new,
            properties -> properties.stacksTo(1)
    );

    public static final DeferredItem<SparklingBoneMealItem> SPARKLING_BONE_MEAL = registerSimpleModel("sparkling_bone_meal",
            SparklingBoneMealItem::new
    );

    public static final DeferredItem<PetSummonerItem> SUMMON_KITTY = registerSimpleModel("summon_kitty",
            properties -> new PetSummonerItem(PetSummonerItem::getCat, properties)
    );
    public static final DeferredItem<PetSummonerItem> SUMMON_PUPPY = registerSimpleModel("summon_puppy",
            properties -> new PetSummonerItem(PetSummonerItem::getDog, properties)
    );

    public static final DeferredItem<Item> POTATO_ON_A_STICK = registerSimpleModel("potato_on_a_stick",
            Item::new,
            properties -> properties
                    .food(GemsFoods.POTATO_ON_A_STICK, Consumables.DEFAULT_FOOD)
                    .usingConvertsTo(Items.STICK)
    );
    public static final DeferredItem<Item> SUGAR_COOKIE = registerSimpleModel("sugar_cookie",
            Item::new,
            properties -> properties
                    .food(GemsFoods.SUGAR_COOKIE, GemsConsumables.SUGAR_COOKIE)
    );
    public static final DeferredItem<Item> CUP_OF_COFFEE = registerSimpleModel("cup_of_coffee",
            Item::new,
            properties -> properties
                    .food(GemsFoods.CUP_OF_COFFEE, GemsConsumables.CUP_OF_COFFEE)
    );
    public static final DeferredItem<Item> UNCOOKED_MEATY_STEW = registerSimpleModel("uncooked_meaty_stew",
            Item::new,
            properties -> properties
                    .food(GemsFoods.UNCOOKED_MEATY_STEW, Consumables.DEFAULT_FOOD)
                    .usingConvertsTo(Items.BOWL)
    );
    public static final DeferredItem<Item> MEATY_STEW = registerSimpleModel("meaty_stew",
            Item::new,
            properties -> properties
                    .food(GemsFoods.MEATY_STEW, Consumables.DEFAULT_FOOD)
                    .usingConvertsTo(Items.BOWL)
    );
    public static final DeferredItem<Item> UNCOOKED_FISHY_STEW = registerSimpleModel("uncooked_fishy_stew",
            Item::new,
            properties -> properties
                    .food(GemsFoods.UNCOOKED_FISHY_STEW, Consumables.DEFAULT_FOOD)
                    .usingConvertsTo(Items.BOWL)
    );
    public static final DeferredItem<Item> FISHY_STEW = registerSimpleModel("fishy_stew",
            Item::new,
            properties -> properties
                    .food(GemsFoods.FISHY_STEW, Consumables.DEFAULT_FOOD)
                    .usingConvertsTo(Items.BOWL)
    );
    public static final DeferredItem<Item> IRON_POTATO = registerSimpleModel("iron_potato",
            Item::new,
            properties -> properties
                    .food(GemsFoods.IRON_POTATO, GemsConsumables.IRON_POTATO)
    );

    private GemsItems() {
    }

    public static Collection<DeferredItem<? extends Item>> getSimpleModelItems() {
        return Collections.unmodifiableCollection(SIMPLE_MODEL_ITEMS);
    }

    static <T extends Item> DeferredItem<T> register(String name, Function<Item.Properties, T> item) {
        return ITEMS.registerItem(name, item);
    }

    static <T extends Item> DeferredItem<T> register(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties) {
        return ITEMS.registerItem(name, item, properties);
    }

    private static <T extends Item> DeferredItem<T> registerSimpleModel(String name, Function<Item.Properties, T> item) {
        return registerSimpleModel(name, item, UnaryOperator.identity());
    }

    private static <T extends Item> DeferredItem<T> registerSimpleModel(String name, Function<Item.Properties, T> item, UnaryOperator<Item.Properties> properties) {
        DeferredItem<T> ret = register(name, item, properties);
        SIMPLE_MODEL_ITEMS.add(ret);
        return ret;
    }

    private static DeferredItem<Item> registerCraftingItem(String name) {
        // Registers a generic, basic item with no special properties. Useful for items that are
        // used primarily for crafting recipes.
        return registerSimpleModel(name, Item::new);
    }
}
