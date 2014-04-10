package silent.gems.core.handler;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.network.packet.Packet;
import silent.gems.control.PlayerInputMap;
import silent.gems.lib.Reference;
import silent.gems.network.PacketTypeHandler;
import silent.gems.network.packet.PacketPlayerUpdate;
import silent.gems.network.packet.PacketSG;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.Player;


public class ClientTickHandler implements ITickHandler {

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        
        // TODO
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {

        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            PlayerInputMap inputMap = PlayerInputMap.getInputMapFor(player.username);
            inputMap.forwardKey = Math.signum(player.movementInput.moveForward);
            inputMap.strafeKey = Math.signum(player.movementInput.moveStrafe);
            inputMap.jumpKey = player.movementInput.jump;
            inputMap.sneakKey = player.movementInput.sneak;
            inputMap.motionX = player.motionX;
            inputMap.motionY = player.motionY;
            inputMap.motionZ = player.motionZ;
            
            if (inputMap.hasChanged()) {
                inputMap.refresh();
                PacketSG inputPacket = new PacketPlayerUpdate((Player) player, inputMap);
                //player.sendQueue.addToSendQueue(inputPacket.getPacket131());
                player.sendQueue.addToSendQueue(PacketTypeHandler.populatePacket(inputPacket));
            }
        }
    }

    @Override
    public EnumSet<TickType> ticks() {

        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {

        return Reference.MOD_ID + ":ClientTick";
    }

}
