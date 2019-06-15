package net.silentchaos512.gems.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.silentchaos512.gems.init.ModEffects;
import net.silentchaos512.gems.util.ModDamageSource;

import java.util.List;
import java.util.UUID;

public class ShockingEffect extends Effect {
    private static final int CONTINUOUS_DAMAGE_DELAY = 15;
    private static final int CONTINUOUS_DAMAGE_AMOUNT = 1;
    private static final int CHAIN_DELAY = 5;
    private static final int CHAIN_DISTANCE = 3;
    public static final UUID MOD_UUID = UUID.fromString("966ce179-1967-4b80-b894-bc743f1edbef");
    public static final String MOD_NAME = "silentgems:shocking_weakness";

    public static boolean CONTINUOUS_DAMAGE_ENABLED = true;
    public static boolean CHAINING_ENABLED = true;

    private final AttributeModifier modifier;

    public ShockingEffect() {
        super(EffectType.HARMFUL, 0xf5ff3d);
        modifier = new AttributeModifier(MOD_UUID, MOD_NAME, -0.3, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void performEffect(LivingEntity entity, int amplifier) {
        EffectInstance effect = entity.getActivePotionEffect(this);
        if (effect == null) return;

        int shockTimer = effect.getDuration();

        tryChaining(entity, shockTimer, amplifier);

        if (!entity.world.isRemote && CONTINUOUS_DAMAGE_ENABLED && !isGrounded(entity)) {
            // Continuous shock damage
            // Add entity ID for a bit of "randomness" in the delay
            if ((shockTimer + entity.getEntityId()) % CONTINUOUS_DAMAGE_DELAY == 0) {
                entity.attackEntityFrom(ModDamageSource.SHOCKING, CONTINUOUS_DAMAGE_AMOUNT);
            }
        }

        // Spawn shock effect particles.
//        Random rand = SilentGems.random;
//        for (int i = 0; i < 3 - SilentGems.proxy.getParticleSettings(); ++i) {
//            double posX = entity.posX + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
//            double posY = entity.posY + 1.1f * rand.nextFloat() * entity.height;
//            double posZ = entity.posZ + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
//            double motionX = 0.02 * rand.nextGaussian();
//            double motionY = 0.05 + Math.abs(0.1 * rand.nextGaussian());
//            double motionZ = 0.02 * rand.nextGaussian();
//            SilentGems.proxy.spawnParticles(EnumModParticles.SHOCKING, new Color(0xffef63),
//                    entity.world, posX, posY, posZ, motionX, motionY, motionZ);
//        }
    }

    private void tryChaining(LivingEntity source, int sourceDuration, int amplifier) {
        if (source.world.isRemote || !CHAINING_ENABLED) return;

        int halfTime = sourceDuration / 2;
        // Chain to nearby mobs. Half this mob's time and effect level 1.
        if (sourceDuration % CHAIN_DELAY == 0 && halfTime > 0) {
            for (LivingEntity target : getChainableEntities(source)) {
                EffectInstance effect = target.getActivePotionEffect(this);
                if (effect == null) {
                    target.addPotionEffect(new EffectInstance(this, halfTime, 0, true, false));
                }
            }
        }
    }

    private static List<LivingEntity> getChainableEntities(LivingEntity source) {
        AxisAlignedBB box = new AxisAlignedBB(
                source.getPosition().add(-CHAIN_DISTANCE, -CHAIN_DISTANCE, -CHAIN_DISTANCE),
                source.getPosition().add(CHAIN_DISTANCE, CHAIN_DISTANCE, CHAIN_DISTANCE));
        return source.world.getEntitiesWithinAABB(LivingEntity.class, box, e1 -> e1 != source && !(e1 instanceof PlayerEntity));
    }

    private static boolean isGrounded(LivingEntity entity) {
        return entity.getActivePotionEffect(ModEffects.grounded) != null;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entity, AbstractAttributeMap attributeMap, int amplifier) {
        if (isGrounded(entity)) return;

        IAttributeInstance iattributeinstance = attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance != null) {
            double amount = modifier.getAmount() * (amplifier + 1);
            // Halve the effect on players.
            if (entity instanceof PlayerEntity)
                amount /= 2.0;
            iattributeinstance.removeModifier(modifier);
            iattributeinstance.applyModifier(new AttributeModifier(modifier.getID(),
                    this.getName() + " " + amplifier, amount, modifier.getOperation()));
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entity, AbstractAttributeMap attributeMap, int amplifier) {
        IAttributeInstance iattributeinstance = attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance != null) {
            iattributeinstance.removeModifier(modifier);
        }
    }
}
