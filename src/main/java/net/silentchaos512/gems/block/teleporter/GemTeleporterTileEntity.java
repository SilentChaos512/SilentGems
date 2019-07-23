package net.silentchaos512.gems.block.teleporter;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.init.GemsTileEntities;
import net.silentchaos512.gems.item.ReturnHomeCharmItem;
import net.silentchaos512.gems.item.TeleporterLinkerItem;
import net.silentchaos512.gems.util.TeleportUtil;
import net.silentchaos512.lib.util.DimPos;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;

public class GemTeleporterTileEntity extends TileEntity {
    private static final Marker MARKER = MarkerManager.getMarker("GemTeleporterTileEntity");

    private DimPos destination = null;
    private boolean isAnchor;

    public GemTeleporterTileEntity() {
        this(false);
    }

    public GemTeleporterTileEntity(boolean isAnchor) {
        super(GemsTileEntities.TELEPORTER.type());
        this.isAnchor = isAnchor;
    }

    public DimPos getDestination() {
        return destination;
    }

    @Override
    public void read(CompoundNBT tags) {
        super.read(tags);
        tryReadDestination(tags);
        isAnchor = tags.getBoolean("IsAnchor");
    }

    @Override
    public CompoundNBT write(CompoundNBT tags) {
        super.write(tags);
        if (this.isDestinationSet()) {
            destination.write(tags);
        }
        tags.putBoolean("IsAnchor", isAnchor);
        return tags;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = getUpdateTag();
        return new SUpdateTileEntityPacket(pos, 0, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        if (this.isDestinationSet()) {
            destination.write(tags);
        }
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        tryReadDestination(packet.getNbtCompound());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        super.handleUpdateTag(tag);
        tryReadDestination(tag);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (world != null && !world.isRemote) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    private void tryReadDestination(CompoundNBT tag) {
        DimPos dimPos = DimPos.read(tag);
        if (!dimPos.equals(DimPos.ZERO)) {
            this.destination = dimPos;
        }
    }

    public boolean interact(PlayerEntity player, ItemStack heldItem, Hand hand) {
        // Link teleporters
        if (!heldItem.isEmpty() && heldItem.getItem() == TeleporterLinkerItem.INSTANCE.get()) {
            return linkTeleporters(player, this.world, this.pos, heldItem, hand);
        }
        // Link a return home charm
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ReturnHomeCharmItem) {
            return linkReturnHomeCharm(player, this.world, this.pos, heldItem, hand);
        }
        // Anchors do not teleport
        if (this.isAnchor) {
            return false;
        }
        // Teleport away!
        return tryTeleport(player, true);
    }

    public boolean tryTeleport(Entity entity, boolean playSfx) {
        // Destination set?
        if (!this.isDestinationSet()) {
            sendMessage(entity, "noDestination");
            return true;
        }

        // Safety checks before teleporting:
        if (!this.isDestinationSane(entity)) {
            sendMessage(entity, "notSane");
            return true;
        }
        if (!this.isDestinationSafe(entity)) {
            sendMessage(entity, "notSafe");
            return true;
        }
        if (!this.isDestinationAllowedIfDumb(entity)) {
            sendMessage(entity, "noReceiver");
            return true;
        }

        // Teleport player
        int chaosGenerated = getChaosGenerated(entity);
        if (this.teleportEntityToDestination(entity) && chaosGenerated > 0) {
            generateChaos(entity, chaosGenerated);
        }

        // Effects
        if (playSfx) {
            playSound();
        }

        return true;
    }

    public void playSound() {
        float pitch = 0.7f + 0.3f * SilentGems.random.nextFloat();
        world.playSound(null, this.pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0f, pitch);
        world.playSound(null, this.destination.getPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0f, pitch);
    }

    private static void sendMessage(ICommandSource entity, String key) {
        if (entity instanceof PlayerEntity) {
            ITextComponent text = new TranslationTextComponent("teleporter.silentgems." + key);
            entity.sendMessage(text);
        }
    }

    private static boolean linkReturnHomeCharm(PlayerEntity player, World world, BlockPos pos, ItemStack heldItem, Hand hand) {
        if (world.isRemote) return true;

        DimPos position = DimPos.of(pos, player.dimension.getId());
        position.write(heldItem.getOrCreateTag());
        sendMessage(player, "returnHomeBound");
        if (player instanceof ServerPlayerEntity) {
            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, heldItem);
        }
        return true;
    }

    private static boolean linkTeleporters(PlayerEntity player, World world, BlockPos pos, ItemStack heldItem, Hand hand) {
        if (world.isRemote) {
            return true;
        }

        if (heldItem.isEmpty() || heldItem.getItem() != TeleporterLinkerItem.INSTANCE.get()) {
            SilentGems.LOGGER.warn("GemTeleporterTileEntity.linkTeleporters: heldItem is not a linker?");
            return false;
        }

        if (TeleporterLinkerItem.isLinked(heldItem)) {
            // Active state: link teleporters and set inactive.
            DimPos position1 = TeleporterLinkerItem.getLinkedPosition(heldItem);
            DimPos position2 = DimPos.of(pos, player.dimension.getId());

            if (DimPos.ZERO.equals(position1)) {
                SilentGems.LOGGER.warn("Teleporter linker tried to link with no position set?");
                return true;
            }

            GemTeleporterTileEntity tile1 = getTeleporterAt(player, position1);
            GemTeleporterTileEntity tile2 = getTeleporterAt(player, position2);

            if (tile1 == null || tile2 == null) {
                // Could not find a teleporter?
                sendMessage(player, "linkFail");
                SilentGems.LOGGER.warn("Could not find teleporter when linking:");
                SilentGems.LOGGER.warn("Teleporter1 @ {}", position1);
                SilentGems.LOGGER.warn("Teleporter1 @ {}", position2);
                TeleporterLinkerItem.setLinked(heldItem, false);
                return false;
            }

            // Create "link"
            tile1.destination = position2;
            tile2.destination = position1;
            SilentGems.LOGGER.debug("Teleporter1: {}", tile1.destination);
            SilentGems.LOGGER.debug("Teleporter2: {}", tile2.destination);
            TeleporterLinkerItem.setLinked(heldItem, false);
            tile1.markDirty();
            tile2.markDirty();
            sendMessage(player, "linkSuccess");

            if (player instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, heldItem);
            }
        } else {
            // Inactive state: set active and location.
            TeleporterLinkerItem.setLinkedPosition(heldItem, DimPos.of(pos, player.dimension.getId()));
            TeleporterLinkerItem.setLinked(heldItem, true);
            sendMessage(player, "linkStart");
        }

        return true;
    }

    @Nullable
    private static GemTeleporterTileEntity getTeleporterAt(PlayerEntity player, DimPos pos) {
        ServerWorld world = getServerWorld(player, pos);
        if (world == null) return null;
        return (GemTeleporterTileEntity) world.getTileEntity(pos.getPos());
    }

    @Nullable
    private static ServerWorld getServerWorld(PlayerEntity player, DimPos pos) {
        MinecraftServer server = player.getServer();
        if (server == null) return null;

        DimensionType dimensionType = DimensionType.getById(pos.getDimension());
        if (dimensionType == null) return null;

        return server.getWorld(dimensionType);
    }

    private int getChaosGenerated(Entity entity) {
        return Chaos.getChaosGeneratedByTeleport(entity, this.destination);
    }

    private static void generateChaos(Entity entity, int amount) {
        if (entity instanceof PlayerEntity) {
            Chaos.generate((PlayerEntity) entity, amount, true);
        } else {
            Chaos.generate(entity.world, amount, entity.getPosition());
        }
    }

    private boolean isDestinationSafe(@Nullable Entity entity) {
        // Check for obstruction at head level. Could use some additional checks maybe...
        if (entity == null || !isDestinationSet()) {
            return true;
        }
        return TeleportUtil.isDestinationSafe(entity, this.destination);
    }

    private boolean isDestinationSane(@Nullable Entity entity) {
        // Checks that the given destination makes sense.
        if (entity == null || !isDestinationSet()) {
            return false;
        }
        return destination.getY() >= 0 && destination.getY() < 256;
    }

    private boolean isDestinationAllowedIfDumb(Entity player) {
        // Checks for a teleporter at the destination if dumb teleports are not allowed.
//        if (player == null || GemsConfig.TELEPORTER_ALLOW_DUMB || destination == null
//                || destination.equals(DimensionalPosition.ZERO)) {
//            return true;
//        }
//        return player.getServer().getWorld(destination.dim)
//                .getBlockState(destination.toBlockPos()).getBlock() instanceof GemTeleporterBlock;
        return true;
    }

    private boolean isDestinationSet() {
        return destination != null && !DimPos.ZERO.equals(destination);
    }

    private boolean teleportEntityToDestination(Entity entity) {
        if (!isDestinationSet()) {
            return false;
        }

        TeleportUtil.teleport(entity, this.destination);
        return true;
    }
}
