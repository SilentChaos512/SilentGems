package net.silentchaos512.gems.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;
import net.silentchaos512.gems.client.ClientPlayerInfo;

import java.util.function.Supplier;

public class GeneralSyncPacket {
    private int playerChaos;
    private int worldChaos;

    public GeneralSyncPacket() {
    }

    public GeneralSyncPacket(int playerChaos, int worldChaos) {
        this.playerChaos = playerChaos;
        this.worldChaos = worldChaos;
    }

    public GeneralSyncPacket(EntityPlayer player) {
        player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                this.playerChaos = source.getChaos());
        player.world.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                this.worldChaos = source.getChaos());
    }

    public static GeneralSyncPacket fromBytes(PacketBuffer buf) {
        GeneralSyncPacket packet = new GeneralSyncPacket();
        packet.playerChaos = buf.readVarInt();
        packet.worldChaos = buf.readVarInt();
        return packet;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeVarInt(this.playerChaos);
        buf.writeVarInt(this.worldChaos);
    }

    public static void handle(GeneralSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientPlayerInfo.playerChaos = packet.playerChaos;
            ClientPlayerInfo.worldChaos = packet.worldChaos;
        });
        context.get().setPacketHandled(true);
    }
}
