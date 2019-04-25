package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;

public enum CorruptedBlocks implements IBlockProvider {
    STONE(Blocks.STONE, block -> block.isIn(Tags.Blocks.STONE)),
    DIRT(Blocks.DIRT, block -> block.isIn(Tags.Blocks.DIRT) || block == Blocks.GRASS_BLOCK);

    private final Lazy<CorruptedBlock> block;
    private final Lazy<Item> pile;
    private final Block purifyBlock;
    private final Predicate<Block> canReplace;

    CorruptedBlocks(Block purifyBlock, Predicate<Block> canReplace) {
        this.purifyBlock = purifyBlock;
        block = Lazy.of(CorruptedBlock::new);
        pile = Lazy.of(() -> new Item(new Item.Properties().group(ModItemGroups.MATERIALS)));
        this.canReplace = canReplace;
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

    public class CorruptedBlock extends Block {
        public CorruptedBlock() {
            super(Properties.create(Material.CLAY)
                    .hardnessAndResistance(1)
                    .sound(SoundType.GROUND)
            );
        }

        @Override
        public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
            return getPile();
        }

        @Override
        public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
            return 4;
        }
    }
}
