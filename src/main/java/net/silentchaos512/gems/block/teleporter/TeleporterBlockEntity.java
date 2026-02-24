package net.silentchaos512.gems.block.teleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.silentchaos512.gems.setup.GemsBlockEntityTypes;
import net.silentchaos512.lib.util.DimPos;

import javax.annotation.Nullable;

public class TeleporterBlockEntity extends BlockEntity {
    public static final String DESTINATION_TAG = "Destination";

    @Nullable
    private DimPos destination = null;

    public TeleporterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    public TeleporterBlockEntity(BlockPos pos, BlockState blockState) {
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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        if (this.destination != null) {
            output.store(DESTINATION_TAG, DimPos.CODEC, this.destination);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.destination = input.read(DESTINATION_TAG, DimPos.CODEC).orElse(null);
    }
}
