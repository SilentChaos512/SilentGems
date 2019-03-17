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
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.utils.Lazy;

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
    NETHER_STAR_SHARD,
    CHAOS_IRON_UNFIRED,
    CHAOS_IRON,
    CHARGING_AGENT,
    SUPER_CHARGING_AGENT,
    ULTRA_CHARGING_AGENT,
    CHAOS_COAL,
    ENDER_SLIMEBALL,
    FLUFFY_PUFF,
    ORNATE_GOLD_ROD,
    ORNATE_SILVER_ROD,
    GILDED_STRING,
    GILDED_BOWSTRING,
    BLANK_TOKEN,
    RUNE_SLATE,
    SOUL_SHELL,
    MYSTERY_GOO,
    IRON_POTATO,
    FLUFFY_FABRIC,
    URN_UPGRADE_BASE,
    LOLINOMICON(false);

    private final Lazy<ItemCrafting> item;
    private final boolean shownInGroup;

    CraftingItems() {
        this(true);
    }

    CraftingItems(boolean shownInGroup) {
        this.item = Lazy.of(ItemCrafting::new);
        this.shownInGroup = shownInGroup;
    }

    @Override
    public Item asItem() {
        return item.get();
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

    final class ItemCrafting extends Item {
        private ItemCrafting() {
            super(new Item.Properties().group(ModItemGroups.MATERIALS));
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TextComponentTranslation("item.silentgems.crafting_material.desc")
                    .applyTextStyle(TextFormatting.GOLD));

            ResourceLocation registryName = getRegistryName();
            if (registryName != null) {
                String key = "item." + registryName.getNamespace() + "." + registryName.getPath() + ".desc";
                tooltip.add(new TextComponentTranslation(key).applyTextStyle(TextFormatting.ITALIC));
            }
        }

        @Override
        public int getBurnTime(ItemStack itemStack) {
            return itemStack.getItem() == CHAOS_COAL.asItem() ? 6400 : 0; // TODO: config
        }

        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!shownInGroup || !isInGroup(group)) return;
            super.fillItemGroup(group, items);
        }
    }
}
