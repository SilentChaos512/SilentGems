package net.silentchaos512.gems.compat.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gear.api.parts.PartType;
import net.silentchaos512.gear.gear.material.MaterialManager;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.altar.AltarScreen;
import net.silentchaos512.gems.block.supercharger.SuperchargerPillarStructure;
import net.silentchaos512.gems.block.supercharger.SuperchargerScreen;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterScreen;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.crafting.recipe.AltarTransmutationRecipe;
import net.silentchaos512.gems.crafting.recipe.TokenEnchanterRecipe;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.item.ChaosRuneItem;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.EnchantmentTokenItem;
import net.silentchaos512.gems.item.SoulGemItem;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.chaosbuff.IChaosBuff;
import net.silentchaos512.gems.lib.soul.Soul;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@JeiPlugin
public class SilentGemsPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = SilentGems.getId("plugin/main");
    static final ResourceLocation ALTAR_TRANSMUTATION = SilentGems.getId("category/altar_transmutation");
    static final ResourceLocation PURIFIER = SilentGems.getId("category/purifier");
    static final ResourceLocation SUPERCHARGER_PILLAR = SilentGems.getId("category/supercharger_pillar");
    static final ResourceLocation SUPERCHARGING = SilentGems.getId("category/supercharging");
    static final ResourceLocation TOKEN_ENCHANTING = SilentGems.getId("category/token_enchanting");
    static final ResourceLocation GUI_TEXTURE = SilentGems.getId("textures/gui/recipe_display.png");

    private static boolean initFailed = true;

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
        reg.addRecipeCategories(
                new PurifierRecipeCategoryJei(guiHelper),
                new TokenEnchanterRecipeCategoryJei(guiHelper),
                new TransmutationAltarRecipeCategoryJei(guiHelper)
        );
        if (SGearProxy.isLoaded()) {
            reg.addRecipeCategories(
                    new SuperchargerPillarCategory(guiHelper),
                    new SuperchargingRecipeCategoryJei(guiHelper)
            );
        }

        initFailed = false;
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        // TODO: Add block corruption hints?

        initFailed = true;

        reg.addRecipes(getRecipesOfType(TokenEnchanterRecipe.RECIPE_TYPE), TOKEN_ENCHANTING);
        reg.addRecipes(getRecipesOfType(AltarTransmutationRecipe.RECIPE_TYPE), ALTAR_TRANSMUTATION);
        reg.addRecipes(Collections.singletonList(new PurifierRecipeCategoryJei.Recipe(Ingredient.fromItems(GemsItems.PURIFYING_POWDER))), PURIFIER);

        if (SGearProxy.isLoaded()) {
            // Supercharger pillars
            reg.addRecipes(ImmutableList.of(
                    new SuperchargerPillarStructure(1, ImmutableList.of(
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL1,
                            GemsTags.Items.SUPERCHARGER_PILLAR_CAP
                    )),
                    new SuperchargerPillarStructure(2, ImmutableList.of(
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL2,
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL1,
                            GemsTags.Items.SUPERCHARGER_PILLAR_CAP
                    )),
                    new SuperchargerPillarStructure(3, ImmutableList.of(
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL3,
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL3,
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL2,
                            GemsTags.Items.SUPERCHARGER_PILLAR_LEVEL1,
                            GemsTags.Items.SUPERCHARGER_PILLAR_CAP
                    ))
            ), SUPERCHARGER_PILLAR);

            // Supercharging
            IntStream.rangeClosed(1, 3).forEach(tier -> reg.addRecipes(
                    MaterialManager.getValues().stream()
                            .filter(mat -> mat.isVisible(PartType.MAIN))
                            .map(mat -> new SuperchargingRecipeCategoryJei.Recipe(mat, tier))
                            .collect(Collectors.toList()),
                    SUPERCHARGING
            ));
        }

        addInfoPage(reg, CraftingItems.ENDER_SLIMEBALL);
        addInfoPage(reg, "glowrose", Arrays.stream(Gems.values()).map(gem -> new ItemStack(gem.getGlowrose())));
        addInfoPage(reg, GemsBlocks.LUMINOUS_FLOWER_POT);
        // FIXME: Fails on servers
//        addInfoPage(reg, SoulGemItem.INSTANCE.get(), Soul.getValues().stream().map(Soul::getSoulGem));

        // Soul urn modify hints
//        reg.addRecipes(RecipeSoulUrnModify.getExampleRecipes(), VanillaRecipeCategoryUid.CRAFTING);

        // Chaos rune hints
        reg.addRecipes(Arrays.stream(Gems.values()).map(gem ->
                new ShapedRecipe(
                        SilentGems.getId("rune_example_" + gem.getName()), "", 2, 1,
                        NonNullList.from(Ingredient.EMPTY,
                                Ingredient.fromItems(gem.getChaosGem()),
                                Ingredient.fromItems(GemsItems.CHAOS_RUNE)
                        ),
                        new ItemStack(gem.getChaosGem())
                )).collect(Collectors.toList()), VanillaRecipeCategoryUid.CRAFTING
        );

        initFailed = false;
    }

    private static List<IRecipe<?>> getRecipesOfType(IRecipeType<?> recipeType) {
        return Minecraft.getInstance().world.getRecipeManager().getRecipes().stream()
                .filter(r -> r.getType() == recipeType)
                .collect(Collectors.toList());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(GemsBlocks.SUPERCHARGER), SUPERCHARGING, SUPERCHARGER_PILLAR);
        reg.addRecipeCatalyst(new ItemStack(GemsBlocks.TOKEN_ENCHANTER), TOKEN_ENCHANTING);
        reg.addRecipeCatalyst(new ItemStack(GemsBlocks.TRANSMUTATION_ALTAR), ALTAR_TRANSMUTATION);
        reg.addRecipeCatalyst(new ItemStack(GemsBlocks.PURIFIER), PURIFIER);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(SuperchargerScreen.class, 79, 32, 24, 23, SUPERCHARGING, SUPERCHARGER_PILLAR);
        reg.addRecipeClickArea(TokenEnchanterScreen.class, 102, 32, 24, 23, TOKEN_ENCHANTING);
        reg.addRecipeClickArea(AltarScreen.class, 80, 32, 24, 23, ALTAR_TRANSMUTATION);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        initFailed = true;
        // Enchantment tokens
        reg.registerSubtypeInterpreter(GemsItems.ENCHANTMENT_TOKEN.get(), stack -> {
            Enchantment enchantment = EnchantmentTokenItem.getSingleEnchantment(stack);
            return enchantment != null ? enchantment.getName() : "none";
        });

        // Chaos Runes
        reg.registerSubtypeInterpreter(GemsItems.CHAOS_RUNE.get(), stack -> {
            IChaosBuff buff = ChaosRuneItem.getBuff(stack);
            return buff != null ? buff.getId().toString() : "";
        });

        // Soul Gems
        reg.registerSubtypeInterpreter(GemsItems.SOUL_GEM.get(), stack -> {
            Soul soul = SoulGemItem.getSoul(stack);
            return soul != null ? soul.getId().toString() : "";
        });

        // Soul Urns
//        reg.registerSubtypeInterpreter(Item.getItemFromBlock(GemsBlocks.soulUrn), stack -> {
//            int color = GemsBlocks.soulUrn.getClayColor(stack);
//            return color != UrnConst.UNDYED_COLOR ? Integer.toString(color, 16) : "uncolored";
//        });
        initFailed = false;
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

    private static void addInfoPage(IRecipeRegistration reg, String itemName, Stream<ItemStack> variants) {
        String key = getDescKey(SilentGems.getId(itemName));
        reg.addIngredientInfo(variants.collect(Collectors.toList()), VanillaTypes.ITEM, key);
    }

    private static String getDescKey(ResourceLocation name) {
        return "jei." + name.getNamespace() + "." + name.getPath() + ".desc";
    }
}
