package net.silentchaos512.gems.api.stats;

public class PartStats {

  public int durability;
  public int enchantablity;
  public float chargeSpeed;
  public int harvestLevel;
  public float harvestSpeed;
  public float meleeDamage;
  public float magicDamage;
  public float attackSpeed;
  public float rangedDamage;
  public float rangedSpeed;
  public float armor;
  public float magicArmor;

  public PartStats() {

    this.durability = 0;
    this.enchantablity = 0;
    this.chargeSpeed = 1f;
    this.harvestLevel = 0;
    this.harvestSpeed = 0f;
    this.meleeDamage = 0f;
    this.magicDamage = 0f;
    this.attackSpeed = 1f;
    this.rangedDamage = 0f;
    this.rangedSpeed = 1f;
    this.armor = 0;
    this.magicArmor = 0;
  }

  public float getStat(ItemStat stat) {

    if (stat == CommonItemStats.DURABILITY)
      return durability;
    if (stat == CommonItemStats.ENCHANTABILITY)
      return enchantablity;
    if (stat == CommonItemStats.HARVEST_LEVEL)
      return harvestLevel;
    if (stat == CommonItemStats.HARVEST_SPEED)
      return harvestSpeed;
    if (stat == CommonItemStats.MELEE_DAMAGE)
      return meleeDamage;
    if (stat == CommonItemStats.MAGIC_DAMAGE)
      return magicDamage;
    if (stat == CommonItemStats.ATTACK_SPEED)
      return attackSpeed;
    if (stat == CommonItemStats.RANGED_DAMAGE)
      return rangedDamage;
    if (stat == CommonItemStats.RANGED_SPEED)
      return rangedSpeed;
    if (stat == CommonItemStats.ARMOR)
      return armor;
    if (stat == CommonItemStats.MAGIC_ARMOR)
      return magicArmor;
    if (stat == CommonItemStats.CHARGE_SPEED)
      return chargeSpeed;

    throw new IllegalArgumentException("Unknown ItemStat: " + stat);
  }
}
