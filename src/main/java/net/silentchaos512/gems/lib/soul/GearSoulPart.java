package net.silentchaos512.gems.lib.soul;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.parts.*;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gear.parts.AbstractGearPart;
import net.silentchaos512.gear.parts.PartData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GearSoulPart extends AbstractGearPart {
    private static final ResourceLocation TYPE_ID = SilentGems.getId("gear_soul");
    public static final PartType TYPE = PartType.create(
            TYPE_ID,
            "S",
            new AbstractGearPart.Serializer<>(TYPE_ID, GearSoulPart::new)
    );
    private static final IPartPosition POSITION = new IPartPosition() {
        @Override
        public String getTexturePrefix() {
            return "soul";
        }

        @Override
        public String getModelIndex() {
            return "soul";
        }
    };

    public GearSoulPart(ResourceLocation id) {
        super(id);
    }

    @Override
    public PartType getType() {
        return TYPE;
    }

    @Override
    public IPartPosition getPartPosition() {
        return POSITION;
    }

    @Override
    public IPartSerializer<?> getSerializer() {
        return TYPE.getSerializer();
    }

    @Override
    public Collection<StatInstance> getStatModifiers(ItemStack gear, ItemStat stat, PartData part) {
        Collection<StatInstance> mods = super.getStatModifiers(gear, stat, part);

        GearSoul soul = getSoul(gear, part);
        if (soul != null) {
            float amount = getSoulStatModifier(soul, stat);
            if (!MathUtils.doublesEqual(amount, 0)) {
                mods.add(getSoulBoostedModifier(stat, amount));
            }
        }

        return mods;
    }

    private static float getSoulStatModifier(GearSoul soul, ItemStat stat) {
        return soul.getStatModifier(stat);
    }

    private static StatInstance getSoulBoostedModifier(ItemStat stat, float value) {
        return new StatInstance(value, StatInstance.Operation.MUL1);
    }

    @Override
    public List<PartTraitInstance> getTraits(ItemStack gear, PartData part) {
        List<PartTraitInstance> traits = new ArrayList<>(super.getTraits(gear, part));

        GearSoul soul = getSoul(gear, part);
        if (soul != null) {
            soul.getSkills().forEach((skill, level) -> {
                if (skill.getTrait() != null) {
                    traits.add(new PartTraitInstance(skill.getTrait(), level, ImmutableList.of()));
                }
            });
        }

        return traits;
    }

    @Nullable
    private static GearSoul getSoul(ItemStack gear, PartData part) {
        GearSoul soul = SoulManager.getSoul(gear);
        return soul != null ? soul : SoulManager.getSoul(part.getCraftingItem());
    }

    @Override
    public void onAddToGear(ItemStack gear, PartData part) {
        GearSoul soul = SoulManager.getSoul(part.getCraftingItem());
        if (soul == null) {
            SilentGems.LOGGER.warn("Gear soul is missing soul data: {}", part.getCraftingItem());
            return;
        }
        SoulManager.setSoul(gear, soul);
    }

    @Override
    public void onGearDamaged(PartData part, ItemStack gear, int amount) {
        SilentGems.LOGGER.debug("Soul onGearDamaged");
        // Unfortunately no way to get the player, is there?
        // This means no level-up notifications for armor, for gear souls should work now
        SoulManager.addSoulXp((int) (GearSoul.XP_FACTOR_ARMOR_DAMAGED * amount), gear, null);
    }

    @Override
    public boolean canAddToGear(ItemStack gear, PartData part) {
        return true;
    }
}
