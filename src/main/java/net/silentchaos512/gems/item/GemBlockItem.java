package net.silentchaos512.gems.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.gems.block.IGemBlock;

public class GemBlockItem extends BlockItem {
    private final Block block;

    public GemBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
        this.block = blockIn;
    }

    @Override
    public Component getName(ItemStack stack) {
        if (block instanceof IGemBlock gemBlock) {
            return gemBlock.getGemBlockName();
        }
        return super.getName(stack);
    }
}
