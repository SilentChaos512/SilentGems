/*
 * Silent's Gems -- GemLampBlock
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
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class GemLampBlock extends Block {
    public enum State {
        UNLIT(false, false, ""),
        LIT(true, false, "_lit"),
        INVERTED_LIT(true, true, "_inverted_lit"),
        INVERTED_UNLIT(false, true, "_inverted");

        private final boolean lit;
        private final boolean inverted;
        private final String suffix;

        State(boolean lit, boolean inverted, String suffix) {
            this.lit = lit;
            this.inverted = inverted;
            this.suffix = suffix;
        }

        public boolean lit() {
            return lit;
        }

        public boolean inverted() {
            return inverted;
        }

        public boolean hasItem() {
            return lit == inverted;
        }

        public State withPower(boolean powered) {
            if (inverted)
                return powered ? INVERTED_UNLIT : INVERTED_LIT;
            else
                return powered ? LIT : UNLIT;
        }
    }

    private final Gems gem;
    private final State lampState;

    public GemLampBlock(Gems gem, State lampState) {
        super(Properties.create(Material.REDSTONE_LIGHT)
                .hardnessAndResistance(0.3f, 15)
                .lightValue(lampState.lit ? 15 : 0));

        this.gem = gem;
        this.lampState = lampState;
    }

    public static String nameFor(Gems gem, State lampState) {
        return gem.getName() + "_lamp" + lampState.suffix;
    }

    private void checkAndUpdateState(World world, BlockPos pos) {
        if (!world.isRemote) {
            boolean powered = world.isBlockPowered(pos);
            State newLampState = this.lampState.withPower(powered);

            if (newLampState != this.lampState) {
                BlockState newState = this.gem.getLamp(newLampState).getDefaultState();
                world.setBlockState(pos, newState, 2);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean p_220082_5_) {
            checkAndUpdateState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
            checkAndUpdateState(worldIn, pos);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random random) {
            checkAndUpdateState(world, pos);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.lampState.hasItem()) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        tooltip.add(gem.getSet().getDisplayName());
        if (flag.isAdvanced()) {
            tooltip.add(new StringTextComponent("Type: " + this.lampState));
        }
    }

    @Override
    public ITextComponent getNameTextComponent() {
        String translationKey = "block.silentgems.gem_lamp" + (this.lampState.inverted ? "_inverted" : "");
        return new TranslationTextComponent(translationKey, this.gem.getDisplayName());
    }

//    @Override
//    public List<String> getWitLines(BlockState state, BlockPos pos, PlayerEntity player, boolean advanced) {
//        String line = inverted ? SilentGems.i18n.blockSubText(Names.GEM_LAMP, "inverted") : "";
//        line += lit ? (line.isEmpty() ? "" : ", ") + SilentGems.i18n.blockSubText(Names.GEM_LAMP, "lit") : "";
//        return line.isEmpty() ? null : Lists.newArrayList(line);
//    }
}
