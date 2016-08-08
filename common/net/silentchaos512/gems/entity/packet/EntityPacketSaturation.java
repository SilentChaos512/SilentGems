package net.silentchaos512.gems.entity.packet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket.ColorPair;
import net.silentchaos512.lib.util.Color;

public class EntityPacketSaturation extends EntityChaosNodePacket {

  public static int AMOUNT_MIN = 1;
  public static int AMOUNT_MAX = 3;

  public static final Color COLOR_HEAD = new Color(0.5f, 0.2f, 0f);
  public static final Color COLOR_TAIL = new Color(0.6f, 0.3f, 0f);
  public static final ColorPair COLOR_PAIR = new ColorPair(COLOR_HEAD, COLOR_TAIL);
  public static final int COLOR_INDEX = 6;

  public EntityPacketSaturation(World worldIn, EntityLivingBase target) {

    this(worldIn, target, AMOUNT_MIN + SilentGems.random.nextInt(AMOUNT_MAX - AMOUNT_MIN + 1));
  }

  public EntityPacketSaturation(World worldIn, EntityLivingBase target, int amount) {

    super(worldIn, target);
    this.amount = amount;
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    entity.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 1, (int) amount));
    entity.worldObj.playSound(null, entity.getPosition(), SoundEvents.ENTITY_PLAYER_BURP, null,
        1.0f, 1.2f + (float) (0.05 * rand.nextGaussian()));
    super.onImpactWithEntity(entity);
  }

  @Override
  public Color getColorHead() {

    return COLOR_HEAD;
  }

  @Override
  public Color getColorTail() {

    return COLOR_TAIL;
  }

  @Override
  public ColorPair getColorPair() {

    return COLOR_PAIR;
  }

  @Override
  public int getColorIndex() {

    return COLOR_INDEX;
  }
}
