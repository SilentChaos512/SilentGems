package silent.gems.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import silent.gems.control.PlayerInputMap;
import silent.gems.core.util.LogHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;


public class MessagePlayerUpdate implements IMessage {

    private String username;
    private PlayerInputMap inputMap;
    
    public MessagePlayerUpdate() {

    }
    
    public MessagePlayerUpdate(EntityPlayer player, PlayerInputMap inputMap) {
        
        this.username = player.getCommandSenderName();
        this.inputMap = inputMap;
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {

        this.username = ByteBufUtils.readUTF8String(buf);
        this.inputMap = PlayerInputMap.getInputMapFor(this.username);
        this.inputMap.readFromStream(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, this.username);
        this.inputMap.writeToStream(buf);
    }

    public static class Handler implements IMessageHandler<MessagePlayerUpdate, IMessage> {

        @Override
        public IMessage onMessage(MessagePlayerUpdate message, MessageContext ctx) {

            LogHelper.derpRand();
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                player.motionX = message.inputMap.motionX;
                player.motionY = message.inputMap.motionY;
                player.motionZ = message.inputMap.motionZ;
            }
            return null;
        }
    }
}
