package net.silentchaos512.gems.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.item.armor.ArmorSG;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.material.ModMaterials;

public class DecorateToolRecipe implements IRecipe {

  public ItemStack getStackInRowAndColumn(InventoryCrafting inventorycrafting, int row, int column,
      int gridWidth) {

    if (row >= 0 && row < gridWidth && column >= 0 && column < gridWidth) {
      int slot = row + column * gridWidth;
      if (slot >= 0 && slot < inventorycrafting.getSizeInventory()) {
        return inventorycrafting.getStackInSlot(slot);
      }
    }

    return null;
  }

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    return getCraftingResult(inv) != null;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    ItemStack stack;
    ItemStack tool = null;

    int i;
    int row = 0;
    int column = 0;

    int toolCount = 0;
    int materialCount = 0;
    int woolCount = 0;
    int rodCount = 0;

    final int gridWidth = inv.getSizeInventory() == 4 ? 2 : 3;

    // Find tool, count materials
    for (i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        if (InventoryHelper.isGemTool(stack) || stack.getItem() instanceof ArmorSG) {
          // Found tool
          tool = stack;
          ++toolCount;
          // Get tool row/column
          row = i % gridWidth;
          column = i / gridWidth;
        } else if (isRepairMaterial(stack)) {
          // Found gem/repair material
          ++materialCount;
        } else if (InventoryHelper.isStackBlock(stack, Blocks.wool)) {
          // Found wool for handle
          ++woolCount;
        } else if (InventoryHelper.matchesOreDict(stack, Strings.ORE_DICT_STICK_FANCY)) {
          // Found a rod (currently not really used, but I'll leave it here).
          ++rodCount;
        } else {
          // Invalid item
          return null;
        }
      }
    }

    // Invalid number of ingredients?
    if (toolCount != 1 || woolCount > 1 || rodCount > 1 || tool == null) {
      return null;
    }
    if (materialCount + woolCount + rodCount == 0) {
      return null;
    }

    // The base material for the tool, needed in some cases.
    byte baseGem = (byte) ToolHelper.getToolGemId(tool);
    // The amount of durability to restore, based on materials used.
    int repairAmount = 0;
    // We need to count adjacent materials to make sure they aren't misplaced!
    int adjacentMaterials = 0;

    // Copy tool, we can't modify the original!
    ItemStack result = tool.copy();
    if (!result.hasTagCompound()) {
      result.setTagCompound(new NBTTagCompound());
    }

    int id;
    // Deco (the decorative bit on the end of the handle)
    stack = getStackInRowAndColumn(inv, row, column + 1, gridWidth);
    if (stack != null && isRepairMaterial(stack)) {
      id = ToolHelper.getIdFromMaterial(stack);
      ToolHelper.setToolRodDeco(result, id);
      repairAmount += getRepairAmount(result, stack);
      ++adjacentMaterials;
    }
    // HeadL
    stack = getStackInRowAndColumn(inv, row - 1, column, gridWidth);
    if (stack != null && isRepairMaterial(stack)) {
      id = ToolHelper.getIdFromMaterial(stack);
      ToolHelper.setToolHeadLeft(result, id);
      repairAmount += getRepairAmount(result, stack);
      ++adjacentMaterials;
    } else if (ToolHelper.getToolHeadLeft(result) == -1) {
      ToolHelper.setToolHeadLeft(result, baseGem);
    }
    // HeadM
    stack = getStackInRowAndColumn(inv, row, column - 1, gridWidth);
    if (stack != null && isRepairMaterial(stack)) {
      id = ToolHelper.getIdFromMaterial(stack);
      ToolHelper.setToolHeadMiddle(result, id);
      repairAmount += getRepairAmount(result, stack);
      ++adjacentMaterials;
    } else if (ToolHelper.getToolHeadMiddle(result) == -1) {
      ToolHelper.setToolHeadMiddle(result, baseGem);
    }
    // HeadR
    stack = getStackInRowAndColumn(inv, row + 1, column, gridWidth);
    if (stack != null && isRepairMaterial(stack)) {
      id = ToolHelper.getIdFromMaterial(stack);
      ToolHelper.setToolHeadRight(result, id);
      repairAmount += getRepairAmount(result, stack);
      ++adjacentMaterials;
    } else if (ToolHelper.getToolHeadRight(result) == -1) {
      ToolHelper.setToolHeadRight(result, baseGem);
    }

    // Right number of adjacent materials?
    if (adjacentMaterials != materialCount) {
      return null;
    }

    // Apply wool/rod
    for (i = 0; i < inv.getSizeInventory(); ++i) {
      stack = inv.getStackInSlot(i);
      if (stack != null) {
        // Wool
        if (InventoryHelper.isStackBlock(stack, Blocks.wool)) {
          ToolHelper.setToolRodWool(result, stack.getItemDamage());
        }
        // Rod (handle)
        else if (InventoryHelper.matchesOreDict(stack, Strings.ORE_DICT_STICK_FANCY)) {
          ToolHelper.setToolRod(tool, getToolRodId(stack));
        }
      }
    }

    // Repair with gems.
    result.attemptDamageItem(-repairAmount, SilentGems.instance.random);

    // Increase redecorated counter.
    ToolHelper.incrementStatRedecorated(result, 1);

    return result;
  }

  /**
   * Determines if the stack can be used for repairing tools.
   * 
   * @param material
   * @return True for valid repair materials, false otherwise.
   */
  public boolean isRepairMaterial(ItemStack material) {

    Item item = material.getItem();
    return item instanceof Gem || item == Items.flint
        || CraftingMaterial.doesStackMatch(material, Names.CHAOS_ESSENCE_PLUS_2)
        || item == Item.getItemFromBlock(Blocks.glass);
  }

  /**
   * Gems the number of durability points that should be repaired by crafting the given tool with the material. The
   * material should return true for isRepairMaterial, but that is not checked here.
   * 
   * @param tool
   *          The tool being repaired. Base material for the tool affects repair values.
   * @param material
   *          The repair material.
   * @return The amount to "undamage" the tool by.
   */
  public int getRepairAmount(ItemStack tool, ItemStack material) {

    if (material == null) {
      LogHelper.derp();
      return 0;
    }

    int baseMaterial = ToolHelper.getToolGemId(tool);
    int maxUses = tool.getItem().getMaxDamage(tool);

    // Glass has no repair value.
    if (material.getItem() == Item.getItemFromBlock(Blocks.glass)) {
      return 0;
    }

    // Crystallized chaos essence is a full repair.
    if (CraftingMaterial.doesStackMatch(material, Names.CHAOS_ESSENCE_PLUS_2)) {
      return maxUses;
    }

    // Various conditions that will be checked below.
    boolean toolIsSuper = ToolHelper.getToolIsSupercharged(tool);
    boolean materialIsGem = material.getItem() instanceof Gem;
    boolean materialIsSuperGem = materialIsGem && material.getItemDamage() >= 0x10;
    boolean materialIsFlint = material.getItem() == Items.flint;

    if (baseMaterial < EnumGem.values().length || baseMaterial == ModMaterials.CHAOS_GEM_ID) {
      // Gem tools
      // Regular tools: regular gems = 1/4, super = 1, other = 0
      // Super tools: regular gems = 1/8, super = 1/2, other = 0
      if (!materialIsGem) {
        return 0;
      }
      int value = maxUses;
      value /= toolIsSuper ? 2 : 1;
      value /= materialIsSuperGem ? 1 : (materialIsGem ? 4 : Integer.MAX_VALUE);
      return value;
    } else if (baseMaterial == ModMaterials.FLINT_GEM_ID) {
      // Flint tools
      // Gem = 1, flint = 1/2
      int value = maxUses;
      value /= materialIsFlint ? 2 : (materialIsGem ? 1 : Integer.MAX_VALUE);
      return value;
    } else if (baseMaterial == ModMaterials.FISH_GEM_ID) {
      // Fish tools
      return materialIsGem || materialIsFlint ? maxUses : 0;
    } else {
      LogHelper.debug("DecorateToolRecipe.getRepairAmount - Unknown gem ID: " + baseMaterial);
    }

    return 0;
  }

  public static int getToolRodId(ItemStack rod) {

    if (!InventoryHelper.matchesOreDict(rod, Strings.ORE_DICT_STICK_FANCY)) {
      LogHelper.warning("DecorateToolRecipe.getToolRodId was passed an invalid item: "
          + rod.getItem().getUnlocalizedName());
      return -1;
    }

    // if (rod.getItem() instanceof GemRod) {
    // return rod.getItemDamage();
    // } else if (rod.getItem() instanceof CraftingMaterial) {
    // int d = rod.getItemDamage();
    // if (d == CraftingMaterial.getMetaFor(Names.ORNATE_STICK)) {
    // return -1;
    // }else if (d == CraftingMaterial.getMetaFor(Names.FANCY_STICK_IRON)) {
    // return EnumGem.all().length;
    // } else if (d == CraftingMaterial.getMetaFor(Names.FANCY_STICK_COPPER)) {
    // return EnumGem.all().length + 1;
    // } else if (d == CraftingMaterial.getMetaFor(Names.FANCY_STICK_TIN)) {
    // return EnumGem.all().length + 2;
    // } else if (d == CraftingMaterial.getMetaFor(Names.FANCY_STICK_SILVER)) {
    // return EnumGem.all().length + 3;
    // }
    // }

    return -1;
  }

  @Override
  public int getRecipeSize() {

    return 9;
  }

  @Override
  public ItemStack getRecipeOutput() {

    return null;
  }

  @Override
  public ItemStack[] getRemainingItems(InventoryCrafting inv) {

    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      if (stack != null) {
        --stack.stackSize;
        if (stack.stackSize <= 0) {
          stack = null;
        }
        inv.setInventorySlotContents(i, stack);
      }
    }
    return new ItemStack[] {};
  }
}
