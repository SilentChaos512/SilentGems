package net.silentchaos512.gems.item.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.silentchaos512.gems.init.GemsContainers;

public class GemBagContainer extends GemContainer {
    public GemBagContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        super(id, playerInventory, GemsContainers.GEM_BAG.get(), GemBagItem.class);
    }
}
