package net.silentchaos512.gems.lib;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.lib.util.StackHelper;

public enum EnumGem implements IStringSerializable {

  // @formatter:off
  //                                  DUR    EFF   MEL   MAG ENC   SPD PRT     COLOR
  RUBY            ("Ruby",            768,  8.0f, 6.0f, 2.0f, 10, 0.8f, 14, 0xE61D1D),
  GARNET          ("Garnet",          512,  7.0f, 4.0f, 3.0f, 12, 0.9f, 16, 0xE64F1D),
  TOPAZ           ("Topaz",           512,  9.0f, 4.0f, 2.0f, 12, 0.8f, 20, 0xE6711D),
  AMBER           ("Amber",           192,  5.0f, 2.0f, 4.0f, 17, 1.1f, 12, 0xE6A31D),
  HELIODOR        ("Heliodor",        384, 12.0f, 4.0f, 3.0f, 10, 1.0f, 12, 0xE6C51D),
  PERIDOT         ("Peridot",         384,  6.0f, 4.0f, 3.0f, 14, 0.7f, 14, 0xA3E61D),
  BERYL           ("Beryl",           512,  9.0f, 4.0f, 2.0f, 16, 1.1f, 14, 0x1DE61D),
  INDICOLITE      ("Indicolite",      512, 10.0f, 2.0f, 5.0f, 12, 1.0f, 12, 0x1DE682),
  AQUAMARINE      ("Aquamarine",      384,  9.0f, 3.0f, 4.0f, 12, 1.1f, 14, 0x1DE6E6),
  SAPPHIRE        ("Sapphire",        768,  8.0f, 4.0f, 4.0f, 10, 0.8f, 16, 0x1D1DE6),
  IOLITE          ("Iolite",          512,  6.0f, 2.0f, 4.0f, 11, 1.0f, 20, 0x601DE6),
  AMETHYST        ("Amethyst",        384,  7.0f, 3.0f, 4.0f, 12, 0.9f, 18, 0xA31DE6),
  AGATE           ("Agate",           256,  8.0f, 3.0f, 3.0f, 14, 1.1f, 16, 0xE61DE6),
  MORGANITE       ("Morganite",       384, 10.0f, 4.0f, 2.0f, 12, 0.9f, 14, 0xFF88FE),
  ONYX            ("Onyx",            192,  8.0f, 7.0f, 2.0f,  8, 0.7f, 12, 0x2F2F2F),
  OPAL            ("Opal",            256,  8.0f, 3.0f, 6.0f, 13, 0.7f, 16, 0xE4E4E4),
  //----------------------------------------------------------------------------------
  CARNELIAN       ("Carnelian",       384,  9.0f, 2.0f, 3.0f, 12, 0.9f, 14, 0xA30E00),
  SPINEL          ("Spinel",          768,  8.0f, 5.0f, 2.0f, 11, 0.7f, 16, 0xA34400),
  CITRINE         ("Citrine",         512, 10.0f, 4.0f, 2.0f, 13, 1.0f, 14, 0xA35F00),
  JASPER          ("Jasper",          384,  7.0f, 3.0f, 3.0f, 14, 0.9f, 18, 0xA38800),
  GOLDEN_BERYL    ("GoldenBeryl",     512, 10.0f, 2.0f, 5.0f, 10, 0.7f, 12, 0xA3A300),
  MOLDAVITE       ("Moldavite",       256,  6.0f, 5.0f, 2.0f, 11, 0.8f, 16, 0x88A300),
  MALACHITE       ("Malachite",       256,  8.0f, 4.0f, 2.0f, 14, 1.3f, 14, 0x00A336),
  TURQUOISE       ("Turquoise",       384,  9.0f, 3.0f, 3.0f, 12, 0.8f, 14, 0x00A388),
  MOONSTONE       ("Moonstone",       384,  9.0f, 3.0f, 5.0f, 15, 1.0f, 14, 0x006DA3),
  BLUE_TOPAZ      ("BlueTopaz",       768,  9.0f, 3.0f, 3.0f, 11, 0.7f, 16, 0x001BA3),
  TANZANITE       ("Tanzanite",       512,  6.0f, 3.0f, 4.0f, 13, 0.7f, 16, 0x5F00A3),
  VIOLET_SAPPHIRE ("VioletSapphire",  768,  8.0f, 4.0f, 3.0f, 11, 0.9f, 16, 0x9500A3),
  LEPIDOLITE      ("Lepidolite",      192,  4.0f, 3.0f, 7.0f, 13, 1.0f, 12, 0xA3007A),
  AMETRINE        ("Ametrine",        512,  8.0f, 4.0f, 2.0f, 10, 0.7f, 12, 0xA30052),
  BLACK_DIAMOND   ("BlackDiamond",   1024, 10.0f, 3.0f, 4.0f,  9, 0.8f, 18, 0x1E1E1E),
  ALEXANDRITE     ("Alexandrite",     768,  8.0f, 3.0f, 3.0f, 10, 0.8f, 14, 0x898989),
  //----------------------------------------------------------------------------------
  PYROPE          ("Pyrope",          512,  8.0f, 6.0f, 2.0f, 12, 1.0f, 16, 0xFF4574),
  CORAL           ("Coral",           256,  9.0f, 3.0f, 5.0f, 18, 1.2f, 20, 0xFF5545),
  SUNSTONE        ("Sunstone",        384,  7.0f, 5.0f, 5.0f, 16, 0.9f, 16, 0xFF7445),
  CATS_EYE        ("CatsEye",         768,  9.0f, 3.0f, 4.0f, 16, 1.1f, 18, 0xFFC145),
  ZIRCON          ("Zircon",          512,  8.0f, 4.0f, 3.0f, 12, 1.3f, 14, 0xFFFF45),
  JADE            ("Jade",            384,  6.0f, 4.0f, 4.0f, 16, 1.0f, 16, 0xA2FF45),
  CHRYSOPRASE     ("Chrysoprase",     512,  7.0f, 3.0f, 2.0f, 14, 1.1f, 18, 0x64FF45),
  APATITE         ("Apatite",         256,  7.0f, 2.0f, 4.0f, 15, 1.0f, 16, 0x45FFD1),
  FLUORITE        ("Fluorite",        256,  6.0f, 2.0f, 5.0f, 17, 0.9f, 14, 0x45D1FF),
  KYANITE         ("Kyanite",         768, 12.0f, 4.0f, 6.0f, 16, 1.2f, 16, 0x4583FF),
  SODALITE        ("Sodalite",        512,  8.0f, 3.0f, 3.0f, 12, 1.1f, 18, 0x5445FF),
  AMMOLITE        ("Ammolite",        256,  7.0f, 3.0f, 6.0f, 18, 1.2f, 14, 0xE045FF),
  KUNZITE         ("Kunzite",         384,  6.0f, 5.0f, 4.0f, 14, 0.8f, 20, 0xFF45E0),
  ROSE_QUARTZ     ("RoseQuartz",      512,  8.0f, 4.0f, 3.0f, 15, 1.0f, 16, 0xFF78B6),
  TEKTITE         ("Tektite",         384,  8.0f, 4.0f, 3.0f, 17, 1.0f, 18, 0x8F7C6B),
  PEARL           ("Pearl",           256,  7.0f, 3.0f, 4.0f, 20, 1.2f, 14, 0xE2E8F1);
  // @formatter:on

  public static final PropertyEnum VARIANT_GEM = PropertyEnum.create("gem", EnumGem.class, RUBY,
      GARNET, TOPAZ, AMBER, HELIODOR, PERIDOT, BERYL, INDICOLITE, AQUAMARINE, SAPPHIRE, IOLITE,
      AMETHYST, AGATE, MORGANITE, ONYX, OPAL);
  // public static final PropertyEnum VARIANT_GEM_DARK = PropertyEnum.create("variant", EnumGem.class,
  // CARNELIAN, SPINEL, CITRINE, JASPER, GOLDEN_BERYL, MOLDAVITE, MALACHITE, TURQUOISE, MOONSTONE,
  // BLUE_TOPAZ, TANZANITE, VIOLET_SAPPHIRE, LEPIDOLITE, AMETRINE, BLACK_DIAMOND, ALEXANDRITE);

  public static final int REGULAR_HARVEST_LEVEL = 2;
  public static final int SUPER_HARVEST_LEVEL = 4;
  public static final int SUPER_DURABILITY_MULTI = 4;
  public static final float SUPER_MINING_SPEED_BOOST = 4.0f;
  public static final float SUPER_MELEE_DAMAGE_BOOST = 3.0f;
  public static final float SUPER_MAGIC_DAMAGE_BOOST = 3.0f;
  public static final int SUPER_ENCHANTABILITY_BOOST = 8;
  public static final float SUPER_MELEE_SPEED_BOOST = 0.2f;
  public static final float SUPER_CHARGE_SPEED_BOOST = 1.0f;
  public static final int SUPER_PROTECTION_BOOST = 4;

  protected static final String STR_SUPER = "Super";

  protected final String name;
  protected final int durability;
  protected final float miningSpeed;
  protected final float meleeDamage;
  protected final float magicDamage;
  protected final float meleeSpeed;
  protected final int enchantability;
  protected final int protection;
  protected final int color;

  private EnumGem(String name, int durability, float miningSpeed, float meleeDamage,
      float magicDamage, int enchantability, float meleeSpeed, int protection, int color) {

    this.name = name;
    this.durability = durability;
    this.miningSpeed = miningSpeed;
    this.meleeDamage = meleeDamage;
    this.magicDamage = magicDamage;
    this.meleeSpeed = meleeSpeed;
    this.enchantability = enchantability;
    this.protection = protection;
    this.color = color;
  }

  /**
   * @return The IStringSerializable name: All lowercase with underscores, good for block states.
   */
  @Override
  public String getName() {

    return name().toLowerCase();
  }

  /**
   * @return A localization-friendly version of the name, capital case with no spaces or underscores.
   */
  public String getGemName() {

    return name;
  }

  // ===========================
  // Tier-sensitive stat getters
  // ===========================

  public int getDurability(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? durability * SUPER_DURABILITY_MULTI : durability;
  }

  public float getMiningSpeed(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? miningSpeed + SUPER_MINING_SPEED_BOOST : miningSpeed;
  }

  public float getMeleeDamage(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? meleeDamage + SUPER_MELEE_DAMAGE_BOOST : meleeDamage;
  }

  public float getMagicDamage(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? magicDamage + SUPER_MAGIC_DAMAGE_BOOST : magicDamage;
  }

  public int getEnchantability(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? enchantability + SUPER_ENCHANTABILITY_BOOST
        : enchantability;
  }

  public float getMeleeSpeed(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? meleeSpeed + SUPER_MELEE_SPEED_BOOST : meleeSpeed;
  }

  public float getChargeSpeed(EnumMaterialTier tier) {

    return 1.0f; // TODO: Are we keeping charge speed?
    // return tier == EnumMaterialTier.SUPER ? chargeSpeed + SUPER_CHARGE_SPEED_BOOST : chargeSpeed;
  }

  public int getProtection(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? protection + SUPER_PROTECTION_BOOST : protection;
  }

  public int getHarvestLevel(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? SUPER_HARVEST_LEVEL : REGULAR_HARVEST_LEVEL;
  }

  public int getColor() {

    return color;
  }

  public static EnumGem getFromStack(ItemStack stack) {

    if (StackHelper.isEmpty(stack) || stack.getItem() != ModItems.gem) {
      return null;
    }

    return values()[MathHelper.clamp(stack.getItemDamage(), 0, values().length - 1)];
  }

  public static EnumGem getRandom() {

    return values()[SilentGems.random.nextInt(values().length)];
  }

  // ======================
  // Block and Item getters
  // ======================

  /**
   * @return The gem block.
   */
  public ItemStack getBlock() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemBlock : ModBlocks.gemBlockDark, 1,
        ordinal() & 0xF);
  }

  /**
   * @return The ore dictionary name for the gem block.
   */
  public String getBlockOreName() {

    return "block" + name;
  }

  /**
   * @return The supercharged gem block.
   */
  public ItemStack getBlockSuper() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemBlockSuper : ModBlocks.gemBlockSuperDark, 1,
        ordinal() & 0xF);
  }

  /**
   * @return The ore dictionary name for the supercharged gem block.
   */
  public String getBlockSuperOreName() {

    return getBlockOreName() + STR_SUPER;
  }

  /**
   * @return The gem ore block.
   */
  public ItemStack getOre() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemOre : ModBlocks.gemOreDark, 1,
        ordinal() & 0xF);
  }

  /**
   * @return The ore dictionary name for the gem ore block.
   */
  public String getOreOreName() {

    return "ore" + name;
  }

  /**
   * @return The gem item.
   */
  public ItemStack getItem() {

    return new ItemStack(ModItems.gem, 1, ordinal());
  }

  /**
   * @return The ore dictionary name for the gem item.
   */
  public String getItemOreName() {

    return "gem" + name;
  }

  /**
   * @return The supercharged gem item.
   */
  public ItemStack getItemSuper() {

    return new ItemStack(ModItems.gemSuper, 1, ordinal());
  }

  /**
   * @return The ore dictionary name for the supercharged gem item.
   */
  public String getItemSuperOreName() {

    return getItemOreName() + STR_SUPER;
  }

  /**
   * @return The gem shard (nugget) item.
   */
  public ItemStack getShard() {

    return new ItemStack(ModItems.gemShard, 1, ordinal());
  }

  /**
   * @return The ore dictionary name of the gem shard (nugget) item.
   */
  public String getShardOreName() {

    return "nugget" + name;
  }

  public Set getSet() {

    if (ordinal() < 16)
      return Set.CLASSIC;
    else if (ordinal() < 32)
      return Set.DARK;
    else
      return Set.LIGHT;
  }

  public static enum Set {

    CLASSIC(0), DARK(16), LIGHT(32); // Overworld, Nether, and the End

    public final int startMeta;

    Set(int startMeta) {

      this.startMeta = startMeta;
    }
  }
}
