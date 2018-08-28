/*
 * Silent's Gems -- TileSupercharger
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

package net.silentchaos512.gems.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileSidedInventorySL;
import net.silentchaos512.lib.util.TimeHelper;

public class TileSupercharger extends TileSidedInventorySL implements ITickable, IChaosAccepter {
    private static final int MAX_CHAOS_STORED = 1000000;
    private static final int STRUCTURE_CHECK_FREQUENCY = TimeHelper.ticksFromSeconds(15);

    @SyncVariable(name = "Energy")
    private int chaosStored;
    @SyncVariable(name = "Progress")
    private int progress;
    @SyncVariable(name = "StructureLevel")
    private int structureLevel;
    private int ticksExisted = 0;

    @Override
    public void update() {
        if (this.world.isRemote) return;

        if (++ticksExisted % STRUCTURE_CHECK_FREQUENCY == 0) {
            checkStructureLevel();
        }

        // TODO
    }

    private void checkStructureLevel() {
        // TODO
        BlockPos pos1 = this.pos.offset(EnumFacing.NORTH, 3).offset(EnumFacing.WEST, 3);
    }

    private int getPillarLevel(BlockPos pos) {
        // TODO
        return 0;
    }

    private int getChaosCostForCharging(ItemStack gearPart, ItemStack chargingAgent) {
        // TODO Include charging agent or structure level?
        int tier = SilentGems.proxy.getSGearPartTier(gearPart);
        if (tier < 0) return 0;
        return 100 * (int) Math.pow(10, tier + 1);
    }

    @Override
    public int receiveCharge(int maxReceive, boolean simulate) {
        int received = Math.min(getMaxCharge() - getCharge(), maxReceive);
        if (!simulate) {
            chaosStored += received;
            if (received != 0) {
                sendUpdate();
            }
        }
        return received;
    }

    @Override
    public int getCharge() {
        return chaosStored;
    }

    @Override
    public int getMaxCharge() {
        return MAX_CHAOS_STORED;
    }

    @Override
    public int getSizeInventory() {
        return 2;
    }
}
