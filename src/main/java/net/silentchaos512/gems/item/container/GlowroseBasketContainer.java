package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerInventory;
import net.silentchaos512.gems.init.GemsContainers;

public class GlowroseBasketContainer extends GemContainer {
    public GlowroseBasketContainer(int id, PlayerInventory playerInventory) {
        super(id, playerInventory, GemsContainers.GLOWROSE_BASKET.type(), GlowroseBasketItem.class);
    }
}
