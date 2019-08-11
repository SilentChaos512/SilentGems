package net.silentchaos512.gems.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.entity.projectile.AbstractWispShotEntity;
import net.silentchaos512.gems.entity.projectile.FireWispShotEntity;
import net.silentchaos512.gems.lib.WispTypes;

public class FireWispEntity extends AbstractWispEntity {
    public FireWispEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public WispTypes getWispType() {
        return WispTypes.FIRE;
    }

    @Override
    protected AbstractWispShotEntity getProjectile(AbstractWispEntity wisp, double accelX, double accelY, double accelZ) {
        return new FireWispShotEntity(WispTypes.FIRE.getShotType(), wisp, accelX, accelY, accelZ, wisp.world);
    }
}
