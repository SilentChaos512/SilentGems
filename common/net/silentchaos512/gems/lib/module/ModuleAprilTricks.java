package net.silentchaos512.gems.lib.module;

import java.util.Calendar;

import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.config.GemsConfig;

public class ModuleAprilTricks {

  public static ModuleAprilTricks instance = new ModuleAprilTricks();

  public Calendar today;
  private boolean rightDay = checkDate();
  public boolean moduleEnabled = true;

  private boolean checkDate() {

    today = Calendar.getInstance();
    int year = today.get(Calendar.YEAR);
    int month = today.get(Calendar.MONTH);
    int date = today.get(Calendar.DATE);

    rightDay = month == Calendar.APRIL &&     // April...
        (date == 1                            // April Fools Day
            || (date == 11 && year == 2017)); // Yooka-Laylee release date
    return rightDay;
  }

  public boolean isRightDay() {

    return rightDay;
  }

  public void loadConfig(Configuration c) {

    moduleEnabled = c.getBoolean("Enable April Trickery", GemsConfig.CAT_MISC, moduleEnabled,
        "May cause silly things to happen on certain day(s) in April.");
  }
}
