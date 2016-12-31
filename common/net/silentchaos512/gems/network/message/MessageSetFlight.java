package net.silentchaos512.gems.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.network.Message;

public class MessageSetFlight extends Message {

  boolean value;

  public MessageSetFlight() {

    value = false;
  }

  public MessageSetFlight(boolean value) {

    this.value = value;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IMessage handleMessage(MessageContext ctx) {

    if (ctx.side != Side.CLIENT)
      return null;

    EntityPlayer player = Minecraft.getMinecraft().player;
    if (value)
      ChaosBuff.FLIGHT.applyToPlayer(player, 1, null);
    else
      ChaosBuff.FLIGHT.removeFromPlayer(player, 1, null);

    return null;
  }
}
