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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GearSoulPart extends AbstractGearPart implements IUpgradePart {
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

        GearSoul soul = SoulManager.getSoul(!gear.isEmpty() ? gear : part.getCraftingItem());
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
        return new StatInstance("silentgems:soul/" + stat.getName().getPath(), value, StatInstance.Operation.MUL1);
    }

    @Override
    public List<PartTraitInstance> getTraits(ItemStack gear, PartData part) {
        List<PartTraitInstance> traits = new ArrayList<>(super.getTraits(gear, part));

        GearSoul soul = SoulManager.getSoul(gear);
        if (soul != null) {
            soul.getSkills().forEach((skill, level) -> {
                if (skill.getTrait() != null) {
                    traits.add(new PartTraitInstance(skill.getTrait(), level, ImmutableList.of()));
                }
            });
        }

        return traits;
    }

    @Override
    public void onAddToGear(ItemStack gear, ItemStack part) {
        GearSoul soul = SoulManager.getSoul(part);
        if (soul == null) {
            SilentGems.LOGGER.warn("Gear soul is missing soul data: {}", part);
            return;
        }
        SoulManager.setSoul(gear, soul);
    }
}
