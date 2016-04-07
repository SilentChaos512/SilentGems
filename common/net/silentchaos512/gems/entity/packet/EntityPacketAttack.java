package net.silentchaos512.gems.entity.packet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.silentchaos512.gems.entity.packet.EntityChaosNodePacket.ColorPair;
import net.silentchaos512.lib.util.Color;

public class EntityPacketAttack extends EntityChaosNodePacket {

  public static final Color COLOR_HEAD = new Color(0.5f, 0f, 0f);
  public static final Color COLOR_TAIL = new Color(1f, 0f, 0f);
  public static final ColorPair COLOR_PAIR = new ColorPair(COLOR_HEAD, COLOR_TAIL);
  public static final int COLOR_INDEX = 3;

  public EntityPacketAttack(World worldIn, EntityLivingBase target, float amount) {

    super(worldIn, target);
    this.amount = amount;
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, null), amount);
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
