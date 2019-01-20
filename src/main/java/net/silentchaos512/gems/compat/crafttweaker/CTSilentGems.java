package net.silentchaos512.gems.compat.crafttweaker;

//import crafttweaker.CraftTweakerAPI;
//import crafttweaker.annotations.ZenRegister;
//import crafttweaker.mc1120.recipes.MCRecipeManager;
//import crafttweaker.mc1120.recipes.MCRecipeManager.ActionBaseAddRecipe;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.silentchaos512.gems.SilentGems;
//import net.silentchaos512.gems.api.lib.EnumMaterialTier;
//import stanhebben.zenscript.annotations.ZenClass;
//import stanhebben.zenscript.annotations.ZenMethod;

//@ZenClass("mods.silentgems")
//@ZenRegister
public class CTSilentGems {

//  static List<ActionBaseAddRecipe> RECIPES_TO_ADD = new ArrayList<>();
//
//  @ZenMethod
//  public static void setToolRecipe(String itemClass, int tierRestriction, String[] recipe,
//      Object... ingredients) {
//
//    Item toolItem = Item.getByNameOrId(SilentGems.RESOURCE_PREFIX + itemClass.toLowerCase());
//
//    EnumMaterialTier tier = null;
//    if (tierRestriction >= 0 && tierRestriction < EnumMaterialTier.values().length) {
//      tier = EnumMaterialTier.values()[tierRestriction];
//    }
//
//    Object[] params = new Object[recipe.length + ingredients.length];
//    for (int i = 0; i < recipe.length; ++i) {
//      params[i] = recipe[i];
//    }
//    for (int i = 0; i < ingredients.length; ++i) {
//      if (ingredients[i] instanceof String) {
//        String str = (String) ingredients[i];
//        if (str.length() == 1) {
//          // Character?
//          params[recipe.length + i] = str.charAt(0);
//        } else if (str.startsWith("ore:")) {
//          // Oredict key?
//          params[recipe.length + i] = str.replaceFirst("ore:", "");
//        } else {
//          // Item string?
//          // We can't use CraftTweaker's typical ingredient annotation (<mod:item_name:meta>) because it doesn't yield
//          // any values I can use. :(
//          String[] array = str.split(":");
//          String itemName = array[0] + ":" + array[1];
//          Item item = Item.getByNameOrId(itemName);
//          if (item != null) {
//            int meta = 0;
//            if (array.length > 2) {
//              try {
//                meta = Integer.parseInt(array[2]);
//              } catch (NumberFormatException ex) {
//                // Ignore
//              }
//            }
//            params[recipe.length + i] = new ItemStack(item, 1, meta);
//          }
//        }
//      } else {
//        // Nothing else will work, I assume? But pass it along anyway.
//        params[recipe.length + i] = ingredients[i];
//      }
//    }
//
//    ActionAddMixedMaterialRecipe action = new ActionAddMixedMaterialRecipe(toolItem, tier, params);
//    MCRecipeManager.recipesToAdd.add(action);
//    //RECIPES_TO_ADD.add(action);
//  }

  public static void postInit() {

    //RECIPES_TO_ADD.forEach(CraftTweakerAPI::apply);
  }
}
