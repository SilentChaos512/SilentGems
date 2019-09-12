/*
 * Silent's Gems -- GemBlock
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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.List;

public class GemBlock extends Block implements IGemBlock {
    private final Gems gem;

    public GemBlock(Gems gem) {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(3, 30)
                .sound(SoundType.METAL));
        this.gem = gem;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    public ITextComponent getGemBlockName() {
        return new TranslationTextComponent("block.silentgems.gem_block", this.gem.getDisplayName());
    }

    @Override
    public boolean isBeaconBase(BlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(gem.getSet().getDisplayName());
    }

    @Override
    public ITextComponent getNameTextComponent() {
        return getGemBlockName();
    }
}
