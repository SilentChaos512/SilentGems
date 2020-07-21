/*
 * Silent's Gems -- SuperchargerTileEntity
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

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.compat.gear.SGearProxy;
import net.silentchaos512.gems.init.GemsEnchantments;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;
import net.silentchaos512.lib.tile.SyncVariable;
import net.silentchaos512.lib.util.TimeUtils;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;

public class SuperchargerTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    private static final int INVENTORY_SIZE = 3;
    private static final int UPDATE_FREQUENCY = TimeUtils.ticksFromSeconds(15);

    @SyncVariable(name = "Progress")
    private int progress;
    @SyncVariable(name = "ProcessTime")
    private int processTime;
    @SyncVariable(name = "StructureLevel")
    private int structureLevel;
    @SyncVariable(name = "ChaosGenerated")
    private int chaosGenerated;
    private int chaosBuffer;

    private int updateTimer = 0;

    final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return SuperchargerTileEntity.this.progress;
                case 1:
                    return SuperchargerTileEntity.this.processTime;
                case 2:
                    return SuperchargerTileEntity.this.chaosGenerated;
                case 3:
                    return SuperchargerTileEntity.this.structureLevel;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    progress = value;
                    break;
                case 1:
                    processTime = value;
                    break;
                case 2:
                    chaosGenerated = value;
                    break;
                case 3:
                    structureLevel = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public SuperchargerTileEntity() {
        super(GemsTileEntities.SUPERCHARGER.get(), INVENTORY_SIZE);
    }

    public int getProgress() {
        return progress;
    }

    public int getProcessTime() {
        return processTime;
    }

    public int getStructureLevel() {
        return structureLevel;
    }

    public int getChaosGenerated() {
        return chaosGenerated;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

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
        int superchargedLevel = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.SUPERCHARGED.get(), input);

        if (!input.isEmpty() && !catalyst.isEmpty() && superchargedLevel < 1) {
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
            int partTier = SGearProxy.getMaterialTier(input);
            chaosGenerated = getEmissionRate(partTier, chargeTier);
            chaosBuffer += chaosGenerated;

            if (wouldFitInOutputSlot(input, chargeTier)) {
                ++progress;
                processTime = getProcessTime(partTier, chargeTier);

                if (progress >= processTime) {
                    if (getStackInSlot(2).isEmpty()) {
                        ItemStack output = input.copy();
                        output.setCount(1);
                        output.addEnchantment(GemsEnchantments.SUPERCHARGED.get(), chargeTier);
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

        if (this.chaosBuffer > 0 && this.world.getGameTime() % 20 == 0) {
            Chaos.generate(this.world, this.chaosBuffer, this.pos);
            this.chaosBuffer = 0;
        }
    }

    private void sendUpdate() {
        if (world != null) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private ItemStack getInputItem() {
        ItemStack stack = getStackInSlot(0);
        return SGearProxy.isMaterial(stack) ? stack : ItemStack.EMPTY;
    }

    private ItemStack getCatalystItem() {
        ItemStack stack = getStackInSlot(1);
        return getChargingAgentTier(stack) > 0 ? stack : ItemStack.EMPTY;
    }

    private boolean checkStructureLevel() {
        int oldValue = this.structureLevel;
        this.structureLevel = MathUtils.min(
                this.getPillarLevel(this.pos.offset(Direction.NORTH, 3).offset(Direction.WEST, 3)),
                this.getPillarLevel(this.pos.offset(Direction.NORTH, 3).offset(Direction.EAST, 3)),
                this.getPillarLevel(this.pos.offset(Direction.SOUTH, 3).offset(Direction.WEST, 3)),
                this.getPillarLevel(this.pos.offset(Direction.SOUTH, 3).offset(Direction.EAST, 3)));
        return this.structureLevel != oldValue;
    }

    private int getPillarLevel(BlockPos pos) {
        BlockState state1 = this.world.getBlockState(pos);
        BlockState state2 = this.world.getBlockState(pos.up());
        if (state1.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                && state2.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_CAP))
            return 1;

        BlockState state3 = this.world.getBlockState(pos.up(2));
        if (state1.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL2)
                && state2.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                && state3.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_CAP))
            return 2;

        BlockState state4 = this.world.getBlockState(pos.up(3));
        BlockState state5 = this.world.getBlockState(pos.up(4));
        if (state1.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL3)
                && state2.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL3)
                && state3.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL2)
                && state4.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_LEVEL1)
                && state5.isIn(GemsTags.Blocks.SUPERCHARGER_PILLAR_CAP))
            return 3;

        return 0;
    }

    private static int getChargingAgentTier(ItemStack stack) {
        Item item = stack.getItem();
        if (item.isIn(GemsTags.Items.CHARGING_AGENT_TIER3))
            return 3;
        if (item.isIn(GemsTags.Items.CHARGING_AGENT_TIER2))
            return 2;
        if (item.isIn(GemsTags.Items.CHARGING_AGENT_TIER1))
            return 1;
        return 0;
    }

    private boolean wouldFitInOutputSlot(ItemStack input, int chargeTier) {
        ItemStack output = getStackInSlot(2);
        if (output.isEmpty()) return true;
        return output.getCount() < output.getMaxStackSize()
                && input.isItemEqual(output)
                && EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.SUPERCHARGED.get(), output) == chargeTier
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
        if (index == 0) return SGearProxy.isMaterial(stack);
        if (index == 1) return stack.getItem().isIn(GemsTags.Items.CHARGING_AGENTS);
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.silentgems.supercharger");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new SuperchargerContainer(id, playerInventory, this);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        switch (side) {
            case UP:
                return new int[]{0, 1};
            case DOWN:
                return new int[]{0, 1, 2};
            default:
                return new int[]{0, 1, 2};
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return index == 2;
    }

    @Override
    public void read(BlockState state, CompoundNBT tags) {
        super.read(state, tags);
        SyncVariable.Helper.readSyncVars(this, tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.WRITE);
        return tags;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        SyncVariable.Helper.writeSyncVars(this, tags, SyncVariable.Type.PACKET);
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        SyncVariable.Helper.readSyncVars(this, packet.getNbtCompound());
    }
}
