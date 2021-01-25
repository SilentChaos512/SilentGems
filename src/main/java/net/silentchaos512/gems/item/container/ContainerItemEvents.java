package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.silentchaos512.gems.GemsBase;

@Mod.EventBusSubscriber(modid = GemsBase.MOD_ID)
public final class ContainerItemEvents {
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        ItemStack itemOnGround = event.getItem().getItem();
        int initialCount = itemOnGround.getCount();
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
                    break;
                }
            }
        }

        if (itemOnGround.getCount() != initialCount) {
            float pitch = ((player.world.rand.nextFloat() - player.world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F;
            player.world.playSound(null, player.getPosX(), player.getPosY() + 0.5, player.getPosZ(),
                    SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, pitch);
        }
    }
}
