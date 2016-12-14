package net.silentchaos512.gems.lib.module;

import java.util.Calendar;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.lib.util.PlayerHelper;

public class ModuleHolidayCheer {

  // Candy configs
  public static final int CANDY_TRY_DELAY = 10 * 60 * 20;
  public static final float CANDY_RATE = 0.125f;
  public static final int CANDY_MAX_QUANTITY = 3;

  // Date range
  public static final int MONTH = Calendar.DECEMBER;
  public static final int DAY_MIN = 20;
  public static final int DAY_MAX = 27;

  public static ModuleHolidayCheer instance = new ModuleHolidayCheer();

  public Calendar today;
  private boolean rightDay = checkDateAgain();
  public boolean moduleEnabled = true;

  @SubscribeEvent
  public void onPlayerJoin(PlayerLoggedInEvent event) {

    if (moduleEnabled)
      greetPlayer(event.player);
  }

  @SubscribeEvent
  public void onPlayerTick(TickEvent.PlayerTickEvent event) {

    if (moduleEnabled && event.player.worldObj.getTotalWorldTime() % CANDY_TRY_DELAY == 0) {
      checkDateAgain();
      if (isRightDay() && !event.player.worldObj.isRemote)
        tryGiveCandy(event.player);
    }
  }

  public boolean isRightDay() {

    return rightDay;
  }

  public boolean checkDateAgain() {

    today = Calendar.getInstance();
    int month = today.get(Calendar.MONTH);
    int day = today.get(Calendar.DATE);
    rightDay = month == MONTH && day >= DAY_MIN && day < DAY_MAX;
    return rightDay;
  }

  public void greetPlayer(EntityPlayer player) {

    checkDateAgain();
    if (isRightDay() && !player.worldObj.isRemote) {
      String str = "[%s] Happy Holidays! Have some candy.";
      str = String.format(str, SilentGems.MOD_NAME/* , day */);

      PlayerHelper.addChatMessage(player, TextFormatting.GREEN + str);
      ItemStack stack = ModItems.food.candyCane.copy();
      PlayerHelper.giveItem(player, stack);
    }
  }

  public void tryGiveCandy(EntityPlayer player) {

    if (SilentGems.random.nextFloat() <= CANDY_RATE) {
      int count = SilentGems.random.nextInt(CANDY_MAX_QUANTITY);
      ItemStack stack = ModItems.food.candyCane.copy();
      stack.stackSize = count;
      PlayerHelper.giveItem(player, stack);
    }
  }

  public void loadConfig(Configuration c) {

    moduleEnabled = c.getBoolean("Enable Holiday Cheer", GemsConfig.CAT_MISC, moduleEnabled,
        "Players will occasionally receive candy during a certain time of the year.");
  }
}
