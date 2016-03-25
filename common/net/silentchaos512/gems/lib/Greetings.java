package net.silentchaos512.gems.lib;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.PlayerHelper;

public class Greetings {

  public static final String PREFIX = "misc.silentgems:alpha";

  public static void greetPlayer(EntityPlayer player) {

    // Reset the random object, because it seems to yield the same value each time. Huh?
    SilentGems.instance.random.setSeed(System.currentTimeMillis());

    List<String> list = SilentGems.instance.localizationHelper.getDescriptionLines(PREFIX);
    if (list.size() > 0) {
      String msg = SilentGems.instance.localizationHelper.getLocalizedString(PREFIX + "Prefix")
          + " ";
      int index = SilentGems.instance.random.nextInt(list.size());
      // SilentGems.instance.logHelper.debug(list.size(), index);
      msg += list.get(index);
      PlayerHelper.addChatMessage(player, TextFormatting.RED + msg);
    }

    // TODO: Remove this later.
    PlayerHelper.addChatMessage(player,
        "[Silent's Gems] Yes, the numbers in the upper-left are my fault. I'm working on it.");
  }
}
