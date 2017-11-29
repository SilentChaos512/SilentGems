package net.silentchaos512.gems.lib.soul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.item.ItemSoulGem;
import net.silentchaos512.gems.item.ItemToolSoul;
import net.silentchaos512.gems.lib.TooltipHelper;
import net.silentchaos512.gems.network.NetworkHandler;
import net.silentchaos512.gems.network.message.MessageSoulSync;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.ChatHelper;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.StackHelper;

public class ToolSoul {

  public static final float XP_FACTOR_KILLS = 0.5f;
  public static final float XP_FACTOR_TILLING = 4.0f;
  public static final float XP_FACTOR_BLOCK_MINED = 1.0f;
  public static final float XP_FACTOR_ARMOR_DAMAGED = 3.0f;
  public static final int XP_MAX_PER_BLOCK = 20;
  public static final float XP_MIN_BLOCK_HARDNESS = 0.5f;
  public static final int AP_START = 50;
  public static final int AP_PER_LEVEL = 10;
  public static final int AP_REGEN_DELAY = 120;

  static final int BASE_XP = 25;
  static final float XP_CURVE_FACTOR = 2.0f;

  String name = "";
  boolean readyToSave = false;

  // Experience and levels
  int xp = 0;
  int level = 1;

  // Elements
  EnumSoulElement element1, element2 = EnumSoulElement.NONE;

  // Personality
  // TODO

  // Skills
  int actionPoints = AP_START;
  Map<SoulSkill, Integer> skills = new LinkedHashMap<>();

  // Temporary variables NOT saved to NBT
  public int climbTimer = 0;
  public int coffeeCooldown = 0;

  // =================
  // = XP and Levels =
  // =================

  public void addXp(int amount, ItemStack tool, EntityPlayer player) {

    xp += amount;
    SoulSkill skillLearned = null;
    boolean packetSent = false;
    while (xp >= getXpToNextLevel()) {
      skillLearned = levelUp(tool, player);
      Integer skillLevel = getSkillLevel(skillLearned);
      sendUpdatePacket(tool, player, skillLearned, skillLevel);
      packetSent = true;
    }

    if (!packetSent) {
      sendUpdatePacket(tool, player, null, 0);
    }
  }

  public int getXp() {

    return xp;
  }

  /**
   * Directly sets the XP amount. This DOES NOT check for level ups! It is meant to be used only by MessageSoulSync.
   * 
   * @param packetAmount
   *          The amount to assign to xp.
   */
  public void setXp(int packetAmount) {

    this.xp = packetAmount;
  }

  public int getXpToNextLevel() {

    return getXpForLevel(level + 1);
  }

  public int getXpForLevel(int target) {

    return BASE_XP * (int) Math.pow(target, XP_CURVE_FACTOR);
  }

  public int getLevel() {

    return level;
  }

  /**
   * Directly sets the level. This SHOULD NOT be used in most cases! It is meant to be used only by MessageSoulSync.
   * 
   * @param packetAmount
   *          The amount to assign to level.
   */
  public void setLevel(int packetAmount) {

    this.level = packetAmount;
  }

  public String getName(ItemStack tool) {

    if (name.isEmpty()) {
      if (StackHelper.isValid(tool)) {
        return tool.getDisplayName();
      }
      return SilentGems.localizationHelper.getMiscText("ToolSoul.nameless");
    }
    return name;
  }

  public void setName(String value) {

    this.name = value;
  }

  public boolean hasName() {

    return !name.isEmpty();
  }

  protected @Nullable SoulSkill levelUp(ItemStack tool, EntityPlayer player) {

    if (player == null || player.world.isRemote)
      return null;

    ++level;
    String line = String.format(
        SilentGems.localizationHelper.getMiscText("ToolSoul.levelUp", getName(tool), level));
    ChatHelper.sendMessage(player, line);
    player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP,
        SoundCategory.PLAYERS, 1.0f, 1.0f);

    addActionPoints(AP_PER_LEVEL);

    // Learn new skill?
    SoulSkill toLearn = SoulSkill.selectSkillToLearn(this, tool);
    if (toLearn != null) {
      SilentGems.logHelper.debug(player.world.isRemote);
      addOrLevelSkill(toLearn, tool, player);
    }

    ToolHelper.recalculateStats(tool);

    // Save soul to NBT
    SoulManager.setSoul(tool, this, false);
    readyToSave = false;

    return toLearn;
  }

  public int getXpForBlockHarvest(World world, BlockPos pos, IBlockState state) {

    float hardness = state.getBlockHardness(world, pos);
    if (hardness < XP_MIN_BLOCK_HARDNESS) {
      return 0;
    }

    // Bonus XP for ores and wood
    int oreBonus = 0;
    ItemStack blockStack = new ItemStack(state.getBlock(), 1,
        state.getBlock().getMetaFromState(state));
    for (int oreId : OreDictionary.getOreIDs(blockStack)) {
      String oreName = OreDictionary.getOreName(oreId);
      if (oreName.startsWith("ore") || oreName.startsWith("log")) {
        oreBonus = this.level;
        break;
      }
    }

    int clamp = MathHelper.clamp(Math.round(XP_FACTOR_BLOCK_MINED * hardness), 1, XP_MAX_PER_BLOCK);
    return oreBonus + clamp;
  }

  // ======================
  // = Action Points (AP) =
  // ======================

  public int getActionPoints() {

    return actionPoints;
  }

  public void addActionPoints(int amount) {

    actionPoints = MathHelper.clamp(actionPoints + amount, 0, getMaxActionPoints());
  }

  /**
   * Directly sets the amount of AP. This SHOULD NOT be used in most cases! It is meant to be used only by
   * MessageSoulSync.
   * 
   * @param packetAmount
   *          The amount to assign to level.
   */
  public void setActionPoints(int packetAmount) {

    this.actionPoints = packetAmount;
  }

  public int getMaxActionPoints() {

    return AP_START + AP_PER_LEVEL * (level - 1);
  }

  // ============
  // = Elements =
  // ============

  public EnumSoulElement getPrimaryElement() {

    return element1;
  }

  public EnumSoulElement getSecondaryElement() {

    return element2;
  }

  // ==========
  // = Skills =
  // ==========

  public boolean addOrLevelSkill(SoulSkill skill, ItemStack tool, EntityPlayer player) {

    if (skill == null) {
      return false;
    }

    LocalizationHelper loc = SilentGems.localizationHelper;
    if (skills.containsKey(skill)) {
      // Has skill already.
      int level = skills.get(skill);
      if (level < skill.maxLevel) {
        // Can be leveled up.
        ++level;
        skills.put(skill, level);
        if (player != null) {
          ChatHelper.sendMessage(player,
              loc.getMiscText("ToolSoul.skillLearned", skill.getLocalizedName(tool, level)));
        }

        return true;
      } else {
        // Already max level.
        return false;
      }
    } else {
      skills.put(skill, 1);
      if (player != null) {
        ChatHelper.sendMessage(player,
            loc.getMiscText("ToolSoul.skillLearned", skill.getLocalizedName(tool, 1)));
      }

      return true;
    }
  }

  public void setSkillLevel(SoulSkill skill, int skillLevel, ItemStack tool, EntityPlayer player) {

    if (skill == null) {
      return;
    }

    if (skillLevel <= 0) {
      skills.remove(skill);
    }

    skills.put(skill, skillLevel > skill.maxLevel ? skill.maxLevel : skillLevel);
  }

  /**
   * Determine if the soul has learned any level of this skill.
   * 
   * @param skill
   *          The skill to check.
   * @return True if any level has been learned, false otherwise.
   */
  public boolean hasSkill(SoulSkill skill) {

    return skills.containsKey(skill);
  }

  /**
   * Determine the level of the skill learned.
   * 
   * @param skill
   *          The skill to check.
   * @return The level of the skill that has been learned, or 0 if the skill has not been learned.
   */
  public int getSkillLevel(SoulSkill skill) {

    if (!hasSkill(skill))
      return 0;
    return skills.get(skill);
  }

  public void addInformation(ItemStack stack, World world, List<String> list, boolean advanced) {

    LocalizationHelper loc = SilentGems.localizationHelper;

    // Level, XP, AP
    list.add(loc.getMiscText("ToolSoul.level", level, xp, getXpToNextLevel()));
    list.add(loc.getMiscText("ToolSoul.actionPoints", actionPoints, getMaxActionPoints()));

    boolean skillsKeyDown = Keyboard.isKeyDown(Keyboard.KEY_S);

    if (skillsKeyDown || stack.getItem() instanceof ItemToolSoul) {
      // Display elements.
      String e1 = element1 == null ? "None" : element1.getDisplayName();
      String e2 = element2 == null ? "None" : element2.getDisplayName();
      String elements = e1 + (e2.equalsIgnoreCase("none") ? "" : ", " + e2);
      list.add(loc.getMiscText("ToolSoul.elements", elements));
    }
    if (skillsKeyDown) {
      // Display stat modifiers.
      String color = "  " + TextFormatting.YELLOW;
      float durability = getDurabilityModifierForDisplay(this);
      float harvestSpeed = getHarvestSpeedModifierForDisplay(this);
      float meleeDamage = getMeleeDamageModifierForDisplay(this);
      float magicDamage = getMagicDamageModifierForDisplay(this);
      float protection = getProtectionModifierForDisplay(this);
      if (durability != 0f)
        list.add(color + TooltipHelper.getAsColoredPercentage("Durability", durability, 0, true));
      if (harvestSpeed != 0f)
        list.add(
            color + TooltipHelper.getAsColoredPercentage("HarvestSpeed", harvestSpeed, 0, true));
      if (meleeDamage != 0f)
        list.add(color + TooltipHelper.getAsColoredPercentage("MeleeDamage", meleeDamage, 0, true));
      if (magicDamage != 0f)
        list.add(color + TooltipHelper.getAsColoredPercentage("MagicDamage", magicDamage, 0, true));
      if (protection != 0f)
        list.add(color + TooltipHelper.getAsColoredPercentage("Protection", protection, 0, true));

      // Display skills.
      for (Entry<SoulSkill, Integer> entry : skills.entrySet()) {
        SoulSkill skill = entry.getKey();
        int level = entry.getValue();
        list.add("  " + skill.getLocalizedName(stack, level));
      }
    } else {
      list.add(TextFormatting.GOLD + loc.getMiscText("Tooltip.keyForSkills"));
    }
  }

  public void applyToStats(ToolStats stats) {

    // Elemental affinities
    stats.durability *= getDurabilityModifier();
    stats.harvestSpeed *= getHarvestSpeedModifier();
    stats.meleeDamage *= getMeleeDamageModifier();
    stats.magicDamage *= getMagicDamageModifier();
    stats.protection *= getProtectionModifier();

    // Skills
    for (Entry<SoulSkill, Integer> entry : skills.entrySet()) {
      SoulSkill skill = entry.getKey();
      int level = entry.getValue();
      skill.applyToStats(stats, level);
    }
  }

  protected float getDurabilityModifier() {

    return 1f + element1.durabilityModifier + element2.durabilityModifier / 2f;
  }

  protected float getHarvestSpeedModifier() {

    return 1f + element1.harvestSpeedModifier + element2.harvestSpeedModifier / 2f;
  }

  protected float getMeleeDamageModifier() {

    return 1f + element1.meleeDamageModifier + element2.meleeDamageModifier / 2f;
  }

  protected float getMagicDamageModifier() {

    return 1f + element1.magicDamageModifier + element2.magicDamageModifier / 2f;
  }

  protected float getProtectionModifier() {

    return 1f + element1.protectionModifier + element2.protectionModifier / 2f;
  }

  public static float getDurabilityModifierForDisplay(@Nullable ToolSoul soul) {

    if (soul == null)
      return 0f;

    float val = soul.getDurabilityModifier();
    SoulSkill skill = SoulSkill.DURABILITY_BOOST;
    val = getSkillStatModifier(soul, val, skill);

    return val - 1f;
  }

  public static float getHarvestSpeedModifierForDisplay(@Nullable ToolSoul soul) {

    if (soul == null)
      return 0f;

    float val = soul.getHarvestSpeedModifier();
    SoulSkill skill = SoulSkill.HARVEST_SPEED_BOOST;
    val = getSkillStatModifier(soul, val, skill);

    return val - 1f;
  }

  public static float getMeleeDamageModifierForDisplay(@Nullable ToolSoul soul) {

    if (soul == null)
      return 0f;

    float val = soul.getMeleeDamageModifier();
    SoulSkill skill = SoulSkill.MELEE_DAMAGE_BOOST;
    val = getSkillStatModifier(soul, val, skill);

    return val - 1f;
  }

  public static float getMagicDamageModifierForDisplay(@Nullable ToolSoul soul) {

    if (soul == null)
      return 0f;

    float val = soul.getMagicDamageModifier();
    SoulSkill skill = SoulSkill.MAGIC_DAMAGE_BOOST;
    val = getSkillStatModifier(soul, val, skill);

    return val - 1f;
  }

  public static float getProtectionModifierForDisplay(@Nullable ToolSoul soul) {

    if (soul == null)
      return 0f;

    float val = soul.getProtectionModifier();
    SoulSkill skill = SoulSkill.PROTECTION_BOOST;
    val = getSkillStatModifier(soul, val, skill);

    return val - 1f;
  }

  private static float getSkillStatModifier(ToolSoul soul, float val, SoulSkill skill) {

    if (skill != null && soul.skills != null && soul.skills.containsKey(skill)) {
      int lvl = soul.skills.get(skill);
      val += skill.getStatBoostMulti() * lvl;
    }
    return val;
  }

  public static ToolSoul construct(ItemSoulGem.Soul... souls) {

    // Soul weight map
    Map<EnumSoulElement, Integer> elements = new HashMap<>();
    for (ItemSoulGem.Soul soul : souls) {
      int current = elements.containsKey(soul.element1) ? elements.get(soul.element1) : 0;
      elements.put(soul.element1, current + 5);
      if (soul.element2 != EnumSoulElement.NONE) {
        current = elements.containsKey(soul.element2) ? elements.get(soul.element2) : 0;
        elements.put(soul.element2, current + 3);
      }
    }

    // Highest weight becomes element 1, second becomes element 2.
    ToolSoul toolSoul = new ToolSoul();
    // Primary
    toolSoul.element1 = selectHighestWeight(elements);
    elements.remove(toolSoul.element1);
    // Secondary (if any are left)
    if (!elements.isEmpty()) {
      toolSoul.element2 = selectHighestWeight(elements);
    }

    return toolSoul;
  }

  public static ToolSoul randomSoul() {

    ToolSoul soul = new ToolSoul();

    List<EnumSoulElement> elements = new ArrayList<>();
    for (EnumSoulElement elem : EnumSoulElement.values()) {
      if (elem != EnumSoulElement.NONE) {
        elements.add(elem);
      }
    }

    soul.element1 = elements.get(SilentGems.random.nextInt(elements.size()));
    elements.remove(soul.element1);
    elements.add(EnumSoulElement.NONE);
    soul.element2 = elements.get(SilentGems.random.nextInt(elements.size()));

    return soul;
  }

  private static EnumSoulElement selectHighestWeight(Map<EnumSoulElement, Integer> elements) {

    EnumSoulElement element = EnumSoulElement.NONE;
    int highestWeight = 0;

    for (Entry<EnumSoulElement, Integer> entry : elements.entrySet()) {
      EnumSoulElement elementInMap = entry.getKey();
      int weightInMap = entry.getValue();
      if (weightInMap > highestWeight
          || (weightInMap == highestWeight && elementInMap.weight > element.weight)) {
        // This element takes priority over the previous best match.
        element = entry.getKey();
        highestWeight = entry.getValue();
      }
    }

    return element;
  }

  // Variables used by updateTick
  int ticksExisted = 0;
  // Salting update delays by tool soul to give appearance of randomness.
  final int apRegenSalt = SilentGems.random.nextInt(AP_REGEN_DELAY / 2);
  final int skillActivateSalt = SilentGems.random.nextInt(SoulSkill.SKILL_ACTIVATE_DELAY / 2);

  public void updateTick(ItemStack tool, EntityPlayer player) {

    // if (SilentGems.BUILD_NUM == 0) {
    // regenDelay /= 5;
    // }

    boolean sendUpdate = false;

    if (!player.world.isRemote) {
      ++ticksExisted;
      // Regen action points
      int regenDelay = AP_REGEN_DELAY;
      if ((ticksExisted + apRegenSalt) % regenDelay == 0) {
        addActionPoints(1);
        sendUpdate = true;
      }

      if (coffeeCooldown > 0) {
        --coffeeCooldown;
      }
    }

    // Try to activate skills?
    boolean inMainHand = player.getHeldItemMainhand() == tool;
    boolean isInHand = inMainHand || player.getHeldItemOffhand() == tool;
    boolean isArmor = tool.getItem() instanceof IArmor;
    int time = ticksExisted + skillActivateSalt;

    for (SoulSkill skill : skills.keySet()) {
      if (isInHand || isArmor || skill.canActivateWhenUnequipped()) {
        int skillLevel = skills.get(skill);
        if (time % skill.getActivateDelay() == 0) {
          if (skill.activate(this, tool, player, skillLevel)) {
            addActionPoints(-skill.apCost);
            if (!player.world.isRemote && skill.sendChatMessageOnActivation()) {
              ChatHelper.sendMessage(player, SilentGems.localizationHelper.getMiscText(
                  "ToolSoul.activated", getName(tool), skill.getLocalizedName(tool, skillLevel)));
            }
          }
        }
      }
    }

    if (readyToSave && !inMainHand) {
      readyToSave = false;
      SoulManager.setSoul(tool, this, false);
    }

    if (sendUpdate) {
      sendUpdatePacket(tool, player, null, 0);
    }
  }

  public void setReadyToSave(boolean value) {

    this.readyToSave = value;
  }

  public boolean activateSkillsOnBlock(ItemStack tool, EntityPlayer player, World world,
      BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {

    AtomicBoolean activated = new AtomicBoolean(false);
    skills.forEach((skill, level) -> activated.set(activated.get()
        || skill.activateOnBlock(this, tool, player, level, world, pos, facing, hitX, hitY, hitZ)));
    return activated.get();
  }

  public boolean activateSkillsOnEntity(ItemStack tool, EntityPlayer player,
      EntityLivingBase target) {

    AtomicBoolean activated = new AtomicBoolean(false);
    skills.forEach((skill, level) -> activated
        .set(activated.get() || skill.onDamageEntity(this, tool, player, level, target)));
    return activated.get();
  }

  public static ToolSoul readFromNBT(NBTTagCompound tags) {

    ToolSoul soul = new ToolSoul();

    soul.name = tags.getString("name");
    String e1 = tags.getString("element1");
    String e2 = tags.getString("element2");
    for (EnumSoulElement element : EnumSoulElement.values()) {
      if (element.name().equalsIgnoreCase(e1)) {
        soul.element1 = element;
      } else if (element.name().equalsIgnoreCase(e2)) {
        soul.element2 = element;
      }
    }

    soul.xp = tags.getInteger("xp");
    soul.level = tags.getInteger("level");
    soul.actionPoints = tags.getInteger("ap");

    // Load skills
    soul.skills.clear();
    NBTTagList tagList = tags.getTagList("skills", 10);
    for (int i = 0; i < tagList.tagCount(); ++i) {
      NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
      SoulSkill skill = SoulSkill.getById(tagCompound.getString("id"));
      if (skill != null) {
        int level = tagCompound.getShort("level");
        soul.skills.put(skill, level);
      }
      // Skills with unknown IDs are ignored!
    }

    return soul;
  }

  public void writeToNBT(NBTTagCompound tags) {

    if (!name.isEmpty()) {
      tags.setString("name", name);
    }
    tags.setString("element1", this.element1.name());
    tags.setString("element2", this.element2.name());
    tags.setInteger("xp", xp);
    tags.setInteger("level", level);
    tags.setInteger("ap", actionPoints);

    // Save skills
    NBTTagList tagList = new NBTTagList();
    for (Entry<SoulSkill, Integer> entry : skills.entrySet()) {
      SoulSkill skill = entry.getKey();
      int level = entry.getValue();
      NBTTagCompound tagCompound = new NBTTagCompound();
      tagCompound.setString("id", skill.id);
      tagCompound.setShort("level", (short) level);
      tagList.appendTag(tagCompound);
    }
    tags.setTag("skills", tagList);
  }

  @Override
  public String toString() {

    return "ToolSoul{" + "Level: " + level + ", XP: " + xp + ", Elements: {" + element1.name()
        + ", " + element2.name() + "}" + "}";
  }

  protected void sendUpdatePacket(ItemStack tool, EntityPlayer player, SoulSkill skillLearned,
      int skillLevel) {

    // Server side: send update packet to player.
    if (!player.world.isRemote) {
      UUID uuid = ToolHelper.getSoulUUID(tool);
      MessageSoulSync message = new MessageSoulSync(uuid, xp, level, actionPoints, skillLearned,
          skillLevel);
      NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
    }
  }
}
