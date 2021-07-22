package net.silentchaos512.gems.item.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.setup.GemsContainers;

public class GemBagItem extends GemContainerItem {
    public GemBagItem(Properties properties) {
        super("gem_bag", properties);
    }

    @Override
    public int getInventorySize(ItemStack stack) {
        return 27;
    }

    @Override
    public boolean canStore(ItemStack stack) {
        return stack.getItem().is(Tags.Items.GEMS);
    }

    @Override
    protected ContainerType<? extends GemContainer> getContainerType() {
        return GemsContainers.GEM_BAG.get();
    }
}
