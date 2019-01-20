package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.FluffyPuffPlant;
import net.silentchaos512.gems.block.GemLamp;
import net.silentchaos512.gems.block.HardenedRock;
import net.silentchaos512.gems.lib.Gems;

import java.util.function.Function;

public final class ModBlocks {
    public static FluffyPuffPlant fluffyPuffPlant;

    private ModBlocks() {}

    public static void registerAll(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> reg = event.getRegistry();

        registerGemBlocks(reg, Gems::getOre, gem -> gem.getName() + "_ore");
        registerGemBlocks(reg, Gems::getBlock, gem -> gem.getName() + "_block");
        registerGemBlocks(reg, Gems::getBricks, gem -> gem.getName() + "_bricks");
        registerGemBlocks(reg, Gems::getGlass, gem -> gem.getName() + "_glass");
        for (GemLamp.State state : GemLamp.State.values()) {
            registerGemBlocks(reg, gem -> gem.getLamp(state), gem -> GemLamp.nameFor(gem, state));
        }
        registerGemBlocks(reg, Gems::getGlowrose, gem -> gem.getName() + "_glowrose");

        register(reg, "multi_ore_classic", Gems.Set.CLASSIC.getMultiOre());
        register(reg, "multi_ore_dark", Gems.Set.DARK.getMultiOre());
        register(reg, "multi_ore_light", Gems.Set.LIGHT.getMultiOre());

        for (HardenedRock.Type type : HardenedRock.Type.values()) {
            register(reg, type.getName(), type.getBlock());
        }

//        soulUrn = register(reg, "soul_urn", new BlockSoulUrn(), new BlockSoulUrn.ItemBlockSoulUrn(soulUrn));

        fluffyPuffPlant = register(reg, "fluffy_puff_plant", new FluffyPuffPlant());
    }

    private static <T extends Block> T register(IForgeRegistry<Block> reg, String name, T block) {
        ItemBlock item = new ItemBlock(block, new Item.Builder().group(ModItemGroups.BLOCKS));
        return register(reg, name, block, item);
    }

    private static <T extends Block> T register(IForgeRegistry<Block> reg, String name, T block, ItemBlock item) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        block.setRegistryName(id);
        reg.register(block);

        item.setRegistryName(id);
        ModItems.blocksToRegister.add(item);

        return block;
    }

    private static void registerGemBlocks(IForgeRegistry<Block> reg, Function<Gems, ? extends Block> factory, Function<Gems, String> name) {
        for (Gems gem : Gems.values()) {
            register(reg, name.apply(gem), factory.apply(gem));
        }
    }
}
