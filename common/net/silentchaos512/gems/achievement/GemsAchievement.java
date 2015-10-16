package net.silentchaos512.gems.achievement;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.EnchantToken;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;

public class GemsAchievement extends Achievement {

  public GemsAchievement(String statName, int x, int y, Block block, Achievement prereq) {

    this(statName, statName, x, y, new ItemStack(block), prereq);
  }

  public GemsAchievement(String statName, int x, int y, Item item, Achievement prereq) {

    this(statName, statName, x, y, new ItemStack(item), prereq);
  }

  public GemsAchievement(String statName, int x, int y, ItemStack stack, Achievement prereq) {

    this(statName, statName, x, y, stack, prereq);
  }

  public GemsAchievement(String statId, String statName, int x, int y, ItemStack stack,
      Achievement prereq) {

    super(statId, statName, x, y, stack, prereq);
    registerStat();
    achievements.add(this);
  }

  public static final ArrayList<Achievement> achievements = new ArrayList<Achievement>();

  public static final Achievement firstTool = new GemsAchievement("FirstTool", 0, 0,
      SRegistry.getItem("PickaxeFlint"), null);
  public static final Achievement redecorated = new GemsAchievement("Redecorated", 2, 0,
      new ItemStack(Blocks.wool, 1, 14), firstTool);

  public static final Achievement mined1K = new GemsAchievement("Mined1K", -2, 0,
      SRegistry.getItem("Pickaxe7"), firstTool);
  public static final Achievement mined10K = new GemsAchievement("Mined10K", -4, 0,
      SRegistry.getItem("Pickaxe7Plus"), mined1K);
  public static final Achievement mined100K = new GemsAchievement("Mined100K", -6, 0,
      SRegistry.getItem("PickaxeChaos"), mined10K);

  public static final Achievement ironTipped = new GemsAchievement("IronTipped", 2, -2,
      ModItems.toolUpgrade.getStack(Names.UPGRADE_IRON_TIP), firstTool);
  public static final Achievement diamondTipped = new GemsAchievement("DiamondTipped", 4, -2,
      ModItems.toolUpgrade.getStack(Names.UPGRADE_DIAMOND_TIP), ironTipped);

  public static final Achievement acquireGems = new GemsAchievement("GetGems", 2, -4,
      new ItemStack(ModBlocks.gemOre, 1, 9), ironTipped);
  public static final Achievement acquireChaos = new GemsAchievement("GetChaos", 4, -4,
      ModBlocks.chaosOre, diamondTipped);

  public static final Achievement torchBandolier = new GemsAchievement("TorchBandolier", 0, -4,
      ModItems.torchBandolier, acquireGems);
  public static final Achievement enchantToken = new GemsAchievement("EnchantToken", 6, -4,
      new ItemStack(ModItems.enchantmentToken, 1, EnchantToken.META_BLANK), acquireChaos);

  public static final Achievement ironPotato = new GemsAchievement("IronPotato", -6, 8,
      CraftingMaterial.getStack(Names.IRON_POTATO), null).setSpecial();

  public static AchievementPage createPage() {

    return new AchievementPage(SilentGems.MOD_NAME,
        achievements.toArray(new Achievement[achievements.size()]));
  }
}
