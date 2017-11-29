package net.silentchaos512.gems.lib.soul;

public enum EnumSoulElement {

  //@formatter:off
  //        WT    DUR     HRV     MEL     MAG     PRT
  NONE(      1,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f),
  FIRE(     13,  0.00f,  0.00f, +0.15f,  0.00f, -0.05f),
  WATER(    12,  0.00f,  0.00f, -0.05f, +0.15f,  0.00f),
  EARTH(    11, +0.15f,  0.00f,  0.00f, -0.05f,  0.00f),
  WIND(     10,  0.00f, +0.15f, -0.05f,  0.00f,  0.00f),
  METAL(    18, +0.20f,  0.00f,  0.00f, -0.10f, +0.10f),
  ICE(      17, -0.05f,  0.00f,  0.00f, +0.20f, -0.05f),
  LIGHTNING(16, -0.05f, +0.10f, +0.10f, +0.05f, -0.10f),
  VENOM(    15, +0.10f, -0.15f, +0.15f,  0.00f,  0.00f),
  FLORA(     5, -0.05f,  0.00f, -0.10f, +0.10f,  0.00f),
  FAUNA(     6,  0.00f,  0.00f, +0.10f, -0.10f, -0.05f),
  MONSTER(   7, +0.10f, -0.05f,  0.00f, -0.10f,  0.00f),
  ALIEN(     8,  0.00f, -0.10f,  0.00f, +0.15f, +0.05f);
  //@formatter:on

  public final int weight;
  public final float durabilityModifier;
  public final float harvestSpeedModifier;
  public final float meleeDamageModifier;
  public final float magicDamageModifier;
  public final float protectionModifier;

  private EnumSoulElement(int weight, float durability, float harvestSpeed, float meleeDamage,
      float magicDamage, float protection) {

    this.weight = weight;
    this.durabilityModifier = durability;
    this.harvestSpeedModifier = harvestSpeed;
    this.meleeDamageModifier = meleeDamage;
    this.magicDamageModifier = magicDamage;
    this.protectionModifier = protection;
  }

  public String getDisplayName() {

    // TODO: Localizations
    String name = name().toLowerCase();
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }
}
