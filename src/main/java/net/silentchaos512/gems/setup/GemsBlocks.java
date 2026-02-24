package net.silentchaos512.gems.setup;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.OreBlockSG;
import net.silentchaos512.gems.block.teleporter.TeleporterAnchorBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public final class GemsBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SilentGems.MOD_ID);

    static {
        Gems.registerBlocks();
    }

    public static final DeferredBlock<TeleporterAnchorBlock> TELEPORTER_ANCHOR = register("teleporter_anchor",
            TeleporterAnchorBlock::new,
            properties -> properties
                    .requiresCorrectToolForDrops()
                    .strength(5f, 3f)
    );

    public static final DeferredBlock<OreBlockSG> CHAOS_ORE = register("chaos_ore",
            GemsBlocks::getChaosOre,
            properties -> properties
                    .requiresCorrectToolForDrops()
                    .strength(5f, 3f),
            GemsBlocks::oreItemBlock
    );

    public static final DeferredBlock<OreBlockSG> DEEPSLATE_CHAOS_ORE = register("deepslate_chaos_ore",
            GemsBlocks::getChaosOre,
            properties -> BlockBehaviour.Properties.ofFullCopy(CHAOS_ORE.get())
                    .strength(6f, 3f)
                    .sound(SoundType.DEEPSLATE),
            GemsBlocks::oreItemBlock
    );

    public static final DeferredBlock<OreBlockSG> SILVER_ORE = register("silver_ore",
            GemsBlocks::getSilverOre,
            properties -> properties
                    .requiresCorrectToolForDrops()
                    .strength(3),
            GemsBlocks::oreItemBlock
    );

    public static final DeferredBlock<OreBlockSG> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore",
            GemsBlocks::getSilverOre,
            properties -> BlockBehaviour.Properties.ofFullCopy(SILVER_ORE.get())
                    .strength(4.5f, 3f)
                    .sound(SoundType.DEEPSLATE),
            GemsBlocks::oreItemBlock
    );

    public static final DeferredBlock<Block> CHAOS_ESSENCE_BLOCK = register("chaos_essence_block",
            Block::new,
            properties -> properties
                    .strength(4, 30)
                    .sound(SoundType.METAL)
    );

    public static final DeferredBlock<Block> SILVER_BLOCK = register("silver_block",
            Block::new,
            properties -> properties
                    .strength(4, 30)
                    .sound(SoundType.METAL)
    );

    private GemsBlocks() {
    }

    static <T extends Block> DeferredBlock<T> registerNoItem(
            String name,
            Function<BlockBehaviour.Properties, T> block,
            UnaryOperator<BlockBehaviour.Properties> properties
    ) {
        return BLOCKS.registerBlock(name, block, properties);
    }

    static <T extends Block> DeferredBlock<T> register(
            String name,
            Function<BlockBehaviour.Properties, T> block
    ) {
        return register(name, block, UnaryOperator.identity(), GemsBlocks::defaultItem, Item.Properties::useBlockDescriptionPrefix);
    }

    static <T extends Block> DeferredBlock<T> register(
            String name,
            Function<BlockBehaviour.Properties, T> block,
            UnaryOperator<BlockBehaviour.Properties> properties
    ) {
        return register(name, block, properties, GemsBlocks::defaultItem, Item.Properties::useBlockDescriptionPrefix);
    }

    static <T extends Block> DeferredBlock<T> register(
            String name,
            Function<BlockBehaviour.Properties, T> block,
            UnaryOperator<BlockBehaviour.Properties> properties,
            Function<DeferredBlock<T>, Function<Item.Properties, ? extends BlockItem>> item
    ) {
        return register(name, block, properties, item, Item.Properties::useBlockDescriptionPrefix);
    }

    static <T extends Block> DeferredBlock<T> registerDefaultProps(
            String name,
            Function<BlockBehaviour.Properties, T> block,
            Function<DeferredBlock<T>, Function<Item.Properties, ? extends BlockItem>> item
    ) {
        return register(name, block, UnaryOperator.identity(), item, Item.Properties::useBlockDescriptionPrefix);
    }

    static <T extends Block> DeferredBlock<T> register(
            String name,
            Function<BlockBehaviour.Properties, T> block,
            UnaryOperator<BlockBehaviour.Properties> properties,
            Function<DeferredBlock<T>, Function<Item.Properties, ? extends BlockItem>> item,
            UnaryOperator<Item.Properties> itemProperties
    ) {
        DeferredBlock<T> ret = registerNoItem(name, block, properties);
        GemsItems.register(name, item.apply(ret), itemProperties);
        return ret;
    }

    private static <T extends Block> Function<Item.Properties, BlockItem> defaultItem(DeferredBlock<T> block) {
        return p -> new BlockItem(block.get(), p);
    }

    private static OreBlockSG getSilverOre(final BlockBehaviour.Properties properties) {
        return new OreBlockSG(GemsItems.RAW_SILVER, 2, ConstantInt.of(0), properties);
    }

    private static OreBlockSG getChaosOre(final BlockBehaviour.Properties properties) {
        return new OreBlockSG(GemsItems.CHAOS_ESSENCE, 3, UniformInt.of(3, 7), properties);
    }

    private static @NotNull Function<Item.Properties, BlockItem> oreItemBlock(DeferredBlock<OreBlockSG> deferredBlock) {
        return properties -> new OreBlockSG.Item(deferredBlock.get(), properties);
    }
}
