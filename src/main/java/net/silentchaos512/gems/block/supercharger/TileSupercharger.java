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

package net.silentchaos512.gems.block.supercharger;

import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.ModEnchantments;
import net.silentchaos512.gems.init.ModTags;
import net.silentchaos512.gems.init.ModTileEntities;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.tile.TileSidedInventorySL;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;

public class TileSupercharger extends TileSidedInventorySL implements ITickable {
    private static final int INVENTORY_SIZE = 3;
    private static final int UPDATE_FREQUENCY =  TimeUtils.ticksFromSeconds(15);

    @Getter
    @SyncVariable(name = "Progress")
    private int progress;
    @Getter
    @SyncVariable(name = "ProcessTime")
    private int processTime;
    @Getter
    @SyncVariable(name = "StructureLevel")
    private int structureLevel;
    @Getter
    @SyncVariable(name = "ChaosGenerated")
    private int chaosGenerated;

    private int updateTimer = 0;

    public TileSupercharger() {
        super(ModTileEntities.SUPERCHARGER.type());
    }

    @Override
    public void tick() {
        if (this.world.isRemote) return;

        ++updateTimer;

        if (updateTimer > UPDATE_FREQUENCY) {
            if (checkStructureLevel()) {
                SilentGems.LOGGER.info("Supercharger at {}: structure level updated to {}",
                        this.pos, this.structureLevel);
            }
            updateTimer = 0;
            sendUpdate();
        }

        ItemStack input = getInputItem();
        ItemStack catalyst = getCatalystItem();

        if (!input.isEmpty() && !catalyst.isEmpty()) {
            handleCharging(input, catalyst);
        } else if (progress > 0) {
            progress = 0;
            chaosGenerated = 0;
            processTime = 100;
            sendUpdate();
        }
    }

    private void handleCharging(ItemStack input, ItemStack catalyst) {
        int chargeTier = getChargingAgentTier(catalyst);
        if (chargeTier > 0 && chargeTier <= this.structureLevel) {
            int partTier = SGearProxy.getPartTier(this.getInputItem());
            chaosGenerated = getEmissionRate(partTier, chargeTier);

            if (wouldFitInOutputSlot(input, chargeTier)) {
                ++progress;
                if (chaosGenerated > 0) {
                    Chaos.generate(this.world, chaosGenerated);
                }
                processTime = getProcessTime(partTier, chargeTier);

                if (progress >= processTime) {
                    if (getStackInSlot(2).isEmpty()) {
                        ItemStack output = input.copy();
                        output.setCount(1);
                        output.addEnchantment(ModEnchantments.supercharged, chargeTier);
                        setInventorySlotContents(2, output);
                    } else {
                        getStackInSlot(2).grow(1);
                    }

                    progress = 0;
                    decrStackSize(0, 1);
                    decrStackSize(1, 1);
                }
            }

            sendUpdate();
        }
    }

    private ItemStack getInputItem() {
        ItemStack stack = getStackInSlot(0);
        return SGearProxy.isMainPart(stack) ? stack : ItemStack.EMPTY;
    }

    private ItemStack getCatalystItem() {
        ItemStack stack = getStackInSlot(1);
        return getChargingAgentTier(stack) > 0 ? stack : ItemStack.EMPTY;
    }

    private boolean checkStructureLevel() {
        int oldValue = this.structureLevel;
        this.structureLevel = MathUtils.min(
                this.getPillarLevel(this.pos.offset(EnumFacing.NORTH, 3).offset(EnumFacing.WEST, 3)),
                this.getPillarLevel(this.pos.offset(EnumFacing.NORTH, 3).offset(EnumFacing.EAST, 3)),
                this.getPillarLevel(this.pos.offset(EnumFacing.SOUTH, 3).offset(EnumFacing.WEST, 3)),
                this.getPillarLevel(this.pos.offset(EnumFacing.SOUTH, 3).offset(EnumFacing.EAST, 3)));
        return this.structureLevel != oldValue;
    }

    private int getPillarLevel(BlockPos pos) {
        IBlockState state1 = this.world.getBlockState(pos);
        IBlockState state2 = this.world.getBlockState(pos.up());
        if (state1.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                && state2.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_CAP))
            return 1;

        IBlockState state3 = this.world.getBlockState(pos.up(2));
        if (state1.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL2)
                && state2.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                && state3.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_CAP))
            return 2;

        IBlockState state4 = this.world.getBlockState(pos.up(3));
        IBlockState state5 = this.world.getBlockState(pos.up(4));
        if (state1.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL3)
                && state2.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL3)
                && state3.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL2)
                && state4.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                && state5.isIn(ModTags.Blocks.SUPERCHARGER_PILLAR_CAP))
            return 3;

        return 0;
    }

    private static int getChargingAgentTier(ItemStack stack) {
        Item item = stack.getItem();
        if (item.isIn(ModTags.Items.CHARGING_AGENT_TIER3))
            return 3;
        if (item.isIn(ModTags.Items.CHARGING_AGENT_TIER2))
            return 2;
        if (item.isIn(ModTags.Items.CHARGING_AGENT_TIER1))
            return 1;
        return 0;
    }

    private boolean wouldFitInOutputSlot(ItemStack input, int chargeTier) {
        ItemStack output = getStackInSlot(2);
        if (output.isEmpty()) return true;
        return output.getCount() < output.getMaxStackSize()
                && input.isItemEqual(output)
                && EnchantmentHelper.getEnchantmentLevel(ModEnchantments.supercharged, output) == chargeTier
                && SGearProxy.getGradeString(input).equalsIgnoreCase(SGearProxy.getGradeString(output));
    }

    public static int getEmissionRate(int partTier, int chargeTier) {
        if (chargeTier <= 0 || partTier < 0) return 0;
        return (int) (4 * chargeTier * Math.pow(5, partTier));
    }

    public static int getProcessTime(int partTier, int chargeTier) {
        if (chargeTier <= 0 || partTier < 0) return 0;
        return (int) (4 + 3 * chargeTier * Math.pow(2, partTier + 1));
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) return SGearProxy.isMainPart(stack);
        if (index == 1) return stack.getItem().isIn(ModTags.Items.CHARGING_AGENTS);
        return false;
    }

    @Override
    public int getSizeInventory() {
        return INVENTORY_SIZE;
    }

    @Override
    public boolean isEmpty() {
        // TODO
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation("container.silentgems.supercharger");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }
}
