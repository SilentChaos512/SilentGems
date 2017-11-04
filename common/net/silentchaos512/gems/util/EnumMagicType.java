package net.silentchaos512.gems.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.item.tool.ItemGemDagger;
import net.silentchaos512.gems.item.tool.ItemGemKatana;
import net.silentchaos512.gems.item.tool.ItemGemMachete;
import net.silentchaos512.gems.item.tool.ItemGemScepter;
import net.silentchaos512.gems.item.tool.ItemGemSword;

public enum EnumMagicType {

  NONE(0, 1.0f, 0),
  SWORD(2, 1.0f, 1000),
  KATANA(5, 1.0f, 2000),
  SCEPTER(5, 1.0f, 5000),
  MACHETE(8, 0.25f, 2500),
  DAGGER(1, 1.0f, 1000);

  final int shotCount;
  final float damagePerShot;
  final int cost;

  private EnumMagicType(int shotCount, float damagePerShot, int cost) {

    this.shotCount = shotCount;
    this.damagePerShot = damagePerShot;
    this.cost = cost;
  }

  public int getShotCount(ItemStack tool) {

    return shotCount;
  }

  public float getDamagePerShotMultiplier() {

    return damagePerShot;
  }

  public float getDamagePerShot(ItemStack tool) {

    float magic = 1f + ToolHelper.getMagicDamageModifier(tool);
    return magic * damagePerShot;
  }

  public int getCost(ItemStack tool) {

    return cost;
  }

  public static EnumMagicType getMagicType(ItemStack tool) {

    if (tool.getItem() instanceof ItemGemDagger)
      return DAGGER;
    if (tool.getItem() instanceof ItemGemMachete)
      return MACHETE;
    if (tool.getItem() instanceof ItemGemScepter)
      return SCEPTER;
    if (tool.getItem() instanceof ItemGemKatana)
      return KATANA;
    // Sword should be the default for any new "swords" added in the future.
    if (tool.getItem() instanceof ItemGemSword)
      return SWORD;
    // Not a caster?
    return NONE;
  }
}
