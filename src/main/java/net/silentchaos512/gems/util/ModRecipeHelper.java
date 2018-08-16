package net.silentchaos512.gems.util;

import net.minecraft.item.ItemStack;

public class ModRecipeHelper {

  //@formatter:off
  private static final String SAG_MILL_MSG =
      "<recipeGroup name=\"SilentGems\">" +
        "<recipe name=\"%s\" energyCost=\"%d\">" +
          "<input>" +
            "<itemStack modID=\"SilentGems\" itemName=\"%s\" itemMeta=\"%d\" />" +
          "</input>" +
          "<output>" +
            "<itemStack modID=\"SilentGems\" itemName=\"%s\" itemMeta=\"%d\" number=\"%d\" />" +
            "<itemStack modID=\"SilentGems\" itemName=\"%s\" itemMeta=\"%d\" number=\"1\" chance=\"0.1\" />" +
            "<itemStack modID=\"minecraft\" itemName=\"%s\" chance=\"0.15\"/>" +
          "</output>" +
        "</recipe>" +
      "</recipeGroup>";
  //@formatter:on

  public static void addSagMillRecipe(String key, ItemStack input, ItemStack output,
      String stoneName, int energy) {

    addSagMillRecipe(key, input, output, stoneName, energy, output);
  }

  public static void addSagMillRecipe(String key, ItemStack input, ItemStack output,
      String stoneName, int energy, ItemStack extra) {

//    String inputName = input.getItem() instanceof IRegistryObject
//        ? ((IRegistryObject) input.getItem()).getName()
//        : input.getItem().getTranslationKey().replaceFirst("(item\\.silentgems:|tile\\.)", "");
//    String outputName = output.getItem() instanceof IRegistryObject
//        ? ((IRegistryObject) output.getItem()).getName()
//        : output.getItem().getTranslationKey().replaceFirst("(item\\.silentgems:|tile\\.)", "");
//    String extraName = extra.getItem() instanceof IRegistryObject
//        ? ((IRegistryObject) extra.getItem()).getName()
//        : extra.getItem().getTranslationKey().replaceFirst("(item\\.silentgems:|tile\\.)", "");
//
//    int inputMeta = input.getItemDamage();
//    int outputMeta = output.getItemDamage();
//    int extraMeta = extra.getItemDamage();
//
//    String str = String.format(SAG_MILL_MSG, key, energy, inputName, inputMeta, outputName,
//        outputMeta, 2, extraName, extraMeta, stoneName);
//    // SilentGems.instance.logHelper.debug(str);
//    FMLInterModComms.sendMessage("EnderIO", "recipe:sagmill", str);
  }
}
