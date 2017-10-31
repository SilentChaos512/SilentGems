package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.client.gui.GuiChaosAltar;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeCategory;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeHandler;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeMaker;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;

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

    doItemBlacklist(jeiHelper.getIngredientBlacklist());

    doRecipeRegistration(reg, guiHelper);

    doAddDescriptions(reg);
  }

  private void doItemBlacklist(IIngredientBlacklist list) {

    // Hide certain blocks/items
    int any = OreDictionary.WILDCARD_VALUE;
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.gemLampInverted, 1, any));
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.gemLampInvertedDark, 1, any));
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.gemLampInvertedLight, 1, any));
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.gemLampLit, 1, any));
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.gemLampLitDark, 1, any));
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.gemLampLitLight, 1, any));
    list.addIngredientToBlacklist(new ItemStack(ModBlocks.fluffyPuffPlant));
    list.addIngredientToBlacklist(new ItemStack(ModItems.toolRenderHelper));
    list.addIngredientToBlacklist(new ItemStack(ModItems.debugItem));
  }

  // FIXME
  private void doRecipeRegistration(IModRegistry reg, IGuiHelper guiHelper) {

    // Categories
    reg.addRecipeCategories(new AltarRecipeCategory(guiHelper));

    // Handlers
    reg.addRecipeHandlers(new AltarRecipeHandler());

    // Recipes
    reg.addRecipes(AltarRecipeMaker.getRecipes());//, SilentGems.MODID + ".altar");
    reg.addRecipes(ToolHelper.EXAMPLE_RECIPES, VanillaRecipeCategoryUid.CRAFTING);

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

    reg.addDescription(new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE),
        prefix + Names.GEM);
    reg.addDescription(new ItemStack(ModItems.torchBandolier), prefix + Names.TORCH_BANDOLIER);
  }

  @Override
  public void registerIngredients(IModIngredientRegistration arg0) {

    // TODO Auto-generated method stub

  }

  @Override
  public void registerItemSubtypes(ISubtypeRegistry reg) {

    // Tools
//    for (Item item : new Item[] { ModItems.sword, ModItems.katana, ModItems.scepter,
//        ModItems.tomahawk, ModItems.pickaxe, ModItems.shovel, ModItems.axe, ModItems.paxel,
//        ModItems.hoe, ModItems.sickle, ModItems.bow, ModItems.shield }) {
//      reg.registerSubtypeInterpreter(item, new ISubtypeInterpreter() {
//
//        @Override
//        public String getSubtypeInfo(ItemStack stack) {
//
//          ToolPart[] parts = ToolHelper.getConstructionParts(stack);
//          if (parts.length == 0)
//            return "unknown";
//          return parts[0].getKey();
//        }
//      });
//    }

    // Enchantment tokens
    reg.registerSubtypeInterpreter(ModItems.enchantmentToken, new ISubtypeInterpreter() {

      @Override
      public String getSubtypeInfo(ItemStack stack) {

        Enchantment ench = ModItems.enchantmentToken.getSingleEnchantment(stack);
        if (ench == null)
          return "none";
        return ench.getName();
      }
    });

    // Chaos Runes
    reg.registerSubtypeInterpreter(ModItems.chaosRune, new ISubtypeInterpreter() {

      @Override
      public String getSubtypeInfo(ItemStack stack) {

        ChaosBuff buff = ModItems.chaosRune.getBuff(stack);
        if (buff == null)
          return "none";
        return buff.getKey();
      }
    });
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration arg0) {

    // TODO Auto-generated method stub
    
  }
}
