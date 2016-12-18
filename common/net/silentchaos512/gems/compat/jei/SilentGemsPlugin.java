package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.client.gui.GuiChaosAltar;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeCategory;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeHandler;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeMaker;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;

@JEIPlugin
public class SilentGemsPlugin implements IModPlugin {

  public static IJeiHelpers jeiHelper;

  @Override
  public void onRuntimeAvailable(IJeiRuntime runtime) {

  }

  @Override
  public void register(IModRegistry reg) {

    jeiHelper = reg.getJeiHelpers();
    IGuiHelper guiHelper = jeiHelper.getGuiHelper();

    doItemBlacklist(jeiHelper.getItemBlacklist());

    doRecipeRegistration(reg, guiHelper);

    // NBT subtypes (doesn't work?)
    // jeiHelper.getSubtypeRegistry().useNbtForSubtypes(ModItems.enchantmentToken);

    // NBT ignores (deprecated)
    // jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(EnumMaterialGrade.NBT_KEY);
    // jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ItemChaosStorage.NBT_CHARGE);
    // jeiHelper.getNbtIgnoreList().ignoreNbtTagNames(ModItems.returnHomeCharm,
    // ItemReturnHome.NBT_READY);

    doAddDescriptions(reg);
  }

  private void doItemBlacklist(IItemBlacklist list) {

    // Hide certain blocks/items
    int any = OreDictionary.WILDCARD_VALUE;
    list.addItemToBlacklist(new ItemStack(ModBlocks.gemLampInverted, 1, any));
    list.addItemToBlacklist(new ItemStack(ModBlocks.gemLampInvertedDark, 1, any));
    list.addItemToBlacklist(new ItemStack(ModBlocks.gemLampLit, 1, any));
    list.addItemToBlacklist(new ItemStack(ModBlocks.gemLampLitDark, 1, any));
    list.addItemToBlacklist(new ItemStack(ModBlocks.fluffyPuffPlant));
    list.addItemToBlacklist(new ItemStack(ModItems.toolRenderHelper));
  }

  private void doRecipeRegistration(IModRegistry reg, IGuiHelper guiHelper) {

    // Categories
    reg.addRecipeCategories(new AltarRecipeCategory(guiHelper));

    // Handlers
    reg.addRecipeHandlers(new AltarRecipeHandler());

    // Recipes
    reg.addRecipes(AltarRecipeMaker.getRecipes());

    // Click areas
    reg.addRecipeClickArea(GuiChaosAltar.class, 80, 34, 25, 16, AltarRecipeCategory.CATEGORY);

    // Recipe crafting items
    reg.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.chaosAltar),
        AltarRecipeCategory.CATEGORY);
  }
 
  private void doAddDescriptions(IModRegistry reg) {

    String prefix = "jei.silentgems:desc.";

    reg.addDescription(new ItemStack(ModBlocks.chaosAltar), prefix + Names.CHAOS_ALTAR);
    reg.addDescription(new ItemStack(ModBlocks.chaosFlowerPot), prefix + Names.CHAOS_FLOWER_POT);
    reg.addDescription(new ItemStack(ModBlocks.chaosNode), prefix + Names.CHAOS_NODE);
    reg.addDescription(new ItemStack(ModBlocks.chaosPylon, 1, 0), prefix + Names.CHAOS_PYLON + "0");
    reg.addDescription(new ItemStack(ModBlocks.chaosPylon, 1, 1), prefix + Names.CHAOS_PYLON + "1");
    reg.addDescription(new ItemStack(ModBlocks.materialGrader), prefix + Names.MATERIAL_GRADER);

    reg.addDescription(new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE), prefix + Names.GEM);
  }

  @Override
  public void registerIngredients(IModIngredientRegistration arg0) {

    // TODO Auto-generated method stub

  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry arg0) {

    // TODO Auto-generated method stub

  }
}
