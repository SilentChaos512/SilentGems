package net.silentchaos512.gems.lib;


public enum EnumTipUpgrade {

  NONE(0, 0, 0, 0F),
  IRON(1, 2, 256, 1F),
  DIAMOND(2, 3, 512, 1.5F),
  EMERALD(3, 2, 1024, 2F),
  GOLD(4, 0, 0, 4F),

  // Metallurgy
  COPPER(100, 2, 90, 0.75F, "ingotCopper", 0xF18849),
  BRONZE(101, 3, 125, 0.9F, "ingotBronze", 0xE1AF82),
  HEPATIZON(102, 3, 150, 1.2F, "ingotHepatizon", 0x947A94),
  DAMASCUS_STEEL(103, 4, 250, 0.9F, "ingotDamascusSteel", 0xC6A58C),
  ANGMALLEN(104, 3, 150, 1.2F, "ingotAngmallen", 0xEBE3B1),
  STEEL(105, 4, 325, 1.2F, "ingotSteel", 0xC9C9C9),
  BRASS(106, 1, 7, 1.5F, "ingotBrass", 0xE2B167),
  SILVER(107, 1, 12, 1.8F, "ingotSilver", 0xE5E5E5),
  ELECTRUM(108, 2, 25, 2.1F, "ingotElectrum", 0xF1ECDC),
  PLATINUM(109, 3, 50, 2.4F, "ingotPlatinum", 0xDCECEF),
  IGNATIUS(110, 2, 100, 0.6F, "ingotIgnatius", 0xFFCE9D),
  SHADOW_IRON(111, 2, 150, 0.75F, "ingotShadowIron", 0x8D7565),
  SHADOW_STEEL(112, 3, 200, 0.9F, "ingotShadowSteel", 0xC7B9AF),
  MIDASIUM(113, 4, 50, 1.5F, "ingotMidasium", 0xFFCB7D),
  VYROXERES(114, 4, 150, 1.05F, "ingotVyroxeres", 0x88FE41),
  CERUCLASE(115, 4, 250, 1.05F, "ingotCeruclase", 0x8CBDD0),
  INOLASHITE(116, 5, 450, 1.2F, "ingotInolashite", 0x94D8BB),
  KALENDRITE(117, 5, 500, 1.2F, "ingotKalendrite", 0xC691D2),
  AMORDRINE(118, 5, 250, 2.1F, "ingotAmordrine", 0xD2C4D7),
  VULCANITE(119, 6, 750, 1.5F, "ingotVulcanite", 0xFFB08A),
  SANGUINITE(120, 7, 875, 1.8F, "ingotSanguinite", 0xFF0F0F),
  PROMETHEUM(121, 2, 100, 0.6F, "ingotPrometheum", 0x6A9865),
  DEEP_IRON(122, 3, 125, 0.9F, "ingotDeepIron", 0x798FA2),
  BLACK_STEEL(123, 3, 250, 1.2F, "ingotBlackSteel", 0x9BB4D0),
  OURECLASE(124, 4, 375, 1.2F, "ingotOureclase", 0xE7862E),
  ASTRAL_SILVER(125, 5, 17, 1.8F, "ingotAstralSilver", 0xD1DDDE),
  CARMOT(126, 5, 25, 1.8F, "ingotCarmot", 0xE7E0B6),
  MITHRIL(127, 5, 500, 1.35F, "ingotMithril", 0xC9F8FC),
  QUICK_SILVER(128, 5, 550, 2.1F, "ingotQuicksilver", 0xD1EFEB),
  HADEROTH(129, 5, 625, 1.8F, "ingotHaderoth", 0xDF5920),
  ORICHALCUM(130, 6, 675, 1.35F, "ingotOrichalcum", 0xA2C886),
  CELENEGIL(131, 6, 800, 2.1F, "ingotCelenegil", 0xC1E197),
  ADAMANTINE(132, 7, 775, 1.5F, "ingotAdamantine", 0xFE4343),
  ATLARUS(133, 7, 875, 1.5F, "ingotAtlarus", 0xFFE004),
  TARTARITE(134, 8, 1500, 2.1F, "ingotTartarite", 0xFFB697),
  EXIMITE(135, 4, 500, 1.2F, "ingotEximite", 0x9E83B4),
  DESICHALKOS(136, 5, 900, 1.5F, "ingotDesichalkos", 0x9E60D2);

  public final int id;
  private int miningLevel;
  private int durabilityBoost;
  private float speedBoost;
  private String materialName = "";
  private int color = 0xFFFFFF;

  private EnumTipUpgrade(int id, int miningLevel, int durabilityBoost, float speedBoost) {

    this.id = id;
    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
  }

  private EnumTipUpgrade(int id, int miningLevel, int durabilityBoost, float speedBoost,
      String materialName, int color) {

    this.id = id;
    this.miningLevel = miningLevel;
    this.durabilityBoost = durabilityBoost;
    this.speedBoost = speedBoost;
    this.materialName = materialName;
    this.color = color;
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

  public String getMaterialName() {

    return materialName;
  }

  public int getColor() {

    return color;
  }
}