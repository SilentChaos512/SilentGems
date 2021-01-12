package net.silentchaos512.gems.trait;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.stats.ItemStat;
import net.silentchaos512.gear.api.stats.ItemStats;
import net.silentchaos512.gear.api.traits.TraitActionContext;
import net.silentchaos512.gear.gear.trait.SimpleTrait;
import net.silentchaos512.gear.util.GearData;
import net.silentchaos512.gems.SilentGems;

public class RunicTrait extends SimpleTrait {
    public static final Serializer<RunicTrait> SERIALIZER = new Serializer<>(SilentGems.getId("runic"), RunicTrait::new);

    public RunicTrait(ResourceLocation id) {
        super(id, SERIALIZER);
    }

    @Override
    public float onAttackEntity(TraitActionContext context, LivingEntity target, float baseValue) {
        final float magicDamage = GearData.getStat(context.getGear(), ItemStats.MAGIC_DAMAGE);
//        ServerTicks.scheduleAction(() -> {
//            // FIXME: infinite loop
//            target.hurtResistantTime = 0;
//            target.attackEntityFrom(new EntityDamageSource("magic", context.getPlayer()), magicDamage / 2);
//        });
//        return super.onAttackEntity(context, target, baseValue);
        return baseValue + magicDamage / 2;
    }

    @Override
    public float onGetStat(TraitActionContext context, ItemStat stat, float value, float damageRatio) {
        return super.onGetStat(context, stat, value, damageRatio);
    }
}
