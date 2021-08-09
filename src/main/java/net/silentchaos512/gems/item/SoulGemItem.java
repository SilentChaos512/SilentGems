package net.silentchaos512.gems.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.silentchaos512.gems.setup.GemsItems;
import net.silentchaos512.gems.soul.Soul;
import net.silentchaos512.gems.soul.SoulElement;
import net.silentchaos512.gems.util.TextUtil;
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
        stack.getOrCreateTag().putInt(NBT_VALUE, Mth.clamp(value, 0, Soul.MAX_VALUE));
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
    public Component getName(ItemStack stack) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            Component entityName = soul.getEntityName();
            return new TranslatableComponent(this.getDescriptionId(), entityName.copy().append(" "));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Soul soul = getSoul(stack);
        if (soul != null) {
            if (flagIn.isAdvanced()) {
                tooltip.add(new TextComponent("Soul ID: " + soul.getId()).withStyle(ChatFormatting.DARK_GRAY));
            }

            // Elements
            tooltip.add(soul.getPrimaryElement().getDisplayName());
            SoulElement element2 = soul.getSecondaryElement();
            if (element2 != SoulElement.NONE) {
                tooltip.add(element2.getDisplayName());
            }

            // Stored value
            int soulValue = getSoulValue(stack);
            Component valueText = soulValue < Soul.MAX_VALUE
                    ? new TextComponent(String.format("%d", soulValue))
                    : TextUtil.itemSub(this, "full");
            tooltip.add(TextUtil.itemSub(this, "value", valueText));
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (allowdedIn(group)) {
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
