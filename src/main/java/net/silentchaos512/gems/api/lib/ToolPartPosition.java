package net.silentchaos512.gems.api.lib;

import javax.annotation.Nullable;

public enum ToolPartPosition implements IPartPosition {
    ROD(0, "PartRod", "DecoRod"),
    HEAD(1, "Part%d", "DecoHeadM"),
    TIP(2, "PartHeadTip", ""),
    ROD_DECO(3, "PartRodDeco", "PartRodDeco"),
    ROD_GRIP(4, "PartRodWool", "");

    final int renderPass;
    final String nbtKey;
    final String decoNbtKey;

    ToolPartPosition(int renderPass, String nbtKey, String decoNbtKey) {
        this.renderPass = renderPass;
        this.nbtKey = nbtKey;
        this.decoNbtKey = decoNbtKey;
    }

    @Nullable
    public static ToolPartPosition forRenderPass(int pass) {
        for (ToolPartPosition pos : values()) {
            if (pos.renderPass == pass) {
                return pos;
            }
        }
        return null;
    }

    @Override
    public int getRenderPass() {
        return renderPass;
    }

    @Override
    public String getKey(int subPosition) {
        if (nbtKey.contains("%d"))
            return String.format(nbtKey, subPosition);
        return nbtKey;
    }

    @Override
    public String getDecoKey() {
        return decoNbtKey;
    }
}
