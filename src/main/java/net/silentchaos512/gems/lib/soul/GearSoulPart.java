package net.silentchaos512.gems.lib.soul;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.item.GearType;
import net.silentchaos512.gear.api.part.IPartSerializer;
import net.silentchaos512.gear.api.part.PartType;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.StatInstance;
import net.silentchaos512.gear.api.traits.TraitInstance;
import net.silentchaos512.gear.gear.part.AbstractGearPart;
import net.silentchaos512.gear.gear.part.PartData;
import net.silentchaos512.gear.gear.part.PartSerializers;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.util.SoulManager;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GearSoulPart extends AbstractGearPart {
    private static final ResourceLocation TYPE_ID = SilentGems.getId("gear_soul");
    public static final IPartSerializer<GearSoulPart> SERIALIZER = new AbstractGearPart.Serializer<>(TYPE_ID, GearSoulPart::new);
    public static final PartType TYPE = PartType.create(PartType.Builder.builder(TYPE_ID)
            .isRemovable(true)
            .isUpgrade(true)
    );

    static {
        PartSerializers.register(SERIALIZER);
    }

    public GearSoulPart(ResourceLocation id) {
        super(id);
    }

    @Override
    public PartType getType() {
        return TYPE;
    }

    @Override
    public IPartSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Collection<StatInstance> getStatModifiers(ItemStat stat, PartData part, GearType gearType, ItemStack gear) {
        Collection<StatInstance> mods = super.getStatModifiers(stat, part, gearType, gear);

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
        return StatInstance.of(value, StatInstance.Operation.MUL1);
    }

    @Override
    public List<TraitInstance> getTraits(PartData part, ItemStack gear) {
        List<TraitInstance> traits = new ArrayList<>(super.getTraits(part, gear));

        GearSoul soul = getSoul(gear, part);
        if (soul != null) {
            soul.getSkills().forEach((skill, level) -> {
                if (skill.getTrait() != null) {
                    traits.add(TraitInstance.of(skill.getTrait(), level));
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
    public int getColor(PartData part, ItemStack gear, int layer, int animationFrame) {
        return Color.VALUE_WHITE;
    }

    @Override
    public boolean canAddToGear(ItemStack gear, PartData part) {
        return true;
    }
}
