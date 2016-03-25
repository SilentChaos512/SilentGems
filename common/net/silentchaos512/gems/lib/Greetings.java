package net.silentchaos512.gems.lib;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;

public class Greetings {

  public static final String PREFIX = "misc.silentgems:alpha";

  public static void greetPlayer(EntityPlayer player) {

    // Reset the random object, because it seems to yield the same value each time. Huh?
    SilentGems.instance.random.setSeed(System.currentTimeMillis());

    List<String> list = SilentGems.instance.localizationHelper.getDescriptionLines(PREFIX);
    String msg = SilentGems.instance.localizationHelper.getLocalizedString(PREFIX + "Prefix") + " ";
    int index = SilentGems.instance.random.nextInt(list.size());
//    SilentGems.instance.logHelper.debug(list.size(), index);
    msg += list.get(index);
    player.addChatMessage(new TextComponentString(TextFormatting.RED + msg));
  }
}
