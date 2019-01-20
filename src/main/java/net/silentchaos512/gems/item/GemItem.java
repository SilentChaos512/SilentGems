package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.lib.IGem;

import javax.annotation.Nullable;
import java.util.List;

public class GemItem extends Item implements IGem {
    private final Gems gem;

    public GemItem(Gems gem) {
        super(new Item.Builder().group(ModItemGroups.MATERIALS));
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
//        Gems gem = Gems.fromStack(stack);
//        boolean controlDown = KeyTracker.isControlDown();
//
//        if (controlDown && (gem == Gems.RUBY || gem == Gems.GREEN_SAPPHIRE || gem == Gems.SAPPHIRE || gem == Gems.TOPAZ)) {
//            list.add(SilentGems.i18n.subText(this, "original4"));
//        }
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
