package net.silentchaos512.gems.init;

import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.item.container.GemBagItem;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.WispTypes;
import net.silentchaos512.lib.registry.ItemRegistryObject;

import java.util.function.Supplier;

public final class GemsItems {
    // Crafting items
    static {
        Gems.registerItems();
        CraftingItems.registerItems();
        WispTypes.registerItems();
        CorruptedBlocks.registerItems();
    }

    // Utility
    public static final ItemRegistryObject<GemBagItem> GEM_BAG = register("gem_bag", GemBagItem::new);
    public static final ItemRegistryObject<GemBagItem> GLOWROSE_BASKET = register("glowrose_basket", GemBagItem::new);
    public static final ItemRegistryObject<SoulGemItem> SOUL_GEM = register("soul_gem", SoulGemItem::new);
    public static final ItemRegistryObject<GearSoulItem> GEAR_SOUL = register("gear_soul", GearSoulItem::new);
    public static final ItemRegistryObject<EnchantmentTokenItem> ENCHANTMENT_TOKEN = register("enchantment_token", EnchantmentTokenItem::new);
    public static final ItemRegistryObject<ChaosRuneItem> CHAOS_RUNE = register("chaos_rune", ChaosRuneItem::new);
    public static final ItemRegistryObject<TeleporterLinkerItem> TELEPORTER_LINKER = register("teleporter_linker", TeleporterLinkerItem::new);
    static {
        SoulUrnUpgrades.registerItems();
    }

    // Utility/Chaos
    public static final ItemRegistryObject<ChaosMeterItem> CHAOS_METER = register("chaos_meter", ChaosMeterItem::new);
    public static final ItemRegistryObject<PatchBlockChangerItem> CORRUPTING_POWDER = register("corrupting_powder", () ->
            new PatchBlockChangerItem(2, PatchBlockChangerItem::corruptBlock));
    public static final ItemRegistryObject<PatchBlockChangerItem> PURIFYING_POWDER = register("purifying_powder", () ->
            new PatchBlockChangerItem(2, PatchBlockChangerItem::purifyBlock));
    public static final ItemRegistryObject<ChaosOrbItem> CHAOS_POTATO = register("chaos_potato", () ->
            new ChaosOrbItem(0, 5000, 0.5f));
    public static final ItemRegistryObject<ChaosOrbItem> FRAGILE_CHAOS_ORB = register("fragile_chaos_orb", () ->
            new ChaosOrbItem(2, 100_000, 0.2f));
    public static final ItemRegistryObject<ChaosOrbItem> REFINED_CHAOS_ORB = register("refined_chaos_orb", () ->
            new ChaosOrbItem(4, 1_000_000, 0.1f));
    public static final ItemRegistryObject<ChaosOrbItem> PERFECT_CHAOS_ORB = register("perfect_chaos_orb", () ->
            new ChaosOrbItem(4, 10_000_000, 0.05f));

    // Random
    static {
        ModFoods.registerItems();
    }
    public static final ItemRegistryObject<BlockNamedItem> FLUFFY_PUFF_SEEDS = register("fluffy_puff_seeds", () ->
            new BlockNamedItem(GemsBlocks.FLUFFY_PUFF_PLANT.get(), new Item.Properties().group(GemsItemGroups.MATERIALS)));
    public static final ItemRegistryObject<GlowroseFertilizerItem> GLOWROSE_FERTILIZER = register("glowrose_fertilizer", GlowroseFertilizerItem::new);
    public static final ItemRegistryObject<PetSummonerItem> SUMMON_KITTY = register("summon_kitty", () -> new PetSummonerItem(PetSummonerItem::getCat));
    public static final ItemRegistryObject<PetSummonerItem> SUMMON_PUPPY = register("summon_puppy", () -> new PetSummonerItem(PetSummonerItem::getDog));
    public static final ItemRegistryObject<SpawnEggItem> CORRUPTED_SLIME_SPAWN_EGG = register("corrupted_slime_spawn_egg", () ->
            new SpawnEggItem(GemsEntities.CORRUPTED_SLIME.get(), 0x8B008B, 0x9932CC, new Item.Properties().group(ItemGroup.MISC)));
    public static final ItemRegistryObject<SpawnEggItem> ENDER_SLIME_SPAWN_EGG = register("ender_slime_spawn_egg", () ->
            new SpawnEggItem(GemsEntities.ENDER_SLIME.get(), 0x003333, 0xAA00AA, new Item.Properties().group(ItemGroup.MISC)));

    private GemsItems() {}

    public static void register() {}

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }
}
