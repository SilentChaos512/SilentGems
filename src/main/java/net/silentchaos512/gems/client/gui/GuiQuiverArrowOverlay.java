package net.silentchaos512.gems.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.quiver.IQuiver;
import net.silentchaos512.lib.util.StackHelper;

public class GuiQuiverArrowOverlay extends Gui {

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if (!GemsConfig.SHOW_ARROW_COUNT || event.getType() != ElementType.HOTBAR)
            return;

        // Player holding a bow?
        EntityPlayer player = SilentGems.proxy.getClientPlayer();
        ItemStack bow = getActiveBow(player);
        if (StackHelper.isValid(bow)) {
            // Get the "arrow" the bow would fire.
            ItemStack arrow = StackHelper.empty();
            int arrowCount = 0;

            ItemStack stack = findAmmo(player);
            if (StackHelper.isValid(stack)) {
                // Is it a quiver?
                if (stack.getItem() instanceof IQuiver) {
                    // Find first arrow.
                    IItemHandler itemHandler = ((IQuiver) stack.getItem()).getInventory(stack);
                    for (int i = 0; i < itemHandler.getSlots(); ++i) {
                        ItemStack itemstack = itemHandler.getStackInSlot(i);
                        if (StackHelper.isValid(itemstack) && itemstack.getItem() instanceof ItemArrow) {
                            if (StackHelper.isEmpty(arrow)) {
                                arrow = itemstack;
                                arrowCount = arrow.getCount();
                            } else if (arrow.isItemEqual(itemstack)
                                    && ((!arrow.hasTagCompound() && !itemstack.hasTagCompound())
                                    || itemstack.getTagCompound().equals(arrow.getTagCompound()))) {
                                arrowCount += itemstack.getCount();
                            }
                        }
                    }
                } else {
                    // It's just an arrow.
                    arrow = stack;
                    arrowCount = arrow.getCount();
                }
            }

            if (StackHelper.isValid(arrow)) {
                renderArrow(arrow, arrowCount, player);
            }
        }
    }

    private ItemStack getActiveBow(EntityPlayer player) {

        if (player.getHeldItemMainhand().getItem() instanceof ItemBow)
            return player.getHeldItemMainhand();
        else if (player.getHeldItemOffhand().getItem() instanceof ItemBow)
            return player.getHeldItemOffhand();
        return ItemStack.EMPTY;
    }

    // Copied from ItemBow
    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    // Copied from ItemBow
    private boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }

    private void renderArrow(ItemStack stack, int count, EntityPlayer player) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderItem itemRenderer = mc.getRenderItem();
        FontRenderer fontRenderer = mc.fontRenderer;
        ScaledResolution res = new ScaledResolution(mc);

        int y = res.getScaledHeight() - 19;
        int x = res.getScaledWidth() / 2 + 97;
        itemRenderer.renderItemAndEffectIntoGUI(player, stack, x, y);
        fontRenderer.drawStringWithShadow(Integer.toString(count), x + 14, y + 9, 0xFFFFFF);
    }
}
