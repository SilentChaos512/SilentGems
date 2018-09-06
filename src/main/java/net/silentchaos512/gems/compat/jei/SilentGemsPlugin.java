package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.gui.GuiChaosAltar;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeCategory;
import net.silentchaos512.gems.compat.jei.altar.AltarRecipeMaker;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.ChaosBuff;
import net.silentchaos512.gems.recipe.RecipeSoulUrnModify;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.util.StackHelper;

import java.util.Collection;
import java.util.Objects;

@JEIPlugin
public class SilentGemsPlugin implements IModPlugin {
    private static boolean initFailed = true;

    public static boolean hasInitFailed() {
        return initFailed;
    }

    @Override
    public void register(IModRegistry reg) {
        initFailed = true;
        doItemBlacklist(reg.getJeiHelpers().getIngredientBlacklist());
        doRecipeRegistration(reg, reg.getJeiHelpers().getGuiHelper());
        doAddDescriptions(reg);
        initFailed = false;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        initFailed = true;
        reg.addRecipeCategories(new AltarRecipeCategory(reg.getJeiHelpers().getGuiHelper()));
        initFailed = false;
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

        // Soul urn modify hints
        reg.addRecipes(RecipeSoulUrnModify.getExampleRecipes(), VanillaRecipeCategoryUid.CRAFTING);

        // Click areas
        reg.addRecipeClickArea(GuiChaosAltar.class, 80, 34, 25, 16, AltarRecipeCategory.CATEGORY);

        // Recipe crafting items
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.chaosAltar), AltarRecipeCategory.CATEGORY);
    }

    private void doAddDescriptions(IModRegistry reg) {
        addIngredientInfoPages(reg, SilentGems.registry.getBlocks());
        addIngredientInfoPages(reg, SilentGems.registry.getItems());
        reg.addIngredientInfo(new ItemStack(ModBlocks.chaosPylon, 1, 0), ItemStack.class, getDescKey("chaospylon0"));
        reg.addIngredientInfo(new ItemStack(ModBlocks.chaosPylon, 1, 1), ItemStack.class, getDescKey("chaospylon1"));
    }

    @Override
    public void registerIngredients(IModIngredientRegistration arg0) {
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry reg) {
        initFailed = true;
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

        // Soul Gems
//        reg.registerSubtypeInterpreter(ModItems.soulGem, stack -> {
//            ItemSoulGem.Soul soul = ModItems.soulGem.getSoul(stack);
//            return soul != null ? soul.id : "null";
//        });

        // Soul Urns
//        reg.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.soulUrn), stack -> {
//            int color = ModBlocks.soulUrn.getClayColor(stack);
//            return color != UrnConst.UNDYED_COLOR ? Integer.toString(color, 16) : "uncolored";
//        });
        initFailed = false;
    }

    private void addIngredientInfoPages(IModRegistry registry, Collection<? extends IForgeRegistryEntry<?>> list) {
        for (IForgeRegistryEntry<?> obj : list) {
            String key = getDescKey(Objects.requireNonNull(obj.getRegistryName()));
//            SilentGems.logHelper.debug("JEI desc key: {}", key);
            if (SilentGems.i18n.hasKey(key)) {
                ItemStack stack = StackHelper.fromBlockOrItem(obj);
                stack.setItemDamage(OreDictionary.WILDCARD_VALUE);
                registry.addIngredientInfo(stack, ItemStack.class, key);
            }
        }
    }

    private String getDescKey(String name) {
        return "jei." + SilentGems.MODID + "." + name + ".desc";
    }

    private String getDescKey(ResourceLocation name) {
        return "jei." + name.getNamespace() + "." + name.getPath() + ".desc";
    }
}
