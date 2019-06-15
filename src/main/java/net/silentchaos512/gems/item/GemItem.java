package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.IGem;

import javax.annotation.Nullable;
import java.util.List;

public class GemItem extends Item implements IGem {
    private final Gems gem;

    public GemItem(Gems gem) {
        super(new Properties().group(ModItemGroups.MATERIALS));
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(gem.getSet().getDisplayName());
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return getName();
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("item.silentgems.gem", this.gem.getDisplayName());
    }
}
