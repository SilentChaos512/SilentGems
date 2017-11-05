package net.silentchaos512.gems.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.part.ArmorPartFrame;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.lib.collection.ItemStackList;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.StackHelper;

public class RecipeMixedMaterialItem extends RecipeBaseSL {

  public static final char CHAR_HEAD_PART = 'h';
  public static final char CHAR_ROD_PART = 'r';
  public static final char CHAR_ARMOR_FRAME = 'f';

  final ShapedRecipes shapedRecipe;
  final Item toolItem;
  final @Nullable EnumMaterialTier tierRestriction;

  public RecipeMixedMaterialItem(@Nullable EnumMaterialTier tierRestriction, Item toolOrArmorItem,
      Object... recipe) {

    this.tierRestriction = tierRestriction;
    this.toolItem = toolOrArmorItem;

    List<String> layout = new ArrayList<>();
    int recipeWidth = 0, recipeHeight = 0, recipeSize;
    int index = 0;

    // First, read layout
    while (index < recipe.length && recipe[index] instanceof String) {
      String str = (String) recipe[index++];
      layout.add(str);
      recipeWidth = str.length();
    }
    recipeHeight = layout.size();
    recipeSize = recipeWidth * recipeHeight;

    Map<Character, Ingredient> itemMap = new HashMap<>();
    itemMap.put(' ', Ingredient.EMPTY);
    itemMap.put(CHAR_HEAD_PART, new IngredientToolPart(ToolPartPosition.HEAD));
    itemMap.put(CHAR_ROD_PART, new IngredientToolPart(ToolPartPosition.ROD));
    itemMap.put(CHAR_ARMOR_FRAME, new IngredientToolPart(ArmorPartPosition.FRAME));

    // Read item map (ignoring any errors for now)
    for (; index < recipe.length; index += 2) {
      Character chr = (Character) recipe[index];
      if (chr == CHAR_HEAD_PART || chr == CHAR_ROD_PART || chr == ' ') {
        throw new IllegalArgumentException(chr + " is a reserved character.");
      }
      Object in = recipe[index + 1];
      Ingredient ing = CraftingHelper.getIngredient(in);

      if (ing != null) {
        itemMap.put(chr, ing);
      }
    }

    NonNullList<Ingredient> ingredients = NonNullList.withSize(recipeSize, Ingredient.EMPTY);

    int x = 0;
    for (String str : layout) {
      for (char chr : str.toCharArray()) {
        Ingredient ing = itemMap.get(chr);
        if (ing != null) {
          ingredients.set(x++, ing);
        }
      }
    }

    this.shapedRecipe = new ShapedRecipes(SilentGems.MODID, recipeWidth, recipeHeight, ingredients,
        new ItemStack(toolOrArmorItem));
  }

  @Override
  public boolean matches(InventoryCrafting inv, World world) {

    if (tierRestriction != null && !partTiersMatch(inv)) {
      // Need to let the checks fall to the next candidate recipe if we have
      // tier restrictions.
      return false;
    }

    ItemStack stackFrame = getFrame(inv);
    if (StackHelper.isValid(stackFrame)) {
      ArmorPartFrame frame = (ArmorPartFrame) ToolPartRegistry.fromStack(stackFrame);
      if (!doesFrameMatchItem(frame)) {
        return false;
      }
    }

    // Don't check tiers, we want to return an empty stack if things don't match.
    // If we return true here with an otherwise correct layout, we get an empty
    // tool thanks to the example recipes.
    return this.shapedRecipe.matches(inv, world);
  }

  @Override
  public @Nonnull ItemStack getRecipeOutput() {

    // Doesn't affect crafting, this just allows CraftTweaker to find the recipe.
    return new ItemStack(this.toolItem);
  }

  protected boolean partTiersMatch(InventoryCrafting inv) {

    EnumMaterialTier tier = null;

    // Check mains
    for (ItemStack stack : getMaterials(inv)) {
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (tier == null) {
        tier = part.getTier();
      } else if (tier != part.getTier()) {
        return false;
      }
    }

    // No mains found?
    if (tier == null) {
      return false;
    }

    // Tier restrictions?
    if (tierRestriction != null && tier != tierRestriction) {
      return false;
    }

    // Check rod
    ItemStack rod = getRod(inv);
    if (StackHelper.isValid(rod))
      return ToolPartRegistry.fromStack(rod).validForToolOfTier(tier);

    // Check frame
    ItemStack frame = getFrame(inv);
    if (StackHelper.isValid(frame)) {
      ToolPart partFrame = ToolPartRegistry.fromStack(frame);
      if (partFrame instanceof ArmorPartFrame) {
        return partFrame.validForToolOfTier(tier) && doesFrameMatchItem((ArmorPartFrame) partFrame);
      }
    }

    return true;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {

    if (!partTiersMatch(inv)) {
      return StackHelper.empty();
    }

    ItemStack rod = getRod(inv);
    ItemStack frame = getFrame(inv);
    ItemStackList materials = getMaterials(inv);
    ItemStack[] array = materials.toArray(new ItemStack[materials.size()]);

    if (toolItem instanceof ITool)
      return ((ITool) toolItem).constructTool(rod, array);
    if (toolItem instanceof IArmor)
      return ((IArmor) toolItem).constructArmor(frame, array);

    return StackHelper.empty();
  }

  protected ItemStackList getMaterials(InventoryCrafting inv) {

    ItemStackList list = ItemStackList.create();
    for (int i = 0; i < inv.getSizeInventory(); ++i) {
      ItemStack stack = inv.getStackInSlot(i);
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && !part.isBlacklisted(stack) && part instanceof ToolPartMain) {
        list.add(stack);
      }
    }
    return list;
  }

  protected ItemStack getRod(InventoryCrafting inv) {

    ItemStack rod = StackHelper.empty();
    for (ItemStack stack : getNonEmptyStacks(inv)) {
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && part instanceof ToolPartRod) {
        if (StackHelper.isEmpty(rod)) {
          rod = stack;
        } else if (!rod.isItemEqual(stack)) {
          return StackHelper.empty();
        }
      }
    }
    return rod;
  }

  protected ItemStack getFrame(InventoryCrafting inv) {

    ItemStack frame = StackHelper.empty();
    for (ItemStack stack : getNonEmptyStacks(inv)) {
      ToolPart part = ToolPartRegistry.fromStack(stack);
      if (part != null && part instanceof ArmorPartFrame) {
        if (StackHelper.isEmpty(frame)) {
          frame = stack;
        } else if (!frame.isItemEqual(stack)) {
          return StackHelper.empty();
        }
      }
    }
    return frame;
  }

  protected boolean doesFrameMatchItem(ArmorPartFrame frame) {

    if (!(toolItem instanceof ItemArmor))
      return true;
    return frame.getSlot() == ((ItemArmor) toolItem).armorType;
  }
}
