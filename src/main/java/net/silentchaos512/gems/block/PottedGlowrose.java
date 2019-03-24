package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Gems;

public class PottedGlowrose extends BlockFlowerPot {
    private final Gems gem;

    public PottedGlowrose(Glowrose flower) {
        super(flower, Block.Properties
                .create(Material.CIRCUITS)
                .lightValue(GemsConfig.COMMON.glowrosePottedLight.get())
                .hardnessAndResistance(0)
        );
        this.gem = flower.getGem();
    }

    @Override
    public ITextComponent getNameTextComponent() {
        return new TextComponentTranslation("block.silentgems.potted_glowrose", this.gem.getDisplayName());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        // Do not show
    }
}
