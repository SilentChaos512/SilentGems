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
import net.silentchaos512.gems.util.Gems;

import java.util.function.Function;
import java.util.function.Supplier;

public final class GemsBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SilentGems.MOD_ID);

    static {
        Gems.registerBlocks();
    }

    public static final DeferredBlock<TeleporterAnchorBlock> TELEPORTER_ANCHOR = register("teleporter_anchor",
            () -> new TeleporterAnchorBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5f, 3f)
            )
    );

    public static final DeferredBlock<OreBlockSG> CHAOS_ORE = register("chaos_ore",
            () -> getChaosOre(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(5f, 3f)
            )
    );
    public static final DeferredBlock<OreBlockSG> DEEPSLATE_CHAOS_ORE = register("deepslate_chaos_ore",
            () -> getChaosOre(BlockBehaviour.Properties.ofFullCopy(CHAOS_ORE.get())
                    .strength(6f, 3f)
                    .sound(SoundType.DEEPSLATE)
            )
    );

    public static final DeferredBlock<OreBlockSG> SILVER_ORE = register("silver_ore",
            () -> getSilverOre(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3)
            )
    );

    public static final DeferredBlock<OreBlockSG> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore",
            () -> getSilverOre(BlockBehaviour.Properties.ofFullCopy(SILVER_ORE.get())
                    .strength(4.5f, 3f)
                    .sound(SoundType.DEEPSLATE)
            )
    );

    public static final DeferredBlock<Block> CHAOS_ESSENCE_BLOCK = register("chaos_essence_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4, 30)
                    .sound(SoundType.METAL)
            )
    );

    public static final DeferredBlock<Block> SILVER_BLOCK = register("silver_block", () ->
            new Block(BlockBehaviour.Properties.of().strength(4, 30).sound(SoundType.METAL)));

    private GemsBlocks() {
    }

    private static <T extends Block> DeferredBlock<T> registerNoItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block) {
        return register(name, block, GemsBlocks::defaultItem);
    }

    private static <T extends Block> DeferredBlock<T> register(String name, Supplier<T> block, Function<DeferredBlock<T>, Supplier<? extends BlockItem>> item) {
        DeferredBlock<T> ret = registerNoItem(name, block);
        GemsItems.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(DeferredBlock<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties());
    }

    private static OreBlockSG getSilverOre(final BlockBehaviour.Properties properties) {
        return new OreBlockSG(GemsItems.RAW_SILVER, 2, ConstantInt.of(0), properties);
    }

    private static OreBlockSG getChaosOre(final BlockBehaviour.Properties properties) {
        return new OreBlockSG(GemsItems.CHAOS_ESSENCE, 3, UniformInt.of(3, 7), properties);
    }
}
