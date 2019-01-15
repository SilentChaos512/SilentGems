/*
 * Silent's Gems -- EvilCraftCompat
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

package net.silentchaos512.gems.compat.evilcraft;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import org.cyclops.evilcraft.EvilCraft;
import org.cyclops.evilcraft.api.tileentity.bloodchest.IBloodChestRepairActionRegistry;
import org.cyclops.evilcraft.tileentity.tickaction.bloodchest.DamageableItemRepairAction;

import java.util.Random;

public class EvilCraftCompat {
    public static void init() {
        SilentGems.logHelper.info("Loading EvilCraft compatibility");

        IBloodChestRepairActionRegistry reg = EvilCraft._instance.getRegistryManager().getRegistry(IBloodChestRepairActionRegistry.class);
        reg.register(new DamageableItemRepairAction() {
            @Override
            public boolean isItemValidForSlot(ItemStack itemStack) {
                return itemStack.getItem() instanceof ITool || itemStack.getItem() instanceof IArmor;
            }

            @Override
            public boolean canRepair(ItemStack itemStack, int tick) {
                return itemStack.isItemDamaged() &&
                        (itemStack.getItem() instanceof ITool || itemStack.getItem() instanceof IArmor);
            }

            @Override
            public float repair(ItemStack itemStack, Random random, boolean doAction, boolean isBulk) {
                boolean wasBroken = ToolHelper.isBroken(itemStack);
                float result = super.repair(itemStack, random, doAction, isBulk);
                boolean isBroken = ToolHelper.isBroken(itemStack);

                // If the item was broken before but isn't now, we need to restore its state
                if (wasBroken != isBroken) ToolHelper.recalculateStats(itemStack);
                return result;
            }
        });
    }
}
