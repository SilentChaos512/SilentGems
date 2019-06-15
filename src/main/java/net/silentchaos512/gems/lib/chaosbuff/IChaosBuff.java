package net.silentchaos512.gems.lib.chaosbuff;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface IChaosBuff {
    ResourceLocation getId();

    void applyTo(PlayerEntity player, int level);

    void removeFrom(PlayerEntity player);

    int getChaosGenerated(@Nullable PlayerEntity player, int level);

    default int getActiveChaosGenerated(int level) {
        return getChaosGenerated(null, level);
    }

    int getMaxLevel();

    int getSlotsForLevel(int level);

    boolean isActive(PlayerEntity player);

    ITextComponent getDisplayName(int level);

    int getRuneColor();

    IChaosBuffSerializer<?> getSerializer();
}
