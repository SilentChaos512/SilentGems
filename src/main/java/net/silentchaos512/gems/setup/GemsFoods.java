package net.silentchaos512.gems.setup;

import net.minecraft.world.food.FoodProperties;

public class GemsFoods {
    public static final FoodProperties POTATO_ON_A_STICK = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.7f)
            .build();
    public static final FoodProperties SUGAR_COOKIE = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.4f)
            .alwaysEdible()
            .build();
    public static final FoodProperties CUP_OF_COFFEE = new FoodProperties.Builder()
            .nutrition(1)
            .saturationModifier(0.2f)
            .alwaysEdible()
            .build();
    public static final FoodProperties UNCOOKED_MEATY_STEW = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.6f)
            .build();
    public static final FoodProperties MEATY_STEW = new FoodProperties.Builder()
            .nutrition(12)
            .saturationModifier(1.6f)
            .build();
    public static final FoodProperties UNCOOKED_FISHY_STEW = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.5f)
            .build();
    public static final FoodProperties FISHY_STEW = new FoodProperties.Builder()
            .nutrition(10)
            .saturationModifier(1.2f)
            .build();
    public static final FoodProperties IRON_POTATO = new FoodProperties.Builder()
            .nutrition(9)
            .saturationModifier(0.9f)
            .alwaysEdible()
            .build();
}
