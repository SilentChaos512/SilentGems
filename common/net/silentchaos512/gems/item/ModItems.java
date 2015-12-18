package net.silentchaos512.gems.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.item.armor.ArmorSG;
import net.silentchaos512.gems.item.tool.GemAxe;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemHoe;
import net.silentchaos512.gems.item.tool.GemPickaxe;
import net.silentchaos512.gems.item.tool.GemShovel;
import net.silentchaos512.gems.item.tool.GemSickle;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.material.ModMaterials;
import net.silentchaos512.gems.recipe.ChaosGemUpgradeRecipe;
import net.silentchaos512.gems.recipe.DecorateToolRecipe;
import net.silentchaos512.gems.recipe.EnchantToolRecipe;
import net.silentchaos512.gems.recipe.RecipeToolUpgrade;
import net.silentchaos512.gems.recipe.TorchBandolierExtractRecipe;
import net.silentchaos512.gems.recipe.TorchBandolierRecipe;

public class ModItems {

  public static Gem gem;
  public static GemShard gemShard;
  public static CraftingMaterial craftingMaterial;
  public static FoodSG food;
  public static TorchBandolier torchBandolier;
  // public static HoldingGem holdingGem;
  public static TeleporterLinker teleporterLinker;
  public static FluffyPlantSeeds fluffyPuff;
  public static ReturnHome returnHome;
  public static PetSummon petSummon;
  public static EnchantToken enchantmentToken;
  public static ItemToolUpgrade toolUpgrade;
  public static ChaosRune chaosRune;
  public static DyeSG dye;
  public static DebugItem debugItem;
  public static ChaosEssence chaosEssence;
  public static ToolRenderHelperBase toolRenderHelper;

  public static void init() {

    gem = (Gem) SRegistry.registerItem(Gem.class, Names.GEM_ITEM);
    gemShard = (GemShard) SRegistry.registerItem(GemShard.class, Names.GEM_SHARD);
    craftingMaterial = (CraftingMaterial) SRegistry.registerItem(CraftingMaterial.class,
        Names.CRAFTING_MATERIALS);
    food = (FoodSG) SRegistry.registerItem(FoodSG.class, Names.FOOD);
    torchBandolier = (TorchBandolier) SRegistry.registerItem(TorchBandolier.class,
        Names.TORCH_BANDOLIER);
    // holdingGem = (HoldingGem) SRegistry.registerItem(HoldingGem.class, Names.HOLDING_GEM);
    teleporterLinker = (TeleporterLinker) SRegistry.registerItem(TeleporterLinker.class,
        Names.TELEPORTER_LINKER);
    fluffyPuff = (FluffyPlantSeeds) SRegistry.registerItem(FluffyPlantSeeds.class,
        Names.FLUFFY_SEED);
    returnHome = (ReturnHome) SRegistry.registerItem(ReturnHome.class, Names.RETURN_HOME);
    petSummon = (PetSummon) SRegistry.registerItem(PetSummon.class, Names.SUMMON_PET);
    enchantmentToken = (EnchantToken) SRegistry.registerItem(EnchantToken.class,
        Names.ENCHANT_TOKEN);
    toolUpgrade = (ItemToolUpgrade) SRegistry.registerItem(ItemToolUpgrade.class,
        Names.TOOL_UPGRADE);
    chaosRune = (ChaosRune) SRegistry.registerItem(ChaosRune.class, Names.CHAOS_RUNE);
    dye = (DyeSG) SRegistry.registerItem(DyeSG.class, Names.DYE);
    chaosEssence = (ChaosEssence) SRegistry.registerItem(ChaosEssence.class,
        Names.CHAOS_ESSENCE_OLD);

    /*
     * Chaos Gems
     */
    for (int i = 0; i < EnumGem.values().length; ++i) {
      SRegistry.registerItem(ChaosGem.class, Names.CHAOS_GEM + i, i);
    }
    SRegistry.registerItem(ChaosGem.class, Names.CHAOS_GEM + ChaosGem.CHEATY_GEM_ID,
        ChaosGem.CHEATY_GEM_ID);

    /*
     * Tools
     */

    if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
      toolRenderHelper = (ToolRenderHelperBase) SRegistry.registerItem(ToolRenderHelper.class,
          "ToolRenderHelper");
    } else {
      toolRenderHelper = (ToolRenderHelperBase) SRegistry.registerItem(ToolRenderHelperBase.class,
          "ToolRenderHelper");
    }
    ToolRenderHelper.init();

    Object[] params = new Object[] { null, 0, false }; // Constructor parameters

    // Flint tools
    params[0] = ModMaterials.toolFlint;
    params[1] = ModMaterials.FLINT_GEM_ID;
    params[2] = false;
    SRegistry.registerItem(GemSword.class, "SwordFlint", params);
    SRegistry.registerItem(GemPickaxe.class, "PickaxeFlint", params);
    SRegistry.registerItem(GemShovel.class, "ShovelFlint", params);
    SRegistry.registerItem(GemAxe.class, "AxeFlint", params);
    SRegistry.registerItem(GemHoe.class, "HoeFlint", params);
    SRegistry.registerItem(GemSickle.class, "SickleFlint", params);
    SRegistry.registerItem(GemBow.class, "BowFlint", params);

    // Chaos tools
    params[0] = ModMaterials.toolChaos;
    params[1] = ModMaterials.CHAOS_GEM_ID;
    params[2] = true;
    SRegistry.registerItem(GemSword.class, "SwordChaos", params);
    SRegistry.registerItem(GemPickaxe.class, "PickaxeChaos", params);
    SRegistry.registerItem(GemShovel.class, "ShovelChaos", params);
    SRegistry.registerItem(GemAxe.class, "AxeChaos", params);
    SRegistry.registerItem(GemHoe.class, "HoeChaos", params);
    SRegistry.registerItem(GemSickle.class, "SickleChaos", params);
    SRegistry.registerItem(GemBow.class, "BowChaos", params);

    // Gem tools
    for (int i = 0; i < 24; ++i) {
      boolean supercharged = i >= 12;
      int gem = supercharged ? i - 12 : i;
      params[0] = EnumGem.values()[gem].getToolMaterial(supercharged);
      params[1] = gem;
      params[2] = supercharged;
      String s = gem + (supercharged ? "Plus" : "");
      SRegistry.registerItem(GemSword.class, "Sword" + s, params);
      SRegistry.registerItem(GemPickaxe.class, "Pickaxe" + s, params);
      SRegistry.registerItem(GemShovel.class, "Shovel" + s, params);
      SRegistry.registerItem(GemAxe.class, "Axe" + s, params);
      SRegistry.registerItem(GemHoe.class, "Hoe" + s, params);
      SRegistry.registerItem(GemSickle.class, "Sickle" + s, params);
      SRegistry.registerItem(GemBow.class, "Bow" + s, params);
    }

    // Fish tools.
    params[0] = ModMaterials.toolFish;
    params[1] = ModMaterials.FISH_GEM_ID;
    params[2] = false;
    SRegistry.registerItem(GemSword.class, "SwordFish", params);
    SRegistry.registerItem(GemPickaxe.class, "PickaxeFish", params);
    SRegistry.registerItem(GemShovel.class, "ShovelFish", params);
    SRegistry.registerItem(GemAxe.class, "AxeFish", params);
    SRegistry.registerItem(GemHoe.class, "HoeFish", params);
    SRegistry.registerItem(GemSickle.class, "SickleFish", params);

    /*
     * Armor
     */
    for (int i = 0; i < 24; ++i) {
      boolean supercharged = i >= 12;
      int gem = supercharged ? i - 12 : i;
      String s = gem + (supercharged ? "Plus" : "");
      ArmorMaterial material = EnumGem.values()[gem].getArmorMaterial(supercharged);
      ItemStack craftingItem = new ItemStack(ModItems.gem, 1, (supercharged ? gem | 16 : gem));

      String name = "Helmet" + s;
      String texture = "GemArmor" + gem;
      SRegistry.registerItem(ArmorSG.class, name, material, 0, 0, name, texture, craftingItem);
      name = "Chestplate" + s;
      SRegistry.registerItem(ArmorSG.class, name, material, 0, 1, name, texture, craftingItem);
      name = "Leggings" + s;
      SRegistry.registerItem(ArmorSG.class, name, material, 0, 2, name, texture, craftingItem);
      name = "Boots" + s;
      SRegistry.registerItem(ArmorSG.class, name, material, 0, 3, name, texture, craftingItem);
    }

    SRegistry.registerItem(ArmorSG.class, "CottonHelmet", ArmorSG.materialCotton, 0, 0,
        "CottonHelmet");
    SRegistry.registerItem(ArmorSG.class, "CottonChestplate", ArmorSG.materialCotton, 0, 1,
        "CottonChestplate");
    SRegistry.registerItem(ArmorSG.class, "CottonLeggings", ArmorSG.materialCotton, 0, 2,
        "CottonLeggings");
    SRegistry.registerItem(ArmorSG.class, "CottonBoots", ArmorSG.materialCotton, 0, 3,
        "CottonBoots");

    // Debug Item
    debugItem = (DebugItem) SRegistry.registerItem(DebugItem.class, Names.DEBUG_ITEM);
  }

  // Recipes
  public static IRecipe recipeChaosGemUpgrade;
  public static IRecipe recipeDecorateTool;
  public static IRecipe recipeEnchantTool;
  public static IRecipe recipeTorchBandolier;
  public static IRecipe recipeTorchBandolierExtract;
  public static IRecipe recipeToolUpgrade;

  public static void initItemRecipes() {

    String afterShapeless = "after:minecraft:shapeless";
    recipeChaosGemUpgrade = addRecipeHandler(ChaosGemUpgradeRecipe.class, "ChaosRune",
        Category.SHAPELESS, afterShapeless);
    recipeDecorateTool = addRecipeHandler(DecorateToolRecipe.class, "DecorateTool", Category.SHAPED,
        afterShapeless);
    recipeEnchantTool = addRecipeHandler(EnchantToolRecipe.class, "EnchantTool", Category.SHAPELESS,
        afterShapeless);
    recipeTorchBandolier = addRecipeHandler(TorchBandolierRecipe.class, "TorchBandolierDecorate",
        Category.SHAPELESS, afterShapeless);
    recipeTorchBandolierExtract = addRecipeHandler(TorchBandolierExtractRecipe.class,
        "TorchBandolierExtract", Category.SHAPELESS, afterShapeless);
    recipeToolUpgrade = addRecipeHandler(RecipeToolUpgrade.class, "ToolUpgrade", Category.SHAPELESS,
        afterShapeless);
    // addRecipeHandler(HoldingGemSetRecipe.class, "HoldingGemSet", Category.SHAPELESS, afterShapeless);
  }

  private static IRecipe addRecipeHandler(Class<? extends IRecipe> recipeClass, String name,
      Category category, String dependancies) {

    try {
      IRecipe recipe = recipeClass.newInstance();
      GameRegistry.addRecipe(recipe);
      RecipeSorter.INSTANCE.register(Strings.RESOURCE_PREFIX + name, recipeClass, category,
          dependancies);
      return recipe;
    } catch (Exception ex) {
      LogHelper.severe("Failed to register recipe class: " + recipeClass.toString());
    }
    return null;
  }

  public static void addRandomChestGenLoot() {

    // Add loot to bonus chest.
    ItemStack stack;
    WeightedRandomChestContent content;
    // Flint
    stack = new ItemStack(Items.flint);
    content = new WeightedRandomChestContent(stack, 3, 7, 12);
    ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(content);
    // Sugar cookies
    stack = new ItemStack(food, 1, 1);
    content = new WeightedRandomChestContent(stack, 4, 8, 8);
    ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(content);

  }
}
