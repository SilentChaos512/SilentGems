package net.silentchaos512.gems.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.block.IGemBlock;

public class GemBlockItem extends BlockItem {
    private final Block block;

    public GemBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
        this.block = blockIn;
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        if (block instanceof IGemBlock) {
            return ((IGemBlock) block).getGemBlockName();
        }
        return super.getName(stack);
    }

    @Override
    public ITextComponent getDescription() {
        return block.getName();
    }
}
