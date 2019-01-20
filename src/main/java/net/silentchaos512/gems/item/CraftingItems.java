/*
 * Silent's Gems -- CraftingItems
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

package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItemGroups;

import javax.annotation.Nullable;
import java.util.List;

public enum CraftingItems {
    CHAOS_CRYSTAL,
    ENRICHED_CHAOS_CRYSTAL,
    CHAOS_CRYSTAL_SHARD,
    ENDER_CRYSTAL,
    ENDER_FROST,
    ENDER_CRYSTAL_SHARD,
    CHAOS_IRON_UNFIRED,
    CHAOS_IRON,
    ENDER_SLIMEBALL,
    SOUL_SHELL,
    CHAOS_COAL,
    ORNATE_GOLD_ROD,
    ORNATE_SILVER_ROD,
    GILDED_STRING,
    MYSTERY_GOO,
    IRON_POTATO,
    FLUFFY_FABRIC,
    YARN_BALL,
    RAWHIDE_BONE,
    URN_UPGRADE_BASE();

    private final ItemCrafting item;

    CraftingItems() {
        this.item = new ItemCrafting();
    }

    public Item getItem() {
        return item;
    }

    public ItemStack getStack() {
        return this.getStack(1);
    }

    public ItemStack getStack(int count) {
        return new ItemStack(item, count);
    }

    public static final class ItemCrafting extends Item {
        private ItemCrafting() {
            super(new Item.Builder().group(ModItemGroups.MATERIALS));
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TextComponentTranslation("item.silentgems.crafting_material.desc")
                    .applyTextStyle(TextFormatting.GOLD));

            ResourceLocation registryName = getRegistryName();
            if (registryName != null) {
                tooltip.add(new TextComponentTranslation("item." + registryName + ".desc")
                        .applyTextStyle(TextFormatting.ITALIC));
            }
        }

        @Override
        public int getBurnTime(ItemStack itemStack) {
            return itemStack.getItem() == CHAOS_COAL.item ? 6400 : 0; // TODO: config
        }
    }
}
