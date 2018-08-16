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

import java.util.Random;
import java.util.UUID;

public class PotionFreezing extends Potion {

  public static final int CONTINUOUS_DAMAGE_DELAY = 40;
  public static final int CONTINUOUS_DAMAGE_AMOUNT = 1;

  public static boolean CONTINUOUS_DAMAGE_ENABLED = true;

  public AttributeModifier modifier;

  public PotionFreezing() {

    super(true, 0x8cfbff);
    setPotionName("effect." + SilentGems.MODID + ".freezing");
    modifier = new AttributeModifier(UUID.fromString("c45e3a61-996a-4cea-977c-5d315365631a"), getName(), -0.5, 2);
  }

  @Override
  public void performEffect(EntityLivingBase entityLiving, int amplifier) {

    if (CONTINUOUS_DAMAGE_ENABLED) {
      // Continuous freeze damage.
      PotionEffect effect = entityLiving.getActivePotionEffect(this);
      if (effect == null)
        return;

      int damageDelay = CONTINUOUS_DAMAGE_DELAY;
      // Extra damage for mobs immune to fire (like blazes)
      if (entityLiving.isImmuneToFire())
        damageDelay /= 4;

      if (effect.getDuration() % damageDelay == 0) {
        int damageAmount = CONTINUOUS_DAMAGE_AMOUNT;
        entityLiving.attackEntityFrom(ModDamageSource.FREEZING, damageAmount);
      }
    }

    // Spawn freeze effect particles.
    Random rand = SilentGems.random;
    for (int i = 0; i < 2; ++i) {
      double posX = entityLiving.posX + 1.2f * (rand.nextFloat() - 0.5f) * entityLiving.width;
      double posY = entityLiving.posY + 1.1f * rand.nextFloat() * entityLiving.height;
      double posZ = entityLiving.posZ + 1.2f * (rand.nextFloat() - 0.5f) * entityLiving.width;
      double motionX = 0.005 * rand.nextGaussian();
      double motionY = 0.005 * rand.nextGaussian();
      double motionZ = 0.005 * rand.nextGaussian();
      SilentGems.proxy.spawnParticles(EnumModParticles.FREEZING, new Color(0x76e3f2),
          entityLiving.world, posX, posY, posZ, motionX, motionY, motionZ);
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    // DoT delay can vary by mob, so we handle that in performEffect instead.
    return true;
  }

  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase entityLiving,
      AbstractAttributeMap attributeMap, int amplifier) {

    IAttributeInstance iattributeinstance = attributeMap
        .getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);

    if (iattributeinstance != null) {
      double amount = modifier.getAmount() * (amplifier + 1);
      // Halve the slowing effect on players.
      if (entityLiving instanceof EntityPlayer)
        amount /= 2.0;
      iattributeinstance.removeModifier(modifier);
      iattributeinstance.applyModifier(new AttributeModifier(modifier.getID(),
          this.getName() + " " + amplifier, amount, modifier.getOperation()));
    }
  }

  @Override
  public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn,
      AbstractAttributeMap attributeMapIn, int amplifier) {

    IAttributeInstance iattributeinstance = attributeMapIn
        .getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED);

    if (iattributeinstance != null) {
      iattributeinstance.removeModifier(modifier);
    }
  }
}
