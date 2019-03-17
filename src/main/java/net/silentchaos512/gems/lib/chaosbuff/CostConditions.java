package net.silentchaos512.gems.lib.chaosbuff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public enum CostConditions {
    BURNING(Entity::isBurning),
    FLYING(p -> p.abilities.isFlying),
    HURT(p -> p.getHealth() < p.getMaxHealth() - 0.5f),
    IN_AIR(p -> !p.onGround),
    MOVING(CostConditions::hasMoved),
    UNDERWATER(Entity::isInWater);

    private final Predicate<EntityPlayer> condition;

    CostConditions(Predicate<EntityPlayer> condition) {
        this.condition = condition;
    }

    public boolean appliesTo(EntityPlayer player) {
        return this.condition.test(player);
    }

    @Nullable
    public static CostConditions from(String str) {
        for (CostConditions c : values()) {
            if (c.name().equalsIgnoreCase(str)) {
                return c;
            }
        }
        return null;
    }

    private static boolean hasMoved(EntityPlayer player) {
        // FIXME: does not work
//        double dx = player.prevPosX - player.posX;
//        double dz = player.prevPosZ - player.posZ;
//        return dx * dx + dz * dz > 0.01;
        return true;
    }
}
