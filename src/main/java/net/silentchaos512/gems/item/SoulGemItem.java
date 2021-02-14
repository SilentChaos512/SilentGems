package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.soul.Soul;
import net.silentchaos512.gems.soul.SoulElement;
import net.silentchaos512.utils.Color;

import javax.annotation.Nullable;
import java.util.List;

public class SoulGemItem extends Item {
    private static final String NBT_SOUL = "Soul";
    private static final String NBT_VALUE = "SoulValue";

    public SoulGemItem(Properties properties) {
        super(properties);
    }

    /**
     * Get a stack with the given soul. In many cases, it may be better to use {@link
     * #getStack(ResourceLocation)}.
     *
     * @param soul The soul
     * @param value The value of souls collected
     * @return A stack of a soul gem with the given soul and kill value
     */
    public static ItemStack getStack(Soul soul, int value) {
        ItemStack result = new ItemStack(GemsItems.SOUL_GEM);
        setSoul(result, soul);
        setSoulValue(result, value);
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
        result.getOrCreateTag().putString(NBT_SOUL, soulId.toString());
        return result;
    }

    private static String getSoulId(ItemStack stack) {
        return stack.getOrCreateTag().getString(NBT_SOUL);
    }

    @Nullable
    public static Soul getSoul(ItemStack stack) {
        return Soul.from(getSoulId(stack));
    }

    public static void setSoul(ItemStack stack, Soul soul) {
        if (!(stack.getItem() instanceof SoulGemItem)) {
            throw new IllegalArgumentException("Tried to set soul on item that is not a soul gem: " + stack);
        }
        stack.getOrCreateTag().putString(NBT_SOUL, soul.getId().toString());
    }

    public static int getSoulValue(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBT_VALUE);
    }

    public static void setSoulValue(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt(NBT_VALUE, MathHelper.clamp(value, 0, Soul.MAX_VALUE));
    }

    public static void addSoulValue(ItemStack stack, int value) {
        setSoulValue(stack, getSoulValue(stack) + value);
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
            return new TranslationTextComponent(this.getTranslationKey(), entityName.deepCopy().appendString(" "));
        }
        return super.getDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            if (flagIn.isAdvanced()) {
                tooltip.add(new StringTextComponent("Soul ID: " + soul.getId()).mergeStyle(TextFormatting.DARK_GRAY));
            }

            // Elements
            tooltip.add(soul.getPrimaryElement().getDisplayName());
            SoulElement element2 = soul.getSecondaryElement();
            if (element2 != SoulElement.NONE) {
                tooltip.add(element2.getDisplayName());
            }

            // Stored value
            int soulValue = getSoulValue(stack);
            ITextComponent valueText = soulValue < Soul.MAX_VALUE
                    ? new StringTextComponent(String.format("%d", soulValue))
                    : GemsBase.TEXT.itemSub(this, "full");
            tooltip.add(GemsBase.TEXT.itemSub(this, "value", valueText));
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isInGroup(group)) {
            items.add(new ItemStack(this));

            for (Soul soul : Soul.getValues()) {
                items.add(getStack(soul, Soul.MAX_VALUE));
            }
        }
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int value = getSoulValue(stack);
        return 1f - (float) value / Soul.MAX_VALUE;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        int value = getSoulValue(stack);
        return value > 0 && value < Soul.MAX_VALUE;
    }
}
