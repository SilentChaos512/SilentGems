package net.silentchaos512.gems.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.item.ChaosGem;

public class MessageSetFlight implements IMessage {
  
  private boolean value;
  
  public MessageSetFlight() {
    
    this(false);
  }
  
  public MessageSetFlight(boolean value) {
    
    this.value = value;
  }

  @Override
  public void fromBytes(ByteBuf buf) {

    this.value = ByteBufUtils.readVarShort(buf) != 0;
  }

  @Override
  public void toBytes(ByteBuf buf) {

    ByteBufUtils.writeVarShort(buf, this.value ? 1 : 0);
  }

  public static class Handler implements IMessageHandler<MessageSetFlight, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(MessageSetFlight message, MessageContext ctx) {

      if (ctx.side == Side.CLIENT) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (message.value) {
          player.capabilities.allowFlying = true;
        } else {
          ChaosGem.removeFlight(player);
        }
      }
      
      return null;
    }
  }
}
