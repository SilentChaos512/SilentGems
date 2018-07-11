package net.silentchaos512.gems.network.message;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiChaosBar;
import net.silentchaos512.gems.client.handler.ClientTickHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.network.Message;

public class MessageDataSync extends Message {

  public NBTTagCompound tags;

  public MessageDataSync() {

  }

  public MessageDataSync(PlayerData data) {

    tags = new NBTTagCompound();
    data.writeToNBT(tags);
    ;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public IMessage handleMessage(MessageContext context) {

    ClientTickHandler.scheduledActions.add(() -> {
      PlayerData data = PlayerDataHandler.get(SilentGems.proxy.getClientPlayer());
      data.readFromNBT(tags);
      GuiChaosBar.INSTANCE.update(data.chaos, data.maxChaos);
    });

    return null;
  }
}
