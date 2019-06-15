package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.function.Predicate;

public enum CorruptedBlocks implements IBlockProvider {
    STONE(Blocks.STONE, block -> block.isIn(Tags.Blocks.STONE)),
    DIRT(Blocks.DIRT, block -> block.isIn(Tags.Blocks.DIRT) || block == Blocks.GRASS_BLOCK);

    private final Lazy<Block> block;
    private final Lazy<Item> pile;
    private final Block purifyBlock;
    private final Predicate<Block> canReplace;

    CorruptedBlocks(Block purifyBlock, Predicate<Block> canReplace) {
        this.purifyBlock = purifyBlock;
        block = Lazy.of(() -> new Block(Block.Properties.create(Material.CLAY).hardnessAndResistance(1).sound(SoundType.GROUND)));
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
}
