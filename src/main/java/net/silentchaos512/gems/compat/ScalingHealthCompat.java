package net.silentchaos512.gems.compat;

import java.util.Random;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.module.ModuleEntityRandomEquipment;
import net.silentchaos512.gems.util.ToolRandomizer;
import net.silentchaos512.lib.util.StackHelper;
import net.silentchaos512.scalinghealth.api.ScalingHealthAPI;
import net.silentchaos512.scalinghealth.utils.StackProducer;

public class ScalingHealthCompat {

  public static void init() {

    StackProducer producerRegular = new StackProducer(StackHelper.empty()) {

      @Override
      public ItemStack get(Random rand) {

        if (rand.nextFloat() < 0.2f) {
          return ToolRandomizer.INSTANCE.randomize(new ItemStack(ModItems.shovel), 0.15f);
        } else {
          return ToolRandomizer.INSTANCE.randomize(new ItemStack(ModItems.sword), 0.0f);
        }
      }
    };

    StackProducer producerSuper = new StackProducer(StackHelper.empty()) {

      @Override
      public ItemStack get(Random rand) {

        if (rand.nextBoolean()) {
          return ToolRandomizer.INSTANCE.randomize(new ItemStack(ModItems.katana), 0.75f);
        } else {
          return ToolRandomizer.INSTANCE.randomize(new ItemStack(ModItems.sword), 0.75f);
        }
      }
    };

    ScalingHealthAPI.addBlightEquipment(producerRegular, EntityEquipmentSlot.MAINHAND, 2);
    ScalingHealthAPI.addBlightEquipment(producerRegular, EntityEquipmentSlot.MAINHAND, 3);
    ScalingHealthAPI.addBlightEquipment(producerRegular, EntityEquipmentSlot.MAINHAND, 4);
    ScalingHealthAPI.addBlightEquipment(producerSuper, EntityEquipmentSlot.MAINHAND, 3);
    ScalingHealthAPI.addBlightEquipment(producerSuper, EntityEquipmentSlot.MAINHAND, 4);
  }
}
