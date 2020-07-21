package net.silentchaos512.gems.lib.soul;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.GearSoulItem;
import net.silentchaos512.gems.util.SoulManager;

import javax.annotation.Nullable;
import java.util.*;

public class GearSoul {
    public static final float XP_FACTOR_KILLS = 0.35f;
    public static final float XP_FACTOR_TILLING = 4.0f;
    public static final float XP_FACTOR_BLOCK_MINED = 1.0f;
    public static final float XP_FACTOR_ARMOR_DAMAGED = 3.0f;
    public static final int XP_MAX_PER_BLOCK = 20;
    public static final float XP_MIN_BLOCK_HARDNESS = 0.5f;

    private static final int BASE_XP = 30;
    private static final float XP_CURVE_FACTOR = 3.0f;

    String name = "";
    boolean readyToSave = false;

    // Experience and levels
    private int xp = 0;
    private int level = 1;

    // Elements
    private SoulElement primaryElement = SoulElement.NONE;
    private SoulElement secondaryElement = SoulElement.NONE;

    // Skills
    final Map<SoulTraits, Integer> skills = new HashMap<>();

    // Temporary variables NOT saved to NBT
    public int climbTimer = 0;
    public int coffeeCooldown = 0;
    // Variables used by updateTick
    private int ticksExisted = 0;

    //region Getters and Setters

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public SoulElement getPrimaryElement() {
        return primaryElement;
    }

    public SoulElement getSecondaryElement() {
        return secondaryElement;
    }

    public Map<SoulTraits, Integer> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public ITextComponent getName(ItemStack tool) {
        if (name.isEmpty()) {
            if (!tool.isEmpty()) {
                return tool.getDisplayName();
            }
        }
        return new StringTextComponent(name);
    }

    public void setName(String value) {
        this.name = value;
    }

    //endregion

    //region XP and Levels

    public void addXp(int amount, ItemStack tool, @Nullable PlayerEntity player) {
        if (!GemsConfig.COMMON.gearSoulsGetXpFromFakePlayers.get() && player instanceof FakePlayer) {
            return;
        }

        xp += amount;
        boolean packetSent = false;
        while (xp >= getXpToNextLevel()) {
            SoulTraits skillLearned = levelUp(tool, player);
            if (skillLearned != null) {
                Integer skillLevel = getSkillLevel(skillLearned);
                if (player != null) {
                    sendUpdatePacket(tool, player, skillLearned, skillLevel);
                    packetSent = true;
                }
            }
        }

        if (!packetSent && player != null) {
            sendUpdatePacket(tool, player, null, 0);
        }
    }

    /**
     * Directly sets the XP amount. This DOES NOT check for level ups! It is meant to be used only
     * by MessageSoulSync.
     *
     * @param packetAmount The amount to assign to xp.
     */
    public void setXp(int packetAmount) {
        this.xp = packetAmount;
    }

    public int getXpToNextLevel() {
        return getXpForLevel(level + 1);
    }

    public static int getXpForLevel(int target) {
        return BASE_XP * (int) Math.pow(target, XP_CURVE_FACTOR);
    }

    /**
     * Directly sets the level. This SHOULD NOT be used in most cases! It is meant to be used only
     * by MessageSoulSync.
     *
     * @param packetAmount The amount to assign to level.
     */
    public void setLevel(int packetAmount) {
        this.level = packetAmount;
    }

    public int getXpForBlockHarvest(IBlockReader world, BlockPos pos, BlockState state) {
        float hardness = state.getBlockHardness(world, pos);
        if (hardness < XP_MIN_BLOCK_HARDNESS) {
            return 0;
        }

        // Bonus XP for ores and logs
        int oreBonus = 0;
        Block block = state.getBlock();
        if (BlockTags.LOGS.contains(block) || Tags.Blocks.ORES.contains(block)) {
            oreBonus = this.level / 2;
        }

        // Wood gives less XP.
        if (state.getMaterial() == Material.WOOD) {
            hardness /= 2;
        }

        int clamp = MathHelper.clamp(Math.round(XP_FACTOR_BLOCK_MINED * hardness), 1, XP_MAX_PER_BLOCK);
        return oreBonus + clamp;
    }

    @Nullable
    private SoulTraits levelUp(ItemStack tool, @Nullable PlayerEntity player) {
        if (player != null && player.world.isRemote) return null;

        ++level;
        if (player != null) {
            player.sendMessage(new TranslationTextComponent("misc.silentgems.gear_soul.levelUp", getName(tool), level), Util.DUMMY_UUID);
            player.world.playSound(null, player.func_233580_cy_(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        // Learn new skill?
        SoulTraits toLearn = SoulTraits.selectTraitToLearn(this, tool);
        if (toLearn != null) {
            addOrLevelSkill(toLearn, tool, player);
        }

        // Save soul to NBT
        SoulManager.setSoul(tool, this);
        readyToSave = false;

        GearData.recalculateStats(tool, player);

        return toLearn;
    }

    public void onBreakBlock(PlayerEntity player, ItemStack gear, IBlockReader world, BlockPos pos, BlockState blockState) {
        int xp = getXpForBlockHarvest(world, pos, blockState);
        this.addXp(xp, gear, player);
    }

    public void onAttackedWith(PlayerEntity player, ItemStack gear, LivingEntity target, float damageAmount) {
        int xp = Math.round(XP_FACTOR_KILLS * damageAmount);
        xp = MathHelper.clamp(xp, 1, 1000);
        this.addXp(xp, gear, player);
    }
    //endregion

    //region Skills

    public boolean addOrLevelSkill(SoulTraits skill, ItemStack tool, @Nullable PlayerEntity player) {
        if (skills.containsKey(skill)) {
            // Has skill already.
            int level = skills.get(skill);
            if (level < skill.getMaxLevel()) {
                // Can be leveled up.
                ++level;
                skills.put(skill, level);
                if (player != null) {
                    player.sendMessage(new TranslationTextComponent("misc.silentgems.gear_soul.skillLearned", skill.getDisplayName(level)), Util.DUMMY_UUID);
                }
                return true;
            } else {
                // Already max level
                return false;
            }
        }

        skills.put(skill, 1);
        if (player != null) {
            player.sendMessage(new TranslationTextComponent("misc.silentgems.gear_soul.skillLearned", skill.getDisplayName(1)), Util.DUMMY_UUID);
        }

        return true;
    }

    public void setSkillLevel(SoulTraits skill, int skillLevel, ItemStack tool, PlayerEntity player) {
        if (skillLevel <= 0) {
            skills.remove(skill);
        }
        skills.put(skill, skillLevel > skill.getMaxLevel() ? skill.getMaxLevel() : skillLevel);
    }

    /**
     * Determine if the soul has learned any level of this skill.
     *
     * @param skill The skill to check.
     * @return True if any level has been learned, false otherwise.
     */
    public boolean hasSkill(SoulTraits skill) {
        return skills.containsKey(skill);
    }

    /**
     * Determine the level of the skill learned.
     *
     * @param skill The skill to check.
     * @return The level of the skill that has been learned, or 0 if the skill has not been learned.
     */
    public int getSkillLevel(SoulTraits skill) {
        if (!hasSkill(skill))
            return 0;
        return skills.get(skill);
    }

    //endregion

    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, boolean advanced) {
        // Level, XP
        list.add(1, new TranslationTextComponent(
                "misc.silentgems.gear_soul.level",
                String.valueOf(level),
                String.format("%,d", xp),
                String.format("%,d", getXpToNextLevel()))
                .func_240699_a_(TextFormatting.GREEN));

        if (stack.getItem() instanceof GearSoulItem) {
            // Display elements
            list.add(2, getElementPairText());
        }
    }

    float getStatModifier(ItemStat stat) {
        String statName = stat.getName().getPath();
        return primaryElement.getStatModifier(statName) + secondaryElement.getStatModifier(statName) / 2f;
    }

    private ITextComponent getElementPairText() {
        if (secondaryElement != SoulElement.NONE)
            return new TranslationTextComponent("misc.silentgems.gear_soul.elements.pair",
                    primaryElement.getDisplayName(),
                    secondaryElement.getDisplayName());
        return new TranslationTextComponent("misc.silentgems.gear_soul.elements.single",
                primaryElement.getDisplayName());
    }

    public static GearSoul construct(Iterable<Soul> souls) {
        // Soul weight map
        Map<SoulElement, Integer> elements = new EnumMap<>(SoulElement.class);
        for (Soul soul : souls) {
            if (soul == null) {
                SilentGems.LOGGER.error("Got a null soul when constructing a gear soul", new NullPointerException("soul is null"));
                continue;
            }

            int current = elements.getOrDefault(soul.getPrimaryElement(), 0);
            elements.put(soul.getPrimaryElement(), current + 5);
            if (soul.getSecondaryElement() != SoulElement.NONE) {
                current = elements.getOrDefault(soul.getSecondaryElement(), 0);
                elements.put(soul.getSecondaryElement(), current + 3);
            }
        }

        // Highest weight becomes element 1, second becomes element 2.
        GearSoul toolSoul = new GearSoul();
        // Primary
        toolSoul.primaryElement = selectHighestWeight(elements);
        elements.remove(toolSoul.primaryElement);
        // Secondary (if any are left)
        if (!elements.isEmpty()) {
            toolSoul.secondaryElement = selectHighestWeight(elements);
        }

        return toolSoul;
    }

    public static GearSoul randomSoul() {
        GearSoul soul = new GearSoul();

        List<SoulElement> elements = new ArrayList<>();
        for (SoulElement elem : SoulElement.values()) {
            if (elem != SoulElement.NONE) {
                elements.add(elem);
            }
        }

        soul.primaryElement = elements.get(SilentGems.random.nextInt(elements.size()));
        elements.remove(soul.primaryElement);
        elements.add(SoulElement.NONE);
        soul.secondaryElement = elements.get(SilentGems.random.nextInt(elements.size()));

        return soul;
    }

    private static SoulElement selectHighestWeight(Map<SoulElement, Integer> elements) {
        SoulElement element = SoulElement.NONE;
        int highestWeight = 0;

        for (Map.Entry<SoulElement, Integer> entry : elements.entrySet()) {
            SoulElement elementInMap = entry.getKey();
            int weightInMap = entry.getValue();
            if (weightInMap > highestWeight || (weightInMap == highestWeight && elementInMap.weight > element.weight)) {
                // This element takes priority over the previous best match.
                element = entry.getKey();
                highestWeight = entry.getValue();
            }
        }

        return element;
    }

    public void updateTick(ItemStack tool, PlayerEntity player) {
        if (!player.world.isRemote) {
            ++ticksExisted;
            if (coffeeCooldown > 0) {
                --coffeeCooldown;
            }
        }

        boolean inMainHand = player.getHeldItemMainhand() == tool;
        if (readyToSave && !inMainHand) {
            readyToSave = false;
            SoulManager.setSoul(tool, this);
        }
    }

    public boolean isReadyToSave() {
        return this.readyToSave;
    }

    public void setReadyToSave(boolean value) {
        this.readyToSave = value;
    }

    public static GearSoul read(CompoundNBT tags) {
        GearSoul soul = new GearSoul();

        soul.name = tags.getString("name");
        soul.primaryElement = SoulElement.fromString(tags.getString("element1"));
        soul.secondaryElement = SoulElement.fromString(tags.getString("element2"));

        soul.xp = tags.getInt("xp");
        soul.level = tags.getInt("level");

        // Load skills
        soul.skills.clear();
        ListNBT tagList = tags.getList("skills", 10);
        for (INBT nbt : tagList) {
            if (nbt instanceof CompoundNBT) {
                CompoundNBT tag = (CompoundNBT) nbt;
                SoulTraits skill = SoulTraits.get(tag.getString("id"));
                if (skill != null) {
                    int level = tag.getShort("level");
                    soul.skills.put(skill, level);
                }
                // Skills with unknown IDs are ignored!
            }
        }

        return soul;
    }

    public void write(CompoundNBT tags) {
        if (!name.isEmpty()) {
            tags.putString("name", name);
        }
        tags.putString("element1", this.primaryElement.name());
        tags.putString("element2", this.secondaryElement.name());
        tags.putInt("xp", xp);
        tags.putInt("level", level);

        // Save skills
        ListNBT tagList = new ListNBT();
        skills.forEach((trait, level) -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("id", trait.getTraitId().toString());
            tag.putShort("level", level.shortValue());
            tagList.add(tag);
        });
        tags.put("skills", tagList);
    }

    @Override
    public String toString() {
        return "GearSoul{" +
                "Level: " + level +
                ", XP: " + xp +
                ", Elements: {" + primaryElement.name() + ", " + secondaryElement.name() + "}" +
                "}";
    }

    private void sendUpdatePacket(ItemStack tool, PlayerEntity player, @Nullable SoulTraits skillLearned, int skillLevel) {
        // Server side: send update packet to player.
        // TODO: GearSoul sendUpdatePacket -- Is this still necessary?
//        if (!player.world.isRemote) {
//            UUID uuid = ToolHelper.getSoulUUID(tool);
//            MessageSoulSync message = new MessageSoulSync(uuid, xp, level, actionPoints, skillLearned,
//                    skillLevel);
//            NetworkHandler.INSTANCE.sendTo(message, (PlayerEntityMP) player);
//        }
    }
}
