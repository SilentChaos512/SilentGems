package net.silentchaos512.gems.api.lib;

public enum EnumDecoPos {
    WEST("DecoHeadL"), NORTH("DecoHeadM"), EAST("DecoHeadR"), SOUTH("PartRodDeco"), ROD("DecoRod");

    public final String nbtKey;

    EnumDecoPos(String nbtKey) {

        this.nbtKey = nbtKey;
    }
}
