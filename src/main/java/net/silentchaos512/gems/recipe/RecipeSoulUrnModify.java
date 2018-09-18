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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.urn.IUrnUpgradeItem;
import net.silentchaos512.gems.lib.urn.UrnConst;
import net.silentchaos512.gems.lib.urn.UrnHelper;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.lib.recipe.RecipeBaseSL;
import net.silentchaos512.lib.registry.RecipeMaker;
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
        Collection<ItemStack> dyes = list.allMatches(DyeUtils::isDye);

        // For upgrade items, make sure the urn doesn't have it already
        for (ItemStack mod : mods) {
            if (mod.getItem() instanceof IUrnUpgradeItem) {
                IUrnUpgradeItem upgradeItem = (IUrnUpgradeItem) mod.getItem();

                List<UrnUpgrade> currentUpgrades = UrnUpgrade.ListHelper.load(urn);
                if (UrnUpgrade.ListHelper.contains(currentUpgrades, upgradeItem.getSerializer())) {
                    return false;
                }
            }
        }

        return !urn.isEmpty() && (list.size() == 1 || !mods.isEmpty() || !dyes.isEmpty());
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        StackList list = StackHelper.getNonEmptyStacks(inv);
        ItemStack urn = list.uniqueMatch(RecipeSoulUrnModify::isSoulUrn).copy();
        Collection<ItemStack> mods = list.allMatches(RecipeSoulUrnModify::isModifierItem);
        Collection<ItemStack> dyes = list.allMatches(DyeUtils::isDye);

        if (mods.isEmpty() && dyes.isEmpty()) {
            boolean lidless = UrnHelper.isLidless(urn);
            UrnHelper.setLidless(urn, !lidless);
        } else {
            mods.forEach(mod -> applyModifierItem(urn, mod));
            applyDyes(urn, dyes);
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
        return stack.getItem() == ModItems.gem || stack.getItem() instanceof IUrnUpgradeItem;
    }

    private static void applyModifierItem(ItemStack urn, ItemStack mod) {
        SilentGems.logHelper.debug("RecipeSoulUrnModify#applyModifierItem: {}, {}", urn, mod);

        if (mod.getItem() == ModItems.gem) {
            EnumGem gem = EnumGem.getFromStack(mod);
            UrnHelper.setGem(urn, gem);
        } else if (mod.getItem() instanceof IUrnUpgradeItem) {
            IUrnUpgradeItem upgradeItem = (IUrnUpgradeItem) mod.getItem();
            NBTTagCompound urnSubcompound = urn.getOrCreateSubCompound(UrnConst.NBT_ROOT);

            List<UrnUpgrade> list = UrnUpgrade.ListHelper.load(urnSubcompound);
            list.add(upgradeItem.getSerializer().deserialize(new NBTTagCompound()));
            UrnUpgrade.ListHelper.save(list, urnSubcompound);
        }
    }

    // Largely copied from RecipesArmorDyes
    private static void applyDyes(ItemStack urn, Collection<ItemStack> dyes) {
        int[] componentSums = new int[3];
        int maxColorSum = 0;
        int colorCount = 0;

        int clayColor = UrnHelper.getClayColor(urn);
        if (clayColor != UrnConst.UNDYED_COLOR) {
            float r = (float) (clayColor >> 16 & 255) / 255.0F;
            float g = (float) (clayColor >> 8 & 255) / 255.0F;
            float b = (float) (clayColor & 255) / 255.0F;
            maxColorSum = (int) ((float) maxColorSum + Math.max(r, Math.max(g, b)) * 255.0F);
            componentSums[0] = (int) ((float) componentSums[0] + r * 255.0F);
            componentSums[1] = (int) ((float) componentSums[1] + g * 255.0F);
            componentSums[2] = (int) ((float) componentSums[2] + b * 255.0F);
            ++colorCount;
        }

        for (ItemStack dye : dyes) {
            float[] componentValues = DyeUtils.colorFromStack(dye).orElse(EnumDyeColor.WHITE).getColorComponentValues();
            int r = (int) (componentValues[0] * 255.0F);
            int g = (int) (componentValues[1] * 255.0F);
            int b = (int) (componentValues[2] * 255.0F);
            maxColorSum += Math.max(r, Math.max(g, b));
            componentSums[0] += r;
            componentSums[1] += g;
            componentSums[2] += b;
            ++colorCount;
        }

        if (colorCount > 0) {
            int r = componentSums[0] / colorCount;
            int g = componentSums[1] / colorCount;
            int b = componentSums[2] / colorCount;
            float maxAverage = (float) maxColorSum / (float) colorCount;
            float max = (float) Math.max(r, Math.max(g, b));
            r = (int) ((float) r * maxAverage / max);
            g = (int) ((float) g * maxAverage / max);
            b = (int) ((float) b * maxAverage / max);
            int finalColor = (r << 8) + g;
            finalColor = (finalColor << 8) + b;
            UrnHelper.setClayColor(urn, finalColor);
        }
    }

    // Examples for JEI (see SilentGemsPlugin in compat.jei package)
    public static Collection<IRecipe> getExampleRecipes() {
        List<IRecipe> result = new ArrayList<>();
        RecipeMaker recipes = SilentGems.registry.getRecipeMaker();
        ItemStack urnBasic = ModBlocks.soulUrn.getStack(UrnConst.UNDYED_COLOR, null);

        for (EnumDyeColor color : EnumDyeColor.values()) {
            result.add(recipes.makeShapeless(
                    ModBlocks.soulUrn.getStack(color.getColorValue(), null),
                    urnBasic,
                    new ItemStack(Items.DYE, 1, color.getDyeDamage())));
        }

        for (EnumGem gem : EnumGem.values()) {
            result.add(recipes.makeShapeless(
                    ModBlocks.soulUrn.getStack(UrnConst.UNDYED_COLOR, gem),
                    urnBasic,
                    gem.getItem()));
        }

        return result;
    }
}
