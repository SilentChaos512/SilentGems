package net.silentchaos512.gems.item.container;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
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
        return stack.is(Tags.Items.GEMS);
    }

    @Override
    protected MenuType<? extends GemContainer> getContainerType() {
        return GemsContainers.GEM_BAG.get();
    }
}
