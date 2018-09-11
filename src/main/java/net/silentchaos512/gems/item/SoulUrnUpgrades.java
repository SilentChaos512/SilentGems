/*
 * Silent's Gems -- SoulUrnUpgrades
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

import lombok.Getter;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.urn.IUrnUpgradeItem;
import net.silentchaos512.gems.lib.urn.UpgradeVacuum;
import net.silentchaos512.gems.lib.urn.UrnUpgrade;
import net.silentchaos512.lib.item.IEnumItems;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public enum SoulUrnUpgrades implements IEnumItems<SoulUrnUpgrades, SoulUrnUpgrades.UpgradeItem> {
    STORAGE_BASIC(basicSerializer("basic_storage", UrnUpgrade::new)),
    STORAGE_ADVANCED(basicSerializer("advanced_storage", UrnUpgrade::new)),
    VACUUM(basicSerializer("vacuum", UpgradeVacuum::new));

    private final UpgradeItem item;
    @Getter
    private final UrnUpgrade.Serializer<? extends UrnUpgrade> serializer;

    SoulUrnUpgrades(UrnUpgrade.Serializer<? extends UrnUpgrade> serializer) {
        this.item = new UpgradeItem();
        this.serializer = serializer;
    }

    @Nonnull
    @Override
    public SoulUrnUpgrades getEnum() {
        return this;
    }

    @Nonnull
    @Override
    public UpgradeItem getItem() {
        return this.item;
    }

    @Nonnull
    @Override
    public String getNamePrefix() {
        return "urn_upgrade";
    }

    private static UrnUpgrade.Serializer<UrnUpgrade> basicSerializer(String name, Supplier<UrnUpgrade> constructor) {
        return new UrnUpgrade.Serializer<>(new ResourceLocation(SilentGems.MODID, name), constructor);
    }

    public class UpgradeItem extends Item implements IUrnUpgradeItem, IAddRecipes {
        @Override
        public UrnUpgrade.Serializer<? extends UrnUpgrade> getSerializer() {
            return serializer;
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            // TODO
        }

        @Override
        public void addRecipes(RecipeMaker recipes) {
            switch (getEnum()) {
                case STORAGE_BASIC:
                    recipes.addShapedOre(getName(), getStack(),
                            "bcb", " u ", "bcb",
                            'u', CraftingItems.URN_UPGRADE_BASE.getStack(),
                            'b', CraftingItems.BLAZESTONE.getStack(),
                            'c', "chestWood");
                    break;
                case STORAGE_ADVANCED:
                    recipes.addShaped(getName(), getStack(),
                            " s ", "eue", " s ",
                            'u', CraftingItems.URN_UPGRADE_BASE.getStack(),
                            's', Items.SHULKER_SHELL,
                            'e', CraftingItems.ENDER_ESSENCE.getStack());
                    break;
                case VACUUM:
                    recipes.addShaped(getName(), getStack(),
                            "cec", " u ",
                            'u', CraftingItems.URN_UPGRADE_BASE.getStack(),
                            'c', CraftingItems.ENRICHED_CHAOS_ESSENCE.getStack(),
                            'e', Items.ENDER_PEARL);
                    break;
            }
        }
    }
}
