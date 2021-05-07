package net.silentchaos512.gems.lib.soul;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.silentchaos512.gear.api.item.GearType;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.api.traits.ITrait;
import net.silentchaos512.gear.gear.trait.TraitManager;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsTraits;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class SoulTraits {
    private static final Map<ResourceLocation, SoulTraits> SOUL_TRAITS = new ConcurrentHashMap<>();

    public static final ResourceLocation AERIAL = SilentGems.getId("soul/aerial");
    public static final ResourceLocation AQUATIC = SilentGems.getId("soul/aquatic");
    public static final ResourceLocation ARMOR = SilentGems.getId("soul/armor");
    public static final ResourceLocation CHILL = SilentGems.getId("soul/chill");
    public static final ResourceLocation COFFEE_POT = SilentGems.getId("soul/coffee_pot");
    public static final ResourceLocation DURABILITY = SilentGems.getId("soul/durability");
    public static final ResourceLocation HARVEST_SPEED = SilentGems.getId("soul/harvest_speed");
    public static final ResourceLocation MAGIC_DAMAGE = SilentGems.getId("soul/magic_damage");
    public static final ResourceLocation MELEE_DAMAGE = SilentGems.getId("soul/melee_damage");
    public static final ResourceLocation PERSISTENCE = SilentGems.getId("soul/persistence");
    public static final ResourceLocation SKULL_COLLECTOR = SilentGems.getId("soul/skull_collector");
    public static final ResourceLocation WARM = SilentGems.getId("soul/warm");

    static {
        // TODO: Should add a way for players to customize this...

        Builder.create(DURABILITY, 10)
                .favorsElements(SoulElement.EARTH, SoulElement.METAL)
                .add();
        Builder.create(HARVEST_SPEED, 10)
                .favorsElements(SoulElement.WIND, SoulElement.LIGHTNING)
                .favorsGear(GearType.HARVEST_TOOL)
                .add();
        Builder.create(MELEE_DAMAGE, 10)
                .favorsElements(SoulElement.FIRE, SoulElement.VENOM)
                .favorsGear(GearType.MELEE_WEAPON)
                .add();
        Builder.create(MAGIC_DAMAGE, 10)
                .favorsElements(SoulElement.WATER, SoulElement.ICE)
                .favorsGear(GearType.MELEE_WEAPON)
                .add();
        Builder.create(ARMOR, 10)
                .favorsElements(SoulElement.METAL)
                .favorsGear(GearType.ARMOR)
                .lockToFavoredGearType()
                .add();

        Builder.create(AERIAL, 5)
                .medianLevel(9)
                .weight(-3, 1.0)
                .favorsElements(SoulElement.WIND)
                .add();
        Builder.create(AQUATIC, 5)
                .medianLevel(9)
                .weight(-3, 1.0)
                .favorsElements(SoulElement.WATER)
                .add();
        /*Builder.create(CHILL, 3)
                .medianLevel(3)
                .weight(-6, 0.5)
                .favorsElements(SoulElement.WATER, SoulElement.ICE)
                .add();*/
        /*Builder.create(COFFEE_POT, 1)
                .medianLevel(13)
                .weight(-7, 0.25)
                .favorsElements(SoulElement.FLORA, SoulElement.EARTH)
                .lockToFavoredElements()
                .add();*/
        Builder.create(PERSISTENCE, 5)
                .medianLevel(11)
                .weight(-2, 0.75)
                .favorsElements(SoulElement.FLORA, SoulElement.ALIEN)
                .add();
        Builder.create(SKULL_COLLECTOR, 5)
                .weight(-5, 0.75)
                .favorsElements(SoulElement.MONSTER, SoulElement.ALIEN)
                .add();
        /*Builder.create(WARM, 3)
                .medianLevel(3)
                .weight(-6, 0.5)
                .favorsElements(SoulElement.FIRE, SoulElement.METAL)
                .add();*/

        Builder.create(GemsTraits.CRITICAL, 5)
                .medianLevel(7)
                .weight(0, 1.1)
                .favorsElements(SoulElement.LIGHTNING, SoulElement.MONSTER)
                .favorsGear(GearType.TOOL)
                .lockToFavoredGearType()
                .add();
        Builder.create(GemsTraits.LUNA, 5)
                .medianLevel(10)
                .weight(-8, 0.5)
                .favorsElements(SoulElement.ICE, SoulElement.FAUNA)
                .add();
    }

    private final ResourceLocation traitId;
    private final int maxLevel;
    private final int medianLevel;
    private final double weight;
    private final SoulElement[] favoredElements;
    private final boolean lockedToFavoredElements;
    private final boolean lockedToFavoredGearType;
    private final double favoredWeightMulti;
    @Nullable private final GearType favoredGearType;

    public SoulTraits(Builder builder) {
        this.traitId = builder.traitId;
        this.maxLevel = builder.maxLevel;
        this.medianLevel = builder.medianLevel;
        this.weight = builder.weight;
        this.favoredElements = builder.favoredElements;
        this.lockedToFavoredElements = builder.lockedToFavoredElements;
        this.lockedToFavoredGearType = builder.lockedToFavoredGearType;
        this.favoredWeightMulti = builder.favoredWeightMulti;
        this.favoredGearType = builder.favoredGearType;
    }

    public ResourceLocation getTraitId() {
        return traitId;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    @Nullable
    public ITrait getTrait() {
        return TraitManager.get(this.traitId);
    }

    public boolean canLearn(GearSoul soul, ItemStack gear) {
        GearType gearType = GearHelper.getType(gear);
        // Locked to specific gear type(s)?
        if (lockedToFavoredGearType && (favoredGearType == null || gearType == null || !favoredGearType.matches(gearType.getName())))
            return false;

        if (this.lockedToFavoredElements) {
            boolean foundMatch = false;
            for (SoulElement element : this.favoredElements) {
                if (element == soul.getPrimaryElement() || element == soul.getSecondaryElement()) {
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                return false;
            }
        }

        return TraitHelper.getTraitLevel(gear, this.traitId) < this.maxLevel;
    }

    public ITextComponent getDisplayName(int level) {
        ITrait trait = this.getTrait();
        if (trait == null) {
            return new StringTextComponent(this.traitId.toString());
        }
        return trait.getDisplayName(level);
    }

    @Nullable
    public static SoulTraits get(String id) {
        ResourceLocation resourceLocation = ResourceLocation.tryCreate(id);
        if (resourceLocation == null) return null;
        return get(resourceLocation);
    }

    @Nullable
    public static SoulTraits get(ResourceLocation id) {
        return SOUL_TRAITS.get(id);
    }

    public static Optional<Hand> getHighestLevelEitherHand(PlayerEntity player, ResourceLocation trait) {
        ItemStack mainHand = player.getHeldItemMainhand();
        ItemStack offHand = player.getHeldItemOffhand();
        int levelMain = traitLevelWithSafetyChecks(mainHand, trait);
        int levelOff = traitLevelWithSafetyChecks(offHand, trait);

        if (levelMain > 0 && levelMain >= levelOff) {
            return Optional.of(Hand.MAIN_HAND);
        }
        if (levelOff > 0) {
            return Optional.of(Hand.OFF_HAND);
        }
        return Optional.empty();
    }

    private static int traitLevelWithSafetyChecks(ItemStack gear, ResourceLocation trait) {
        if (!(gear.getItem() instanceof ICoreItem) || GearHelper.isBroken(gear)) {
            return 0;
        }
        return TraitHelper.getTraitLevel(gear, trait);
    }

    @Nullable
    public static SoulTraits selectTraitToLearn(GearSoul soul, ItemStack tool) {
        Map<SoulTraits, Double> candidates = new LinkedHashMap<>();

        // Create list of candidates
        for (SoulTraits skill : SOUL_TRAITS.values()) {
            if (skill.canLearn(soul, tool)) {
                boolean favorsElement = false;
                // Select a weight based on favored elements.
                double weight = skill.favoredElements.length < 1 ? 20 : 7;
                for (SoulElement elem : skill.favoredElements) {
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
                if (skill.favoredGearType != null) {
                    GearType toolType = tool.getItem() instanceof ICoreItem
                            ? ((ICoreItem) tool.getItem()).getGearType()
                            : null;
                    if (toolType == skill.favoredGearType) {
                        weight += 5;
                    }
                }

                // If skill has a median level, apply that to the weight.
                if (skill.medianLevel > 0) {
                    int diff = Math.abs(soul.getLevel() - skill.medianLevel);
                    if (diff > 6) {
                        diff = 6;
                    }
                    weight -= 0.75 * diff;
                }

                // If a lower level of the skill is already known, reduce the weight.
                Map<SoulTraits, Integer> skills = soul.getSkills();
                if (skills.containsKey(skill)) {
                    weight -= 2.5 * skills.get(skill);
                }

                // Base weight diff, favors multiplier
                weight += skill.weight;
                if (favorsElement) {
                    weight *= skill.favoredWeightMulti;
                }

                // Make sure weight is at least 1.
                if (weight < 1) {
                    weight = 1;
                }

                candidates.put(skill, weight);
            }
        }

        // Seed based on soul elements, level, and tool UUID.
        Random rand = new Random(soul.getPrimaryElement().ordinal()
                + (soul.getSecondaryElement().ordinal() << 4)
                + (soul.getLevel() << 8)
                + (GearData.getUUID(tool).getLeastSignificantBits() << 16));

        // Weighted random selection.
        SoulTraits selected = null;
        double bestValue = Double.MIN_VALUE;

        for (SoulTraits skill : candidates.keySet()) {
            double value = -Math.log(rand.nextFloat() / candidates.get(skill));

            if (value > bestValue) {
                bestValue = value;
                selected = skill;
            }
        }

        return selected;
    }

    public static final class Builder {
        private final ResourceLocation traitId;
        private final int maxLevel;
        private int medianLevel;
        private double weight;
        private SoulElement[] favoredElements = new SoulElement[0];
        private boolean lockedToFavoredElements;
        private boolean lockedToFavoredGearType;
        private double favoredWeightMulti;
        private GearType favoredGearType;

        Builder(ResourceLocation traitId, int maxLevel) {
            this.traitId = traitId;
            this.maxLevel = maxLevel;
        }

        public static Builder create(ResourceLocation traitId, int maxLevel) {
            return new Builder(traitId, maxLevel);
        }

        public Builder medianLevel(int value) {
            this.medianLevel = value;
            return this;
        }

        public Builder weight(double weight, double favoredWeightMulti) {
            this.weight = weight;
            this.favoredWeightMulti = favoredWeightMulti;
            return this;
        }

        public Builder favorsElements(SoulElement... elements) {
            this.favoredElements = elements.clone();
            return this;
        }

        public Builder favorsGear(GearType type) {
            this.favoredGearType = type;
            return this;
        }

        public Builder lockToFavoredElements() {
            this.lockedToFavoredElements = true;
            return this;
        }

        public Builder lockToFavoredGearType() {
            this.lockedToFavoredGearType = true;
            return this;
        }

        public void add() {
            SOUL_TRAITS.put(this.traitId, new SoulTraits(this));
        }
    }
}
