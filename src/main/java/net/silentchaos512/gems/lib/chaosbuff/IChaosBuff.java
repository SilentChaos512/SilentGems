package net.silentchaos512.gems.lib.chaosbuff;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public interface IChaosBuff {
    ResourceLocation getId();

    void applyTo(EntityPlayer player, int level);

    void removeFrom(EntityPlayer player);

    int getChaosGenerated(@Nullable EntityPlayer player, int level);

    default int getActiveChaosGenerated(int level) {
        return getChaosGenerated(null, level);
    }

    int getMaxLevel();

    int getSlotsForLevel(int level);

    boolean isActive(EntityPlayer player);

    ITextComponent getDisplayName(int level);

    int getRuneColor();

    IChaosBuffSerializer<?> getSerializer();
}
