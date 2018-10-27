package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemTorchBandolier extends ItemBlockPlacer {
    private static final int MAX_DAMAGE = 1024;

    public ItemTorchBandolier() {
        super(MAX_DAMAGE);
    }

    @Override
    public IBlockState getBlockPlaced(ItemStack stack) {
        return Blocks.TORCH.getDefaultState();
    }

    @Override
    public int getBlockMetaDropped(ItemStack stack) {
        return 0;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(stack, world, list, flag);
        list.add(I18n.format("misc.silentgems.legacyItem"));
    }
}
