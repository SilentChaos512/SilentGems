package net.silentchaos512.gems.client.gui;

import com.google.common.collect.ImmutableList;
import net.silentchaos512.gems.client.ClientPlayerInfo;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.lib.client.gui.DebugRenderOverlay;

import javax.annotation.Nonnull;
import java.util.List;

public class DebugOverlay extends DebugRenderOverlay {
    @Nonnull
    @Override
    public List<String> getDebugText() {
        return ImmutableList.of(
                "Chaos",
                "- Player=" + String.format("%,d", ClientPlayerInfo.playerChaos),
                "- World=" + String.format("%,d", ClientPlayerInfo.worldChaos),
                "- Equilibrium=" + String.format("%,d", ClientPlayerInfo.equilibriumChaos)
        );
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
