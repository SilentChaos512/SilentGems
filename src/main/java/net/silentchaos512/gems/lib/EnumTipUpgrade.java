package net.silentchaos512.gems.lib;

public enum EnumTipUpgrade {

  //@formatter:off
  NONE    (0,    0, 0.0F, 0.0F, 0.0F),
  IRON    (2,  256, 1.0F, 1.0F, 0.0F),
  GOLD    (0,    0, 4.0F, 0.0F, 3.0F),
  DIAMOND (3,  512, 2.0F, 2.0F, 1.0F),
  EMERALD (2, 1024, 2.0F, 1.0F, 2.0F);

  // TODO: Fun Ores?

  // Metallurgy
//  COPPER        (2, 90, 0.0F, 0.0F, 0.75F, "ingotCopper", 0xF18849),
//  BRONZE        (3, 125, 0.0F, 0.0F, 0.9F, "ingotBronze", 0xE1AF82),
//  HEPATIZON     (3, 150, 0.0F, 0.0F, 1.2F, "ingotHepatizon", 0x947A94),
//  DAMASCUS_STEEL(4, 250, 0.0F, 0.0F, 0.9F, "ingotDamascusSteel", 0xC6A58C),
//  ANGMALLEN     (3, 150, 0.0F, 0.0F, 1.2F, "ingotAngmallen", 0xEBE3B1),
//  STEEL         (4, 325, 0.0F, 0.0F, 1.2F, "ingotSteel", 0xC9C9C9),
//  BRASS         (1, 7, 0.0F, 0.0F, 1.5F, "ingotBrass", 0xE2B167),
//  SILVER        (1, 12, 0.0F, 0.0F, 1.8F, "ingotSilver", 0xE5E5E5),
//  ELECTRUM      (2, 25, 0.0F, 0.0F, 2.1F, "ingotElectrum", 0xF1ECDC),
//  PLATINUM      (3, 50, 0.0F, 0.0F, 2.4F, "ingotPlatinum", 0xDCECEF),
//  IGNATIUS      (2, 100, 0.0F, 0.0F, 0.6F, "ingotIgnatius", 0xFFCE9D),
//  SHADOW_IRON   (2, 150, 0.0F, 0.0F, 0.75F, "ingotShadowIron", 0x8D7565),
//  SHADOW_STEEL  (3, 200, 0.0F, 0.0F, 0.9F, "ingotShadowSteel", 0xC7B9AF),
//  MIDASIUM      (4, 50, 0.0F, 0.0F, 1.5F, "ingotMidasium", 0xFFCB7D),
//  VYROXERES     (4, 150, 0.0F, 0.0F, 1.05F, "ingotVyroxeres", 0x88FE41),
//  CERUCLASE     (4, 250, 0.0F, 0.0F, 1.05F, "ingotCeruclase", 0x8CBDD0),
//  INOLASHITE    (5, 450, 0.0F, 0.0F, 1.2F, "ingotInolashite", 0x94D8BB),
//  KALENDRITE    (5, 500, 0.0F, 0.0F, 1.2F, "ingotKalendrite", 0xC691D2),
//  AMORDRINE     (5, 250, 0.0F, 0.0F, 2.1F, "ingotAmordrine", 0xD2C4D7),
//  VULCANITE     (6, 750, 0.0F, 0.0F, 1.5F, "ingotVulcanite", 0xFFB08A),
//  SANGUINITE    (7, 875, 0.0F, 0.0F, 1.8F, "ingotSanguinite", 0xFF0F0F),
//  PROMETHEUM    (2, 100, 0.0F, 0.0F, 0.6F, "ingotPrometheum", 0x6A9865),
//  DEEP_IRON     (3, 125, 0.0F, 0.0F, 0.9F, "ingotDeepIron", 0x798FA2),
//  BLACK_STEEL   (3, 250, 0.0F, 0.0F, 1.2F, "ingotBlackSteel", 0x9BB4D0),
//  OURECLASE     (4, 375, 0.0F, 0.0F, 1.2F, "ingotOureclase", 0xE7862E),
//  ASTRAL_SILVER (5, 17, 0.0F, 0.0F, 1.8F, "ingotAstralSilver", 0xD1DDDE),
//  CARMOT        (5, 25, 0.0F, 0.0F, 1.8F, "ingotCarmot", 0xE7E0B6),
//  MITHRIL       (5, 500, 0.0F, 0.0F, 1.35F, "ingotMithril", 0xC9F8FC),
//  QUICK_SILVER  (5, 550, 0.0F, 0.0F, 2.1F, "ingotQuicksilver", 0xD1EFEB),
//  HADEROTH      (5, 625, 0.0F, 0.0F, 1.8F, "ingotHaderoth", 0xDF5920),
//  ORICHALCUM    (6, 675, 0.0F, 0.0F, 1.35F, "ingotOrichalcum", 0xA2C886),
//  CELENEGIL     (6, 800, 0.0F, 0.0F, 2.1F, "ingotCelenegil", 0xC1E197),
//  ADAMANTINE    (7, 775, 0.0F, 0.0F, 1.5F, "ingotAdamantine", 0xFE4343),
//  ATLARUS       (7, 875, 0.0F, 0.0F, 1.5F, "ingotAtlarus", 0xFFE004),
//  TARTARITE     (8, 1500, 0.0F, 0.0F, 2.1F, "ingotTartarite", 0xFFB697),
//  EXIMITE       (4, 500, 0.0F, 0.0F, 1.2F, "ingotEximite", 0x9E83B4),
//  DESICHALKOS   (5, 900, 0.0F, 0.0F, 1.5F, "ingotDesichalkos", 0x9E60D2);
  //@formatter:on

  private int miningLevel;
  private int durabilityBoost;
  private float speedBoost;
  private float meleeBoost;
  private float magicBoost;
  private String materialName = "";
  private int color = 0xFFFFFF;

  private EnumTipUpgrade(int miningLevel, int durabilityBoost, float speedBoost, float meleeBoost,
      float magicBoost) {

    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
    this.meleeBoost = meleeBoost;
    this.magicBoost = magicBoost;
  }

  private EnumTipUpgrade(int miningLevel, int durabilityBoost, float speedBoost, float meleeBoost,
      float magicBoost, String materialName, int color) {

    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
    this.meleeBoost = meleeBoost;
    this.magicBoost = magicBoost;
    this.materialName = materialName;
    this.color = color;
  }

  public void init(int miningLevel, int durabilityBoost, float speedBoost) {

    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
  }

  // public static EnumTipUpgrade getById(int id) {
  //
  // for (EnumTipUpgrade tip : values()) {
  // if (tip.id == id) {
  // return tip;
  // }
  // }
  // return NONE;
  // }

  public int getMiningLevel() {

    return miningLevel;
  }

  public int getDurabilityBoost() {

    return durabilityBoost;
  }

  public float getSpeedBoost() {

    return speedBoost;
  }

  public float getMeleeBoost() {

    return meleeBoost;
  }

  public float getMagicBoost() {

    return magicBoost;
  }

  public String getMaterialName() {

    return materialName;
  }

  public int getColor() {

    return color;
  }
}
