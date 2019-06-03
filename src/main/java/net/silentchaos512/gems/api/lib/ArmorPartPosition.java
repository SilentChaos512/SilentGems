package net.silentchaos512.gems.api.lib;

import javax.annotation.Nullable;

public enum ArmorPartPosition implements IPartPosition {
    WEST(0, "Part1", "DecoWest"),
    NORTH(1, "Part0", "DecoNorth"),
    EAST(2, "Part2", "DecoEast"),
    SOUTH(3, "Part3", "DecoSouth"),
    FRAME(4, "PartFrame", "");

    final int renderPass;
    final String nbtKey;
    final String decoNbtKey;

    ArmorPartPosition(int renderPass, String nbtKey, String decoNbtKey) {
        this.renderPass = renderPass;
        this.nbtKey = nbtKey;
        this.decoNbtKey = decoNbtKey;
    }

    @Nullable
    public static ArmorPartPosition forRenderPass(int pass) {
        for (ArmorPartPosition pos : values()) {
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
