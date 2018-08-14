package net.silentchaos512.gems.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockGemLamp;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.item.ItemBlockMetaSubtypes;

public class ItemBlockGemLamp extends ItemBlockMetaSubtypes {
    public ItemBlockGemLamp(Block block) {
        super(block, 16);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        BlockGemLamp block = (BlockGemLamp) Block.getBlockFromItem(stack.getItem());
        String suffix = block.inverted
                ? " (" + SilentGems.i18n.blockSubText(Names.GEM_LAMP, "inverted") + ")"
                : "";
        return super.getItemStackDisplayName(stack) + suffix;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        BlockGemLamp block = (BlockGemLamp) Block.getBlockFromItem(stack.getItem());
        return "tile." + SilentGems.RESOURCE_PREFIX + block.nameForLocalization + stack.getItemDamage();
    }
}
