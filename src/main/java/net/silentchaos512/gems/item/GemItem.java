package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
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
    public ITextComponent getName() {
        return new TextComponentTranslation("item.silentgems.gem", this.gem.getDisplayName());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;

        items.add(new ItemStack(this));

        // If Silent Gear is present, add new supercharged gems
        // TODO: Remove me? Supercharged gem sub-items might be a bit much...
//        if (Loader.isModLoaded("silentgear") && SilentGems.instance.isDevBuild()) {
//            for (int level = 1; level <= ModEnchantments.supercharged.getMaxLevel(); ++level) {
//                ItemStack stack = gem.getItem();
//                EnchantmentHelper.setEnchantments(ImmutableMap.of(ModEnchantments.supercharged, level), stack);
//                items.add(stack);
//            }
//        }
    }
}
