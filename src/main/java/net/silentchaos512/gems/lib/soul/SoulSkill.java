package net.silentchaos512.gems.lib.soul;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.lib.EnumToolType;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.ItemHelper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static net.silentchaos512.gems.lib.soul.EnumSoulElement.*;

public class SoulSkill {

  public static final int SKILL_ACTIVATE_DELAY = 200;

  static Map<String, SoulSkill> SKILL_LIST = new LinkedHashMap<>();
  static final float STAT_BOOST_MULTI = 0.05f;

  // public static SoulSkill SUPER_SKILL;
  // Stat boosters
  public static SoulSkill DURABILITY_BOOST;
  public static SoulSkill HARVEST_SPEED_BOOST;
  public static SoulSkill MELEE_DAMAGE_BOOST;
  public static SoulSkill MAGIC_DAMAGE_BOOST;
  public static SoulSkill PROTECTION_BOOST;
  // Active
  public static SoulSkill WARM;
  public static SoulSkill CHILL;
  // Attack
  public static SoulSkill TAG;
  // Passive
  public static SoulSkill HEAD_BONUS;
  // public static SoulSkill ANTIVENOM;
  // public static SoulSkill SLOW_FALL;
  public static SoulSkill CROP_GROWTH;
  // public static SoulSkill WALL_CLIMB;
  // public static SoulSkill COFFEE_POT;
  public static SoulSkill MENDING;
  public static SoulSkill AQUATIC;
  public static SoulSkill AERIAL;

  public static void init() {

    //@formatter:off
//    SUPER_SKILL = new SoulSkill("super_skill", 1, 0, 5, 0.0);
    DURABILITY_BOOST = new SoulSkill("durability_boost", 10, 0, 0, 0.0, EARTH, METAL);
    HARVEST_SPEED_BOOST = new SoulSkill("harvest_speed_boost", 10, 0, 0, 0.0, WIND, LIGHTNING)
        .setFavorsType(EnumToolType.HARVEST);
    MELEE_DAMAGE_BOOST = new SoulSkill("melee_damage_boost", 10, 0, 0, 0.0, FIRE, VENOM)
        .setFavorsType(EnumToolType.SWORD);
    MAGIC_DAMAGE_BOOST = new SoulSkill("magic_damage_boost", 10, 0, 0, 0.0, WATER, ICE)
        .setFavorsType(EnumToolType.SWORD);
    PROTECTION_BOOST = new SoulSkill("protection_boost", 10, 0, 0, 0.0, METAL)
        .setFavorsType(EnumToolType.ARMOR);
    WARM = new SoulSkill("warm", 3, 40, 3, -6.0, FIRE, METAL)
        .setFavorWeightMulti(0.5);
    CHILL = new SoulSkill("chill", 3, 40, 3, -6.0, WATER, ICE)
        .setFavorWeightMulti(0.5);
    TAG = new SoulSkill("tag", 4, 1, 5, -4.0f, VENOM, ALIEN)
        .setFavorWeightMulti(0.75).setFavorsType(EnumToolType.BOW);
    HEAD_BONUS = new SoulSkill("head_bonus", 5, 0, 0, -5.0f, MONSTER, ALIEN, METAL)
        .setFavorWeightMulti(0.75);
//    ANTIVENOM = new SoulSkill("antivenom", 1, 5, 4, -5.0, VENOM, FLORA)
//        .setFavorWeightMulti(0.5f);
//    SLOW_FALL = new SoulSkill("slow_fall", 1, 2, 10, -5.0, WIND, ALIEN)
//        .setActivateDelay(1)
//        .setFavorWeightMulti(0.25f)
//        .lockToFavoredElements();
    CROP_GROWTH = new SoulSkill("crop_growth", 4, 5, 4, -6.0, FLORA)
        .setActivateDelay(300)
        .setFavorWeightMulti(0.75)
        .lockToFavoredElements();
//    WALL_CLIMB = new SoulSkill("wall_climb", 1, 5, 6, -7.0, METAL, MONSTER)
//        .setFavorWeightMulti(0.25f)
//        .lockToFavoredElements();
//    COFFEE_POT = new SoulSkill("coffee_pot", 1, 20, 13, -8.0, FLORA, EARTH)
//        .setFavorWeightMulti(0.25f);
    MENDING = new SoulSkill("mending", 5, 5, 11, -2.0f, FLORA, ALIEN)
        .setFavorWeightMulti(0.75);
    AQUATIC = new SoulSkill("aquatic", 5, 5, 9, -3.0f, WATER);
    AERIAL = new SoulSkill("aerial", 5, 5, 9, -3.0f, WIND);
    //@formatter:on
  }

  /** Unique String ID */
  public final String id;
  /** The maximum level the skill can level up to. */
  public final int maxLevel;
  /** The number of action points required to activate this skill. */
  public final int apCost;
  /** The level at which this skill is most likely to be learned (0 means no preference) */
  public final int medianXpLevel;
  /** Additional weight applied when selecting a skill to learn. */
  public final double weightDiff;
  /**
   * If not empty, souls with matching elements are more likely to learn this skill. Souls without one of the favored
   * elements are less likely to learn the skill.
   */
  public final EnumSoulElement[] favoredElements;

  /** The number of ticks that must pass before activation is tried again. */
  protected int activateDelay = SKILL_ACTIVATE_DELAY;
  /** Weight is multiplied by this when selecting a level-up skill. Set less than 1 to make the skill rarer. */
  protected double favorWeightMulti = 1.0;
  protected EnumToolType favoredType = EnumToolType.NONE;
  /** If true, only souls with one of the favored elements can learn this skill. */
  protected boolean lockedToFavoredElements = false;

  public SoulSkill(String id, int maxLevel, int apCost, int medianXpLevel, double weightDiff,
      EnumSoulElement... favoredElements) {

    this.id = id;
    this.maxLevel = maxLevel;
    this.apCost = apCost;
    this.medianXpLevel = medianXpLevel;
    this.weightDiff = weightDiff;
    this.favoredElements = favoredElements;

    // Randomize activate delays a bit.
    this.activateDelay = 160 + SilentGems.random.nextInt(81);

    SKILL_LIST.put(id, this);
  }

  public boolean activate(ToolSoul soul, ItemStack tool, EntityPlayer player, int level) {

    if (!shouldActivateOnClient() && player.world.isRemote) {
      return false;
    }

    if (soul.getActionPoints() < this.apCost) {
      return false;
    }

    Random random = SilentGems.random;

    // Warm TODO
    if (this == WARM) {
    }
    // Chill TODO
    else if (this == CHILL) {
    }
    // Nature's Bounty
    else if (this == CROP_GROWTH) {
      int startX = (int) player.posX - 6;
      int startY = (int) player.posY - 1;
      int startZ = (int) player.posZ - 6;
      int endX = startX + 13;
      int endY = startY + 2;
      int endZ = startZ + 13;
      float chance = 0.1125f * level; // max 0.45
      boolean ret = false;
      for (int y = startY; y <= endY; ++y) {
        for (int x = startX; x <= endX; ++x) {
          for (int z = startZ; z <= endZ; ++z) {
            BlockPos pos = new BlockPos(x, y, z);
            IBlockState state = player.world.getBlockState(pos);
            if (state.getBlock() instanceof BlockCrops && random.nextFloat() < chance) {
              IGrowable growable = (IGrowable) state.getBlock();
              if (growable.canGrow(player.world, pos, state, player.world.isRemote)) {
                growable.grow(player.world, random, pos, state);
                player.world.playEvent(2005, pos, 0);
                ret = true;
              }
            }
          }
        }
      }
      return ret;
    }
    // // Coffee Pot
    // else if (this == COFFEE_POT) {
    // if (soul.coffeeCooldown <= 0 && player.world.getWorldTime() < 2400) {
    // soul.coffeeCooldown = 12000;
    // PlayerHelper.giveItem(player, ModItems.food.coffeeCup);
    // ChatHelper.sendMessage(player, SilentGems.localizationHelper.getLocalizedString("skill",
    // "coffee_pot.act", soul.getName(tool)));
    // player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BREWING_STAND_BREW,
    // SoundCategory.PLAYERS, 0.9f, 1.5f);
    // return true;
    // }
    // }
    // Mending
    else if (this == MENDING && soul.actionPoints > soul.getMaxActionPoints() / 2) {
      if (tool.getItemDamage() == 0)
        return false;
      if (random.nextFloat() < 0.2f) {
        int amount = 2 * (level + 1) + random.nextInt(4);
//        player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_ANVIL_USE,
//            SoundCategory.BLOCKS, 0.5f, 2.0f + (float) (0.2 * random.nextGaussian()));
        ItemHelper.attemptDamageItem(tool, -amount, random, player);
        return true;
      }
    }

    return false;
  }

  static final int WARM_CHILL_ACT_COST = 10;

  public boolean activateOnBlock(ToolSoul soul, ItemStack tool, EntityPlayer player, int level,
      World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (this == WARM && soul.actionPoints >= WARM_CHILL_ACT_COST) {
      BlockPos blockpos = pos.offset(facing);
      // Can place fire?
      if (world.isAirBlock(blockpos) && Blocks.FIRE.canPlaceBlockAt(world, blockpos)) {
        // Don't modify the world on client-side.
        if (world.isRemote) {
          return true;
        }

        float chance = 0.25f + 0.125f * (level - 1);
        // Should proc?
        if (SilentGems.random.nextFloat() < chance) {
          // world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
          // Using flint and steel should allow compatibility with other mods, maybe?
          ItemHelper.useItemAsPlayer(new ItemStack(Items.FLINT_AND_STEEL), player, world, pos,
              facing, hitX, hitY, hitZ);
          soul.addActionPoints(-WARM_CHILL_ACT_COST);
        }
        // Damage tool and play sound regardless.
        ToolHelper.attemptDamageTool(tool, 1, player);
        world.playSound(null, player.posX, player.posY, player.posZ,
            SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0f,
            SilentGems.random.nextFloat() * 0.4F + 0.8F);
        return true;
      }
    } else if (this == CHILL && soul.actionPoints >= apCost / 2) {
      BlockPos blockpos = pos.offset(facing);
      // Can freeze water?
      if (world.getBlockState(blockpos) == Blocks.WATER.getDefaultState()) {
        // Don't modify the world on client-side.
        if (world.isRemote) {
          return true;
        }

        float chance = 0.25f + 0.125f * (level - 1);
        // Should proc?
        if (SilentGems.random.nextFloat() < chance) {
          world.setBlockState(blockpos, Blocks.ICE.getDefaultState());
          soul.addActionPoints(-WARM_CHILL_ACT_COST);
        }
        // Damage tool and play sound regardless.
        ToolHelper.attemptDamageTool(tool, 1, player);
        world.playSound(null, player.posX, player.posY, player.posZ,
            SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0f,
            SilentGems.random.nextFloat() * 0.4F + 0.8F);
      }

      return true;
    }

    return false;
  }

  public boolean onDamageEntity(ToolSoul soul, ItemStack tool, EntityPlayer player, int level,
      EntityLivingBase target) {

    if (soul.actionPoints < this.apCost)
      return false;

    boolean flag = false;

    if (this == TAG) {
      flag = true;
      int duration = 50 * (level + 1);
      target.addPotionEffect(new PotionEffect(MobEffects.GLOWING, duration, 0, true, false));
    }

    if (flag)
      soul.addActionPoints(-apCost);

    return flag;
  }

  public void applyToStats(ToolStats stats, int level) {

    if (this == DURABILITY_BOOST) {
      stats.durability *= 1f + (getStatBoostMulti() * level);
    } else if (this == HARVEST_SPEED_BOOST) {
      stats.harvestSpeed *= 1f + (getStatBoostMulti() * level);
    } else if (this == MELEE_DAMAGE_BOOST) {
      stats.meleeDamage *= 1f + (getStatBoostMulti() * level);
    } else if (this == MAGIC_DAMAGE_BOOST) {
      stats.magicDamage *= 1f + (getStatBoostMulti() * level);
    } else if (this == PROTECTION_BOOST) {
      stats.protection *= 1f + (getStatBoostMulti() * level);
    }
  }

  public float getStatBoostMulti() {

    if (this == PROTECTION_BOOST)
      return STAT_BOOST_MULTI / 2f;
    return STAT_BOOST_MULTI;
  }

  public String getLocalizedName(ItemStack tool, int level) {

    // if (this == SUPER_SKILL) {
    // ToolSkill superSkill = ToolHelper.getSuperSkill(tool);
    // if (superSkill != null) {
    // return superSkill.getTranslatedName();
    // }
    // }

    String strName = SilentGems.localizationHelper.getLocalizedString("skill", id + ".name");
    if (maxLevel == 1 || level == 0) {
      return strName;
    }
    String strNum = level > 10 ? Integer.toString(level)
        : SilentGems.localizationHelper.getLocalizedString("enchantment.level." + level);
    return strName + " " + strNum;
  }

  public boolean canLearn(ToolSoul soul, ItemStack tool) {

    // if (this == SUPER_SKILL && soul.level != 5) {
    // return false;
    // }

    // Is the skill locked to favored elements?
    if (lockedToFavoredElements) {
      boolean foundMatch = false;
      for (EnumSoulElement elem : favoredElements) {
        if (elem == soul.element1 || elem == soul.element2) {
          foundMatch = true;
          break;
        }
      }

      if (!foundMatch) {
        return false;
      }
    }

    if (soul.skills.containsKey(this)) {
      int currentLevel = soul.skills.get(this);
      return currentLevel < maxLevel;
    }
    return true;
  }

  public int getActivateDelay() {

    return activateDelay;
  }

  public SoulSkill setActivateDelay(int value) {

    this.activateDelay = value;
    return this;
  }

  public SoulSkill lockToFavoredElements() {

    this.lockedToFavoredElements = true;
    return this;
  }

  public boolean isLockedToFavoredElements() {

    return this.lockedToFavoredElements;
  }

  public double getFavorWeightMulti() {

    return this.favorWeightMulti;
  }

  public SoulSkill setFavorWeightMulti(double value) {

    this.favorWeightMulti = value;
    return this;
  }

  public SoulSkill setFavorsType(EnumToolType type) {

    this.favoredType = type;
    return this;
  }

  public boolean sendChatMessageOnActivation() {

    return this != CROP_GROWTH && this != MENDING /* && this != COFFEE_POT */;
  }

  public boolean canActivateWhenUnequipped() {

    return this == MENDING;
    // return this == COFFEE_POT;
  }

  public boolean shouldActivateOnClient() {

    return false;
    // return this == ANTIVENOM || this == SLOW_FALL;
  }

  public static @Nullable SoulSkill getById(String id) {

    return SKILL_LIST.get(id);
  }

  public static SoulSkill selectSkillToLearn(ToolSoul soul, ItemStack tool) {

    // if (soul.getLevel() == 5) {
    // return SUPER_SKILL;
    // }

    Map<SoulSkill, Double> candidates = new LinkedHashMap<>();

    // Create list of candidates
    for (SoulSkill skill : SKILL_LIST.values()) {
      if (skill.canLearn(soul, tool)) {
        boolean favorsElement = false;
        // Select a weight based on favored elements.
        double weight = skill.favoredElements.length < 1 ? 20 : 7;
        for (EnumSoulElement elem : skill.favoredElements) {
          if (elem == soul.getPrimaryElement()) {
            weight = 20;
            favorsElement = true;
            break;
          } else if (elem == soul.getSecondaryElement()) {
            weight = 15;
            favorsElement = true;
            break;
          }
        }

        // Favors certain tool types?
        if (skill.favoredType != EnumToolType.NONE) {
          EnumToolType toolType = tool.getItem() instanceof ITool
              ? ((ITool) tool.getItem()).getToolType()
              : tool.getItem() instanceof IArmor ? ((IArmor) tool.getItem()).getToolType()
                  : EnumToolType.NONE;
          if (toolType == skill.favoredType) {
            weight += 5;
          }
        }

        // If skill has a median level, apply that to the weight.
        if (skill.medianXpLevel > 0) {
          int diff = Math.abs(soul.level - skill.medianXpLevel);
          if (diff > 6) {
            diff = 6;
          }
          weight -= 0.75 * diff;
        }

        // If a lower level of the skill is already known, reduce the weight.
        if (soul.skills.containsKey(skill)) {
          weight -= 2.5 * soul.skills.get(skill);
        }

        // Base weight diff, favors multiplier
        weight += skill.weightDiff;
        if (favorsElement) {
          weight *= skill.favorWeightMulti;
        }

        // Make sure weight is at least 1.
        if (weight < 1) {
          weight = 1;
        }

        candidates.put(skill, weight);
      }
    }

    // Seed based on soul elements, level, and tool UUID.
    Random rand = new Random(
        soul.getPrimaryElement().ordinal() + (soul.getSecondaryElement().ordinal() << 4)
            + (soul.getLevel() << 8) + (ToolHelper.getUUID(tool).getLeastSignificantBits() << 16));

    // Weighted random selection.
    SoulSkill selected = null;
    double bestValue = Double.MIN_VALUE;

    for (SoulSkill skill : candidates.keySet()) {
      double value = -Math.log(rand.nextFloat() / candidates.get(skill));
      SilentGems.logHelper.debug(skill.id, candidates.get(skill), value, bestValue);

      if (value > bestValue) {
        bestValue = value;
        selected = skill;
      }
    }

    return selected;
  }

  public static Collection<SoulSkill> getSkillList() {
    return SKILL_LIST.values();
  }
}
