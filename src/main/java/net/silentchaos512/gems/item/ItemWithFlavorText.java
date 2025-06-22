package net.silentchaos512.gems.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.silentchaos512.gems.util.TextUtil;

import java.util.List;

public class ItemWithFlavorText extends Item {
    public ItemWithFlavorText(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(TextUtil.itemSub(this, "desc").withStyle(ChatFormatting.ITALIC));
    }
}
