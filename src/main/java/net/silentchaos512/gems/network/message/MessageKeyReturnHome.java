package net.silentchaos512.gems.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.gems.compat.BaublesCompat;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.network.Message;
import net.silentchaos512.lib.event.ServerTicks;
import net.silentchaos512.lib.util.PlayerHelper;

import java.util.function.Predicate;

public class MessageKeyReturnHome extends Message {
    public MessageKeyReturnHome() {
    }

    @Override
    public IMessage handleMessage(MessageContext ctx) {
        if (ctx.side != Side.SERVER)
            return null;

        Predicate<ItemStack> predicate = s -> s.getItem() == ModItems.returnHomeCharm;
        EntityPlayer player = ctx.getServerHandler().player;
        NonNullList<ItemStack> stacks = BaublesCompat.getBaubles(player, predicate);
        stacks.addAll(PlayerHelper.getNonEmptyStacks(player, predicate));

        if (!stacks.isEmpty())
            ServerTicks.scheduleAction(() -> ModItems.returnHomeCharm.tryTeleportPlayer(stacks.get(0), player));

        return null;
    }
}
