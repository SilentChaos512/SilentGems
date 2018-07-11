package net.silentchaos512.gems.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.silentchaos512.lib.util.StackHelper;

public class NBTHelper {

  private static void createTagIfNeeded(ItemStack stack) {

    if (StackHelper.isValid(stack) && !stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
  }

  public static boolean hasKey(ItemStack stack, String key) {

    if (StackHelper.isEmpty(stack) || !stack.hasTagCompound()) {
      return false;
    }
    return stack.getTagCompound().hasKey(key);
  }

  public static int getTagInt(ItemStack stack, String key) {

    createTagIfNeeded(stack);
    return stack.getTagCompound().getInteger(key);
  }

  public static void setTagInt(ItemStack stack, String key, int value) {

    createTagIfNeeded(stack);
    stack.getTagCompound().setInteger(key, value);
  }

  public static boolean getTagBoolean(ItemStack stack, String key) {

    createTagIfNeeded(stack);
    return stack.getTagCompound().getBoolean(key);
  }

  public static void setTagBoolean(ItemStack stack, String key, boolean value) {

    createTagIfNeeded(stack);
    stack.getTagCompound().setBoolean(key, value);
  }

  public static String getTagString(ItemStack stack, String key) {

    createTagIfNeeded(stack);
    return stack.getTagCompound().getString(key);
  }

  public static void setTagString(ItemStack stack, String key, String value) {

    createTagIfNeeded(stack);
    stack.getTagCompound().setString(key, value);
  }
}
