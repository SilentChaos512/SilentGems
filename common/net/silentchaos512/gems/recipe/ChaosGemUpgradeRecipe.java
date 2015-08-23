package net.silentchaos512.gems.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.ChaosGem;
import net.silentchaos512.gems.item.ChaosRune;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.buff.ChaosBuff;

public class ChaosGemUpgradeRecipe implements IRecipe {

  private ItemStack getResult(ItemStack gemStack, List<ItemStack> upgrades) {

    // Check for null/bad recipe
    if (gemStack == null || upgrades == null || upgrades.isEmpty()) {
      return null;
    }

    // Don't modify the original!
    ItemStack result = gemStack.copy();
    ChaosGem chaosGem = (ChaosGem) gemStack.getItem();

    // Apply everything that we can.
    for (ItemStack upgrade : upgrades) {
      if (upgrade.getItem() instanceof ChaosRune) {
        // Apply rune
        if (upgrade.getItemDamage() < ChaosBuff.values().length) {
          ChaosBuff buff = ChaosBuff.values()[upgrade.getItemDamage()];
          if (chaosGem.canAddBuff(result, buff)) {
            chaosGem.addBuff(result, buff);
          } else {
            return null;
          }
        } else {
          return null;
        }
      } else if (upgrade.getItem() instanceof CraftingMaterial) {
        // Apply mini pylon
        if (upgrade.getItemDamage() == CraftingMaterial.getMetaFor(Names.MINI_PYLON)) {
          if (chaosGem.canAddMiniPylon(result)) {
            chaosGem.addMiniPylon(result);
          } else {
            return null;
          }
        } else {
          return null;
        }
      } else {
        return null;
      }
    }

    return result;
  }

  @Override
  public boolean matches(InventoryCrafting inventorycrafting, World world) {

    ItemStack stack = null;
    ItemStack chaosGem = null;
    ArrayList<ItemStack> upgrades = new ArrayList<ItemStack>();

    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof ChaosGem) {
          chaosGem = stack;
        } else if (stack.getItem() instanceof ChaosRune) {
          upgrades.add(stack);
        } else if (stack.getItem() instanceof CraftingMaterial) {
          if (stack.getItemDamage() == CraftingMaterial.getMetaFor(Names.MINI_PYLON)) {
            upgrades.add(stack);
          } else {
            return false;
          }
        }
      }
    }
    
    return getResult(chaosGem, upgrades) != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {

    ItemStack stack = null;
    ItemStack chaosGem = null;
    ArrayList<ItemStack> upgrades = new ArrayList<ItemStack>();
    
    for (int i = 0; i < inventorycrafting.getSizeInventory(); ++i) {
      stack = inventorycrafting.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof ChaosGem) {
          chaosGem = stack;
        } else if (stack.getItem() instanceof ChaosRune) {
          upgrades.add(stack);
        } else if (stack.getItem() instanceof CraftingMaterial) {
          if (stack.getItemDamage() == CraftingMaterial.getMetaFor(Names.MINI_PYLON)) {
            upgrades.add(stack);
          } else {
            return null;
          }
        }
      }
    }
    
    return getResult(chaosGem, upgrades);
  }

  @Override
  public int getRecipeSize() {

    return 9;
  }

  @Override
  public ItemStack getRecipeOutput() {

    return null;
  }

}
