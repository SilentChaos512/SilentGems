package net.silentchaos512.gems.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.item.HoldingGem;
import net.silentchaos512.gems.item.ModItems;


public class HoldingGemSetRecipe implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    int numHoldingGem = 0;
    int numBlocks = 0;
    
    ItemStack stack;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof HoldingGem) {
          ++numHoldingGem;
        } else if (stack.getItem() instanceof ItemBlock) {
          ++numBlocks;
        } else {
          return false;
        }
      }
    }
    
    return numHoldingGem == 1 && numBlocks == 1;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    
    ItemStack holdingGem = null;
    ItemStack itemBlock = null;
    
    ItemStack stack;
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (stack.getItem() instanceof HoldingGem) {
          holdingGem = stack;
        } else if (stack.getItem() instanceof ItemBlock) {
          itemBlock = stack;
        } else {
          return null;
        }
      }
    }
    
    if (holdingGem == null || itemBlock == null) {
      return null;
    }
    
    ItemStack result = holdingGem.copy();
    HoldingGem gem = (HoldingGem) holdingGem.getItem();
    gem.setContainedItemCount(result, itemBlock.stackSize);
    gem.setContainedItemName(result, itemBlock.getUnlocalizedName());
    gem.setContainedItemMeta(result, itemBlock.getItemDamage());
    
    return result;
  }

  @Override
  public int getRecipeSize() {

    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public ItemStack getRecipeOutput() {

    // TODO Auto-generated method stub
    return null;
  }

}
