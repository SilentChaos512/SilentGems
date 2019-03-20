package net.silentchaos512.gems.lib.soul;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.parts.IPartPosition;
import net.silentchaos512.gear.api.parts.IPartSerializer;
import net.silentchaos512.gear.api.parts.IUpgradePart;
import net.silentchaos512.gear.api.parts.PartType;
import net.silentchaos512.gear.api.stats.CommonItemStats;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gear.api.traits.ITrait;
import net.silentchaos512.gear.parts.AbstractGearPart;
import net.silentchaos512.gear.parts.PartData;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.utils.MathUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

        GearSoul soul = SoulManager.getSoul(gear);
        if (soul != null) {
            float amount = getSoulStatModifier(soul, stat);
            if (!MathUtils.doublesEqual(amount, 0)) {
                mods.add(getSoulBoostedModifier(stat, amount));
            }
        }

        return mods;
    }

    private static float getSoulStatModifier(GearSoul soul, ItemStat stat) {
        if (stat == CommonItemStats.ARMOR) return soul.getArmorModifier();
        if (stat == CommonItemStats.DURABILITY) return soul.getDurabilityModifier();
        if (stat == CommonItemStats.HARVEST_SPEED) return soul.getHarvestSpeedModifier();
        if (stat == CommonItemStats.MAGIC_DAMAGE) return soul.getMagicDamageModifier();
        if (stat == CommonItemStats.MELEE_DAMAGE) return soul.getMeleeDamageModifier();
        return 0;
    }

    private static StatInstance getSoulBoostedModifier(ItemStat stat, float value) {
        return new StatInstance("silentgems:soul/" + stat.getName().getPath(), value, StatInstance.Operation.MUL1);
    }

    @Override
    public Map<ITrait, Integer> getTraits(ItemStack gear, PartData part) {
        Map<ITrait, Integer> traits = new HashMap<>(super.getTraits(gear, part));

        GearSoul soul = SoulManager.getSoul(gear);
        if (soul != null) {
            soul.getSkills().forEach((skill, level) -> traits.put(skill.getTrait(), level));
        }

        return traits;
    }
}
