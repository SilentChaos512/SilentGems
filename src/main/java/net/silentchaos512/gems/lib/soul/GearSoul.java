package net.silentchaos512.gems.lib.soul;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.silentchaos512.gear.api.stats.IItemStat;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.GearSoulItem;

import javax.annotation.Nullable;
import java.util.*;

public class GearSoul {
    public static final float XP_FACTOR_KILLS = 0.35f;
    public static final float XP_FACTOR_TILLING = 4.0f;
    public static final float XP_FACTOR_BLOCK_MINED = 1.0f;
    public static final float XP_FACTOR_DAMAGE_TAKEN = 50f;
    public static final int XP_MAX_PER_BLOCK = 20;
    public static final float XP_MIN_BLOCK_HARDNESS = 0.5f;

    private static final int BASE_XP = 30;
    private static final float XP_CURVE_FACTOR = 3.0f;

    private String name = "";

    private final ItemStack item;

    public GearSoul(ItemStack stack) {
        this.item = stack;
    }

    private GearSoul(ItemStack stack, SoulElement primary, SoulElement secondary) {
        this.item = stack;
        getTag().putString("element1", primary.name());
        getTag().putString("element2", secondary.name());
    }

    private CompoundNBT getTag() {
        return !item.isEmpty() ? item.getOrCreateChildTag("SG_GearSoul") : new CompoundNBT();
    }

    //region Getters and Setters

    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the XP value stored in the item's NBT
     *
     * @return The total XP gained
     */
    public int getXp() {
        return getTag().getInt("xp");
    }

    /**
     * Directly sets the XP amount. This DOES NOT check for level ups! It is mostly meant to be used
     * internally.
     *
     * @param xp The amount to assign to xp.
     */
    public void setXp(int xp) {
        getTag().putInt("xp", xp);
    }

    public int getLevel() {
        short level = getTag().getShort("level");
        return level > 0 ? level : 1;
    }


    /**
     * Directly sets the level. This SHOULD NOT be used in most cases! It is mostly meant to be used
     * internally.
     *
     * @param level The amount to assign to level.
     */
    public void setLevel(int level) {
        getTag().putShort("level", (short) level);
    }

    public SoulElement getPrimaryElement() {
        return SoulElement.fromString(getTag().getString("element1"));
    }

    public SoulElement getSecondaryElement() {
        return SoulElement.fromString(getTag().getString("element2"));
    }

    public Map<SoulTraits, Integer> getSkills() {
        Map<SoulTraits, Integer> ret = new LinkedHashMap<>();
        ListNBT listNbt = getTag().getList("skills", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < listNbt.size(); i++) {
            CompoundNBT nbt = listNbt.getCompound(i);
            SoulTraits trait = SoulTraits.get(nbt.getString("id"));
            if (trait != null) {
                ret.put(trait, (int) nbt.getShort("level"));
            }
        }

        return ret;
    }

    public ITextComponent getName() {
        if (name.isEmpty()) {
            if (!this.item.isEmpty()) {
                return this.item.getDisplayName();
            }
        }
        return new StringTextComponent(name);
    }

    public void setName(String value) {
        this.name = value;
    }

    //endregion

    //region XP and Levels

    public void addXp(int amount, @Nullable PlayerEntity player) {
        if (!GemsConfig.Common.gearSoulsGetXpFromFakePlayers.get() && player instanceof FakePlayer) {
            return;
        }

        setXp(getXp() + amount);
        while (getXp() >= getXpToNextLevel()) {
            levelUp(player);
        }
    }

    public int getXpToNextLevel() {
        return getXpForLevel(getLevel() + 1);
    }

    public static int getXpForLevel(int target) {
        return BASE_XP * (int) Math.pow(target, XP_CURVE_FACTOR);
    }

    @Nullable
    private SoulTraits levelUp(@Nullable PlayerEntity player) {
        if (player != null && player.world.isRemote) return null;

        setLevel(getLevel() + 1);
        if (player != null) {
            player.sendMessage(new TranslationTextComponent("misc.silentgems.gear_soul.levelUp", getName(), getLevel()), Util.DUMMY_UUID);
            player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }

        // Learn new skill?
        SoulTraits toLearn = SoulTraits.selectTraitToLearn(this, this.item);
        if (toLearn != null) {
            addOrLevelSkill(toLearn, player);
        }

        GearData.recalculateStats(this.item, player);

        return toLearn;
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
            oreBonus = this.getLevel() / 2;
        }

        // Wood gives less XP.
        if (state.getMaterial() == Material.WOOD) {
            hardness /= 2;
        }

        int clamp = MathHelper.clamp(Math.round(XP_FACTOR_BLOCK_MINED * hardness), 1, XP_MAX_PER_BLOCK);
        return oreBonus + clamp;
    }

    public void onBreakBlock(PlayerEntity player, IBlockReader world, BlockPos pos, BlockState blockState) {
        int xp = getXpForBlockHarvest(world, pos, blockState);
        this.addXp(xp, player);
    }

    public void onAttackedWith(PlayerEntity player, LivingEntity target, float damageAmount) {
        int xp = Math.round(XP_FACTOR_KILLS * damageAmount);
        xp = MathHelper.clamp(xp, 1, 1000);
        this.addXp(xp, player);
    }
    //endregion

    //region Skills

    private void addOrLevelSkill(SoulTraits skill, @Nullable PlayerEntity player) {
        Map<SoulTraits, Integer> skills = getSkills();

        if (skills.containsKey(skill)) {
            // Has skill already.
            int level = skills.get(skill);
            if (level < skill.getMaxLevel()) {
                // Can be leveled up.
                ++level;
                skills.put(skill, level);
                writeLearnedSkills(skills, getTag());
                if (player != null) {
                    player.sendMessage(new TranslationTextComponent("misc.silentgems.gear_soul.skillLearned", skill.getDisplayName(level)), Util.DUMMY_UUID);
                }
                return;
            }
            // Already max level
            return;
        }

        skills.put(skill, 1);
        writeLearnedSkills(skills, getTag());
        if (player != null) {
            player.sendMessage(new TranslationTextComponent("misc.silentgems.gear_soul.skillLearned", skill.getDisplayName(1)), Util.DUMMY_UUID);
        }

    }

    //endregion

    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, boolean advanced) {
        // Level, XP
        list.add(1, new TranslationTextComponent(
                "misc.silentgems.gear_soul.level",
                String.valueOf(getLevel()),
                String.format("%,d", getXp()),
                String.format("%,d", getXpToNextLevel()))
                .mergeStyle(TextFormatting.GREEN));

        if (stack.getItem() instanceof GearSoulItem) {
            // Display elements
            list.add(2, getElementPairText());
        }
    }

    float getStatModifier(IItemStat stat) {
        String statName = stat.getStatId().getPath();
        return getPrimaryElement().getStatModifier(statName) + getSecondaryElement().getStatModifier(statName) / 2f;
    }

    private ITextComponent getElementPairText() {
        if (getSecondaryElement() != SoulElement.NONE)
            return new TranslationTextComponent("misc.silentgems.gear_soul.elements.pair",
                    getPrimaryElement().getDisplayName(),
                    getSecondaryElement().getDisplayName());
        return new TranslationTextComponent("misc.silentgems.gear_soul.elements.single",
                getPrimaryElement().getDisplayName());
    }

    public static GearSoul construct(ItemStack stack, Iterable<Soul> souls) {
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
        // Primary
        SoulElement primaryElement = selectHighestWeight(elements);
        elements.remove(primaryElement);
        // Secondary (if any are left)
        SoulElement secondaryElement = !elements.isEmpty() ? selectHighestWeight(elements) : SoulElement.NONE;

        return new GearSoul(stack, primaryElement, secondaryElement);
    }

    public static GearSoul randomSoul() {
        List<SoulElement> elements = new ArrayList<>();
        for (SoulElement elem : SoulElement.values()) {
            if (elem != SoulElement.NONE) {
                elements.add(elem);
            }
        }

        SoulElement primaryElement = elements.get(SilentGems.random.nextInt(elements.size()));
        elements.remove(primaryElement);
        elements.add(SoulElement.NONE);
        SoulElement secondaryElement = elements.get(SilentGems.random.nextInt(elements.size()));

        return new GearSoul(ItemStack.EMPTY, primaryElement, secondaryElement);
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

    public void updateTick(ItemStack tool, PlayerEntity player) {}

    public void write(CompoundNBT tags) {
        if (!name.isEmpty()) {
            tags.putString("name", name);
        }
        tags.putString("element1", getPrimaryElement().name());
        tags.putString("element2", getSecondaryElement().name());
        tags.putInt("xp", getXp());
        tags.putInt("level", getLevel());

        // Save skills
        writeLearnedSkills(getSkills(), tags);
    }

    private static void writeLearnedSkills(Map<SoulTraits, Integer> skills, CompoundNBT tags) {
        ListNBT tagList = new ListNBT();

        for (Map.Entry<SoulTraits, Integer> entry : skills.entrySet()) {
            SoulTraits trait = entry.getKey();
            Integer value = entry.getValue();

            CompoundNBT tag = new CompoundNBT();
            tag.putString("id", trait.getTraitId().toString());
            tag.putShort("level", value.shortValue());
            tagList.add(tag);
        }

        tags.put("skills", tagList);
    }

    @Override
    public String toString() {
        return "GearSoul{" +
                "Level: " + getLevel() +
                ", XP: " + getXp() +
                ", Elements: {" + getPrimaryElement().name() + ", " + getSecondaryElement().name() + "}" +
                "}";
    }
}
