package silent.gems.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import silent.gems.lib.Reference;
import silent.gems.network.packet.PacketPlayerUpdate;
import silent.gems.network.packet.PacketSG;
import silent.gems.network.packet.PacketTest;


public enum PacketTypeHandler {

    PLAYER_UPDATE(PacketPlayerUpdate.class),
    TEST_PACKET(PacketTest.class);
    
    private Class <? extends PacketSG> clazz;
    
    PacketTypeHandler(Class <? extends PacketSG> clazz) {
        
        this.clazz = clazz;
    }
    
    public static PacketSG buildPacket(byte[] data) {
        
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        int selector = bis.read();
        DataInputStream dis = new DataInputStream(bis);
        
        PacketSG packet = null;
        
        try {
            packet = values()[selector].clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
        
        packet.readPopulate(dis);
        
        return packet;
    }
    
    public static PacketSG buildPacket(PacketTypeHandler type) {
        
        PacketSG packet = null;
        
        try {
            packet = values()[type.ordinal()].clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
        
        return packet;
    }
    
    public static Packet populatePacket(PacketSG packet) {
        
        byte[] data = packet.populate();
        
        Packet250CustomPayload packet250 = new Packet250CustomPayload();
        packet250.channel = Reference.CHANNEL_NAME;
        packet250.data = data;
        packet250.length = data.length;
        packet250.isChunkDataPacket = packet.isChunkDataPacket;
        
        return packet250;
    }
}
