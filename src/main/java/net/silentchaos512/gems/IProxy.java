package net.silentchaos512.gems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nullable;

public interface IProxy {
    @Nullable
    default PlayerEntity getClientPlayer() {
        return null;
    }

    MinecraftServer getServer();
}
