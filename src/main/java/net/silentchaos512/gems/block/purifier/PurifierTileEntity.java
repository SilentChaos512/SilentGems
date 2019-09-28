package net.silentchaos512.gems.block.purifier;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.gems.item.PatchBlockChangerItem;
import net.silentchaos512.gems.util.TextUtil;
import net.silentchaos512.lib.tile.LockableSidedInventoryTileEntity;

import javax.annotation.Nullable;

public class PurifierTileEntity extends LockableSidedInventoryTileEntity implements ITickableTileEntity {
    private static final int CHAOS_PER_POWDER = 100_000;
    private static final int TICKS_PER_POWDER = 1000;
    private static final int CHAOS_PER_TICK = CHAOS_PER_POWDER / TICKS_PER_POWDER;

    private int burnTime;
    private int totalBurnTime;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return burnTime;
                case 1:
                    return totalBurnTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    burnTime = value;
                    break;
                case 1:
                    totalBurnTime = value;
                    break;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public PurifierTileEntity() {
        super(GemsTileEntities.PURIFIER.type(), 1);
    }

    public static boolean isPurifyingCatalyst(ItemStack stack) {
        return stack.getItem() == PatchBlockChangerItem.PURIFYING_POWDER.get();
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        if (Chaos.getChaos(world) > Chaos.getEquilibriumPoint(world) + CHAOS_PER_TICK) {
            if (burnTime > 0) {
                --burnTime;
                Chaos.dissipate(world, CHAOS_PER_TICK);
            } else {
                ItemStack stack = getStackInSlot(0);
                if (isPurifyingCatalyst(stack)) {
                    burnTime = totalBurnTime = TICKS_PER_POWDER;
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        setInventorySlotContents(0, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return itemStackIn.getItem() == PatchBlockChangerItem.PURIFYING_POWDER.get();
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return TextUtil.translate("container", "purifier");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PurifierContainer(id, player, this, this.fields);
    }

    @Override
    public void read(CompoundNBT tags) {
        this.burnTime = tags.getInt("BurnTime");
        this.totalBurnTime = tags.getInt("TotalBurnTime");
        super.read(tags);
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        tags.putInt("BurnTime", this.burnTime);
        tags.putInt("TotalBurnTime", this.totalBurnTime);
        return super.write(tags);
    }
}
