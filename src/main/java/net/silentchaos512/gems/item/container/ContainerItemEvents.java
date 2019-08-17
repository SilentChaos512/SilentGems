package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.SilentGems;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class ContainerItemEvents {
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        ItemStack itemOnGround = event.getItem().getItem();
        PlayerEntity player = event.getPlayer();

        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof IContainerItem && ((IContainerItem) stack.getItem()).canStore(itemOnGround)) {
                IItemHandler itemHandler = ((IContainerItem) stack.getItem()).getInventory(stack);
                itemOnGround = ItemHandlerHelper.insertItem(itemHandler, itemOnGround, false);
                ((IContainerItem) stack.getItem()).saveInventory(stack, itemHandler, player);
                event.getItem().getItem().setCount(itemOnGround.getCount());

                if (itemOnGround.isEmpty()) {
                    event.setResult(Event.Result.ALLOW);
                    event.getItem().remove();
                    return;
                }
            }
        }
    }
}
