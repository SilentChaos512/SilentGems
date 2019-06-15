package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.api.chaos.ChaosEmissionRate;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.lib.chaosbuff.ChaosBuffManager;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.gems.lib.chaosbuff.PotionChaosBuff;
import net.silentchaos512.utils.Color;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.List;

public final class ChaosRuneItem extends Item {
    public static final Lazy<ChaosRuneItem> INSTANCE = Lazy.of(ChaosRuneItem::new);

    private static final String NBT_KEY = "SGems_BuffRune";

    private ChaosRuneItem() {
        super(new Properties().group(GemsItemGroups.UTILITY));
    }

    public static ItemStack getStack(IChaosBuff buff) {
        ItemStack result = new ItemStack(INSTANCE.get());
        result.getOrCreateTag().putString(NBT_KEY, buff.getId().toString());
        return result;
    }

    @Nullable
    public static IChaosBuff getBuff(ItemStack stack) {
        String string = stack.getOrCreateTag().getString(NBT_KEY);
        return ChaosBuffManager.get(string);
    }

    public static int getColor(ItemStack stack, int tintIndex) {
        if (tintIndex != 1) return Color.VALUE_WHITE;
        IChaosBuff buff = getBuff(stack);
        if (buff == null) return Color.VALUE_WHITE;
        return buff.getRuneColor();
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        IChaosBuff buff = getBuff(stack);
        if (buff != null) {
            ITextComponent buffName = buff.getDisplayName(0);
            return new TranslationTextComponent(this.getTranslationKey() + ".nameProper", buffName);
        }
        return super.getDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        IChaosBuff buff = getBuff(stack);
        if (buff == null) return;

        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".maxLevel", buff.getMaxLevel()));
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".slotsUsed", buff.getSlotsForLevel(1)));
        int activeChaosGenerated = buff.getActiveChaosGenerated(1);
        ChaosEmissionRate emissionRate = ChaosEmissionRate.fromAmount(activeChaosGenerated);
        tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".chaos", emissionRate.getDisplayName(activeChaosGenerated)));

        // Debug
        if (KeyTracker.isAltDown()) {
            tooltip.add(new StringTextComponent(String.format("Buff ID: %s", buff.getId())).applyTextStyle(TextFormatting.DARK_GRAY));
            tooltip.add(new StringTextComponent(String.format("Color: %X", buff.getRuneColor())).applyTextStyle(TextFormatting.DARK_GRAY));
            if (buff instanceof PotionChaosBuff) {
                Effect effect = ((PotionChaosBuff) buff).getEffect();
                tooltip.add(new StringTextComponent(String.format("Effect: %s", effect)).applyTextStyle(TextFormatting.DARK_GRAY));
            }
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (!isInGroup(group)) return;
        for (IChaosBuff buff : ChaosBuffManager.getValues()) {
            items.add(getStack(buff));
        }
    }
}
