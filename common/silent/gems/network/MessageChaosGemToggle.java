package silent.gems.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import silent.gems.item.ChaosGem;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;


public class MessageChaosGemToggle implements IMessage {
    
    private String username;
    
    public MessageChaosGemToggle() {
        
    }
    
    public MessageChaosGemToggle(EntityPlayer player) {
        
        this.username = player.getCommandSenderName();
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        this.username = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        ByteBufUtils.writeUTF8String(buf, this.username);
    }

    public static class Handler implements IMessageHandler<MessageChaosGemToggle, IMessage> {

        @Override
        public IMessage onMessage(MessageChaosGemToggle message, MessageContext ctx) {

            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().playerEntity;
                // Find first chaos gem in player's inventory and toggle it.
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (stack != null && stack.getItem() instanceof ChaosGem) {
                        ((ChaosGem) stack.getItem()).onItemRightClick(stack, player.worldObj, player);
                        return null;
                    }
                }
            }
            
            return null;
        }
    }
}
