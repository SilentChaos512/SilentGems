package net.silentchaos512.gems.lib.soul;

public enum EnumSoulElement {

  NONE(1),
  FIRE(13),
  WATER(12),
  EARTH(11),
  WIND(10),
  METAL(18),
  ICE(17),
  LIGHTNING(16),
  VENOM(15),
  FLORA(5),
  FAUNA(6),
  MONSTER(7),
  ALIEN(8);

  public final int weight;

  private EnumSoulElement(int weight) {

    this.weight = weight;
  }

  public String getDisplayName() {

    // TODO: Localizations
    String name = name().toLowerCase();
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }
}
