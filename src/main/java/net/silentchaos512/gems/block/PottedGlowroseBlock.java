package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Gems;

import java.util.function.Supplier;

public class PottedGlowroseBlock extends FlowerPotBlock {
    private final Gems gem;

    public PottedGlowroseBlock(Gems gem, Supplier<GlowroseBlock> flower) {
        super(() -> (FlowerPotBlock) Blocks.FLOWER_POT, flower, Block.Properties
                .create(Material.MISCELLANEOUS)
                .lightValue(GemsConfig.COMMON.glowrosePottedLight.get())
                .hardnessAndResistance(0)
        );
        this.gem = gem;
    }

    @Override
    public ITextComponent getNameTextComponent() {
        return new TranslationTextComponent("block.silentgems.potted_glowrose", this.gem.getDisplayName());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Do not show
    }
}
