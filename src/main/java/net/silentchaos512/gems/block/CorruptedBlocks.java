package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.utils.Lazy;
import net.silentchaos512.utils.MathUtils;

import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;

public enum CorruptedBlocks implements IBlockProvider {
    STONE(Blocks.STONE, GemsTags.Blocks.CORRUPTABLE_STONE),
    DIRT(Blocks.DIRT, GemsTags.Blocks.CORRUPTABLE_DIRT);

    private final Lazy<Block> block;
    private final Lazy<Item> pile;
    private final Block purifyBlock;
    private final Predicate<Block> canReplace;

    CorruptedBlocks(Block purifyBlock, Tag<Block> replaces) {
        this.purifyBlock = purifyBlock;
        block = Lazy.of(CorruptedBlock::new);
        pile = Lazy.of(() -> new Item(new Item.Properties().group(GemsItemGroups.MATERIALS)));
        this.canReplace = block -> block.isIn(replaces);
    }

    @Override
    public Block asBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return asBlock().asItem();
    }

    public Item getPile() {
        return pile.get();
    }

    public Block getPurifyBlock() {
        return purifyBlock;
    }

    public String getName() {
        return "corrupted_" + name().toLowerCase(Locale.ROOT);
    }

    public boolean canReplace(Block block) {
        return canReplace.test(block);
    }

    private class CorruptedBlock extends Block {
        CorruptedBlock() {
            super(Block.Properties.create(Material.CLAY)
                    .hardnessAndResistance(1)
                    .sound(SoundType.GROUND)
                    .lightValue(7)
                    .tickRandomly()
            );
        }

        @SuppressWarnings("deprecation")
        @Override
        public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
            // Small chance of turning back
            if (MathUtils.tryPercentage(rand, 0.01)) {
                worldIn.setBlockState(pos, CorruptedBlocks.this.purifyBlock.getDefaultState(), 3);
            }
        }
    }
}
