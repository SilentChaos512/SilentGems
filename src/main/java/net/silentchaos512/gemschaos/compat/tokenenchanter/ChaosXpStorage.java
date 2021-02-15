package net.silentchaos512.gemschaos.compat.tokenenchanter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.silentchaos512.gemschaos.api.ChaosApi;
import net.silentchaos512.gemschaos.item.PlayerLinkedItem;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageItemImpl;

import javax.annotation.Nullable;
import java.util.UUID;

public class ChaosXpStorage extends XpStorageItemImpl {
    private static final int CHAOS_PER_LEVEL = 10_000; // TODO: make it a config?

    private final ItemStack stack;

    public ChaosXpStorage(ItemStack stack) {
        super(stack, Integer.MAX_VALUE, true);
        this.stack = stack;
    }

    @Override
    public float getLevels() {
        return getOwnerByUuid(this.stack) != null ? getCapacity() : 0;
    }

    @Override
    public void setLevels(float levels) {
    }

    @Override
    public void drainLevels(float amount) {
        if (amount > 0) {
            PlayerEntity player = getOwnerByUuid(this.stack);

            if (player != null) {
                ChaosApi.Chaos.generate(player, (int) (CHAOS_PER_LEVEL * amount), true);
            }
        }
    }

    @Nullable
    private static PlayerEntity getOwnerByUuid(ItemStack stack) {
        UUID uuid = PlayerLinkedItem.getOwnerUuid(stack);
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (uuid != null && server != null) {
            return server.getPlayerList().getPlayerByUUID(uuid);
        }
        return null;
    }
}
