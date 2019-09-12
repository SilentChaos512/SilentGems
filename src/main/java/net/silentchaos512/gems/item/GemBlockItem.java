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
    public ITextComponent getDisplayName(ItemStack stack) {
        if (block instanceof IGemBlock) {
            return ((IGemBlock) block).getGemBlockName();
        }
        return super.getDisplayName(stack);
    }

    @Override
    public ITextComponent getName() {
        return block.getNameTextComponent();
    }
}
