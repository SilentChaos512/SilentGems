package net.silentchaos512.gems.lib.fun;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.time.LocalDate;
import java.time.Month;

public final class AprilFools {
    public static final AprilFools INSTANCE = new AprilFools();

    private static final LocalDate RIGHT_DAY = LocalDate.of(LocalDate.now().getYear(), Month.APRIL, 1);

    private AprilFools() {}

    public static boolean isRightDay() {
        LocalDate date = LocalDate.now();
        return date.compareTo(RIGHT_DAY) == 0;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTooltip(ItemTooltipEvent event) {
        for (int i = 0; i < event.getToolTip().size(); ++i) {
            ITextComponent text = event.getToolTip().get(i);
            String str = text.getString();
            if (str.contains("Silent's Gems 3")) {
                event.getToolTip().set(i, text.copyRaw().func_230529_a_(new StringTextComponent(" & Knuckles")));
            }
        }
    }
}
