package net.silentchaos512.gems.data.recipe;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.crafting.ingredient.SoulGemIngredient;
import net.silentchaos512.gems.init.GemsItems;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.SoulUrnUpgrades;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.soul.Soul;
import net.silentchaos512.lib.util.NameUtils;

import java.util.function.Consumer;

public class GemsRecipeProvider extends RecipeProvider {
    public GemsRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public String getName() {
        return "Silent's Gems - Recipes";
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerTokenEnchanting(consumer);
    }

    private void registerTokenEnchanting(Consumer<IFinishedRecipe> consumer) {
        for (Gems gem : Gems.values()) {
            TokenEnchantingRecipeBuilder.builder(gem.getChaosGem(), 1, 3333, 300)
                    .token(gem.getBlockItemTag())
                    .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 10)
                    .build(consumer, idTokenEnchanting("chaos_gem/" + gem.getName()));
        }

        for (SpawnEggItem spawnEggItem : SpawnEggItem.getEggs()) {
            EntityType<?> type = spawnEggItem.getType(new CompoundNBT());
            if (Soul.canHaveSoulGem(type)) {
                ResourceLocation typeId = NameUtils.from(type);
                TokenEnchantingRecipeBuilder.builder(spawnEggItem, 1, 1000, 200)
                        .token(Items.EGG)
                        .addIngredient(SoulGemIngredient.of(typeId), 4)
                        .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 4)
                        .build(consumer, idTokenEnchanting("spawn_egg/" + typeId.getPath()));
            }
        }

        TokenEnchantingRecipeBuilder.builder(CraftingItems.URN_UPGRADE_BASE, 4, 100, 20)
                .token(Blocks.TERRACOTTA)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 1)
                .build(consumer, idTokenEnchanting("urn/upgrade_base"));
        TokenEnchantingRecipeBuilder.builder(SoulUrnUpgrades.PLANTER, 1, 500, 100)
                .token(CraftingItems.URN_UPGRADE_BASE)
                .addIngredient(Blocks.DIRT, 10)
                .addIngredient(Items.BONE_MEAL, 30)
                .addIngredient(GemsTags.Items.GLOWROSES, 2)
                .build(consumer, idTokenEnchanting("urn/planter_upgrade"));
        TokenEnchantingRecipeBuilder.builder(SoulUrnUpgrades.VACUUM, 1, 500, 100)
                .token(CraftingItems.URN_UPGRADE_BASE)
                .addIngredient(Items.ENDER_EYE, 2)
                .addIngredient(Items.HOPPER, 1)
                .build(consumer, idTokenEnchanting("urn/vacuum_upgrade"));

        TokenEnchantingRecipeBuilder.builder(CraftingItems.CHAOS_COAL, 8, 250, 20)
                .token(GemsTags.Items.GEMS_CHAOS)
                .addIngredient(Items.COAL, 8)
                .build(consumer, idTokenEnchanting("chaos_coal"));
        TokenEnchantingRecipeBuilder.builder(CraftingItems.CHAOS_COAL, 6, 250, 20)
                .token(GemsTags.Items.GEMS_CHAOS)
                .addIngredient(Items.CHARCOAL, 8)
                .build(consumer, idTokenEnchanting("chaos_coal2"));
        TokenEnchantingRecipeBuilder.builder(GemsItems.CORRUPTING_POWDER, 4, 50, 20)
                .token(CraftingItems.CHAOS_DUST)
                .addIngredient(GemsTags.Items.CORRUPTED_DUSTS, 2)
                .addIngredient(Tags.Items.DUSTS_REDSTONE, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.ENDER_FROST, 1, 150, 20)
                .token(Blocks.BLUE_ICE)
                .addIngredient(CraftingItems.ENDER_CRYSTAL, 4)
                .addIngredient(Tags.Items.GEMS_LAPIS, 8)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.ENDER_SLIME_CRYSTAL, 1, 250, 100)
                .token(Tags.Items.GEMS_DIAMOND)
                .addIngredient(CraftingItems.ENDER_SLIMEBALL, 6)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 1, 150, 20)
                .token(Tags.Items.GEMS_QUARTZ)
                .addIngredient(GemsTags.Items.GEMS_CHAOS, 4)
                .addIngredient(Tags.Items.DUSTS_GLOWSTONE, 4)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.MAGMA_CREAM_CRYSTAL, 1, 250, 100)
                .token(Tags.Items.GEMS_DIAMOND)
                .addIngredient(Items.MAGMA_CREAM, 6)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(GemsItems.PURIFYING_POWDER, 4, 50, 20)
                .token(CraftingItems.CHAOS_DUST)
                .addIngredient(GemsTags.Items.CORRUPTED_DUSTS, 2)
                .addIngredient(Tags.Items.DUSTS_GLOWSTONE, 2)
                .build(consumer);
        TokenEnchantingRecipeBuilder.builder(CraftingItems.SLIME_CRYSTAL, 1, 250, 100)
                .token(Tags.Items.GEMS_DIAMOND)
                .addIngredient(Items.SLIME_BALL, 6)
                .addIngredient(CraftingItems.ENRICHED_CHAOS_CRYSTAL, 2)
                .build(consumer);
    }

    private static ResourceLocation idTokenEnchanting(String path) {
        return SilentGems.getId("token_enchanting/" + path);
    }
}
