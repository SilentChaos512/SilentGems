package net.silentchaos512.gems.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.silentchaos512.gems.setup.Gems;

public class GemItem extends Item {
    private final Gems gem;
    private final String descriptionId;

    public GemItem(Gems gem, String descriptionId, Properties properties) {
        super(properties);
        this.gem = gem;
        this.descriptionId = descriptionId;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.descriptionId, gem.getDisplayName());
    }
}
