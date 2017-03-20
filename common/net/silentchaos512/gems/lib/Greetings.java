package net.silentchaos512.gems.lib;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class Greetings {

  public static boolean IS_BETA_BUILD = true;
  public static final String PREFIX = "[Silent's Gems] BETA: ";
  // @formatter:off
  public static final String[] LINES = new String[] {
      // Alpha
//      "Things may change, break, and/or dance to Macarena.",
//      "Catgirls not included. D:",
      "The flowers probably won't kill you.",
//      "That texture count...",
      "Try the donuts!",
      "May contain unintended &cR&6a&ei&an&9b&do&5w&fs!",
      "Shake well and refrigerate after opening.",
//      "May contain crazy people.",
//      "Bunny bowling. No regrets."

      // Beta
//      "Still dancing to Macarena!",
//      "Your free Windows 10 upgrade is ready!",
      "Drowning in [slightly fewer] JSON files...",
      "Download only from CurseForge!",

      // XCompat
      "Rabbit poop coffee!",
      "Now works on Minecraft 1.10.2! Because other modders are SOOO slow to update...",
      "It stares into your soul.",
      "Pot now included... flower pot, that is.",
      "Did you know Chaos Gems are finally back?",
      "Also try Extra Parts!",
      "Your wish has been granted!",
  };
  // @formatter:on

  static List<String> extraMessages = Lists.newArrayList();

  // For the strawpoll, so we don't spam players too much.
  static Set<String> strawpollNotifiedPlayers = Sets.newHashSet();

  /**
   * Adds messages to the player's chat log. Use addExtraMessage to add messages to the list.
   */
  public static void greetPlayer(EntityPlayer player) {

    if (IS_BETA_BUILD)
      doBetaGreeting(player);

    // Strawpoll notification (will stop displaying at the end of the year)
    Calendar cal = Calendar.getInstance();
    boolean strawpollNotExpired = cal.get(Calendar.YEAR) == 2016 && cal.get(Calendar.MONTH) == Calendar.DECEMBER;
    if (strawpollNotExpired && !strawpollNotifiedPlayers.contains(player.getName())) {
      player.sendMessage(ForgeHooks.newChatWithLinks(TextFormatting.DARK_PURPLE
          + "[Silent's Gems]" + TextFormatting.RESET + " Like the current textures? Or would you"
          + " prefer 16x16 textures like vanilla? I'd like to hear your opinions:"
          + " http://www.strawpoll.me/11872834"));
      strawpollNotifiedPlayers.add(player.getName());
    }

    for (String str : extraMessages)
      ChatHelper.sendMessage(player, "[Silent's Gems] " + str);
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
    ChatHelper.sendMessage(player, TextFormatting.RED + line);
  }

  /**
   * Add an additional message to display when the player logs in to a world.
   */
  public static void addExtraMessage(String str) {

    extraMessages.add(str);
  }
}
