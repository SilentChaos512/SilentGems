package net.silentchaos512.gems.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.silentchaos512.gems.SilentGems;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class NetworkEvents {
    private static final int SYNC_PACKET_FREQUENCY = SilentGems.isDevBuild() ? 20 : 600;

    private NetworkEvents() {}

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player instanceof EntityPlayerMP && event.player.ticksExisted % SYNC_PACKET_FREQUENCY == 0 ) {
            GeneralSyncPacket packet = new GeneralSyncPacket(event.player);
            Network.channel.sendTo(packet, ((EntityPlayerMP) event.player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
