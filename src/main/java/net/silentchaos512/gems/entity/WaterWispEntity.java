package net.silentchaos512.gems.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.entity.projectile.AbstractWispShotEntity;
import net.silentchaos512.gems.entity.projectile.WaterWispShotEntity;
import net.silentchaos512.gems.lib.WispTypes;

public class WaterWispEntity extends AbstractWispEntity {
    public WaterWispEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public WispTypes getWispType() {
        return WispTypes.WATER;
    }

    @Override
    protected AbstractWispShotEntity getProjectile(AbstractWispEntity wisp, double accelX, double accelY, double accelZ) {
        return new WaterWispShotEntity(WispTypes.WATER.getShotType(), wisp, accelX, accelY, accelZ, wisp.world);
    }
}
