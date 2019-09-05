package net.silentchaos512.gems.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class ChaosLightningBoltEntity extends LightningBoltEntity {
    private int lightningState;

    public ChaosLightningBoltEntity(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z, true);
        this.lightningState = 2;
    }

    @Override
    public void tick() {
        super.tick();

        --this.lightningState;

        if (this.lightningState >= 0) {
            if (this.world.isRemote) {
                this.world.setLastLightningBolt(2);
            } else {
                List<Entity> list = this.world.getEntitiesInAABBexcluding(this, new AxisAlignedBB(this.posX - 3.0D, this.posY - 3.0D, this.posZ - 3.0D, this.posX + 3.0D, this.posY + 6.0D + 3.0D, this.posZ + 3.0D), Entity::isAlive);

                for(Entity entity : list) {
                    if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this))
                        entity.onStruckByLightning(this);
                }
            }
        }

    }
}
