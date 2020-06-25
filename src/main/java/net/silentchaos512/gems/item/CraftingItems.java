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

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.lib.registry.ItemRegistryObject;

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
    SILVER_INGOT,
    SILVER_NUGGET,
    CHAOS_IRON_UNFIRED,
    CHAOS_IRON,
    CHARGING_AGENT,
    SUPER_CHARGING_AGENT,
    ULTRA_CHARGING_AGENT,
    CHAOS_COAL,
    ENDER_SLIMEBALL,
    CORRUPTED_SLIMEBALL,
    SLIME_CRYSTAL,
    MAGMA_CREAM_CRYSTAL,
    ENDER_SLIME_CRYSTAL,
    CHAOS_WISP_ESSENCE,
    FIRE_WISP_ESSENCE,
    WATER_WISP_ESSENCE,
    ICE_WISP_ESSENCE,
    LIGHTNING_WISP_ESSENCE,
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

    @SuppressWarnings("NonFinalFieldInEnum")
    private ItemRegistryObject<ItemCrafting> item;
    private final boolean shownInGroup;

    CraftingItems() {
        this(true);
    }

    CraftingItems(boolean shownInGroup) {
        this.shownInGroup = shownInGroup;
    }

    public static void registerItems() {
        for (CraftingItems item : values()) {
            item.item = new ItemRegistryObject<>(Registration.ITEMS.register(item.getName(), () -> new ItemCrafting(item)));
        }
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

    static final class ItemCrafting extends Item {
        CraftingItems type;

        private ItemCrafting(CraftingItems type) {
            super(new Item.Properties().group(GemsItemGroups.MATERIALS));
            this.type = type;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TranslationTextComponent("item.silentgems.crafting_material.desc")
                    .applyTextStyle(TextFormatting.GOLD));

            String descKey = this.getTranslationKey() + ".desc";
            if (I18n.hasKey(descKey)) {
                tooltip.add(new TranslationTextComponent(descKey).applyTextStyle(TextFormatting.ITALIC));
            }
        }

        @Override
        public int getBurnTime(ItemStack itemStack) {
            if (itemStack.getItem() == CHAOS_COAL.asItem()) {
                return GemsConfig.COMMON.chaosCoalBurnTime.get();
            }
            return 0;
        }

        @Override
        public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
            if (!type.shownInGroup || !isInGroup(group)) return;
            super.fillItemGroup(group, items);
        }
    }
}
