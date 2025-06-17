package net.silentchaos512.gems.item;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.silentchaos512.gems.block.teleporter.AbstractTeleporterBlock;
import net.silentchaos512.gems.block.teleporter.TeleporterBlockEntity;
import net.silentchaos512.gems.setup.GemsDataComponents;
import net.silentchaos512.lib.util.DimPos;

public class TeleporterLinker extends Item {
    public TeleporterLinker(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var state = context.getLevel().getBlockState(context.getClickedPos());
        if (!(state.getBlock() instanceof AbstractTeleporterBlock)) {
            return super.useOn(context);
        }

        var stack = context.getItemInHand();
        var clickedDimPos = DimPos.of(context.getClickedPos(), context.getLevel().dimension());
        var player = context.getPlayer();
        if (stack.has(GemsDataComponents.LINKED_POS)) {
            // Link teleporters
            var storedDimPos = stack.get(GemsDataComponents.LINKED_POS);
            assert storedDimPos != null;
            if (player != null) {
                tryLinkTeleporter(player, storedDimPos, clickedDimPos.offset(Direction.UP, 1));
                tryLinkTeleporter(player, clickedDimPos, storedDimPos.offset(Direction.UP, 1));
                stack.remove(GemsDataComponents.LINKED_POS);
                player.displayClientMessage(Component.literal("Teleporters linked!"), true);
            }
        } else {
            // Save position for linking
            stack.set(GemsDataComponents.LINKED_POS, clickedDimPos);
            if (player != null) {
                player.displayClientMessage(Component.literal("Teleporter position stored!"), true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void tryLinkTeleporter(Player player, DimPos teleporterPosition, DimPos newDestination) {
        var level = teleporterPosition.getPosLevel(player.level());
        if (level.isPresent()) {
            var blockEntity = level.get().getBlockEntity(teleporterPosition.getPos());
            if (blockEntity instanceof TeleporterBlockEntity teleporterBlockEntity) {
                teleporterBlockEntity.setDestination(newDestination);
            }
        } else if (!player.level().isClientSide) {
            player.displayClientMessage(Component.literal("Could not link teleporter at " + teleporterPosition), false);
        }
    }
}
