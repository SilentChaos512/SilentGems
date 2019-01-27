/*
 * Silent's Gems -- GemGlass
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

package net.silentchaos512.gems.block;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class GemGlass extends BlockGlass {
    private final Gems gem;

    public GemGlass(Gems gem) {
        super(Builder.create(Material.GLASS)
                .hardnessAndResistance(0.3f, 5)
                .sound(SoundType.GLASS));
        this.gem = gem;
    }

    @Override
    public int quantityDropped(IBlockState state, Random random) {
        // Don't destroy
        return 1;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(gem.getSet().getDisplayName());
    }
}
