package silent.gems.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet131MapData;
import net.minecraft.network.packet.Packet250CustomPayload;
import silent.gems.core.util.LogHelper;
import silent.gems.network.packet.PacketSG;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.ITinyPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler, ITinyPacketHandler {

    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {

        LogHelper.derp("PacketHandler.onPacketData");
        PacketSG packetSG = PacketTypeHandler.buildPacket(packet.data);
        packetSG.execute(manager, player);
    }

    @Override
    public void handle(NetHandler handler, Packet131MapData mapData) {
        
        // Why does this never get called D:
        LogHelper.derp("PacketHandler.handle");
        DataInputStream data = new DataInputStream(new ByteArrayInputStream(mapData.itemData));
        try {
            int packetType = mapData.uniqueID;
            EntityPlayer player = handler.getPlayer();
            // I don't know what's going on :(
        }
        catch (Exception ex) {
            LogHelper.debug("Problem reading Packet131: " + ex);
        }
    }
}
