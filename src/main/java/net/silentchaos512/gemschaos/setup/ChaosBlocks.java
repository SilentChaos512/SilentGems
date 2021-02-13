package net.silentchaos512.gemschaos.setup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChaosBlocks {
    private static final Collection<BlockRegistryObject<? extends Block>> SIMPLE_BLOCKS = new ArrayList<>();

    public static final BlockRegistryObject<Block> CHAOS_ORE = registerSimple("chaos_ore", () ->
            new Block(AbstractBlock.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4, 20)
                    .harvestLevel(3)
                    .setRequiresTool()
            ) {
                @Override
                public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silkTouch) {
                    return silkTouch == 0 ? MathHelper.nextInt(RANDOM, 2, 7) : 0;
                }
            });

    public static final BlockRegistryObject<Block> CHAOS_CRYSTAL_BLOCK = registerSimple("chaos_crystal_block", () ->
            new Block(AbstractBlock.Properties.create(Material.IRON)
                    .hardnessAndResistance(4, 30)
                    .sound(SoundType.METAL)));

    private ChaosBlocks() {}

    public static void register() {}

    @OnlyIn(Dist.CLIENT)
    static void registerRenderTypes(FMLClientSetupEvent event) {
    }

    public static Collection<BlockRegistryObject<? extends Block>> getSimpleBlocks() {
        return Collections.unmodifiableCollection(SIMPLE_BLOCKS);
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(ChaosRegistration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, ChaosBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        BlockRegistryObject<T> ret = registerNoItem(name, block);
        ChaosRegistration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static <T extends Block> BlockRegistryObject<T> registerSimple(String name, Supplier<T> block) {
        BlockRegistryObject<T> ret = register(name, block);
        SIMPLE_BLOCKS.add(ret);
        return ret;
    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(ChaosMod.ITEM_GROUP));
    }
}
