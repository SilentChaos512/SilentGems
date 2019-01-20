package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public enum Foods {
    POTATO_ON_A_STICK(FoodSG.Builder
            .create(8, 0.8f, false)),
    SUGAR_COOKIE(FoodSG.Builder
            .create(2, 0.4f, false)),
    SECRET_DONUT(FoodSG.Builder
            .create(6, 0.8f, false)),
    UNCOOKED_MEATY_STEW(FoodSG.Builder
            .create(4, 0.6f, false)),
    MEATY_STEW(FoodSG.Builder
            .create(12, 1.6f, false)),
    CANDY_CANE(FoodSG.Builder
            .create(2, 0.2f, false)
            .setUseDuration(16)),
    COFFEE_CUP(FoodSG.Builder
            .create(1, 0.2f, false)
            .setUseAction(EnumAction.DRINK));

    private final FoodSG item;

    Foods(FoodSG.Builder builder) {
        this.item = new FoodSG(builder);
    }

    public FoodSG getItem() {
        return item;
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    // TODO: How to handle potion effects?
    private static final class FoodSG extends ItemFood {
        private static final class Builder extends Item.Builder {
            private int healAmount;
            private float saturation;
            private boolean isMeat;
            private EnumAction useAction = EnumAction.EAT;
            private int useDuration = 32;

            private static Builder create(int healAmount, float saturation, boolean isMeat) {
                Builder builder = new Builder();
                builder.healAmount = healAmount;
                builder.saturation = saturation;
                builder.isMeat = isMeat;
                return builder;
            }

            private Builder setUseAction(EnumAction useAction) {
                this.useAction = useAction;
                return this;
            }

            private Builder setUseDuration(int useDuration) {
                this.useDuration = useDuration;
                return this;
            }
        }

        private final EnumAction useAction;
        private final int useDuration;

        FoodSG(FoodSG.Builder builder) {
            super(builder.healAmount, builder.saturation, builder.isMeat, builder);
            this.useAction = builder.useAction;
            this.useDuration = builder.useDuration;
        }

        @Override
        public int getUseDuration(ItemStack stack) {
            return useDuration;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            // TODO
        }
    }
}
