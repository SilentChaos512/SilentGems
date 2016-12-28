package net.silentchaos512.gems.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ItemChaosGem;
import net.silentchaos512.gems.network.Message;
import net.silentchaos512.lib.util.PlayerHelper;

public class MessageToggleChaosGem extends Message {

  public boolean all;
 
  public MessageToggleChaosGem() {

    this.all = false;
  }

  public MessageToggleChaosGem(boolean all) {

    this.all = all;
  }

  @Override
  public IMessage handleMessage(MessageContext context) {

    if (context.side != Side.SERVER)
      return null;

    EntityPlayer player = context.getServerHandler().playerEntity;
    for (ItemStack stack : PlayerHelper.getNonNullStacks(player)) {
      if (stack.getItem() instanceof ItemChaosGem) {
        ItemChaosGem item = (ItemChaosGem) stack.getItem();
        item.setEnabled(stack, !item.isEnabled(stack));
        if (!all)
          return null;
      }
    }

    return null;
  }
}
