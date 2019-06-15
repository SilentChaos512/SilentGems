package net.silentchaos512.gems.lib.fun;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class AprilFools {
    public static final AprilFools INSTANCE = new AprilFools();

    private AprilFools() {}

    public static boolean isRightDay() {
        // TODO
        return false;
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!isRightDay()) return;

        for (int i = 0; i < event.getToolTip().size(); ++i) {
            ITextComponent text = event.getToolTip().get(i);
            String str = text.getFormattedText();
            if (str.contains("Silent's Gems 3")) {
                event.getToolTip().set(i, text.appendSibling(new StringTextComponent(" & Knuckles")));
            }
        }
    }
}
