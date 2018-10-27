/*
 * Silent's Gems -- BlockGemBrick
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

import net.minecraft.block.SoundType;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;

public class BlockGemBrick extends BlockGemSubtypes {
    private final boolean coated;
    private final String blockName;

    public BlockGemBrick(EnumGem.Set set, boolean coated) {
        super(set);
        this.coated = coated;
        this.blockName = Names.GEM_BRICK + (coated ? "coated" : "speckled");

        setHardness(2.0f);
        setResistance(30.0f);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    String getBlockName() {
        return nameForSet(this.getGemSet(), blockName);
    }
}
