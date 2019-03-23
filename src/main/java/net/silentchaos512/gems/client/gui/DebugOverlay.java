package net.silentchaos512.gems.client.gui;

import com.google.common.collect.ImmutableList;
import net.silentchaos512.gems.client.ClientPlayerInfo;
import net.silentchaos512.lib.client.gui.DebugRenderOverlay;

import javax.annotation.Nonnull;
import java.util.List;

public class DebugOverlay extends DebugRenderOverlay {
    @Nonnull
    @Override
    public List<String> getDebugText() {
        return ImmutableList.of(
                "Chaos (world)=" + ClientPlayerInfo.playerChaos,
                "Chaos (player)=" + ClientPlayerInfo.worldChaos
        );
    }

    @Override
    public float getTextScale() {
        return 1;
    }

    @Override
    public boolean isHidden() {
        return false;
    }
}
