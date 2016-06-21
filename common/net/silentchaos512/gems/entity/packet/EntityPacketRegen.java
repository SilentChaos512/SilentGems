package net.silentchaos512.gems.entity.packet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket.ColorPair;
import net.silentchaos512.lib.util.Color;

public class EntityPacketRegen extends EntityChaosNodePacket {

  public static final int DURATION_MIN = 60;
  public static final int DURATION_MAX = 150;

  public static final Color COLOR_HEAD = new Color(0xFF8FD8);
  public static final Color COLOR_TAIL = new Color(0xFFC7EB);
  public static final ColorPair COLOR_PAIR = new ColorPair(COLOR_HEAD, COLOR_TAIL);
  public static final int COLOR_INDEX = 4;

  public EntityPacketRegen(World worldIn, EntityLivingBase target) {

    this(worldIn, target, DURATION_MIN + SilentGems.random.nextInt(DURATION_MAX - DURATION_MIN));
  }

  public EntityPacketRegen(World worldIn, EntityLivingBase target, int amount) {

    super(worldIn, target);
    this.amount = amount;
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    entity.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, (int) amount, 1));
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
