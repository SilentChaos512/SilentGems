package net.silentchaos512.gems.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.lib.Gems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public final class ModItems {
    public static PetSummoner summonKitty;
    public static PetSummoner summonPuppy;

    static final Collection<ItemBlock> blocksToRegister = new ArrayList<>();

    private ModItems() {}

    public static void registerAll(RegistryEvent.Register<Item> event) {
        if (!event.getRegistry().getRegistryName().equals(ForgeRegistries.ITEMS.getRegistryName())) return;

        blocksToRegister.forEach(ForgeRegistries.ITEMS::register);

        registerGemItems(Gems::getItem, Gems::getName);
        registerGemItems(Gems::getShard, gem -> gem.getName() + "_shard");

        register("soul_gem", SoulGem.INSTANCE.get());
        if (SGearProxy.isLoaded()) {
            register("gear_soul", GearSoulItem.INSTANCE.get());
        }

        for (CraftingItems item : CraftingItems.values()) {
            register(item.getName(), item.asItem());
        }

        register("enchantment_token", EnchantmentToken.INSTANCE.get());
        registerGemItems(Gems::getReturnHomeCharm, gem -> gem.getName() + "_return_home_charm");
        register("teleporter_linker", TeleporterLinker.INSTANCE.get());

        registerGemItems(Gems::getChaosGem, gem -> "chaos_" + gem.getName());
        register("chaos_rune", ChaosRune.INSTANCE.get());

        register("chaos_potato", new ChaosOrb(0, 5000, 0.5f));
        register("fragile_chaos_orb", new ChaosOrb(2, 100_000, 0.2f));
        register("refined_chaos_orb", new ChaosOrb(4, 1_000_000, 0.1f));
        register("perfect_chaos_orb", new ChaosOrb(4, 10_000_000, 0.05f));

        register("fluffy_puff_seeds", FluffyPuffSeeds.INSTANCE.get());
        register("glowrose_fertilizer", GlowroseFertilizer.INSTANCE.get());

        for (CorruptedBlocks block : CorruptedBlocks.values()) {
            register(block.getName() + "_pile", block.getPile());
        }

        for (Foods food : Foods.values()) {
            register(food.getName(), food.asItem());
        }

        summonKitty = register("summon_kitty", new PetSummoner(PetSummoner::getCat));
        summonPuppy = register("summon_puppy", new PetSummoner(PetSummoner::getDog));

        for (ModEntities entity : ModEntities.values()) {
            register(entity.getName() + "_spawn_egg", entity.getSpawnEgg());
        }
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
