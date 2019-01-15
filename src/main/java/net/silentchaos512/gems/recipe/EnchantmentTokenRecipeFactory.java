/*
 * Silent's Gems -- EnchantmentTokenRecipeFactory
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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;

public class EnchantmentTokenRecipeFactory implements IRecipeFactory {
    @Override
    public IRecipe parse(JsonContext jsonContext, JsonObject jsonObject) {
        ShapedOreRecipe recipe = ShapedOreRecipe.factory(jsonContext, jsonObject);

        CraftingHelper.ShapedPrimer primer = new CraftingHelper.ShapedPrimer();
        primer.width = recipe.getRecipeWidth();
        primer.height = recipe.getRecipeHeight();
        primer.mirrored = JsonUtils.getBoolean(jsonObject, "mirrored", true);
        primer.input = recipe.getIngredients();

        JsonObject resultObj = jsonObject.getAsJsonObject("result");
        if (resultObj == null || !resultObj.has("enchantment"))
            throw new NullPointerException("Missing enchantment in token recipe!");

        String key = resultObj.get("enchantment").getAsString();
        Enchantment enchantment = Enchantment.REGISTRY.getObject(new ResourceLocation(key));
        if (enchantment == null) {
            // Enchantment is missing -- typo or registry tamper?
            // Just log a warning normally, let it crash in dev
            if (SilentGems.instance.isDevBuild()) {
                throw new NullPointerException("Enchantment '" + key + "' not found!");
            } else {
//                SilentGems.logHelper.warn("Failed to load enchantment token recipe: enchantment '{}' not found!", key);
                return new DummyRecipe();
            }
        }

        ItemStack result = ModItems.enchantmentToken.constructToken(enchantment);
        result.setCount(recipe.getRecipeOutput().getCount());

        return new ShapedOreRecipe(new ResourceLocation(SilentGems.MOD_ID, "enchantment_token"), result, primer);
    }
}
