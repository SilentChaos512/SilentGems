package net.silentchaos512.gems.item.quiver;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.gems.lib.Names;

import java.util.ArrayList;
import java.util.List;

public class QuiverHelper {
    public static final QuiverHelper instance = new QuiverHelper();

    private List<EntityArrow> firedArrows = new ArrayList<>();

    void addFiredArrow(EntityArrow arrow) {
        firedArrows.add(arrow);
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        for (EntityArrow arrow : firedArrows) {
            arrow.pickupStatus = PickupStatus.ALLOWED;
        }

        firedArrows.removeIf(a -> true);
    }

    @SubscribeEvent
    public void onItemPickup(EntityItemPickupEvent event) {
        // FIXME: Works for arrow items, but not arrow entities stuck in ground. Also does not work properly if quiver GUI
        // is open.

        //@formatter:off
//    ItemStack entityStack = event.getItem().getItem();
//    if (entityStack.getItem() instanceof ItemArrow && !(entityStack.getItem() instanceof IQuiver)) {
//      // It's an arrow
//      EntityPlayer player = event.getEntityPlayer();
//      for (ItemStack stack : PlayerHelper.getNonEmptyStacks(player)) {
//        if (stack.getItem() instanceof IQuiver) {
//          // Found a quiver in player inventory
//          IQuiver itemQuiver = (IQuiver) stack.getItem();
//          IItemHandler itemHandler = itemQuiver.getInventory(stack);
//          for (int i = 0; i < itemHandler.getSlots(); ++i) {
//            entityStack = itemHandler.insertItem(i, entityStack, false);
//            if (StackHelper.getCount(entityStack) <= 0) {
//              event.getItem().setDead();
//              event.setResult(Result.ALLOW);
//              player.world.playSound(null, event.getItem().getPosition(),
//                  SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f,
//                  (SilentGems.random.nextFloat() - SilentGems.random.nextFloat()) * 1.4F + 2.0F);
//              itemQuiver.updateQuiver(stack, itemHandler, player);
//              return;
//            }
//          }
//        }
//      }
//    }
    //@formatter:on
    }

    @SideOnly(Side.CLIENT)
    public static void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.isEmpty() || !(stack.getItem() instanceof IQuiver)) {
            return;
        }

        IItemHandler itemHandler = ((IQuiver) stack.getItem()).getInventory(stack);
        for (int i = 0; i < itemHandler.getSlots(); ++i) {
            ItemStack arrow = itemHandler.getStackInSlot(i);
            if (!arrow.isEmpty()) {
                tooltip.add(SilentGems.i18n.itemSubText(Names.QUIVER, "arrowFormat", arrow.getCount(), arrow.getDisplayName()));
            }
        }
    }

    public static ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack heldItem = player.getHeldItem(hand);

        world.playSound(player.posX, player.posY, player.posZ, SoundEvents.BLOCK_CLOTH_BREAK,
                SoundCategory.NEUTRAL, 0.8F, 0.8F, false);
        GuiTypes.QUIVER.open(player, world, hand == EnumHand.OFF_HAND ? 1 : 0);

        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
    }
}
