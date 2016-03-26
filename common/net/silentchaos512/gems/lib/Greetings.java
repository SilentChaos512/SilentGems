package net.silentchaos512.gems.lib;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.PlayerHelper;

public class Greetings {

  public static final String PREFIX = "[Silent's Gems] EARLY ALPHA: ";
  // @formatter:off
  public static final String[] LINES = new String[] {
      "Things may change, break, and/or dance to Macarena.",
      "Catgirls not included. D:",
      "The flowers probably won't kill you.",
      "That texture count...",
      "Try the donuts!",
      "May contain unintended &cR&6a&ei&an&9b&do&5w&fs!",
      "Shake well and refrigerate after opening.",
      "May contain crazy people.",
      "Bunny bowling. No regrets."
  };
  // @formatter:on

  public static void greetPlayer(EntityPlayer player) {

    // Reset the random object, because it seems to yield the same value each time. Huh?
    SilentGems.instance.random.setSeed(System.currentTimeMillis());

    String line = LINES[SilentGems.instance.random.nextInt(LINES.length)];
    line = PREFIX + line;
    line = line.replaceAll("&", "\u00a7");
    PlayerHelper.addChatMessage(player, TextFormatting.RED + line);
  }
}
