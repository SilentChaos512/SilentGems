package net.silentchaos512.gems.compat.gear;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.gems.util.SoulManager;

public class SGearCompatEvents {
    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        // Add tool soul tooltips to gear items
        if (SGearProxy.isGearItem(event.getItemStack())) {
            ToolSoul soul = SoulManager.getSoul(event.getItemStack());
            if (soul != null) {
                soul.addInformation(event.getItemStack(), null, event.getToolTip(), false);
            }
        }
    }
}
