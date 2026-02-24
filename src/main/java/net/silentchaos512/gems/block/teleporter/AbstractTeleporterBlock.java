package net.silentchaos512.gems.block.teleporter;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.silentchaos512.gems.item.TeleporterLinker;
import net.silentchaos512.lib.util.DimPos;
import net.silentchaos512.lib.util.TeleportUtils;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class AbstractTeleporterBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private final MapCodec<? extends AbstractTeleporterBlock> codec;

    public AbstractTeleporterBlock(Properties properties, Function<Properties, ? extends AbstractTeleporterBlock> constructor) {
        super(properties);
        this.codec = simpleCodec(constructor);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return this.codec;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeleporterBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (playerHoldingLinker(player)) {
            return InteractionResult.PASS;
        }

        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TeleporterBlockEntity teleporterBlockEntity) {
            if (player instanceof ServerPlayer) {
                var destination = teleporterBlockEntity.getDestination();
                if (destination != null) {
                    teleportEntity(level, player, destination);
                    playTeleportSound(level, player.blockPosition().above());
                    playTeleportSound(level, destination);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean playerHoldingLinker(Player player) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof TeleporterLinker
                || player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof TeleporterLinker;
    }

    protected void teleportEntity(Level level, Entity entity, DimPos destination) {
        if (entity instanceof Player player) {
            TeleportUtils.teleport(player, destination, null);
        } else {
            TeleportUtils.teleportEntity(entity, destination, null);
        }
    }

    protected void playTeleportSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.PLAYER_TELEPORT, SoundSource.BLOCKS, 0.7f, 1.2f);
    }

    protected void playTeleportSound(Level level, DimPos destination) {
        var server = level.getServer();
        if (server == null) return;

        var destinationLevel = server.getLevel(destination.dimension());
        if (destinationLevel == null) return;

        playTeleportSound(destinationLevel, destination.getPos());
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
