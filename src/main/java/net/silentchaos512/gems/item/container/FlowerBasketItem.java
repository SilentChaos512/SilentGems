package net.silentchaos512.gems.item.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gems.setup.GemsContainers;
import net.silentchaos512.gems.setup.GemsTags;

public class FlowerBasketItem extends GemContainerItem {
    public FlowerBasketItem(Properties properties) {
        super("flower_basket", properties);
    }

    @Override
    protected MenuType<? extends GemContainer> getContainerType() {
        return GemsContainers.FLOWER_BASKET.get();
    }

    @Override
    public int getInventorySize(ItemStack stack) {
        return 27;
    }

    @Override
    public boolean canStore(ItemStack stack) {
        return stack.is(GemsTags.Items.FLOWER_BASKET_CAN_STORE);
    }
}
