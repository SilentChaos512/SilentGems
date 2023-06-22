package net.silentchaos512.gems.item.container;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
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
        Player player = event.getEntity();

        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof IContainerItem && ((IContainerItem) stack.getItem()).canStore(itemOnGround)) {
                IItemHandler itemHandler = ((IContainerItem) stack.getItem()).getInventory(stack);
                itemOnGround = ItemHandlerHelper.insertItem(itemHandler, itemOnGround, false);
                ((IContainerItem) stack.getItem()).saveInventory(stack, itemHandler, player);
                event.getItem().getItem().setCount(itemOnGround.getCount());

                if (itemOnGround.isEmpty()) {
                    event.setResult(Event.Result.ALLOW);
                    event.getItem().remove(Entity.RemovalReason.DISCARDED);
                    break;
                }
            }
        }

        if (itemOnGround.getCount() != initialCount) {
            Level level = player.level();
            float pitch = ((level.random.nextFloat() - level.random.nextFloat()) * 0.7F + 1.0F) * 2.0F;
            level.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(),
                    SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, pitch);
        }
    }
}
