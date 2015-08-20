package net.silentchaos512.gems.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.silentchaos512.gems.item.ChaosGem;

public class MessageDisableFlight implements IMessage {

  @Override
  public void fromBytes(ByteBuf buf) {

  }

  @Override
  public void toBytes(ByteBuf buf) {

  }

  public static class Handler implements IMessageHandler<MessageDisableFlight, IMessage> {

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(MessageDisableFlight message, MessageContext ctx) {

      if (ctx.side == Side.CLIENT) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ChaosGem.removeFlight(player);
      }
      
      return null;
    }
  }
}
