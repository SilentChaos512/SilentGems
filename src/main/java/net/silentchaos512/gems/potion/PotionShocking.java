package net.silentchaos512.gems.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ModDamageSource;
import net.silentchaos512.lib.util.Color;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PotionShocking extends Potion {
    private static final int CONTINUOUS_DAMAGE_DELAY = 15;
    private static final int CONTINUOUS_DAMAGE_AMOUNT = 1;
    private static final int CHAIN_DELAY = 5;
    private static final double CHAIN_RADIUS_SQUARED = 2.5 * 2.5;

    public static boolean CONTINUOUS_DAMAGE_ENABLED = true;
    public static boolean CHAINING_ENABLED = true;

    private final AttributeModifier modifier;

    public PotionShocking() {
        super(true, 0xf5ff3d);
//        setPotionName("effect." + SilentGems.MOD_ID + ".shocking");
        modifier = new AttributeModifier(UUID.fromString("966ce179-1967-4b80-b894-bc743f1edbef"), getName(), -0.3, 2);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        PotionEffect effect = entity.getActivePotionEffect(this);
        if (effect == null) return;

        int shockTimer = effect.getDuration();

        tryChaining(entity, shockTimer, amplifier);

        if (!entity.world.isRemote && CONTINUOUS_DAMAGE_ENABLED) {
            // Continuous shock damage
            // Add entity ID for a bit of "randomness" in the delay
            if ((shockTimer + entity.getEntityId()) % CONTINUOUS_DAMAGE_DELAY == 0) {
                entity.attackEntityFrom(ModDamageSource.SHOCKING, CONTINUOUS_DAMAGE_AMOUNT);
            }
        }

        // Spawn shock effect particles.
        Random rand = SilentGems.random;
        for (int i = 0; i < 3 - SilentGems.proxy.getParticleSettings(); ++i) {
            double posX = entity.posX + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
            double posY = entity.posY + 1.1f * rand.nextFloat() * entity.height;
            double posZ = entity.posZ + 1.2f * (rand.nextFloat() - 0.5f) * entity.width;
            double motionX = 0.02 * rand.nextGaussian();
            double motionY = 0.05 + Math.abs(0.1 * rand.nextGaussian());
            double motionZ = 0.02 * rand.nextGaussian();
            SilentGems.proxy.spawnParticles(EnumModParticles.SHOCKING, new Color(0xffef63),
                    entity.world, posX, posY, posZ, motionX, motionY, motionZ);
        }
    }

    private void tryChaining(EntityLivingBase source, int sourceDuration, int amplifier) {
        if (source.world.isRemote || !CHAINING_ENABLED) return;

        int halfTime = sourceDuration / 2;
        // Chain to nearby mobs. Half this mob's time and effect level 1.
        if (sourceDuration % CHAIN_DELAY == 0 && halfTime > 0) {
            for (EntityLivingBase target : getChainableEntities(source)) {
                PotionEffect effect = target.getActivePotionEffect(this);
                if (effect == null) {
                    target.addPotionEffect(new PotionEffect(this, halfTime, 0, true, false));
                }
            }
        }
    }

    private static List<EntityLivingBase> getChainableEntities(EntityLivingBase source) {
        return source.world.getEntities(EntityLivingBase.class, e ->
                e.getDistanceSq(source) < CHAIN_RADIUS_SQUARED && !(e instanceof EntityPlayer));
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, AbstractAttributeMap attributeMap, int amplifier) {
        IAttributeInstance iattributeinstance = attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance != null) {
            double amount = modifier.getAmount() * (amplifier + 1);
            // Halve the effect on players.
            if (entity instanceof EntityPlayer)
                amount /= 2.0;
            iattributeinstance.removeModifier(modifier);
            iattributeinstance.applyModifier(new AttributeModifier(modifier.getID(),
                    this.getName() + " " + amplifier, amount, modifier.getOperation()));
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, AbstractAttributeMap attributeMap, int amplifier) {
        IAttributeInstance iattributeinstance = attributeMap.getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

        if (iattributeinstance != null) {
            iattributeinstance.removeModifier(modifier);
        }
    }
}
