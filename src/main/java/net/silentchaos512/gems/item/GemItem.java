package net.silentchaos512.gems.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.gems.util.IGem;

public class GemItem extends Item implements IGem {
    private final Gems gem;
    private final String translationKey;

    public GemItem(Gems gem, String translationKey, Properties properties) {
        super(properties);
        this.gem = gem;
        this.translationKey = translationKey;
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }

    @Override
    public Component getName(ItemStack stack) {
        return getDescription();
    }

    @Override
    public Component getDescription() {
        return new TranslatableComponent("item.silentgems." + this.translationKey, this.gem.getDisplayName());
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return gem.getRarity();
    }
}
