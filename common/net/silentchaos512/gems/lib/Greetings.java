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

  public static boolean IS_BETA_BUILD = false;
  public static final String PREFIX = "[Silent's Gems] BETA: ";
  // @formatter:off
  public static final String[] LINES = new String[] {
      "Try the new Tool Soul item! Also works on armor, in spite of the name... Check the guide for info. Any feedback is appreciated."
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

    // Strawpoll notification (new difficulty poll)
    Calendar cal = Calendar.getInstance();
    boolean strawpollNotExpired = cal.get(Calendar.YEAR) == 2017 && cal.get(Calendar.MONTH) <= Calendar.OCTOBER;
    if (strawpollNotExpired && !strawpollNotifiedPlayers.contains(player.getName())) {
      player.sendMessage(ForgeHooks.newChatWithLinks(TextFormatting.DARK_PURPLE
          + "[Silent's Gems]" + TextFormatting.RESET
          + " I'm looking for some feedback on difficulty. Your vote would be greatly appreciated!"
          + " http://www.strawpoll.me/13966373"));
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
