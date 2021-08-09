/*
 * Silent's Gems -- GemOreBlock
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

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.silentchaos512.gems.util.Gems;

public class GemOreBlock extends OreBlockSG implements IGemBlock {
    private final Gems gem;
    private final String translationKey;

    public GemOreBlock(Gems gem, int harvestLevelIn, String translationKey, Properties properties) {
        super(gem::getItem, harvestLevelIn, properties.harvestLevel(harvestLevelIn));
        this.gem = gem;
        this.translationKey = translationKey;
    }

    @Override
    public Gems getGem() {
        return gem;
    }

    @Override
    public MutableComponent getGemBlockName() {
        return new TranslatableComponent("block.silentgems." + this.translationKey, this.gem.getDisplayName());
    }

    @Override
    public int getExpRandom() {
        return Mth.nextInt(RANDOM, 1, 5);
    }

    @Override
    public MutableComponent getName() {
        return getGemBlockName();
    }
}
