package net.silentchaos512.gems.entity.packet;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.silentchaos512.lib.util.Color;

public class EntityPacketAttack extends EntityChaosNodePacket {

  public static final Color COLOR_HEAD = new Color(0.5f, 0f, 0f);
  public static final Color COLOR_TAIL = new Color(1f, 0f, 0f);

  public EntityPacketAttack(World worldIn, EntityLivingBase target, float amount) {

    super(worldIn, target);
    this.amount = amount;
    this.colorHead = COLOR_HEAD;
    this.colorTail = COLOR_TAIL;
  }

  @Override
  public void onImpactWithEntity(EntityLivingBase entity) {

    entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, null), amount);
    super.onImpactWithEntity(entity);
  }
}
