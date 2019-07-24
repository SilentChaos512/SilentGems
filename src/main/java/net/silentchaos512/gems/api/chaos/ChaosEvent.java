package net.silentchaos512.gems.api.chaos;

import net.minecraft.entity.Entity;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.utils.MathUtils;

import java.util.function.BiFunction;

public class ChaosEvent {
    private final float chance;
    private final int minChaos;
    private final int maxChaos;
    private final int chaosDissipated;
    private final BiFunction<Entity, Integer, Boolean> action;

    public ChaosEvent(float chance, int minChaos, int maxChaos, int chaosDissipated, BiFunction<Entity, Integer, Boolean> action) {
        this.chance = chance;
        this.minChaos = minChaos;
        this.maxChaos = maxChaos;
        this.chaosDissipated = chaosDissipated;
        this.action = action;
    }

    public boolean tryActivate(Entity entity, int chaos) {
        if (chaos > this.minChaos && tryChance(this.chance, chaos, this.maxChaos)) {
            return activate(entity, chaos);
        }
        return false;
    }

    public boolean activate(Entity entity, int chaos) {
        if (this.action.apply(entity, chaos)) {
            Chaos.dissipate(entity.world, this.chaosDissipated);
            return true;
        }
        return false;
    }

    private static boolean tryChance(float max, int chaos, int maxChaos) {
        float chance = Math.min(max * chaos / maxChaos, max);
        return MathUtils.tryPercentage(chance);
    }
}
