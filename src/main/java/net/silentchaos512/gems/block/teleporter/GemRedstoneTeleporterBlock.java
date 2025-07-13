package net.silentchaos512.gems.block.teleporter;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.util.DimPos;

public class GemRedstoneTeleporterBlock extends GemTeleporterBlock {
    public GemRedstoneTeleporterBlock(Gems gem, Properties properties) {
        super(gem, properties, GemRedstoneTeleporterBlock::new);
    }

    @Override
    public MutableComponent getGemBlockName() {
        return Component.translatable("block.silentgems.gem_redstone_teleporter", this.gem.getDisplayName());
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (level.isClientSide) return;

        boolean hasSignal = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
        boolean triggered = state.getValue(TRIGGERED);
        if (hasSignal && !triggered) {
            tryTeleportNearbyEntities(level, pos);
            level.setBlock(pos, state.setValue(TRIGGERED, true), 2);
        } else if (!hasSignal && triggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, false), 2);
        }
    }

    private void tryTeleportNearbyEntities(Level level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof TeleporterBlockEntity teleporterBlockEntity)) return;

        var destination = teleporterBlockEntity.getDestination();
        if (destination == null) return;

        final int searchRadius = 5; // TODO: Add config
        var entitiesInArea = level.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(searchRadius));
        boolean playSound = false;

        for (Entity entity : entitiesInArea) {
            if (canTeleportEntity(level, entity, destination)) {
                teleportEntity(level, entity, destination);
                playSound = true;
            }
        }

        if (playSound) {
            playTeleportSound(level, pos);
            playTeleportSound(level, destination);
        }
    }

    private boolean canTeleportEntity(Level level, Entity entity, DimPos destination) {
        // Some entities cannot change dimensions and some mods may forbid teleporting
        return (entity.canChangeDimensions(level, destination.getPosLevel(level).orElse(level)) || destination.dimension().equals(entity.level().dimension()))
                && !entity.getType().is(Tags.EntityTypes.TELEPORTING_NOT_SUPPORTED);
    }
}
