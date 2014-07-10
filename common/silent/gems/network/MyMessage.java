package silent.gems.network;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;


public class MyMessage implements IMessage {
    
    private String text;
    
    public MyMessage() {
        
    }
    
    public MyMessage(String text) {
        
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<MyMessage, IMessage> {

        @Override
        public IMessage onMessage(MyMessage message, MessageContext ctx) {

            System.out.println(String.format("Received %s", message.text));
            return null;
        }
    }
}
