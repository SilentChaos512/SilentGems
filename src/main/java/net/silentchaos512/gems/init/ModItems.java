package net.silentchaos512.gems.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.gems.lib.Gems;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public final class ModItems {
    // item fields here
    public static Item fluffyPuff;
    public static FluffyPuffSeeds fluffyPuffSeeds;

    static final Collection<ItemBlock> blocksToRegister = new ArrayList<>();

    private ModItems() {}

    public static void registerAll(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();

        blocksToRegister.forEach(reg::register);

        registerGemItems(reg, Gems::getItem, Gems::getName);
        registerGemItems(reg, Gems::getShard, gem -> gem.getName() + "_shard");

        register(reg, "soul_gem", ItemSoulGem.INSTANCE.get());

        register(reg, "blank_enchantment_token", new Item(new Item.Builder().group(ModItemGroups.UTILITY)));
        register(reg, "enchantment_token", EnchantmentToken.INSTANCE);

        Item.Builder defaultBuilder = new Item.Builder().group(ModItemGroups.MATERIALS);
        fluffyPuff = register(reg, "fluffy_puff", new Item(defaultBuilder));
        fluffyPuffSeeds = register(reg, "fluffy_puff_seeds", new FluffyPuffSeeds());
        register(reg, "glowrose_fertilizer", new GlowroseFertilizer());

        for (Foods food : Foods.values()) {
            register(reg, food.getName(), food.getItem());
        }

        register(reg, "summon_kitty", new PetSummoner(PetSummoner::getCat));
        register(reg, "summon_puppy", new PetSummoner(PetSummoner::getDog));
    }

    private static <T extends Item> T register(IForgeRegistry<Item> reg, String name, T item) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        item.setRegistryName(id);
        reg.register(item);

        return item;
    }

    private static void registerGemItems(IForgeRegistry<Item> reg, Function<Gems, ? extends Item> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(reg, name.apply(gem), factory.apply(gem));
        }
    }
}
