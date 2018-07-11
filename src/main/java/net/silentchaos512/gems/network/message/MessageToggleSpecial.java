package net.silentchaos512.gems.network.message;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.network.Message;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.StackHelper;

public class MessageToggleSpecial extends Message {

  @Override
  public IMessage handleMessage(MessageContext context) {

    if (context.side == Side.SERVER) {
      EntityPlayerMP player = context.getServerHandler().player;
      ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);

      if (StackHelper.isValid(mainHand) && mainHand.getItem() instanceof ITool) {
        if (ToolHelper.getToolTier(mainHand) == EnumMaterialTier.SUPER) {
          ToolHelper.toggleSpecialAbility(mainHand);
        }
      }
    }
    return null;
  }
}
