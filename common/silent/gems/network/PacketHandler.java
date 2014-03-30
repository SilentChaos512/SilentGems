package silent.gems.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import silent.gems.network.packet.PacketSG;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {

        PacketSG packetSG = PacketTypeHandler.buildPacket(packet.data);
        packetSG.execute(manager, player);
    }

}
