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
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItemGroups;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public enum CraftingItems implements IItemProvider, IStringSerializable {
    CHAOS_CRYSTAL,
    ENRICHED_CHAOS_CRYSTAL,
    CHAOS_CRYSTAL_SHARD,
    CHAOS_DUST,
    ENDER_CRYSTAL,
    ENDER_CRYSTAL_SHARD,
    ENDER_FROST,
    CHAOS_IRON_UNFIRED,
    CHAOS_IRON,
    CHAOS_COAL,
    ENDER_SLIMEBALL,
    SOUL_SHELL,
    ORNATE_GOLD_ROD,
    ORNATE_SILVER_ROD,
    GILDED_STRING,
    MYSTERY_GOO,
    IRON_POTATO,
    FLUFFY_FABRIC,
    URN_UPGRADE_BASE;

    private final LazyLoadBase<ItemCrafting> item;

    CraftingItems() {
        this.item = new LazyLoadBase<>(ItemCrafting::new);
    }

    @Override
    public Item asItem() {
        return item.getValue();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public ItemStack getStack() {
        return this.getStack(1);
    }

    public ItemStack getStack(int count) {
        return new ItemStack(this, count);
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
            return itemStack.getItem() == CHAOS_COAL.asItem() ? 6400 : 0; // TODO: config
        }
    }
}
