package net.silentchaos512.gems.entity.packet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket.ColorPair;
import net.silentchaos512.lib.util.Color;

public class EntityPacketLevitation extends EntityChaosNodePacket {

  public static final int DURATION_MIN = 60;
  public static final int DURATION_MAX = 120;

  public static final Color COLOR_HEAD = new Color(0f, 0.75f, 0f);
  public static final Color COLOR_TAIL = new Color(0f, 1f, 0f);
  public static final ColorPair COLOR_PAIR = new ColorPair(COLOR_HEAD, COLOR_TAIL);
  public static final int COLOR_INDEX = 5;

  public EntityPacketLevitation(World worldIn, EntityLivingBase target) {

    this(worldIn, target,
        DURATION_MIN + SilentGems.random.nextInt(DURATION_MAX - DURATION_MIN + 1));
  }

  public EntityPacketLevitation(World worldIn, EntityLivingBase target, int duration) {

    super(worldIn, target);
    this.amount = duration;
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    entity.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, (int) amount, 1));
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
