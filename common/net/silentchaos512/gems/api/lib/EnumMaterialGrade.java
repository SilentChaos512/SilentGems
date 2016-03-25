package net.silentchaos512.gems.api.lib;

import net.minecraft.item.ItemStack;

public enum EnumMaterialGrade {

  NONE, E, D, C, B, A, S, SS, SSS;

  public static final String NBT_KEY = "grade";

  public static EnumMaterialGrade fromStack(ItemStack stack) {

    if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey(NBT_KEY)) {
      String str = stack.getTagCompound().getString(NBT_KEY);
      return fromString(str);
    }
    return EnumMaterialGrade.NONE;
  }

  public static EnumMaterialGrade fromString(String str) {

    if (str != null) {
      for (EnumMaterialGrade grade : values()) {
        if (grade.name().equals(str)) {
          return grade;
        }
      }
    }
    return EnumMaterialGrade.NONE;
  }
}
