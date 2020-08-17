package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.gems.lib.soul.SoulElement;
import net.silentchaos512.utils.Color;

import javax.annotation.Nullable;
import java.util.List;

public class SoulGemItem extends Item {
    private static final String NBT_KEY = "SGems_SoulGem";

    public SoulGemItem() {
        super(new Properties().group(GemsItemGroups.MATERIALS));
    }

    /**
     * Get a stack with the given soul. In many cases, it may be better to use {@link
     * #getStack(ResourceLocation)}.
     *
     * @param soul The soul
     * @return A stack of a single soul gem with the given soul
     */
    public static ItemStack getStack(Soul soul) {
        return getStack(soul, 1);
    }

    /**
     * Get a stack with the given soul. In many cases, it may be better to use {@link
     * #getStack(ResourceLocation)}.
     *
     * @param soul The soul
     * @param count The stack size
     * @return A stack of {@code count} soul gems with the given soul
     */
    public static ItemStack getStack(Soul soul, int count) {
        ItemStack result = new ItemStack(GemsItems.SOUL_GEM, count);
        setSoul(result, soul);
        return result;
    }

    /**
     * Get a stack with the given soul. Does not check whether or not the soul actually exists,
     * making this version useful for loading recipes and ingredients, when souls don't exist yet.
     *
     * @param soulId The soul ID (should match the entity ID)
     * @return A stack of a single soul gem with the given soul
     */
    public static ItemStack getStack(ResourceLocation soulId) {
        ItemStack result = new ItemStack(GemsItems.SOUL_GEM);
        result.getOrCreateTag().putString(NBT_KEY, soulId.toString());
        return result;
    }

    public static void setSoul(ItemStack stack, Soul soul) {
        if (!(stack.getItem() instanceof SoulGemItem)) {
            throw new IllegalArgumentException("Tried to set soul on item that is not a soul gem: " + stack);
        }
        stack.getOrCreateTag().putString(NBT_KEY, soul.getId().toString());
    }

    @Nullable
    public static Soul getSoul(ItemStack stack) {
        return Soul.from(getSoulId(stack));
    }

    private static String getSoulId(ItemStack stack) {
        return stack.getOrCreateTag().getString(NBT_KEY);
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        Soul soul = getSoul(stack);
        if (soul == null) return Color.VALUE_WHITE;
        if (tintIndex == 0) return soul.getPrimaryColor();
        if (tintIndex == 1) return soul.getSecondaryColor();
        return Color.VALUE_WHITE;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            ITextComponent entityName = soul.getEntityName();
            return new TranslationTextComponent(this.getTranslationKey() + ".nameProper", entityName);
        }
        return super.getDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            tooltip.add(soul.getPrimaryElement().getDisplayName());
            SoulElement element2 = soul.getSecondaryElement();
            if (element2 != SoulElement.NONE) {
                tooltip.add(element2.getDisplayName());
            }

            if (flagIn.isAdvanced()) {
                tooltip.add(new StringTextComponent("Soul ID: " + soul.getId()).mergeStyle(TextFormatting.DARK_GRAY));
            }

            if (GemsConfig.COMMON.debugExtraTooltipInfo.get()) {
                tooltip.add(new StringTextComponent("DEBUG:"));
                tooltip.add(new StringTextComponent(String.format("- Base drop rate: %.4f", soul.getBaseDropRate())));
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;
        for (Soul soul : Soul.getValues()) {
            items.add(getStack(soul));
        }
    }
}
