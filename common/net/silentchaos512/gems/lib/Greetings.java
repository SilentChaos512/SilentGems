package net.silentchaos512.gems.lib;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.PlayerHelper;

public class Greetings {

  public static boolean IS_BETA_BUILD = false;
  public static final String PREFIX = "[Silent's Gems] BETA: ";
  // @formatter:off
  public static final String[] LINES = new String[] {
      // Alpha
//      "Things may change, break, and/or dance to Macarena.",
      "Catgirls not included. D:",
      "The flowers probably won't kill you.",
//      "That texture count...",
      "Try the donuts!",
      "May contain unintended &cR&6a&ei&an&9b&do&5w&fs!",
      "Shake well and refrigerate after opening.",
//      "May contain crazy people.",
//      "Bunny bowling. No regrets."

      // Beta
      "Still dancing to Macarena!",
      "Your free Windows 10 upgrade is ready!",
      "Drowning in JSON files...",
      "Download only from Curse/CurseForge!"
  };
  // @formatter:on

  static List<String> extraMessages = Lists.newArrayList();

  /**
   * Adds messages to the player's chat log. Use addExtraMessage to add messages to the list.
   */
  public static void greetPlayer(EntityPlayer player) {

    if (IS_BETA_BUILD)
      doBetaGreeting(player);

    for (String str : extraMessages)
      PlayerHelper.addChatMessage(player, "[Silent's Gems] " + str);
  }

  /**
   * Random, funny beta greetings. Will be disabled in version 2.1.
   */
  public static void doBetaGreeting(EntityPlayer player) {

    // Reset the random object, because it seems to yield the same value each time. Huh?
    SilentGems.instance.random.setSeed(System.currentTimeMillis());

    String line = LINES[SilentGems.random.nextInt(LINES.length)];
    line = PREFIX + line;
    line = line.replaceAll("&", "\u00a7");
    PlayerHelper.addChatMessage(player, TextFormatting.RED + line);
  }

  /**
   * Add an additional message to display when the player logs in to a world.
   */
  public static void addExtraMessage(String str) {

    extraMessages.add(str);
  }
}
