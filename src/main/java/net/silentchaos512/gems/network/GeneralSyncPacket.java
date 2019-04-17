package net.silentchaos512.gems.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.chaos.ChaosSourceCapability;
import net.silentchaos512.gems.client.ClientPlayerInfo;

import java.util.function.Supplier;

public class GeneralSyncPacket {
    private int playerChaos;
    private int worldChaos;
    private int equilibrium;

    public GeneralSyncPacket() {
    }

    public GeneralSyncPacket(int playerChaos, int worldChaos, int equilibrium) {
        this.playerChaos = playerChaos;
        this.worldChaos = worldChaos;
        this.equilibrium = equilibrium;
    }

    public GeneralSyncPacket(EntityPlayer player) {
        player.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                this.playerChaos = source.getChaos());
        player.world.getCapability(ChaosSourceCapability.INSTANCE).ifPresent(source ->
                this.worldChaos = source.getChaos());
        this.equilibrium = Chaos.getEquilibriumPoint(player.world);
    }

    public static GeneralSyncPacket fromBytes(PacketBuffer buf) {
        GeneralSyncPacket packet = new GeneralSyncPacket();
        packet.playerChaos = buf.readVarInt();
        packet.worldChaos = buf.readVarInt();
        packet.equilibrium = buf.readVarInt();
        return packet;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeVarInt(this.playerChaos);
        buf.writeVarInt(this.worldChaos);
        buf.writeVarInt(this.equilibrium);
    }

    public static void handle(GeneralSyncPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientPlayerInfo.playerChaos = packet.playerChaos;
            ClientPlayerInfo.worldChaos = packet.worldChaos;
            ClientPlayerInfo.equilibriumChaos = packet.equilibrium;
        });
        context.get().setPacketHandled(true);
    }
}
