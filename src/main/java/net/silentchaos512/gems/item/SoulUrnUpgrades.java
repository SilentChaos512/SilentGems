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

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.urn.ISoulUrnUpgrade;
import net.silentchaos512.gems.lib.urn.SoulUrnUpgradeBase;
import net.silentchaos512.gems.lib.urn.UpgradeVacuum;
import net.silentchaos512.lib.item.IEnumItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public enum SoulUrnUpgrades implements IEnumItems<SoulUrnUpgrades, SoulUrnUpgrades.UpgradeItem> {
    EXTENDED_STORAGE_BASIC("silentgems:basic_storage", 2, SoulUrnUpgradeBase::new),
    EXTENDED_STORAGE_ADVANCED("silentgems:advanced_storage", 2, SoulUrnUpgradeBase::new),
    VACUUM("silentgems:vacuum", 1, UpgradeVacuum::new);

    private final String upgradeId;
    private final UpgradeItem item;
    private final int maxCopies;
    private final Supplier<? extends ISoulUrnUpgrade> upgradeFactory;

    SoulUrnUpgrades(String id, int maxCopies, Supplier<? extends ISoulUrnUpgrade> upgradeFactory) {
        this.upgradeId = id;
        this.item = new UpgradeItem();
        this.maxCopies = maxCopies;
        this.upgradeFactory = upgradeFactory;
    }

    @Nullable
    public static SoulUrnUpgrades byId(String id) {
        for (SoulUrnUpgrades upgrade : values())
            if (upgrade.upgradeId.equals(id))
                return upgrade;
        return null;
    }

    public ISoulUrnUpgrade createUpgradeObject() {
        return this.upgradeFactory.get();
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

    public static class UpgradeItem extends Item {
        @Override
        public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
            // TODO
        }
    }
}
