package net.silentchaos512.gems.lib.soul;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.FakePlayer;
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
    @Getter private int xp = 0;
    @Getter private int level = 1;

    // Elements
    @Getter private SoulElement primaryElement = SoulElement.NONE;
    @Getter private SoulElement secondaryElement = SoulElement.NONE;

    // Skills
    @Getter private final Map<SoulTraits, Integer> skills = new HashMap<>();

    // Temporary variables NOT saved to NBT
    public int climbTimer = 0;
    public int coffeeCooldown = 0;
    // Variables used by updateTick
    private int ticksExisted = 0;

    // =================
    // = XP and Levels =
    // =================

    public void addXp(int amount, ItemStack tool, EntityPlayer player) {
        if (!GemsConfig.COMMON.gearSoulsGetXpFromFakePlayers.get() && player instanceof FakePlayer) {
            return;
        }

        xp += amount;
        boolean packetSent = false;
        while (xp >= getXpToNextLevel()) {
            SoulTraits skillLearned = levelUp(tool, player);
            if (skillLearned != null) {
                Integer skillLevel = getSkillLevel(skillLearned);
                sendUpdatePacket(tool, player, skillLearned, skillLevel);
                packetSent = true;
            }
        }

        if (!packetSent) {
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

    public ITextComponent getName(ItemStack tool) {
        if (name.isEmpty()) {
            if (!tool.isEmpty()) {
                return tool.getDisplayName();
            }
        }
        return new TextComponentString(name);
    }

    public void setName(String value) {
        this.name = value;
    }

    public boolean hasName() {
        return !name.isEmpty();
    }

    @Nullable
    private SoulTraits levelUp(ItemStack tool, EntityPlayer player) {
        if (player == null || player.world.isRemote) return null;

        ++level;
        player.sendMessage(new TextComponentTranslation("misc.silentgems.gear_soul.levelUp", getName(tool), level));
        player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);

        // Learn new skill?
        SoulTraits toLearn = SoulTraits.selectTraitToLearn(this, tool);
        if (toLearn != null) {
            addOrLevelSkill(toLearn, tool, player);
        }

        // Save soul to NBT
        SoulManager.setSoul(tool, this);
        readyToSave = false;

        GearData.recalculateStats(player, tool);

        return toLearn;
    }

    public int getXpForBlockHarvest(IBlockReader world, BlockPos pos, IBlockState state) {
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

    public void onBreakBlock(EntityPlayer player, ItemStack gear, IBlockReader world, BlockPos pos, IBlockState blockState) {
        int xp = getXpForBlockHarvest(world, pos, blockState);
        this.addXp(xp, gear, player);
    }

    public void onAttackedWith(EntityPlayer player, ItemStack gear, EntityLivingBase target, float damageAmount) {
        int xp = Math.round(XP_FACTOR_KILLS * damageAmount);
        xp = MathHelper.clamp(xp, 1, 1000);
        this.addXp(xp, gear, player);
    }

    // ==========
    // = Skills =
    // ==========

    public boolean addOrLevelSkill(SoulTraits skill, ItemStack tool, EntityPlayer player) {
        if (skills.containsKey(skill)) {
            // Has skill already.
            int level = skills.get(skill);
            if (level < skill.getMaxLevel()) {
                // Can be leveled up.
                ++level;
                skills.put(skill, level);
                player.sendMessage(new TextComponentTranslation("misc.silentgems.gear_soul.skillLearned", skill.getDisplayName(level)));

                return true;
            } else {
                // Already max level
                return false;
            }
        }

        skills.put(skill, 1);
        player.sendMessage(new TextComponentTranslation("misc.silentgems.gear_soul.skillLearned", skill.getDisplayName(1)));

        return true;
    }

    public void setSkillLevel(SoulTraits skill, int skillLevel, ItemStack tool, EntityPlayer player) {
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

    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, boolean advanced) {
        // Level, XP
        list.add(1, new TextComponentTranslation(
                "misc.silentgems.gear_soul.level",
                String.valueOf(level),
                String.format("%,d", xp),
                String.format("%,d", getXpToNextLevel()))
                .applyTextStyle(TextFormatting.GREEN));

        boolean skillsKeyDown = true; //Keyboard.isKeyDown(Keyboard.KEY_S);

        if (skillsKeyDown || stack.getItem() instanceof GearSoulItem) {
            // Display elements
            if (secondaryElement != SoulElement.NONE) {
                list.add(2, new TextComponentTranslation("misc.silentgems.gear_soul.elements.pair",
                        primaryElement.getDisplayName(),
                        secondaryElement.getDisplayName()));
            } else {
                list.add(2, new TextComponentTranslation("misc.silentgems.gear_soul.elements.single",
                        primaryElement.getDisplayName()));
            }
        }
//        if (skillsKeyDown) {
//            // Display stat modifiers.
//            color = "  " + TextFormatting.YELLOW;
//            float durability = getDurabilityModifierForDisplay(this);
//            float harvestSpeed = getHarvestSpeedModifierForDisplay(this);
//            float meleeDamage = getMeleeDamageModifierForDisplay(this);
//            float magicDamage = getMagicDamageModifierForDisplay(this);
//            float protection = getProtectionModifierForDisplay(this);
//            if (durability != 0f)
//                list.add(color + TooltipHelper.getAsColoredPercentage("Durability", durability, 0, true));
//            if (harvestSpeed != 0f)
//                list.add(
//                        color + TooltipHelper.getAsColoredPercentage("HarvestSpeed", harvestSpeed, 0, true));
//            if (meleeDamage != 0f)
//                list.add(color + TooltipHelper.getAsColoredPercentage("MeleeDamage", meleeDamage, 0, true));
//            if (magicDamage != 0f)
//                list.add(color + TooltipHelper.getAsColoredPercentage("MagicDamage", magicDamage, 0, true));
//            if (protection != 0f)
//                list.add(color + TooltipHelper.getAsColoredPercentage("Protection", protection, 0, true));
//        } else {
//            list.add(TextFormatting.GOLD + SilentGems.i18n.miscText("tooltip.keyForSkills"));
//        }
    }

    protected float getDurabilityModifier() {
        return primaryElement.durabilityModifier + secondaryElement.durabilityModifier / 2f;
    }

    protected float getHarvestSpeedModifier() {
        return primaryElement.harvestSpeedModifier + secondaryElement.harvestSpeedModifier / 2f;
    }

    protected float getMeleeDamageModifier() {
        return primaryElement.meleeDamageModifier + secondaryElement.meleeDamageModifier / 2f;
    }

    protected float getMagicDamageModifier() {
        return primaryElement.magicDamageModifier + secondaryElement.magicDamageModifier / 2f;
    }

    protected float getArmorModifier() {
        return primaryElement.protectionModifier + secondaryElement.protectionModifier / 2f;
    }

//    public static float getDurabilityModifierForDisplay(@Nullable GearSoul soul) {
//        if (soul == null)
//            return 0f;
//
//        float val = soul.getDurabilityModifier();
//        SoulSkill skill = SoulSkill.DURABILITY_BOOST;
//        val = getSkillStatModifier(soul, val, skill);
//
//        return val - 1f;
//    }
//
//    public static float getHarvestSpeedModifierForDisplay(@Nullable GearSoul soul) {
//        if (soul == null)
//            return 0f;
//
//        float val = soul.getHarvestSpeedModifier();
//        SoulSkill skill = SoulSkill.HARVEST_SPEED_BOOST;
//        val = getSkillStatModifier(soul, val, skill);
//
//        return val - 1f;
//    }
//
//    public static float getMeleeDamageModifierForDisplay(@Nullable GearSoul soul) {
//        if (soul == null)
//            return 0f;
//
//        float val = soul.getMeleeDamageModifier();
//        SoulSkill skill = SoulSkill.MELEE_DAMAGE_BOOST;
//        val = getSkillStatModifier(soul, val, skill);
//
//        return val - 1f;
//    }
//
//    public static float getMagicDamageModifierForDisplay(@Nullable GearSoul soul) {
//        if (soul == null)
//            return 0f;
//
//        float val = soul.getMagicDamageModifier();
//        SoulSkill skill = SoulSkill.MAGIC_DAMAGE_BOOST;
//        val = getSkillStatModifier(soul, val, skill);
//
//        return val - 1f;
//    }
//
//    public static float getProtectionModifierForDisplay(@Nullable GearSoul soul) {
//        if (soul == null)
//            return 0f;
//
//        float val = soul.getProtectionModifier();
//        SoulSkill skill = SoulSkill.PROTECTION_BOOST;
//        val = getSkillStatModifier(soul, val, skill);
//
//        return val - 1f;
//    }
//
//    private static float getSkillStatModifier(GearSoul soul, float val, SoulSkill skill) {
//        if (skill != null && soul.skills != null && soul.skills.containsKey(skill)) {
//            int lvl = soul.skills.get(skill);
//            val += skill.getStatBoostMulti() * lvl;
//        }
//        return val;
//    }

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

    public void updateTick(ItemStack tool, EntityPlayer player) {
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

    public static GearSoul read(NBTTagCompound tags) {
        GearSoul soul = new GearSoul();

        soul.name = tags.getString("name");
        soul.primaryElement = SoulElement.fromString(tags.getString("element1"));
        soul.secondaryElement = SoulElement.fromString(tags.getString("element2"));

        soul.xp = tags.getInt("xp");
        soul.level = tags.getInt("level");

        // Load skills
        soul.skills.clear();
        NBTTagList tagList = tags.getList("skills", 10);
        for (INBTBase nbt : tagList) {
            if (nbt instanceof NBTTagCompound) {
                NBTTagCompound tag = (NBTTagCompound) nbt;
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

    public void write(NBTTagCompound tags) {
        if (!name.isEmpty()) {
            tags.setString("name", name);
        }
        tags.setString("element1", this.primaryElement.name());
        tags.setString("element2", this.secondaryElement.name());
        tags.setInt("xp", xp);
        tags.setInt("level", level);

        // Save skills
        NBTTagList tagList = new NBTTagList();
        skills.forEach((trait, level) -> {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("id", trait.getTraitId().toString());
            tag.setShort("level", level.shortValue());
            tagList.add(tag);
        });
        tags.setTag("skills", tagList);
    }

    @Override
    public String toString() {
        return "GearSoul{" +
                "Level: " + level +
                ", XP: " + xp +
                ", Elements: {" + primaryElement.name() + ", " + secondaryElement.name() + "}" +
                "}";
    }

    private void sendUpdatePacket(ItemStack tool, EntityPlayer player, @Nullable SoulTraits skillLearned, int skillLevel) {
        // Server side: send update packet to player.
        // TODO: GearSoul sendUpdatePacket -- Is this still necessary?
//        if (!player.world.isRemote) {
//            UUID uuid = ToolHelper.getSoulUUID(tool);
//            MessageSoulSync message = new MessageSoulSync(uuid, xp, level, actionPoints, skillLearned,
//                    skillLevel);
//            NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
//        }
    }
}
