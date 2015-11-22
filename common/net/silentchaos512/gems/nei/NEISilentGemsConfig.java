package net.silentchaos512.gems.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class NEISilentGemsConfig implements IConfigureNEI {

  @Override
  public void loadConfig() {

    API.hideItem(new ItemStack(ModBlocks.fluffyPlant));
    for (int i = 0; i < EnumGem.values().length; ++i) {
      API.hideItem(new ItemStack(ModBlocks.gemLampLit, 1, i));
      API.hideItem(new ItemStack(ModBlocks.gemLampInverted, 1, i));
    }
    for (int i = CraftingMaterial.HIDE_AFTER_META; i < CraftingMaterial.NAMES.length; ++i) {
      API.hideItem(new ItemStack(ModItems.craftingMaterial, 1, i));
    }
    API.hideItem(CraftingMaterial.getStack(Names.CHAOS_BOOSTER));
    API.hideItem(CraftingMaterial.getStack(Names.CHAOS_CAPACITOR));
    API.hideItem(new ItemStack(ModItems.chaosRune, 1, 10)); // Capacity
    API.hideItem(new ItemStack(ModItems.chaosRune, 1, 11)); // Boost
    API.hideItem(new ItemStack(ModItems.chaosRune, 1, 12)); // Absorption
    API.hideItem(new ItemStack(ModItems.debugItem));
  }

  @Override
  public String getName() {

    return "Silent's Gems NEI Plugin";
  }

  @Override
  public String getVersion() {

    return SilentGems.VERSION_NUMBER;
  }

}
