package net.silentchaos512.gems.lib.module;

import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.ModItems;

import java.util.Random;

public class ModuleCoffee {
    public static final String MODULE_NAME = "coffee";

    public static final String NBT_TIME_TO_NEXT_COFFEE = "SGTimeToNextCoffee";
    public static boolean MODULE_ENABLED = true;
    public static int INTERVAL_MIN = 9000;
    public static int INTERVAL_MAX = 18000;

    // static long testTotalTime = 0;
    // static long testTotalDataCount = 0;

    public static void loadConfig(Configuration c) {
        String cat = GemsConfig.CAT_MAIN + c.CATEGORY_SPLITTER + MODULE_NAME;
        c.setCategoryComment(cat,
                "Configs for coffee drops. Where does coffee drop from, you ask? I have no idea :p. Note that coffee\n"
                        + "drops will be disabled if the min and max intervals have nonsense values (see the comments for\n"
                        + "those options).");

        MODULE_ENABLED = c.getBoolean("Enabled", cat, MODULE_ENABLED, "Enable/disable coffee drops.");
        INTERVAL_MIN = c.getInt("DropIntervalMin", cat, INTERVAL_MIN, 0, Integer.MAX_VALUE,
                "The minimum time between coffee drops. Must be less than maximum and greater than zero.");
        INTERVAL_MAX = c.getInt("DropIntervalMax", cat, INTERVAL_MAX, 0, Integer.MAX_VALUE,
                "The maximum time between coffee drops. Must be greater than minimum and greater than zero.");

        // Sanity checks.
        if (INTERVAL_MIN >= INTERVAL_MAX || INTERVAL_MIN == 0 || INTERVAL_MAX == 0)
            MODULE_ENABLED = false;
    }

    public static void tickRabbit(EntityRabbit rabbit) {
        if (!MODULE_ENABLED || rabbit.world.isRemote)
            return;

        // long test = System.nanoTime();

        Random rand = SilentGems.random;

        if (!rabbit.getEntityData().hasKey(NBT_TIME_TO_NEXT_COFFEE))
            rabbit.getEntityData().setInteger(NBT_TIME_TO_NEXT_COFFEE,
                    getIntervalToNextCoffee(rabbit, rand));

        int time = rabbit.getEntityData().getInteger(NBT_TIME_TO_NEXT_COFFEE);
        if (--time <= 0) {
            int rabbitType = rabbit.getRabbitType();
            ItemStack coffee = getCoffeeForRabbitType(rabbitType);

            if (!coffee.isEmpty()) {
                rabbit.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f,
                        (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);
                rabbit.entityDropItem(coffee.copy(), 0.0f);
            }
            time = getIntervalToNextCoffee(rabbit, rand);
        }
        rabbit.getEntityData().setInteger(NBT_TIME_TO_NEXT_COFFEE, time);

        // test = System.nanoTime() - test;
        // testTotalTime += test;
        // ++testTotalDataCount;
        // long avgTime = testTotalTime / testTotalDataCount;
        // SilentGems.instance.logHelper.debug(String.format("%d ns (%d ns avg)", test, avgTime));

        // Test results: average time 1280 ns (0.00128 ms).
        // With 1000 rabbits, about 2.56% of a 50 ms tick time would be this function. Seems acceptable.
    }

    private static ItemStack getCoffeeForRabbitType(int rabbitType) {
        // Maybe have different types of coffee?
        // switch (rabbitType) {
        // case 0:
        // // Brown
        // break;
        // case 1:
        // // White
        // break;
        // case 2:
        // // Black
        // break;
        // case 3:
        // // Black & White
        // break;
        // case 4:
        // // Gold = Vanilla
        // break;
        // case 5:
        // // Salt & Pepper
        // break;
        // case 99:
        // // Killer Bunny
        // return null;
        // default:
        // return null;
        // }

        return ModItems.food.coffeeCup;
    }

    private static final int getIntervalToNextCoffee(EntityRabbit rabbit, Random rand) {
        return INTERVAL_MIN + rand.nextInt(INTERVAL_MAX - INTERVAL_MIN);
    }
}
