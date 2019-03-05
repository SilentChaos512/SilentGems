package net.silentchaos512.gems.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.Lazy;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public enum Foods implements IItemProvider, IStringSerializable {
    POTATO_ON_A_STICK(FoodSG.Builder
            .create(8, 0.8f)
            .setReturnItem(Items.STICK)
            .addEffect(MobEffects.STRENGTH, TimeUtils.ticksFromSeconds(45))
    ),
    SUGAR_COOKIE(FoodSG.Builder
            .create(2, 0.4f)
            .setAlwaysEdible()
            .addEffect(MobEffects.SPEED, TimeUtils.ticksFromSeconds(30))
            .addEffect(MobEffects.HASTE, TimeUtils.ticksFromSeconds(30))
    ),
    SECRET_DONUT(FoodSG.Builder
            .create(6, 0.8f)
    ),
    UNCOOKED_MEATY_STEW(FoodSG.Builder
            .create(4, 0.6f)
            .setReturnItem(Items.BOWL)
    ),
    MEATY_STEW(FoodSG.Builder
            .create(12, 1.6f)
            .setReturnItem(Items.BOWL)
    ),
    UNCOOKED_FISHY_STEW(FoodSG.Builder
            .create(4, 0.5f)
            .setReturnItem(Items.BOWL)
    ),
    FISHY_STEW(FoodSG.Builder
            .create(10, 1.2f)
            .setReturnItem(Items.BOWL)
    ),
    CANDY_CANE(FoodSG.Builder
            .create(2, 0.2f)
            .setUseDuration(16)
            .setAlwaysEdible()
            .addEffect(MobEffects.REGENERATION, TimeUtils.ticksFromSeconds(5))
    ),
    CUP_OF_COFFEE(FoodSG.Builder
            .create(1, 0.2f)
            .setActionToDrink()
            .setAlwaysEdible()
            .addEffect(MobEffects.SPEED, TimeUtils.ticksFromSeconds(60))
            .addEffect(MobEffects.HASTE, TimeUtils.ticksFromSeconds(30))
    );

    private final Lazy<FoodSG> item;

    Foods(FoodSG.Builder builder) {
        this.item = Lazy.of(() -> new FoodSG(this, builder));
    }

    @Override
    public Item asItem() {
        return item.get();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    private static final class FoodSG extends ItemFood {
        private static final class Builder extends Item.Properties {
            private int healAmount;
            private float saturation;
            private boolean isMeat;
            private boolean alwaysEdible;
            @Nullable private IItemProvider returnItem;
            private EnumAction useAction = EnumAction.EAT;
            private int useDuration = 32;
            private final Collection<PotionEffect> potionEffects = new ArrayList<>();

            private static Builder create(int healAmount, float saturation) {
                Builder builder = new Builder();
                builder.healAmount = healAmount;
                builder.saturation = saturation;
                builder.isMeat = false;
                builder.group(ModItemGroups.UTILITY);
                return builder;
            }

            private Builder setReturnItem(IItemProvider returnItem) {
                this.returnItem = returnItem;
                return this;
            }

            private Builder setAlwaysEdible() {
                this.alwaysEdible = true;
                return this;
            }

            private Builder setActionToDrink() {
                this.useAction = EnumAction.DRINK;
                return this;
            }

            @SuppressWarnings("SameParameterValue")
            private Builder setUseDuration(int useDuration) {
                this.useDuration = useDuration;
                return this;
            }

            private Builder addEffect(Potion potion, int duration) {
                this.potionEffects.add(new PotionEffect(potion, duration, 0));
                return this;
            }
        }

        private static final List<Consumer<EntityPlayer>> SECRET_DONUT_EFFECTS = ImmutableList.of(
                addEffect(MobEffects.BLINDNESS, TimeUtils.ticksFromSeconds(15)),
                addEffect(MobEffects.NAUSEA, TimeUtils.ticksFromSeconds(15)),
                addEffect(MobEffects.STRENGTH, TimeUtils.ticksFromSeconds(30)),
                addEffect(MobEffects.MINING_FATIGUE, TimeUtils.ticksFromSeconds(60)),
                addEffect(MobEffects.HASTE, TimeUtils.ticksFromSeconds(60)),
                addEffect(MobEffects.FIRE_RESISTANCE, TimeUtils.ticksFromSeconds(120)),
                addEffect(MobEffects.HUNGER, TimeUtils.ticksFromSeconds(15)),
                addEffect(MobEffects.INVISIBILITY, TimeUtils.ticksFromSeconds(30)),
                addEffect(MobEffects.JUMP_BOOST, TimeUtils.ticksFromSeconds(45)),
                addEffect(MobEffects.SLOWNESS, TimeUtils.ticksFromSeconds(60)),
                addEffect(MobEffects.SPEED, TimeUtils.ticksFromSeconds(75)),
                addEffect(MobEffects.NIGHT_VISION, TimeUtils.ticksFromSeconds(60)),
                addEffect(MobEffects.POISON, TimeUtils.ticksFromSeconds(10)),
                addEffect(MobEffects.REGENERATION, TimeUtils.ticksFromSeconds(15)),
                addEffect(MobEffects.RESISTANCE, TimeUtils.ticksFromSeconds(15)),
                addEffect(MobEffects.WATER_BREATHING, TimeUtils.ticksFromSeconds(90)),
                addEffect(MobEffects.WEAKNESS, TimeUtils.ticksFromSeconds(45)),
                addEffect(MobEffects.WITHER, TimeUtils.ticksFromSeconds(10)),
                addEffect(MobEffects.GLOWING, TimeUtils.ticksFromSeconds(30)),
                addEffect(MobEffects.DOLPHINS_GRACE, TimeUtils.ticksFromSeconds(30)),
                addEffect(MobEffects.LUCK, TimeUtils.ticksFromSeconds(120)),
                addEffect(MobEffects.UNLUCK, TimeUtils.ticksFromSeconds(120)),
                p -> p.setFire(5),
                p -> p.heal(SilentGems.random.nextInt(10)),
                p -> p.attackEntityFrom(DamageSource.CACTUS, SilentGems.random.nextInt(5))
        );
        private static final float SECRET_DONUT_EFFECT_CHANCE = 0.33f;
        private static final float SECRET_DONUT_TEXT_CHANCE = 0.66f;
        private static int donutMessageCount;

        private final Foods foodType;
        private final EnumAction useAction;
        private final int useDuration;
        @Nullable private final IItemProvider returnItem;
        private final Collection<PotionEffect> potionEffects = new ArrayList<>();

        FoodSG(Foods foodType, FoodSG.Builder builder) {
            super(builder.healAmount, builder.saturation, builder.isMeat, builder);
            this.foodType = foodType;
            this.returnItem = builder.returnItem;
            this.useAction = builder.useAction;
            this.useDuration = builder.useDuration;
            if (builder.alwaysEdible) setAlwaysEdible();
            this.potionEffects.addAll(builder.potionEffects);
        }

        @Override
        protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
            super.onFoodEaten(stack, worldIn, player);
            if (worldIn.isRemote) return;

            if (returnItem != null) {
                PlayerUtils.giveItem(player, new ItemStack(returnItem));
            }

            this.potionEffects.forEach(effect -> {
                PotionEffect effect1 = new PotionEffect(
                        effect.getPotion(), effect.getDuration(), effect.getAmplifier(),
                        true, false);
                player.addPotionEffect(effect1);
            });

            if (this.foodType == SECRET_DONUT) {
                onSecretDonutEaten(stack, worldIn, player);
            }
        }

        private static Consumer<EntityPlayer> addEffect(Potion potion, int duration) {
            return player -> player.addPotionEffect(new PotionEffect(potion, duration, 0, true, false));
        }

        private static void onSecretDonutEaten(ItemStack stack, World world, EntityPlayer player) {
            Random random = SilentGems.random;
            // Give potion effect?
            if (MathUtils.tryPercentage(random, SECRET_DONUT_EFFECT_CHANCE)) {
                addSecretDonutEffect(world, player);
                // Smaller chance of second effect
                if (MathUtils.tryPercentage(random, SECRET_DONUT_EFFECT_CHANCE)) {
                    addSecretDonutEffect(world, player);
                    // Even small chance of third effect
                    if (MathUtils.tryPercentage(random, SECRET_DONUT_EFFECT_CHANCE)) {
                        addSecretDonutEffect(world, player);
                    }
                }
            }

            // Add chat message
            if (MathUtils.tryPercentage(random, SECRET_DONUT_TEXT_CHANCE)) {
                String key = "donut.silentgems." + MathUtils.nextIntInclusive(1, getSecretDonutMessageCount());
                player.sendMessage(new TextComponentTranslation(key));
            }
        }

        private static void addSecretDonutEffect(World world, EntityPlayer player) {
            SECRET_DONUT_EFFECTS.get(SilentGems.random.nextInt(SECRET_DONUT_EFFECTS.size())).accept(player);
        }

        private static int getSecretDonutMessageCount() {
            if (donutMessageCount == 0) {
                // Calculate the value
                do {
                    ++donutMessageCount;
                } while (I18n.hasKey("donut.silentgems." + (donutMessageCount + 1)));
            }
            return donutMessageCount;
        }

        @Override
        public EnumAction getUseAction(ItemStack stack) {
            return useAction;
        }

        @Override
        public int getUseDuration(ItemStack stack) {
            return useDuration;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TextComponentTranslation(this.getTranslationKey() + ".desc").applyTextStyle(TextFormatting.ITALIC));
        }
    }
}
