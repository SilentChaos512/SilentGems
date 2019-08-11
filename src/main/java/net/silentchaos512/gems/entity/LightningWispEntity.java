package net.silentchaos512.gems.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.entity.projectile.AbstractWispShotEntity;
import net.silentchaos512.gems.entity.projectile.LightningWispShotEntity;
import net.silentchaos512.gems.lib.WispTypes;

public class LightningWispEntity extends AbstractWispEntity {
    public LightningWispEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public WispTypes getWispType() {
        return WispTypes.LIGHTNING;
    }

    @Override
    protected AbstractWispShotEntity getProjectile(AbstractWispEntity wisp, double accelX, double accelY, double accelZ) {
        return new LightningWispShotEntity(WispTypes.LIGHTNING.getShotType(), wisp, accelX, accelY, accelZ, wisp.world);
    }
}
