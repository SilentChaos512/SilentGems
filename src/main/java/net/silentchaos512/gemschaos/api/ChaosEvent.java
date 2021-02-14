package net.silentchaos512.gemschaos.api;

import net.minecraft.entity.player.PlayerEntity;
import net.silentchaos512.utils.MathUtils;

import java.util.function.BiFunction;

public class ChaosEvent {
    private final float chance;
    private final int cooldownTime;
    private final int minChaos;
    private final int maxChaos;
    private final int chaosDissipated;
    private final String configComment;
    private final BiFunction<PlayerEntity, Integer, Boolean> action;

    public ChaosEvent(float chance, int cooldownTimeInSeconds, int minChaos, int maxChaos, int chaosDissipated, String configComment, BiFunction<PlayerEntity, Integer, Boolean> action) {
        this.chance = chance;
        this.cooldownTime = cooldownTimeInSeconds;
        this.minChaos = minChaos;
        this.maxChaos = maxChaos;
        this.chaosDissipated = chaosDissipated;
        this.configComment = configComment;
        this.action = action;
    }

    public String getConfigComment() {
        return configComment;
    }

    public boolean tryActivate(PlayerEntity entity, int chaos) {
        if (chaos > this.minChaos && tryChance(this.chance, chaos, this.maxChaos)) {
            return activate(entity, chaos);
        }
        return false;
    }

    public boolean activate(PlayerEntity entity, int chaos) {
        if (this.action.apply(entity, chaos)) {
            ChaosApi.Chaos.dissipate(entity, this.chaosDissipated);
            return true;
        }
        return false;
    }

    private static boolean tryChance(float max, int chaos, int maxChaos) {
        float chance = Math.min(max * chaos / maxChaos, max);
        return MathUtils.tryPercentage(chance);
    }

    public int getCooldownTime() {
        return cooldownTime;
    }
}
