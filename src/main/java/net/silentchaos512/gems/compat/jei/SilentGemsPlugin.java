package net.silentchaos512.gems.compat.jei;

import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;

//@JeiPlugin
public class SilentGemsPlugin /*implements IModPlugin*/ {
    private static final ResourceLocation PLUGIN_UID = SilentGems.getId("plugin/main");
    static final ResourceLocation SUPERCHARGER_PILLAR = SilentGems.getId("category/supercharger_pillar");
    static final ResourceLocation SUPERCHARGING = SilentGems.getId("category/supercharging");
    static final ResourceLocation TOKEN_ENCHANTING = SilentGems.getId("category/token_enchanting");
    static final ResourceLocation GUI_TEXTURE = SilentGems.getId("textures/gui/recipe_display.png");

    private static boolean initFailed = true;

    public static boolean hasInitFailed() {
        return initFailed;
    }

    /*@Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        initFailed = true;

        IGuiHelper guiHelper = reg.getJeiHelpers().getGuiHelper();
        reg.addRecipeCategories(
                new TokenEnchanterRecipeCategoryJei(guiHelper)
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
        initFailed = true;

        reg.addRecipes(TokenEnchanterRecipeManager.getValues(), TOKEN_ENCHANTING);

        if (SGearProxy.isLoaded()) {
            reg.addRecipes(ImmutableList.of(
                    new SuperchargerPillarStructure(1, ImmutableList.of(
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL1,
                            ModTags.Items.SUPERCHARGER_PILLAR_CAP
                    )),
                    new SuperchargerPillarStructure(2, ImmutableList.of(
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL2,
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL1,
                            ModTags.Items.SUPERCHARGER_PILLAR_CAP
                    )),
                    new SuperchargerPillarStructure(3, ImmutableList.of(
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL3,
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL3,
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL2,
                            ModTags.Items.SUPERCHARGER_PILLAR_LEVEL1,
                            ModTags.Items.SUPERCHARGER_PILLAR_CAP
                    ))
            ), SUPERCHARGER_PILLAR);
            reg.addRecipes(IntStream.rangeClosed(1, 3)
                    .mapToObj(SuperchargingRecipeCategoryJei.Recipe::new)
                    .collect(Collectors.toList()), SUPERCHARGING);
        }

        addInfoPage(reg, CraftingItems.ENDER_SLIMEBALL);
        addInfoPage(reg, LuminousFlowerPotBlock.INSTANCE.get());
        addInfoPage(reg, SoulGemItem.INSTANCE.get(), Soul.getValues().stream().map(Soul::getSoulGem));

        // Soul urn modify hints
//        reg.addRecipes(RecipeSoulUrnModify.getExampleRecipes(), VanillaRecipeCategoryUid.CRAFTING);

        initFailed = false;
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration reg) {
        reg.addRecipeCatalyst(new ItemStack(SuperchargerBlock.INSTANCE.get()), SUPERCHARGING, SUPERCHARGER_PILLAR);
        reg.addRecipeCatalyst(new ItemStack(TokenEnchanterBlock.INSTANCE.get()), TOKEN_ENCHANTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration reg) {
        reg.addRecipeClickArea(SuperchargerScreen.class, 79, 32, 24, 23, SUPERCHARGING, SUPERCHARGER_PILLAR);
        reg.addRecipeClickArea(TokenEnchanterScreen.class, 102, 32, 24, 23, TOKEN_ENCHANTING);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
        initFailed = true;
        // Enchantment tokens
        reg.registerSubtypeInterpreter(EnchantmentTokenItem.INSTANCE.get(), stack -> {
            Enchantment enchantment = EnchantmentTokenItem.getSingleEnchantment(stack);
            return enchantment != null ? enchantment.getName() : "none";
        });

        // Chaos Runes
        reg.registerSubtypeInterpreter(ChaosRuneItem.INSTANCE.get(), stack -> {
            IChaosBuff buff = ChaosRuneItem.getBuff(stack);
            return buff != null ? buff.getId().toString() : "none";
        });

        // Soul Gems
        reg.registerSubtypeInterpreter(SoulGemItem.INSTANCE.get(), stack -> {
            Soul soul = SoulGemItem.getSoul(stack);
            return soul != null ? soul.getId().toString() : "none";
        });

        // Soul Urns
//        reg.registerSubtypeInterpreter(Item.getItemFromBlock(ModBlocks.soulUrn), stack -> {
//            int color = ModBlocks.soulUrn.getClayColor(stack);
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

    private static String getDescKey(ResourceLocation name) {
        return "jei." + name.getNamespace() + "." + name.getPath() + ".desc";
    }*/
}
