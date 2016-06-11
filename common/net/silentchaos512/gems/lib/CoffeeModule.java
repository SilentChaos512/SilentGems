package net.silentchaos512.gems.lib;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;

public class CoffeeModule {

  public static final String NBT_TIME_TO_NEXT_COFFEE = "SGTimeToNextCoffee";
  public static final int BASE_INTERVAL = 9000;

//  static long testTotalTime = 0;
//  static long testTotalDataCount = 0;

  public static void tickRabbit(EntityRabbit rabbit) {

//    long test = System.nanoTime();

    Random rand = SilentGems.instance.random;

    if (!rabbit.getEntityData().hasKey(NBT_TIME_TO_NEXT_COFFEE))
      rabbit.getEntityData().setInteger(NBT_TIME_TO_NEXT_COFFEE,
          getIntervalToNextCoffee(rabbit, rand));

    int time = rabbit.getEntityData().getInteger(NBT_TIME_TO_NEXT_COFFEE);
    if (--time <= 0) {
      int rabbitType = rabbit.getRabbitType();
      ItemStack coffee = getCoffeeForRabbitType(rabbitType);

      if (coffee != null) {
        rabbit.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f,
            (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);
        rabbit.entityDropItem(coffee.copy(), 0.0f);
      }
      time = getIntervalToNextCoffee(rabbit, rand);
    }
    rabbit.getEntityData().setInteger(NBT_TIME_TO_NEXT_COFFEE, time);

//    test = System.nanoTime() - test;
//    testTotalTime += test;
//    ++testTotalDataCount;
//    long avgTime = testTotalTime / testTotalDataCount;
//    SilentGems.instance.logHelper.debug(String.format("%d ns (%d ns avg)", test, avgTime));

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

    return BASE_INTERVAL + rand.nextInt(BASE_INTERVAL);
  }
}
