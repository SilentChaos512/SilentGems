package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.silentchaos512.gems.item.EnchantToken;

public class ModEnchantments {

  public final static int MENDING_ID_DEFAULT = 128;
  public static int MENDING_ID;
  public static EnchantmentMending mending;

  public final static int AOE_ID_DEFAULT = 129;
  public static int AOE_ID;
  public static EnchantmentAOE aoe;
  
  public static final int LUMBERJACK_ID_DEFAULT = 130;
  public static int LUMBERJACK_ID;
  public static EnchantmentLumberjack lumberjack;

  public static void init() {

    mending = new EnchantmentMending(MENDING_ID, 1, EnumEnchantmentType.ALL);
    aoe = new EnchantmentAOE(AOE_ID, 1, EnumEnchantmentType.DIGGER);
    lumberjack = new EnchantmentLumberjack(LUMBERJACK_ID, 1, EnumEnchantmentType.DIGGER);

    EnchantToken.init();
  }
}
