package net.silentchaos512.gems.potion;

import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.enchantment.EnchantmentLightningAspect;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.util.ModDamageSource;
import net.silentchaos512.lib.util.Color;

public class PotionShocking extends Potion {

  public static final int CONTINUOUS_DAMAGE_DELAY = 15;
  public static final int CONTINUOUS_DAMAGE_AMOUNT = 1;
  public static final int CHAIN_DELAY = 5;
  public static final double CHAIN_RADIUS_SQUARED = 2.5 * 2.5;

  public static boolean CONTINUOUS_DAMAGE_ENABLED = true;
  public static boolean CHAINING_ENABLED = true;

  public AttributeModifier modifier;

  public PotionShocking() {

    super(true, 0xf5ff3d);
    String name = SilentGems.RESOURCE_PREFIX + "shocking";
    setPotionName("effect." + name);
    setRegistryName(new ResourceLocation(name));

    modifier = new AttributeModifier(UUID.fromString("966ce179-1967-4b80-b894-bc743f1edbef"),
        getName(), -0.3, 2);
  }

  @Override
  public void performEffect(EntityLivingBase entityLiving, int amplifier) {

    PotionEffect effect = entityLiving.getActivePotionEffect(this);
    if (effect == null)
      return;

    int shockTimer = effect.getDuration();

    if (!entityLiving.world.isRemote && CHAINING_ENABLED) {
      int halfTime = shockTimer / 2;
      // Chain to nearby mobs. Half this mob's time and effect level 1.
      if (shockTimer % CHAIN_DELAY == 0 && halfTime > 0) {
        for (EntityLivingBase nearby : entityLiving.world.getEntities(EntityLivingBase.class,
            e -> e.getDistanceSqToEntity(entityLiving) < CHAIN_RADIUS_SQUARED
                && !(e instanceof EntityPlayer))) {
          PotionEffect effectNearby = nearby.getActivePotionEffect(this);
          if (effectNearby == null) {
            nearby.addPotionEffect(new PotionEffect(this, halfTime, 0, true, false));
          }
        }
      }
    }

    if (!entityLiving.world.isRemote && CONTINUOUS_DAMAGE_ENABLED) {
      // Continuous shock damage.
      int damageDelay = CONTINUOUS_DAMAGE_DELAY;

      // Add entity ID for a bit of "randomness"
      if ((shockTimer + entityLiving.getEntityId()) % damageDelay == 0) {
        int damageAmount = CONTINUOUS_DAMAGE_AMOUNT;
        entityLiving.attackEntityFrom(ModDamageSource.SHOCKING, damageAmount);
      }
    }

    // Spawn shock effect particles.
    Random rand = SilentGems.random;
    for (int i = 0; i < 3 - SilentGems.proxy.getParticleSettings(); ++i) {
      double posX = entityLiving.posX + 1.2f * (rand.nextFloat() - 0.5f) * entityLiving.width;
      double posY = entityLiving.posY + 1.1f * rand.nextFloat() * entityLiving.height;
      double posZ = entityLiving.posZ + 1.2f * (rand.nextFloat() - 0.5f) * entityLiving.width;
      double motionX = 0.02 * rand.nextGaussian();
      double motionY = 0.05 + Math.abs(0.1 * rand.nextGaussian());
      double motionZ = 0.02 * rand.nextGaussian();
      SilentGems.proxy.spawnParticles(EnumModParticles.SHOCKING, new Color(0xffef63),
          entityLiving.world, posX, posY, posZ, motionX, motionY, motionZ);
    }
  }

  @Override
  public boolean isReady(int duration, int amplifier) {

    return true;
  }

  @Override
  public void applyAttributesModifiersToEntity(EntityLivingBase entityLiving,
      AbstractAttributeMap attributeMap, int amplifier) {

    IAttributeInstance iattributeinstance = attributeMap
        .getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

    if (iattributeinstance != null) {
      double amount = modifier.getAmount() * (amplifier + 1);
      // Halve the effect on players.
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
        .getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE);

    if (iattributeinstance != null) {
      iattributeinstance.removeModifier(modifier);
    }
  }
}
