package net.silentchaos512.gems.client.gui;

import net.minecraft.client.Minecraft;
import net.silentchaos512.gems.chaos.ChaosEvents;
import net.silentchaos512.gems.client.ClientPlayerInfo;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.lib.client.gui.DebugRenderOverlay;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class DebugOverlay extends DebugRenderOverlay {
    @Nonnull
    @Override
    public List<String> getDebugText() {
        List<String> list = new ArrayList<>();
        list.add("Chaos");
        list.add("- Player=" + String.format("%,d", ClientPlayerInfo.playerChaos));
        list.add("- World=" + String.format("%,d", ClientPlayerInfo.worldChaos));
        list.add("- Equilibrium=" + String.format("%,d", ClientPlayerInfo.equilibriumChaos));
        list.add("- Cooldown Timers");
        ChaosEvents.getCooldownTimersDebugText(Minecraft.getInstance().player).forEach(s -> list.add("    - " + s));
        return list;
    }

    @Override
    public float getTextScale() {
        return 1;
    }

    @Override
    public boolean isHidden() {
        return !GemsConfig.COMMON.debugShowOverlay.get();
    }
}
