package net.silentchaos512.gems.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.silentchaos512.gems.util.TextUtil;

import java.util.function.Consumer;

public class ItemWithFlavorText extends Item {
    public ItemWithFlavorText(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(TextUtil.itemSub(this, "desc").withStyle(ChatFormatting.ITALIC));
    }
}
