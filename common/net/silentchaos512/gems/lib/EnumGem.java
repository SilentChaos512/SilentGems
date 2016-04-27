package net.silentchaos512.gems.lib;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.ModItems;

public enum EnumGem implements IStringSerializable {

  // @formatter:off
  RUBY            ("Ruby",            512,  8.0f, 5.0f, 2.0f, 10, 0.7f, 1.0f, 7),
  GARNET          ("Garnet",          384,  7.0f, 4.0f, 3.0f, 12, 0.9f, 1.0f, 8),
  TOPAZ           ("Topaz",           384,  9.0f, 4.0f, 2.0f, 10, 0.8f, 2.0f, 8),
  AMBER           ("Amber",           128,  5.0f, 2.0f, 2.0f, 14, 1.1f, 4.0f, 6),
  HELIODOR        ("Heliodor",        256, 11.0f, 4.0f, 3.0f, 10, 1.0f, 2.0f, 6),
  PERIDOT         ("Peridot",         256,  6.0f, 4.0f, 3.0f, 14, 0.7f, 3.0f, 7),
  BERYL           ("Beryl",           384,  7.0f, 4.0f, 2.0f, 12, 0.9f, 3.0f, 7),
  INDICOLITE      ("Indicolite",      384, 10.0f, 2.0f, 5.0f, 12, 1.0f, 1.0f, 6),
  AQUAMARINE      ("Aquamarine",      256,  9.0f, 3.0f, 4.0f, 12, 1.1f, 2.0f, 7),
  SAPPHIRE        ("Sapphire",        512,  8.0f, 3.0f, 4.0f, 10, 0.7f, 1.0f, 8),
  IOLITE          ("Iolite",          512,  6.0f, 2.0f, 4.0f, 11, 1.0f, 2.0f, 9),
  AMETHYST        ("Amethyst",        256,  7.0f, 3.0f, 3.0f, 12, 0.9f, 3.0f, 9),
  AGATE           ("Agate",           192,  8.0f, 3.0f, 3.0f, 14, 1.1f, 4.0f, 8),
  MORGANITE       ("Morganite",       256,  9.0f, 4.0f, 2.0f, 12, 0.9f, 3.0f, 7),
  ONYX            ("Onyx",            128,  8.0f, 6.0f, 2.0f,  8, 0.6f, 1.0f, 6),
  OPAL            ("Opal",            192,  8.0f, 3.0f, 5.0f, 13, 0.7f, 2.0f, 8),
  //--------------------------------------------------------------------------
  CARNELIAN       ("Carnelian",       256,  9.0f, 2.0f, 3.0f, 12, 0.9f, 2.0f, 7),
  SPINEL          ("Spinel",          512,  8.0f, 4.0f, 2.0f, 11, 0.7f, 1.0f, 8),
  CITRINE         ("Citrine",         384, 10.0f, 3.0f, 2.0f, 13, 1.0f, 2.0f, 7),
  JASPER          ("Jasper",          256,  7.0f, 3.0f, 3.0f, 14, 0.9f, 2.0f, 9),
  GOLDEN_BERYL    ("GoldenBeryl",     384, 10.0f, 2.0f, 5.0f, 10, 0.7f, 1.0f, 6),
  MOLDAVITE       ("Moldavite",       192,  6.0f, 5.0f, 2.0f, 11, 0.8f, 3.0f, 8),
  MALACHITE       ("Malachite",       128,  8.0f, 4.0f, 2.0f, 14, 1.3f, 2.0f, 7),
  TURQUOISE       ("Turquoise",       256,  9.0f, 3.0f, 3.0f, 12, 0.8f, 1.0f, 7),
  MOONSTONE       ("Moonstone",       256,  9.0f, 3.0f, 3.0f, 15, 1.0f, 3.0f, 7),
  BLUE_TOPAZ      ("BlueTopaz",       512,  9.0f, 3.0f, 3.0f, 11, 0.7f, 1.0f, 8),
  TANZANITE       ("Tanzanite",       384,  6.0f, 3.0f, 4.0f, 13, 0.7f, 2.0f, 8),
  VIOLET_SAPPHIRE ("VioletSapphire",  512,  7.0f, 4.0f, 3.0f, 11, 0.9f, 1.0f, 8),
  LEPIDOLITE      ("Lepidolite",      128,  5.0f, 3.0f, 6.0f, 13, 1.0f, 1.0f, 6),
  AMETRINE        ("Ametrine",        384,  8.0f, 4.0f, 2.0f, 10, 0.7f, 4.0f, 6),
  BLACK_DIAMOND   ("BlackDiamond",    768,  9.0f, 3.0f, 4.0f,  9, 0.7f, 1.0f, 9),
  ALEXANDRITE     ("Alexandrite",     512,  8.0f, 3.0f, 3.0f, 10, 0.8f, 2.0f, 7);
  // @formatter:on

  public static final PropertyEnum VARIANT_GEM = PropertyEnum.create("variant", EnumGem.class, RUBY,
      GARNET, TOPAZ, AMBER, HELIODOR, PERIDOT, BERYL, INDICOLITE, AQUAMARINE, SAPPHIRE, IOLITE,
      AMETHYST, AGATE, MORGANITE, ONYX, OPAL);
  // public static final PropertyEnum VARIANT_GEM_DARK = PropertyEnum.create("variant", EnumGem.class,
  // CARNELIAN, SPINEL, CITRINE, JASPER, GOLDEN_BERYL, MOLDAVITE, MALACHITE, TURQUOISE, MOONSTONE,
  // BLUE_TOPAZ, TANZANITE, VIOLET_SAPPHIRE, LEPIDOLITE, AMETRINE, BLACK_DIAMOND, ALEXANDRITE);

  protected final String name;
  protected final int durability;
  protected final float miningSpeed;
  protected final float meleeDamage;
  protected final float magicDamage;
  protected final float meleeSpeed;
  protected final int enchantability;
  protected final float chargeSpeed;
  protected final int protection;

  private EnumGem(String name, int durability, float miningSpeed, float meleeDamage,
      float magicDamage, int enchantability, float meleeSpeed, float chargeSpeed, int protection) {

    this.name = name;
    this.durability = durability;
    this.miningSpeed = miningSpeed;
    this.meleeDamage = meleeDamage;
    this.magicDamage = magicDamage;
    this.meleeSpeed = meleeSpeed;
    this.enchantability = enchantability;
    this.chargeSpeed = chargeSpeed;
    this.protection = protection;
  }

  @Override
  public String getName() {

    return name().toLowerCase();
  }

  public String getGemName() {

    return name;
  }

  public int getDurability(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? 4 * durability : durability;
  }

  public float getMiningSpeed(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? miningSpeed + 4.0f : miningSpeed;
  }

  public float getMeleeDamage(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? meleeDamage + 2.0f : meleeDamage;
  }

  public float getMagicDamage(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? magicDamage + 2.0f : magicDamage;
  }

  public int getEnchantability(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? enchantability + 6 : enchantability;
  }

  public float getMeleeSpeed(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? meleeSpeed + 0.2f : meleeSpeed;
  }

  public float getChargeSpeed(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? chargeSpeed + 1.0f : chargeSpeed;
  }

  public int getHarvestLevel(EnumMaterialTier tier) {

    return tier == EnumMaterialTier.SUPER ? 4 : 2;
  }

  public static EnumGem getFromStack(ItemStack stack) {

    if (stack == null || stack.getItem() != ModItems.gem) {
      return null;
    }

    return values()[stack.getItemDamage() & 0x1F];
  }

  // ======================
  // Block and Item getters
  // ======================

  public ItemStack getBlock() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemBlock : ModBlocks.gemBlockDark, 1,
        ordinal() & 0xF);
  }

  public String getBlockOreName() {

    return "block" + name;
  }

  public ItemStack getBlockSuper() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemBlockSuper : ModBlocks.gemBlockSuperDark, 1,
        ordinal() & 0xF);
  }

  public String getBlockSuperOreName() {

    return getBlockOreName() + "Super";
  }

  public ItemStack getOre() {

    return new ItemStack(ordinal() < 16 ? ModBlocks.gemOre : ModBlocks.gemOreDark, 1,
        ordinal() & 0xF);
  }

  public String getOreOreName() {

    return "ore" + name;
  }

  public ItemStack getItem() {

    return new ItemStack(ModItems.gem, 1, ordinal());
  }

  public String getItemOreName() {

    return "gem" + name;
  }

  public ItemStack getItemSuper() {

    return new ItemStack(ModItems.gem, 1, ordinal() + 32);
  }

  public String getItemSuperOreName() {

    return getItemOreName() + "Super";
  }

  public ItemStack getShard() {

    return new ItemStack(ModItems.gemShard, 1, ordinal());
  }

  public String getShardOreName() {

    return "nugget" + name;
  }
}
