package net.silentchaos512.gems.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.CorruptedBlocks;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.lib.util.WorldUtils;
import net.silentchaos512.utils.Lazy;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PatchBlockChangerItem extends Item {
    public static final Lazy<PatchBlockChangerItem> CORRUPTING_POWDER = Lazy.of(() ->
            new PatchBlockChangerItem(2, PatchBlockChangerItem::corruptBlock));
    public static final Lazy<PatchBlockChangerItem> PURIFYING_POWDER = Lazy.of(() ->
            new PatchBlockChangerItem(4, PatchBlockChangerItem::purifyBlock));

    private final int range;
    private final Function<Block, Block> replaceFunction;

    public PatchBlockChangerItem(int range, Function<Block, Block> replaceFunction) {
        super(new Properties().group(GemsItemGroups.UTILITY).rarity(Rarity.RARE));
        this.range = range;
        this.replaceFunction = replaceFunction;
    }

    public static Block corruptBlock(Block block) {
        for (CorruptedBlocks corruptedBlocks : CorruptedBlocks.values()) {
            if (corruptedBlocks.canReplace(block)) {
                return corruptedBlocks.asBlock();
            }
        }
        return block;
    }

    public static Block purifyBlock(Block block) {
        for (CorruptedBlocks corruptedBlocks : CorruptedBlocks.values()) {
            if (corruptedBlocks.asBlock() == block) {
                return corruptedBlocks.getPurifyBlock();
            }
        }
        return block;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if (applyToPatch(context.getWorld(), context.getPos())) {
            context.getItem().shrink(1);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean applyToPatch(World world, BlockPos pos) {
        Map<BlockPos, Block> map = WorldUtils.getBlocksInSphere(world, pos, this.range, (w, p) ->
                Optional.of(w.getBlockState(p).getBlock()));

        boolean changesMade = false;
        for (Map.Entry<BlockPos, Block> entry : map.entrySet()) {
            BlockPos pos1 = entry.getKey();
            Block block = entry.getValue();
            Block newBlock = this.replaceFunction.apply(block);
            if (newBlock != block) {
                world.setBlockState(pos1, newBlock.getDefaultState(), 3);
                changesMade = true;
            }
        }

        return changesMade;
    }
}
