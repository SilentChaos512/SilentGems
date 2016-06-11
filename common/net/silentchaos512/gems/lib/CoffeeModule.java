package net.silentchaos512.gems.lib;

import java.util.Random;

import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ModItems;

public class CoffeeModule {

  public static final String NBT_TIME_TO_NEXT_COFFEE = "SGTimeToNextCoffee";

  public static void tickRabbit(EntityRabbit rabbit) {

    Random rand = SilentGems.instance.random;

    if (!rabbit.getEntityData().hasKey(NBT_TIME_TO_NEXT_COFFEE))
      rabbit.getEntityData().setInteger(NBT_TIME_TO_NEXT_COFFEE, rand.nextInt(6000) + 6000);

    int time = rabbit.getEntityData().getInteger(NBT_TIME_TO_NEXT_COFFEE);
    if (--time <= 0) {
      int rabbitType = rabbit.getRabbitType();
      ItemStack coffee = getCoffeeForRabbitType(rabbitType);

      if (coffee != null) {
        rabbit.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f,
            (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);
        rabbit.entityDropItem(coffee.copy(), 0.0f);
      }
      time = rand.nextInt(6000) + 6000;
    }
    rabbit.getEntityData().setInteger(NBT_TIME_TO_NEXT_COFFEE, time);
  }

  private static ItemStack getCoffeeForRabbitType(int rabbitType) {

    // Maybe have different types of coffee?
//    switch (rabbitType) {
//      case 0:
//        // Brown
//        break;
//      case 1:
//        // White
//        break;
//      case 2:
//        // Black
//        break;
//      case 3:
//        // Black & White
//        break;
//      case 4:
//        // Gold = Vanilla
//        break;
//      case 5:
//        // Salt & Pepper
//        break;
//      case 99:
//        // Killer Bunny
//        return null;
//      default:
//        return null;
//    }

    return ModItems.food.coffeeCup;
  }
}
