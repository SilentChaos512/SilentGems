package net.silentchaos512.gems.block;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.silentchaos512.gems.util.Gems;

import java.util.function.Supplier;

public class PottedGlowroseBlock extends FlowerPotBlock {
    private final Gems gem;

    public PottedGlowroseBlock(Gems gem, Supplier<GlowroseBlock> flower, Properties properties) {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, flower, properties);
        this.gem = gem;
    }

    @Override
    public MutableComponent getName() {
        return new TranslatableComponent("block.silentgems.potted_glowrose", this.gem.getDisplayName());
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        // Do not show
    }
}
