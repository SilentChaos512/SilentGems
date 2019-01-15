package net.silentchaos512.gems.item;

import net.minecraft.item.Item;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.ModItemGroups;
import net.silentchaos512.gems.lib.IGem;


public class GemShard extends Item implements IGem {
    private final Gems gem;

    public GemShard(Gems gem) {
        super(new Item.Builder().group(ModItemGroups.MATERIALS));
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }
}
