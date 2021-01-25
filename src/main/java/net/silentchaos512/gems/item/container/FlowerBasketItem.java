package net.silentchaos512.gems.item.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.silentchaos512.gems.setup.GemsContainers;

public class FlowerBasketItem extends GemContainerItem {
    public FlowerBasketItem(Properties properties) {
        super("flower_basket", properties);
    }

    @Override
    protected ContainerType<? extends GemContainer> getContainerType() {
        return GemsContainers.FLOWER_BASKET.get();
    }

    @Override
    public int getInventorySize(ItemStack stack) {
        return 27;
    }

    @Override
    public boolean canStore(ItemStack stack) {
        return stack.getItem().isIn(ItemTags.FLOWERS);
    }
}
