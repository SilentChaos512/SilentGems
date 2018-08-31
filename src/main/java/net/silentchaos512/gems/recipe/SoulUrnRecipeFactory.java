/*
 * Silent's Gems -- SoulUrnRecipeFactory
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

import com.google.gson.JsonObject;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.item.ItemGem;
import net.silentchaos512.gems.item.ItemSoulGem;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.collection.StackList;
import net.silentchaos512.lib.util.StackHelper;

import javax.annotation.Nonnull;

public class SoulUrnRecipeFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext context, JsonObject json) {
        ShapedOreRecipe recipe = ShapedOreRecipe.factory(context, json);

        CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
        primer.width = recipe.getRecipeWidth();
        primer.height = recipe.getRecipeHeight();
        primer.mirrored = JsonUtils.getBoolean(json, "mirrored", true);
        primer.input = recipe.getIngredients();

        return new ShapedOreRecipe(new ResourceLocation(SilentGems.MODID, "soul_urn"), new ItemStack(ModBlocks.soulUrn), primer) {
            @Nonnull
            @Override
            public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
                StackList list = StackHelper.getNonEmptyStacks(var1);
                ItemStack clay = list.firstMatch(stack ->
                        !(stack.getItem() instanceof ItemGem || stack.getItem() instanceof ItemSoulGem));
                ItemStack gemStack = list.firstOfType(ItemGem.class);
//                ItemStack soulGem = list.firstOfType(ItemSoulGem.class);

                EnumGem gem = EnumGem.getFromStack(gemStack);
                EnumDyeColor color = null;
                if (clay.getItem() == Item.getItemFromBlock(Blocks.STAINED_HARDENED_CLAY)) {
                    color = EnumDyeColor.byMetadata(clay.getItemDamage());
                }

                return ModBlocks.soulUrn.getStack(color, gem);
            }
        };
    }
}
