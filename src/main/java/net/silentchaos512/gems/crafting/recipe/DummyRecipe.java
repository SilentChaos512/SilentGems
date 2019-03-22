/*
 * Silent's Gems -- DummyRecipe
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

package net.silentchaos512.gems.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;

/**
 * A recipe that does not nothing, just a placeholder for recipes that can't be loaded correctly.
 */
public class DummyRecipe implements IRecipe {
    private static final ResourceLocation NAME = new ResourceLocation(SilentGems.MOD_ID, "dummy_recipe");

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return NAME;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static final class Serializer implements IRecipeSerializer<DummyRecipe> {
        public static Serializer INSTANCE = new Serializer();

        private Serializer() { }

        @Override
        public DummyRecipe read(ResourceLocation recipeId, JsonObject json) {
            return new DummyRecipe();
        }

        @Override
        public DummyRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            return new DummyRecipe();
        }

        @Override
        public void write(PacketBuffer buffer, DummyRecipe recipe) { }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }
}
