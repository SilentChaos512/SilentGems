package net.silentchaos512.gems.block.teleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.silentchaos512.gems.setup.GemsBlockEntityTypes;
import net.silentchaos512.lib.util.DimPos;

import javax.annotation.Nullable;

public class GemTeleporterBlockEntity extends BlockEntity {
    public static final String DESTINATION_TAG = "Destination";

    private DimPos destination = null;

    public GemTeleporterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public GemTeleporterBlockEntity(BlockPos pos, BlockState blockState) {
        this(GemsBlockEntityTypes.TELEPORTER.get(), pos, blockState);
    }

    @Nullable
    public DimPos getDestination() {
        return destination;
    }

    public void setDestination(DimPos newDestination) {
        this.destination = newDestination;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (this.destination != null) {
            tag.put(DESTINATION_TAG, this.destination.serializeNbt());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(DESTINATION_TAG, 10)) {
            this.destination = DimPos.deserializeNbt(tag.getCompound(DESTINATION_TAG));
        }
    }
}
