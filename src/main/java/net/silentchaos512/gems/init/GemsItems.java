package net.silentchaos512.gems.init;

import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.block.FluffyPuffPlant;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.item.container.GemBagItem;
import net.silentchaos512.gems.item.container.GlowroseBasketItem;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.WispTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public final class GemsItems {
    public static BlockNamedItem fluffyPuffSeeds;
    public static PetSummonerItem summonKitty;
    public static PetSummonerItem summonPuppy;

    static final Collection<BlockItem> BLOCKS_TO_REGISTER = new ArrayList<>();

    private GemsItems() {}

    public static void registerAll(RegistryEvent.Register<Item> event) {
        BLOCKS_TO_REGISTER.forEach(ForgeRegistries.ITEMS::register);

        registerGemItems(Gems::getItem, Gems::getName);
        registerGemItems(Gems::getShard, gem -> gem.getName() + "_shard");

        register("soul_gem", SoulGemItem.INSTANCE.get());
        if (SGearProxy.isLoaded()) {
            register("gear_soul", GearSoulItem.INSTANCE.get());
        }

        for (CraftingItems item : CraftingItems.values()) {
            register(item.getName(), item.asItem());
        }

        register("enchantment_token", EnchantmentTokenItem.INSTANCE.get());
        registerGemItems(Gems::getReturnHomeCharm, gem -> gem.getName() + "_return_home_charm");
        register("teleporter_linker", TeleporterLinkerItem.INSTANCE.get());

        registerGemItems(Gems::getChaosGem, gem -> "chaos_" + gem.getName());
        register("chaos_rune", ChaosRuneItem.INSTANCE.get());

        register("chaos_potato", new ChaosOrbItem(0, 5000, 0.5f));
        register("fragile_chaos_orb", new ChaosOrbItem(2, 100_000, 0.2f));
        register("refined_chaos_orb", new ChaosOrbItem(4, 1_000_000, 0.1f));
        register("perfect_chaos_orb", new ChaosOrbItem(4, 10_000_000, 0.05f));

        fluffyPuffSeeds = register("fluffy_puff_seeds", new BlockNamedItem(FluffyPuffPlant.INSTANCE.get(), new Item.Properties().group(GemsItemGroups.MATERIALS)));
        register("glowrose_fertilizer", GlowroseFertilizerItem.INSTANCE.get());

        for (SoulUrnUpgrades upgrade : SoulUrnUpgrades.values()) {
            register(upgrade.getName(), upgrade.asItem());
        }

        for (CorruptedBlocks block : CorruptedBlocks.values()) {
            register(block.getName() + "_pile", block.getPile());
        }

        register("corrupting_powder", PatchBlockChangerItem.CORRUPTING_POWDER.get());
        register("purifying_powder", PatchBlockChangerItem.PURIFYING_POWDER.get());

        register("chaos_meter", ChaosMeterItem.INSTANCE.get());
        register("gem_bag", new GemBagItem());
        register("glowrose_basket", new GlowroseBasketItem());

        for (ModFoods food : ModFoods.values()) {
            register(food.getName(), food.asItem());
        }

        summonKitty = register("summon_kitty", new PetSummonerItem(PetSummonerItem::getCat));
        summonPuppy = register("summon_puppy", new PetSummonerItem(PetSummonerItem::getDog));

        register("ender_slime_spawn_egg", new SpawnEggItem(GemsEntities.ENDER_SLIME.get(), 0x003333, 0xAA00AA, new Item.Properties().group(ItemGroup.MISC)));
        register("corrupted_slime_spawn_egg", new SpawnEggItem(GemsEntities.CORRUPTED_SLIME.get(), 0x8B008B, 0x9932CC, new Item.Properties().group(ItemGroup.MISC)));
        Arrays.stream(WispTypes.values()).forEach(entity -> register(entity.getName() + "_spawn_egg", entity.getSpawnEgg()));
    }

    private static <T extends Item> T register(String name, T item) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        item.setRegistryName(id);
        ForgeRegistries.ITEMS.register(item);

        return item;
    }

    private static void registerGemItems(Function<Gems, ? extends Item> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(name.apply(gem), factory.apply(gem));
        }
    }
}
