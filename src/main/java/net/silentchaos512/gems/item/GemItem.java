package net.silentchaos512.gems.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
    public ITextComponent getDisplayName(ItemStack stack) {
        return getName();
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("item.silentgems." + this.translationKey, this.gem.getDisplayName());
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return gem.getRarity();
    }
}
