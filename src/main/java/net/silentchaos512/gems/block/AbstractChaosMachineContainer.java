package net.silentchaos512.gems.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

public class AbstractChaosMachineContainer<T extends AbstractChaosMachineTileEntity<?>> extends Container {
    protected final T tileEntity;
    protected final IIntArray fields;

    protected AbstractChaosMachineContainer(ContainerType<?> type, int id, T tileEntity, IIntArray fields) {
        super(type, id);
        this.tileEntity = tileEntity;
        this.fields = fields;

        trackIntArray(this.fields);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public T getTileEntity() {
        return tileEntity;
    }

    public int getChaosGenerated() {
        int upper = fields.get(3) & 0xFFFF;
        int lower = fields.get(2) & 0xFFFF;
        return (upper << 16) + lower;
    }

    public int getProgress() {
        return fields.get(0);
    }

    public int getProcessTime() {
        return fields.get(1);
    }
}
