/*
 * Silent's Gems -- RecipeSoulUrnModify
 * Copyright (C) 2018 SilentChaos512
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 3
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.silentchaos512.gems.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.DyeHelper;
import net.silentchaos512.lib.util.StackHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecipeSoulUrnModify extends RecipeBaseSL {
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        StackList list = StackHelper.getNonEmptyStacks(inv);
        ItemStack urn = list.uniqueMatch(RecipeSoulUrnModify::isSoulUrn);
        Collection<ItemStack> mods = list.allMatches(RecipeSoulUrnModify::isModifierItem);
        return !urn.isEmpty() && (list.size() == 1 || !mods.isEmpty());
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        StackList list = StackHelper.getNonEmptyStacks(inv);
        ItemStack urn = list.uniqueMatch(RecipeSoulUrnModify::isSoulUrn).copy();
        Collection<ItemStack> mods = list.allMatches(RecipeSoulUrnModify::isModifierItem);

        if (mods.isEmpty()) {
            boolean lidless = ModBlocks.soulUrn.isStackLidless(urn);
            ModBlocks.soulUrn.setStackLidless(urn, !lidless);
        } else {
            mods.forEach(mod -> applyModifierItem(urn, mod));
        }

        return urn;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModBlocks.soulUrn);
    }

    private static boolean isSoulUrn(ItemStack stack) {
        return stack.getItem() == Item.getItemFromBlock(ModBlocks.soulUrn);
    }

    private static boolean isModifierItem(ItemStack stack) {
        return DyeHelper.isItemDye(stack) || stack.getItem() == ModItems.gem;
    }

    private static void applyModifierItem(ItemStack urn, ItemStack mod) {
        SilentGems.logHelper.debug("{}, {}", urn, mod);
        if (mod.getItem() == ModItems.gem) {
            EnumGem gem = EnumGem.getFromStack(mod);
            ModBlocks.soulUrn.setGem(urn, gem);
        } else if (DyeHelper.isItemDye(mod)) {
            EnumDyeColor color = EnumDyeColor.byMetadata(DyeHelper.oreDictDyeToVanilla(mod).getItemDamage());
            ModBlocks.soulUrn.setClayColor(urn, color);
        }
    }

    // Examples for JEI (see SilentGemsPlugin in compat.jei package)
    public static Collection<IRecipe> getExampleRecipes() {
        List<IRecipe> result = new ArrayList<>();
        RecipeMaker recipes = SilentGems.registry.getRecipeMaker();
        ItemStack urnBasic = ModBlocks.soulUrn.getStack(null, null);

        for (EnumDyeColor color : EnumDyeColor.values()) {
            result.add(recipes.makeShapeless(ModBlocks.soulUrn.getStack(color, null),
                    urnBasic, new ItemStack(Items.DYE, 1, color.getDyeDamage())));
        }

        for (EnumGem gem : EnumGem.values()) {
            result.add(recipes.makeShapeless(ModBlocks.soulUrn.getStack(null, gem),
                    urnBasic, gem.getItem()));
        }

        return result;
    }
}
