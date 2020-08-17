package net.silentchaos512.gems.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public enum ModFoods implements IItemProvider {
    POTATO_ON_A_STICK(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(8).saturation(0.8f).build())
            .setReturnItem(Items.STICK)
            .addEffect(Effects.STRENGTH, TimeUtils.ticksFromSeconds(45))
    ),
    SUGAR_COOKIE(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(2).saturation(0.4f).setAlwaysEdible().build())
            .addEffect(Effects.SPEED, TimeUtils.ticksFromSeconds(30))
            .addEffect(Effects.HASTE, TimeUtils.ticksFromSeconds(30))
    ),
    SECRET_DONUT(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(6).saturation(0.8f).build())
    ),
    UNCOOKED_MEATY_STEW(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(4).saturation(0.6f).build())
            .setReturnItem(Items.BOWL)
    ),
    MEATY_STEW(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(12).saturation(1.6f).build())
            .setReturnItem(Items.BOWL)
    ),
    UNCOOKED_FISHY_STEW(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(4).saturation(0.5f).build())
            .setReturnItem(Items.BOWL)
    ),
    FISHY_STEW(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(10).saturation(1.2f).build())
            .setReturnItem(Items.BOWL)
    ),
    IRON_POTATO(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(9).saturation(0.9f).setAlwaysEdible().build())
            .addEffect(Effects.ABSORPTION, TimeUtils.ticksFromMinutes(10), 4)
            .addEffect(Effects.RESISTANCE, TimeUtils.ticksFromMinutes(5), 0)
            .addEffect(Effects.STRENGTH, TimeUtils.ticksFromMinutes(5), 1)
    ),
    CANDY_CANE(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(2).saturation(0.2f).fastToEat().setAlwaysEdible().build())
            .addEffect(Effects.REGENERATION, TimeUtils.ticksFromSeconds(5))
    ),
    CUP_OF_COFFEE(SGFoodItem.Builder.create()
            .food(new Food.Builder().hunger(1).saturation(0.2f).setAlwaysEdible().build())
            .setActionToDrink()
            .addEffect(Effects.SPEED, TimeUtils.ticksFromSeconds(60))
            .addEffect(Effects.HASTE, TimeUtils.ticksFromSeconds(30))
    );

    private final SGFoodItem.Builder builder;
    @SuppressWarnings("NonFinalFieldInEnum")
    private ItemRegistryObject<SGFoodItem> item;

    ModFoods(SGFoodItem.Builder builder) {
        this.builder = builder;
    }

    public static void registerItems() {
        for (ModFoods food : values()) {
            food.item = new ItemRegistryObject<>(Registration.ITEMS.register(food.getName(), () ->
                    new SGFoodItem(food, food.builder)));
        }
    }

    @Override
    public Item asItem() {
        return item.get();
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    private static final class SGFoodItem extends Item {
        private static final class Builder extends Item.Properties {
            @Nullable private IItemProvider returnItem;
            private UseAction useAction = UseAction.EAT;
            private final Collection<EffectInstance> potionEffects = new ArrayList<>();

            private static Builder create() {
                Builder builder = new Builder();
                builder.group(GemsItemGroups.UTILITY);
                return builder;
            }

            @Override
            public Builder food(Food foodIn) {
                return (Builder) super.food(foodIn);
            }

            private Builder setReturnItem(IItemProvider returnItem) {
                this.returnItem = returnItem;
                return this;
            }

            private Builder setActionToDrink() {
                this.useAction = UseAction.DRINK;
                return this;
            }

            @SuppressWarnings("SameParameterValue")
            private Builder addEffect(Effect effect, int duration) {
                return addEffect(effect, duration, 0);
            }

            @SuppressWarnings("SameParameterValue")
            private Builder addEffect(Effect effect, int duration, int amplifier) {
                this.potionEffects.add(new EffectInstance(effect, duration, amplifier));
                return this;
            }
        }

        private static final List<Consumer<PlayerEntity>> SECRET_DONUT_EFFECTS = ImmutableList.of(
                addEffect(Effects.BLINDNESS, TimeUtils.ticksFromSeconds(15)),
                addEffect(Effects.NAUSEA, TimeUtils.ticksFromSeconds(15)),
                addEffect(Effects.STRENGTH, TimeUtils.ticksFromSeconds(30)),
                addEffect(Effects.MINING_FATIGUE, TimeUtils.ticksFromSeconds(60)),
                addEffect(Effects.HASTE, TimeUtils.ticksFromSeconds(60)),
                addEffect(Effects.FIRE_RESISTANCE, TimeUtils.ticksFromSeconds(120)),
                addEffect(Effects.HUNGER, TimeUtils.ticksFromSeconds(15)),
                addEffect(Effects.INVISIBILITY, TimeUtils.ticksFromSeconds(30)),
                addEffect(Effects.JUMP_BOOST, TimeUtils.ticksFromSeconds(45)),
                addEffect(Effects.SLOWNESS, TimeUtils.ticksFromSeconds(60)),
                addEffect(Effects.SPEED, TimeUtils.ticksFromSeconds(75)),
                addEffect(Effects.NIGHT_VISION, TimeUtils.ticksFromSeconds(60)),
                addEffect(Effects.POISON, TimeUtils.ticksFromSeconds(10)),
                addEffect(Effects.REGENERATION, TimeUtils.ticksFromSeconds(15)),
                addEffect(Effects.RESISTANCE, TimeUtils.ticksFromSeconds(15)),
                addEffect(Effects.WATER_BREATHING, TimeUtils.ticksFromSeconds(90)),
                addEffect(Effects.WEAKNESS, TimeUtils.ticksFromSeconds(45)),
                addEffect(Effects.WITHER, TimeUtils.ticksFromSeconds(10)),
                addEffect(Effects.GLOWING, TimeUtils.ticksFromSeconds(30)),
                addEffect(Effects.DOLPHINS_GRACE, TimeUtils.ticksFromSeconds(30)),
                addEffect(Effects.LUCK, TimeUtils.ticksFromSeconds(120)),
                addEffect(Effects.UNLUCK, TimeUtils.ticksFromSeconds(120)),
                p -> p.setFire(5),
                p -> p.heal(SilentGems.random.nextInt(10)),
                p -> p.attackEntityFrom(DamageSource.CACTUS, SilentGems.random.nextInt(5))
        );
        private static final float SECRET_DONUT_EFFECT_CHANCE = 0.33f;
        private static final float SECRET_DONUT_TEXT_CHANCE = 0.66f;

        private final ModFoods foodType;
        private final UseAction useAction;
        @Nullable private final IItemProvider returnItem;
        private final Collection<EffectInstance> potionEffects = new ArrayList<>();

        SGFoodItem(ModFoods foodType, SGFoodItem.Builder builder) {
            super(builder);
            this.foodType = foodType;
            this.returnItem = builder.returnItem;
            this.useAction = builder.useAction;
            this.potionEffects.addAll(builder.potionEffects);
        }

        @Override
        public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entity) {
            ItemStack result = super.onItemUseFinish(stack, worldIn, entity);
            if (worldIn.isRemote || !(entity instanceof PlayerEntity)) return result;

            PlayerEntity player = (PlayerEntity) entity;
            if (returnItem != null) {
                PlayerUtils.giveItem(player, new ItemStack(returnItem));
            }

            this.potionEffects.forEach(effect -> {
                EffectInstance effect1 = new EffectInstance(effect.getPotion(), effect.getDuration(), effect.getAmplifier(), true, false);
                player.addPotionEffect(effect1);
            });

            if (this.foodType == SECRET_DONUT) {
                onSecretDonutEaten(stack, worldIn, player);
            }

            return result;
        }

        private static Consumer<PlayerEntity> addEffect(Effect effect, int duration) {
            return player -> player.addPotionEffect(new EffectInstance(effect, duration, 0, true, false));
        }

        private static void onSecretDonutEaten(ItemStack stack, World world, PlayerEntity player) {
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
                String key = "donut.silentgems." + MathUtils.nextIntInclusive(1, 8);
                player.sendMessage(new TranslationTextComponent(key), Util.DUMMY_UUID);
            }
        }

        private static void addSecretDonutEffect(World world, PlayerEntity player) {
            SECRET_DONUT_EFFECTS.get(SilentGems.random.nextInt(SECRET_DONUT_EFFECTS.size())).accept(player);
        }

        @Override
        public UseAction getUseAction(ItemStack stack) {
            return useAction;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".desc").mergeStyle(TextFormatting.ITALIC));
        }
    }
}
