package net.silentchaos512.gems.lib.soul;

public enum EnumSoulElement {

    //@formatter:off
    //        WT    DUR     HRV     MEL     MAG     PRT
    NONE(      1,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f, 0xFFFFFF),
    FIRE(     13,  0.00f,  0.00f, +0.15f,  0.00f, -0.05f, 0xF48C42),
    WATER(    12,  0.00f,  0.00f, -0.05f, +0.15f,  0.00f, 0x4189F4),
    EARTH(    11, +0.15f,  0.00f,  0.00f, -0.05f,  0.00f, 0x1FC121),
    WIND(     10,  0.00f, +0.15f, -0.05f,  0.00f,  0.00f, 0x83F7C1),
    METAL(    18, +0.20f,  0.00f,  0.00f, -0.10f, +0.10f, 0xAAAAAA),
    ICE(      17, -0.05f,  0.00f,  0.00f, +0.20f, -0.05f, 0x8EFFEC),
    LIGHTNING(16, -0.05f, +0.10f, +0.10f, +0.05f, -0.10f, 0xFFFF47),
    VENOM(    15, +0.10f, -0.15f, +0.15f,  0.00f,  0.00f, 0x83C14D),
    FLORA(     5, -0.05f,  0.00f, -0.10f, +0.10f,  0.00f, 0x277C2F),
    FAUNA(     6,  0.00f,  0.00f, +0.10f, -0.10f, -0.05f, 0xFFA3D7),
    MONSTER(   7, +0.10f, -0.05f,  0.00f, -0.10f,  0.00f, 0x635538),
    ALIEN(     8,  0.00f, -0.10f,  0.00f, +0.15f, +0.05f, 0x8E42A5);
    //@formatter:on

    public final int weight;
    public final float durabilityModifier;
    public final float harvestSpeedModifier;
    public final float meleeDamageModifier;
    public final float magicDamageModifier;
    public final float protectionModifier;
    public final int color;

    EnumSoulElement(int weight, float durability, float harvestSpeed, float meleeDamage, float magicDamage, float protection, int color) {
        this.weight = weight;
        this.durabilityModifier = durability;
        this.harvestSpeedModifier = harvestSpeed;
        this.meleeDamageModifier = meleeDamage;
        this.magicDamageModifier = magicDamage;
        this.protectionModifier = protection;
        this.color = color;
    }

    public String getDisplayName() {
        // TODO: Localizations
        String name = name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
