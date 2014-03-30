package silent.gems.network.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import silent.gems.control.PlayerInputMap;
import silent.gems.network.PacketTypeHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;


public class PacketPlayerUpdate extends PacketSG {

    String username;
    PlayerInputMap inputMap;
    
    public PacketPlayerUpdate() {
        
        super(PacketTypeHandler.PLAYER_UPDATE, false);
    }
    
    public PacketPlayerUpdate(Player player, PlayerInputMap inputMap) {
        
        super(PacketTypeHandler.PLAYER_UPDATE, false);
        this.username = ((EntityPlayer) player).username;
        this.inputMap = inputMap;
    }
    
    @Override
    public void readData(DataInputStream data) throws IOException {
        
        username = data.readUTF();
        inputMap = PlayerInputMap.getInputMapFor(username);
        inputMap.readFromStream(data);
    }
    
    @Override
    public void writeData(DataOutputStream data) throws IOException {
        
        data.writeUTF(username);
        inputMap.writeToStream(data);
    }
    
    @Override
    public void execute(INetworkManager manager, Player player) {
        
        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        PacketPlayerUpdate updatePacket = new PacketPlayerUpdate(player, inputMap);
        playerMP.motionX = inputMap.motionX;
        playerMP.motionY = inputMap.motionY;
        playerMP.motionZ = inputMap.motionZ;
        PacketDispatcher.sendPacketToAllAround(playerMP.posX, playerMP.posY, playerMP.posZ, 128, playerMP.dimension, updatePacket.getPacket131());
    }
}
