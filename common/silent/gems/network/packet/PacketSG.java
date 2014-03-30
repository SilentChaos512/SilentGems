package silent.gems.network.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet131MapData;
import silent.gems.SilentGems;
import silent.gems.network.PacketTypeHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public abstract class PacketSG {

    public PacketTypeHandler packetType;
    public boolean isChunkDataPacket;

    public PacketSG(PacketTypeHandler packetType, boolean isChunkDataPacket) {

        this.packetType = packetType;
        this.isChunkDataPacket = isChunkDataPacket;
    }

    public byte[] populate() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            dos.writeByte(packetType.ordinal());
            this.writeData(dos);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return bos.toByteArray();
    }

    public void readPopulate(DataInputStream data) {

        try {
            this.readData(data);
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void readData(DataInputStream data) throws IOException {

    }

    public void writeData(DataOutputStream dos) throws IOException {

    }

    public void execute(INetworkManager network, Player player) {

    }

    public void setKey(int key) {

    }
    
//    @SideOnly(Side.CLIENT)
//    public void handleClient(EntityClientPlayerMP player) {
//        
//    }
//    
//    public void handleServer(EntityPlayerMP player) {
//        
//    }
    
    public Packet131MapData getPacket131() {
        
        return PacketDispatcher.getTinyPacket((Object) SilentGems.instance, (short) packetType.ordinal(), populate());
    }
}
