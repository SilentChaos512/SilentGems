package net.silentchaos512.gems.block.teleporter;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.silentchaos512.gems.block.IGemBlock;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.List;

public class GemTeleporterBlock extends TeleporterBaseBlock implements IGemBlock {
    final Gems gem;

    public GemTeleporterBlock(Gems gem, boolean isAnchor) {
        super(isAnchor);
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    public ITextComponent getGemBlockName() {
        return new TranslationTextComponent("block.silentgems.teleporter", this.gem.getDisplayName());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(gem.getSet().getDisplayName());
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ITextComponent getNameTextComponent() {
        return getGemBlockName();
    }
}
