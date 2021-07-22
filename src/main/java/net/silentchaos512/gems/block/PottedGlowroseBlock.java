package net.silentchaos512.gems.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.util.Gems;

import java.util.function.Supplier;

public class PottedGlowroseBlock extends FlowerPotBlock {
    private final Gems gem;

    public PottedGlowroseBlock(Gems gem, Supplier<GlowroseBlock> flower, Properties properties) {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, flower, properties);
        this.gem = gem;
    }

    @Override
    public IFormattableTextComponent getName() {
        return new TranslationTextComponent("block.silentgems.potted_glowrose", this.gem.getDisplayName());
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        // Do not show
    }
}
