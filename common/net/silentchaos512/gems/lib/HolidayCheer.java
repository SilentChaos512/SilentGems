package net.silentchaos512.gems.lib;

import java.util.Calendar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.item.ModItems;

public class HolidayCheer {

  public static final String[] HOLIDAYS = { "Present", "Candy", "Reindeer", "Elf" };

  public static final int CANDY_TRY_DELAY = 600 * 20; // Check/try things every 10 minutes.
  public static final float CANDY_RATE = 0.15f;
  public static final int CANDY_MAX_QUANTITY = 3;

  public static Calendar today;
  private static boolean rightDay = checkDateAgain();

  public static boolean isRightDay() {

    return rightDay;
  }

  public static boolean checkDateAgain() {

    today = Calendar.getInstance();
    rightDay = today.get(Calendar.MONTH) == Calendar.DECEMBER && today.get(Calendar.DATE) == 25;
    return rightDay;
  }

  public static void greetPlayer(EntityPlayer player) {

    checkDateAgain();
    if (isRightDay() && !player.worldObj.isRemote) {
      String str = "[%s] Happy %s Day! Have some candy.";
      String day = HOLIDAYS[SilentGems.instance.random.nextInt(HOLIDAYS.length)];
      str = String.format(str, SilentGems.MOD_NAME, day);

      PlayerHelper.addChatMessage(player, EnumChatFormatting.GREEN + str);
      ItemStack stack = ModItems.food.getStack(Names.CANDY_CANE, 1);
      PlayerHelper.addItemToInventoryOrDrop(player, stack);
    }
  }

  public static void tryGiveCandy(EntityPlayer player) {

    boolean rightDelay = player.worldObj.getTotalWorldTime() % CANDY_TRY_DELAY == 0;
    if (rightDelay) {
      checkDateAgain();
    }

    if (isRightDay() && !player.worldObj.isRemote && rightDelay) {
      if (SilentGems.instance.random.nextFloat() <= CANDY_RATE) {
        int count = SilentGems.instance.random.nextInt(CANDY_MAX_QUANTITY);
        ItemStack stack = ModItems.food.getStack(Names.CANDY_CANE, count);
        PlayerHelper.addItemToInventoryOrDrop(player, stack);
      }
    }
  }
}
