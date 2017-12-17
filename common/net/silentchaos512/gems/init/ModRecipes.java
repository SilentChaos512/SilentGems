package net.silentchaos512.gems.init;

import static net.silentchaos512.gems.api.lib.EnumMaterialTier.MUNDANE;
import static net.silentchaos512.gems.api.lib.EnumMaterialTier.REGULAR;
import static net.silentchaos512.gems.api.lib.EnumMaterialTier.SUPER;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.silentchaos512.gems.api.SilentGemsAPI;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.recipe.RecipeApplyEnchantmentToken;
import net.silentchaos512.gems.recipe.RecipeApplyToolSoul;
import net.silentchaos512.gems.recipe.RecipeChaosGemUpgrade;
import net.silentchaos512.gems.recipe.RecipeDecorateArmor;
import net.silentchaos512.gems.recipe.RecipeDecorateTool;
import net.silentchaos512.gems.recipe.RecipeHoldingGemSetBlock;
import net.silentchaos512.gems.recipe.RecipeMixedMaterialItem;
import net.silentchaos512.gems.recipe.RecipeNamePlate;
import net.silentchaos512.gems.recipe.RecipeToolSoul;
import net.silentchaos512.lib.registry.IRegistrationHandler;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.registry.SRegistry;

public class ModRecipes implements IRegistrationHandler<IRecipe> {

  public static final boolean ADD_SOUL_RECIPES = true;

  @Override
  public void registerAll(SRegistry reg) {

    RecipeMaker recipes = reg.recipes;

    // Chaos Essence creation.
    SilentGemsAPI.addAltarRecipe(ModItems.craftingMaterial.chaosEssence,
        ModItems.craftingMaterial.getStack(Names.CHAOS_ESSENCE_SHARD, 4), 240000,
        new ItemStack(Items.DIAMOND));

    // Ender Essence creation.
//    SilentGemsAPI.addAltarRecipe(ModItems.craftingMaterial.enderEssence,
//        ModItems.craftingMaterial.getStack(Names.ENDER_ESSENCE_SHARD, 4), 240000,
//        ModItems.craftingMaterial.enderSlimeBall);

    // Light <--> Dark gem conversion.
    ItemStack slimeBall = new ItemStack(Items.SLIME_BALL);
    ItemStack magmaCream = new ItemStack(Items.MAGMA_CREAM);
    ItemStack enderSlime = ModItems.craftingMaterial.enderSlimeBall;
    for (int i = 0; i < 16; ++i) {
      EnumGem classic = EnumGem.values()[i];
      EnumGem dark = EnumGem.values()[i + 16];
      EnumGem light = EnumGem.values()[i + 32];
      ItemStack classicShards = new ItemStack(ModItems.gemShard, 6, classic.ordinal());
      ItemStack darkShards = new ItemStack(ModItems.gemShard, 6, dark.ordinal());
      ItemStack lightShards = new ItemStack(ModItems.gemShard, 6, light.ordinal());
      SilentGemsAPI.addAltarRecipe(darkShards, classic.getItem(), 80000, magmaCream);
      SilentGemsAPI.addAltarRecipe(classicShards, dark.getItem(), 80000, slimeBall);
      SilentGemsAPI.addAltarRecipe(lightShards, classic.getItem(), 80000, enderSlime);
      SilentGemsAPI.addAltarRecipe(classicShards, light.getItem(), 80000, slimeBall);
    }

    //@formatter:off
    // Axe
    recipes.addCustomRecipe("multipart_axe",
        new RecipeMixedMaterialItem(null, ModItems.axe, "hh", "hr", " r"));
    // Bow
    recipes.addCustomRecipe("multipart_bow_mundane",
        new RecipeMixedMaterialItem(MUNDANE, ModItems.bow, "rhs", "h s", "rhs", 's', Items.STRING));
    recipes.addCustomRecipe("multipart_bow_regular",
        new RecipeMixedMaterialItem(REGULAR, ModItems.bow, "rhs", "h s", "rhs", 's', Items.STRING));
    recipes.addCustomRecipe("multipart_bow_super",
        new RecipeMixedMaterialItem(SUPER, ModItems.bow, "rhs", "h s", "rhs", 's', ModItems.craftingMaterial.gildedString));
    // Dagger
    recipes.addCustomRecipe("multipart_dagger",
        new RecipeMixedMaterialItem(null, ModItems.dagger, "h", "r"));
    // Hoe
    recipes.addCustomRecipe("multipart_hoe",
        new RecipeMixedMaterialItem(null, ModItems.hoe, "hh", " r", " r"));
    // Katana
    recipes.addCustomRecipe("multipart_katana",
        new RecipeMixedMaterialItem(null, ModItems.katana, "hh", "h ", "r "));
    // Machete
    recipes.addCustomRecipe("multipart_machete",
        new RecipeMixedMaterialItem(null, ModItems.machete, " hh", " h ", "r  "));
    // Paxel
    recipes.addCustomRecipe("multipart_paxel",
        new RecipeMixedMaterialItem(null, ModItems.paxel, "hhh", "hrh", "hr "));
    // Pickaxe
    recipes.addCustomRecipe("multipart_pickaxe",
        new RecipeMixedMaterialItem(null, ModItems.pickaxe, "hhh", " r ", " r "));
    // Scepter
    recipes.addCustomRecipe("multipart_scepter",
        new RecipeMixedMaterialItem(null, ModItems.scepter, " h ", "hrh", "hrh"));
    // Shield
    recipes.addCustomRecipe("multipart_shield",
        new RecipeMixedMaterialItem(null, ModItems.shield, "hwh", "wrw", " h ", 'w', "plankWood"));
    // Shovel
    recipes.addCustomRecipe("multipart_shovel",
        new RecipeMixedMaterialItem(null, ModItems.shovel, "h", "r", "r"));
    // Sickle
    recipes.addCustomRecipe("multipart_sickle",
        new RecipeMixedMaterialItem(null, ModItems.sickle, " h", "hh", "r "));
    // Sword
    recipes.addCustomRecipe("multipart_sword",
        new RecipeMixedMaterialItem(null, ModItems.sword, "h", "h", "r"));
    // Tomahawk
    recipes.addCustomRecipe("multipart_tomahawk",
        new RecipeMixedMaterialItem(null, ModItems.tomahawk, "hhh", "hr ", " r "));

    // Helmet
    recipes.addCustomRecipe("multipart_helmet",
        new RecipeMixedMaterialItem(null, ModItems.gemHelmet, " h ", "hfh", " h "));
    // Chestplate
    recipes.addCustomRecipe("multipart_chestplate",
        new RecipeMixedMaterialItem(null, ModItems.gemChestplate, " h ", "hfh", " h "));
    // Leggings
    recipes.addCustomRecipe("multipart_leggings",
        new RecipeMixedMaterialItem(null, ModItems.gemLeggings, " h ", "hfh", " h "));
    // Boots
    recipes.addCustomRecipe("multipart_boots",
        new RecipeMixedMaterialItem(null, ModItems.gemBoots, " h ", "hfh", " h "));
    //@formatter:on

    recipes.addCustomRecipe("decorate_tool", new RecipeDecorateTool());
    recipes.addCustomRecipe("decorate_armor", new RecipeDecorateArmor());
    recipes.addCustomRecipe("apply_enchantment_token", new RecipeApplyEnchantmentToken());
    recipes.addCustomRecipe("chaos_gem_upgrade", new RecipeChaosGemUpgrade());
    recipes.addCustomRecipe("name_plate_use", new RecipeNamePlate());
    recipes.addCustomRecipe("holding_gem_set_block", new RecipeHoldingGemSetBlock());
    if (ADD_SOUL_RECIPES) {
      recipes.addCustomRecipe("craft_tool_soul", new RecipeToolSoul());
      recipes.addCustomRecipe("apply_tool_soul", new RecipeApplyToolSoul());
    }
  }
}
