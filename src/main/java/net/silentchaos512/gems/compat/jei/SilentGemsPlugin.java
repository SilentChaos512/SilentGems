package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.client.gui.GuiChaosAltar;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeCategory;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeMaker;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;

@JEIPlugin
public class SilentGemsPlugin implements IModPlugin {
    @Override
    public void register(IModRegistry reg) {
        doItemBlacklist(reg.getJeiHelpers().getIngredientBlacklist());
        doRecipeRegistration(reg, reg.getJeiHelpers().getGuiHelper());
        doAddDescriptions(reg);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        reg.addRecipeCategories(new AltarRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
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
        // Recipes
        reg.addRecipes(AltarRecipeMaker.getRecipes(), AltarRecipeCategory.CATEGORY);
        reg.addRecipes(ToolHelper.EXAMPLE_RECIPES, VanillaRecipeCategoryUid.CRAFTING);

        // Click areas
        reg.addRecipeClickArea(GuiChaosAltar.class, 80, 34, 25, 16, AltarRecipeCategory.CATEGORY);

        // Recipe crafting items
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.chaosAltar), AltarRecipeCategory.CATEGORY);
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
        reg.addDescription(new ItemStack(ModItems.torchBandolier), prefix + Names.TORCH_BANDOLIER);
        reg.addDescription(new ItemStack(ModItems.fluffyPuffSeeds), prefix + Names.FLUFFY_PUFF_SEEDS);
    }

    @Override
    public void registerIngredients(IModIngredientRegistration arg0) {
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry reg) {
        // Enchantment tokens
        reg.registerSubtypeInterpreter(ModItems.enchantmentToken, stack -> {
            Enchantment ench = ModItems.enchantmentToken.getSingleEnchantment(stack);
            return ench == null ? "none" : ench.getName();
        });

        // Chaos Runes
        reg.registerSubtypeInterpreter(ModItems.chaosRune, stack -> {
            ChaosBuff buff = ModItems.chaosRune.getBuff(stack);
            return buff == null ? "none" : buff.getKey();
        });
    }
}
