/*
 * Silent's Gems -- SoulUrnUpgradeBase
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

package net.silentchaos512.gems.lib.urn;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.urn.BlockSoulUrn;
import net.silentchaos512.gems.block.urn.TileSoulUrn;

public class SoulUrnUpgradeBase implements ISoulUrnUpgrade {
    @Override
    public void tickTile(TileSoulUrn urn, BlockSoulUrn.LidState lid, World world, BlockPos pos) {
    }

    @Override
    public void tickItem(ItemStack urn, World world, EntityPlayer player, int itemSlot, boolean isSelected) {
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return new NBTTagCompound();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbtTagCompound) {
    }
}
