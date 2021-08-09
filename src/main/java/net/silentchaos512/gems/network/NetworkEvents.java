package net.silentchaos512.gems.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.GemsBase;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID)
public final class NetworkEvents {
    private static final int SYNC_PACKET_FREQUENCY = GemsBase.isDevBuild() ? 20 : 600;

    private NetworkEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof ServerPlayer && event.player.tickCount % SYNC_PACKET_FREQUENCY == 0 ) {
//            GeneralSyncPacket packet = new GeneralSyncPacket(event.player);
//            Network.channel.sendTo(packet, ((ServerPlayerEntity) event.player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
