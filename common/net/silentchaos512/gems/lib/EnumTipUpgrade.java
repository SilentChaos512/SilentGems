package net.silentchaos512.gems.lib;


public enum EnumTipUpgrade {

  NONE(0, 0, 0, 0f),
  IRON(1, 2, 256, 1f),
  DIAMOND(2, 3, 512, 1.5f),
  EMERALD(3, 2, 1024, 2f);

  public final int id;
  private int miningLevel;
  private int durabilityBoost;
  private float speedBoost;

  private EnumTipUpgrade(int id, int miningLevel, int durabilityBoost, float speedBoost) {

    this.id = id;
    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
  }

  public void init(int miningLevel, int durabilityBoost, float speedBoost) {

    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
  }

  public static EnumTipUpgrade getById(int id) {

    for (EnumTipUpgrade tip : values()) {
      if (tip.id == id) {
        return tip;
      }
    }
    return NONE;
  }

  public int getMiningLevel() {

    return miningLevel;
  }

  public int getDurabilityBoost() {

    return durabilityBoost;
  }

  public float getSpeedBoost() {

    return speedBoost;
  }
}
