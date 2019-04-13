package net.silentchaos512.gems.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotBlock;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterBlock;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterGui;
import net.silentchaos512.gems.crafting.tokenenchanter.TokenEnchanterRecipeManager;
import net.silentchaos512.gems.item.ChaosRune;
import net.silentchaos512.gems.item.EnchantmentToken;
import net.silentchaos512.gems.item.SoulGem;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.gems.lib.soul.Soul;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JeiPlugin
public class SilentGemsPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = SilentGems.getId("plugin/main");
    static final ResourceLocation TOKEN_ENCHANTING = SilentGems.getId("category/token_enchanting");
    static final ResourceLocation GUI_TEXTURE = SilentGems.getId("textures/gui/recipe_display.png");

    private static boolean initFailed = true;

    @Nullable private TokenEnchanterRecipeCategoryJei tokenEnchanterCategory;

    public static boolean hasInitFailed() {
        return initFailed;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        initFailed = true;

        IGuiHelper guiHelper = reg.getJeiHelpers().getGuiHelper();
        tokenEnchanterCategory = new TokenEnchanterRecipeCategoryJei(guiHelper);
        reg.addRecipeCategories(
                tokenEnchanterCategory
        );

        initFailed = false;
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        initFailed = true;

        checkNotNull(tokenEnchanterCategory, "tokenEnchanterCategory");

        reg.addRecipes(TokenEnchanterRecipeManager.getValues(), TOKEN_ENCHANTING);

        addInfoPage(reg, LuminousFlowerPotBlock.INSTANCE.get());
        addInfoPage(reg, SoulGem.INSTANCE.get(), Soul.getValues().stream().map(Soul::getSoulGem));

        // Soul urn modify hints
//        reg.addRecipes(RecipeSoulUrnModify.getExampleRecipes(), VanillaRecipeCategoryUid.CRAFTING);

        initFailed = false;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(TokenEnchanterBlock.INSTANCE.get()), TOKEN_ENCHANTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(TokenEnchanterGui.class, 102, 32, 24, 23, TOKEN_ENCHANTING);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        initFailed = true;
        // Enchantment tokens
        reg.registerSubtypeInterpreter(EnchantmentToken.INSTANCE.get(), stack -> {
            Enchantment enchantment = EnchantmentToken.getSingleEnchantment(stack);
            return enchantment != null ? enchantment.getName() : "none";
        });

        // Chaos Runes
        reg.registerSubtypeInterpreter(ChaosRune.INSTANCE.get(), stack -> {
            IChaosBuff buff = ChaosRune.getBuff(stack);
            return buff != null ? buff.getId().toString() : "none";
        });

        // Soul Gems
        reg.registerSubtypeInterpreter(SoulGem.INSTANCE.get(), stack -> {
            Soul soul = SoulGem.getSoul(stack);
            return soul != null ? soul.getId().toString() : "none";
        });

        // Soul Urns
//        reg.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.soulUrn), stack -> {
//            int color = ModBlocks.soulUrn.getClayColor(stack);
//            return color != UrnConst.UNDYED_COLOR ? Integer.toString(color, 16) : "uncolored";
//        });
        initFailed = false;
    }

    private static void checkNotNull(@Nullable Object obj, String name) {
        if (obj == null) {
            throw new NullPointerException(name + " must not be null");
        }
    }

    private static void addInfoPage(IRecipeRegistration reg, IItemProvider item) {
        String key = getDescKey(Objects.requireNonNull(item.asItem().getRegistryName()));
        ItemStack stack = new ItemStack(item);
        reg.addIngredientInfo(stack, VanillaTypes.ITEM, key);
    }

    private static void addInfoPage(IRecipeRegistration reg, IItemProvider item, Stream<ItemStack> variants) {
        String key = getDescKey(Objects.requireNonNull(item.asItem().getRegistryName()));
        reg.addIngredientInfo(variants.collect(Collectors.toList()), VanillaTypes.ITEM, key);
    }

    private static String getDescKey(ResourceLocation name) {
        return "jei." + name.getNamespace() + "." + name.getPath() + ".desc";
    }
}
