package net.silentchaos512.gems.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.lib.Strings;

public class TippedToolRecipe implements IRecipe {

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    return getCraftingResult(inv) != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack tool = null;
    ItemStack upgrade = null;
    int upgradeValue = 0;
    ItemStack stack;
    Item item;
    int meta;

    // Find tool and upgrade
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        item = stack.getItem();
        meta = stack.getItemDamage();
        if (item instanceof GemPickaxe || item instanceof GemShovel || item instanceof GemAxe) {
          // Tool
          if (tool != null) {
            return null;
          }
          tool = stack.copy();
        } else if (item == ModItems.toolUpgrade && (meta == 0 || meta == 1)) {
          // Upgrade
          if (upgrade != null) {
            return null;
          }
          upgrade = stack;
          upgradeValue = meta + 1;
        } else {
          // Invalid
          return null;
        }
      }
    }

    if (tool != null && upgrade != null) {
      // Apply
      ItemStack result = tool.copy();
      if (result.stackTagCompound == null) {
        // Create NBT if needed.
        result.setTagCompound(new NBTTagCompound());
      } else if (result.stackTagCompound.hasKey(Strings.TOOL_ICON_TIP)) {
        // Can't upgrade a tool that has already been upgraded.
        return null;
      }
      result.stackTagCompound.setByte(Strings.TOOL_ICON_TIP, (byte) upgradeValue);
      return result;
    }

    return null;
  }

  @Override
  public int getRecipeSize() {

    return 2;
  }

  @Override
  public ItemStack getRecipeOutput() {

    // TODO Auto-generated method stub
    return null;
  }

}
