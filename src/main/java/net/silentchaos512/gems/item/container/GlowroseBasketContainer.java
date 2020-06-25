package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.silentchaos512.gems.init.GemsContainers;

public class GlowroseBasketContainer extends GemContainer {
    public GlowroseBasketContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(id, playerInventory, GemsContainers.GLOWROSE_BASKET.get(), GlowroseBasketItem.class);
    }
}
