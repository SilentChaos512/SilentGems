package silent.gems.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import silent.gems.core.util.PlayerHelper;
import silent.gems.network.PacketTypeHandler;
import cpw.mods.fml.common.network.Player;


public class PacketTest extends PacketSG {

    String message = "";
    
    public PacketTest() {
        
        super(PacketTypeHandler.TEST_PACKET, false);
    }
    
    public PacketTest(String message) {
        
        super(PacketTypeHandler.TEST_PACKET, false);
        this.message = message;
    }
    
    public PacketTest(PacketTypeHandler packetType, boolean isChunkDataPacket) {

        super(packetType, isChunkDataPacket);
    }
    
    @Override
    public void readData(DataInputStream data) throws IOException {
        
        message = data.readUTF();
    }
    
    @Override
    public void writeData(DataOutputStream data) throws IOException {
        
        data.writeUTF(message);
    }
    
    @Override
    public void execute(INetworkManager manager, Player player) {
        
        EntityPlayer entityPlayer = (EntityPlayer) player;
        PlayerHelper.addChatMessage(entityPlayer, message, false);
    }
}
