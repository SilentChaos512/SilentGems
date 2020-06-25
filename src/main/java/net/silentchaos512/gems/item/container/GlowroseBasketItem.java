package net.silentchaos512.gems.item.container;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.GemsTags;

public class GlowroseBasketItem extends GemContainerItem {
    public GlowroseBasketItem() {
        super(new Properties().group(GemsItemGroups.UTILITY).maxStackSize(1));
    }

    @Override
    public int getInventorySize(ItemStack stack) {
        return 27;
    }

    @Override
    public boolean canStore(ItemStack stack) {
        return stack.getItem().isIn(GemsTags.Items.GLOWROSES);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            playerIn.openContainer(new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("container.silentgems.glowrose_basket");
                }

                @Override
                public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
                    return new GlowroseBasketContainer(id, playerInventory, new PacketBuffer(Unpooled.buffer()));
                }
            });
        }

        return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
