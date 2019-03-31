package net.silentchaos512.gems.block.teleporter;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.init.ModTileEntities;
import net.silentchaos512.gems.item.ReturnHomeCharm;
import net.silentchaos512.gems.item.TeleporterLinker;
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
        super(ModTileEntities.TELEPORTER.type());
        this.isAnchor = isAnchor;
    }

    public DimPos getDestination() {
        return destination;
    }

    @Override
    public void read(NBTTagCompound tags) {
        super.read(tags);

        destination = DimPos.read(tags);
        isAnchor = tags.getBoolean("IsAnchor");
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tags) {
        super.write(tags);

        if (this.isDestinationSet()) {
            destination.write(tags);
        }
        tags.putBoolean("IsAnchor", isAnchor);
        return tags;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tags = new NBTTagCompound();
        if (this.isDestinationSet()) {
            destination.write(tags);
        }
        return new SPacketUpdateTileEntity(pos, 0, tags);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tags = super.getUpdateTag();
        if (this.isDestinationSet()) {
            destination.write(tags);
        }
        return tags;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.destination = DimPos.read(packet.getNbtCompound());
    }

    public boolean interact(EntityPlayer player, ItemStack heldItem, EnumHand hand) {
        // Link teleporters
        if (!heldItem.isEmpty() && heldItem.getItem() == TeleporterLinker.INSTANCE.get()) {
            return linkTeleporters(player, this.world, this.pos, heldItem, hand);
        }
        // Link a return home charm
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ReturnHomeCharm) {
            return linkReturnHomeCharm(player, this.world, this.pos, heldItem, hand);
        }
        // Anchors do not teleport
        if (this.isAnchor) {
            return false;
        }
        // Teleport away!
        return tryTeleportPlayer(player);
    }

    private boolean tryTeleportPlayer(EntityPlayer player) {
        // Destination set?
        if (!this.isDestinationSet()) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.noDestination"));
            return true;
        }

        // Safety checks before teleporting:
        if (!this.isDestinationSane(player)) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.notSane"));
            return true;
        }
        if (!this.isDestinationSafe(player)) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.notSafe"));
            return true;
        }
        if (!this.isDestinationAllowedIfDumb(player)) {
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.noReceiver"));
            return true;
        }

        // Teleport player
        float pitch = 0.7f + 0.3f * SilentGems.random.nextFloat();
        world.playSound(null, this.pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0f, pitch);
        int chaosGenerated = getChaosGenerated(player);
        if (this.teleportEntityToDestination(player)) {
            Chaos.generate(player, chaosGenerated);
        }
        world.playSound(null, this.destination.getPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1.0f, pitch);

        return true;
    }

    private boolean linkReturnHomeCharm(EntityPlayer player, World world, BlockPos pos, ItemStack heldItem, EnumHand hand) {
        if (world.isRemote) return true;

        DimPos position = DimPos.of(pos, player.dimension.getId());
        position.write(heldItem.getOrCreateTag());
        player.sendMessage(new TextComponentTranslation("teleporter.silentgems.returnHomeBound"));
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, heldItem);
        }
        return true;
    }

    private static boolean linkTeleporters(EntityPlayer player, World world, BlockPos pos, ItemStack heldItem, EnumHand hand) {
        if (world.isRemote) {
            return true;
        }

        if (heldItem.isEmpty() || heldItem.getItem() != TeleporterLinker.INSTANCE.get()) {
            SilentGems.LOGGER.warn("GemTeleporterTileEntity.linkTeleporters: heldItem is not a linker?");
            return false;
        }

        if (TeleporterLinker.isLinked(heldItem)) {
            // Active state: link teleporters and set inactive.
            DimPos position1 = TeleporterLinker.getLinkedPosition(heldItem);
            DimPos position2 = DimPos.of(pos, player.dimension.getId());

            if (DimPos.ZERO.equals(position1)) {
                SilentGems.LOGGER.warn("Teleporter linker tried to link with no position set?");
                return true;
            }

            GemTeleporterTileEntity tile1 = getTeleporterAt(player, position1);
            GemTeleporterTileEntity tile2 = getTeleporterAt(player, position2);

            if (tile1 == null || tile2 == null) {
                // Could not find a teleporter?
                player.sendMessage(new TextComponentTranslation("teleporter.silentgems.linkFail"));
                SilentGems.LOGGER.warn("Could not find teleporter when linking:");
                SilentGems.LOGGER.warn("Teleporter1 @ {}", position1);
                SilentGems.LOGGER.warn("Teleporter1 @ {}", position2);
                TeleporterLinker.setLinked(heldItem, false);
                return false;
            }

            // Create "link"
            tile1.destination = position2;
            tile2.destination = position1;
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.linkSuccess"));
            TeleporterLinker.setLinked(heldItem, false);
            tile1.markDirty();
            tile2.markDirty();

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, heldItem);
            }
        } else {
            // Inactive state: set active and location.
            TeleporterLinker.setLinkedPosition(heldItem, DimPos.of(pos, player.dimension.getId()));
            TeleporterLinker.setLinked(heldItem, true);
            player.sendMessage(new TextComponentTranslation("teleporter.silentgems.linkStart"));
        }

        return true;
    }

    @Nullable
    private static GemTeleporterTileEntity getTeleporterAt(EntityPlayer player, DimPos pos) {
        WorldServer world = getServerWorld(player, pos);
        if (world == null) return null;
        return (GemTeleporterTileEntity) world.getTileEntity(pos.getPos());
    }

    @Nullable
    private static WorldServer getServerWorld(EntityPlayer player, DimPos pos) {
        MinecraftServer server = player.getServer();
        if (server == null) return null;

        DimensionType dimensionType = DimensionType.getById(pos.getDimension());
        if (dimensionType == null) return null;

        return server.getWorld(dimensionType);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Chunk chunk = world.getChunk(pos);
        IBlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, chunk, state, state, 2);
    }

    private int getChaosGenerated(EntityPlayer player) {
        return Chaos.getChaosGeneratedByTeleport(player, this.destination);
    }

    private boolean isDestinationSafe(@Nullable EntityPlayer player) {
        // Check for obstruction at head level. Could use some additional checks maybe...
        if (player == null || !isDestinationSet()) {
            return true;
        }
        return TeleportUtil.isDestinationSafe(player, this.destination);
    }

    private boolean isDestinationSane(@Nullable EntityPlayer player) {
        // Checks that the given destination makes sense.
        if (player == null || !isDestinationSet()) {
            return false;
        }
        return destination.getY() >= 0 && destination.getY() < 256;
    }

    private boolean isDestinationAllowedIfDumb(EntityPlayer player) {
        // Checks for a teleporter at the destination if dumb teleports are not allowed.
//        if (player == null || GemsConfig.TELEPORTER_ALLOW_DUMB || destination == null
//                || destination.equals(DimensionalPosition.ZERO)) {
//            return true;
//        }
//        return player.getServer().getWorld(destination.dim)
//                .getBlockState(destination.toBlockPos()).getBlock() instanceof GemTeleporter;
        return true;
    }

    private boolean isDestinationSet() {
        return destination != null && !DimPos.ZERO.equals(destination);
    }

    private boolean teleportEntityToDestination(Entity entity) {
        if (!isDestinationSet()) {
            return false;
        }

        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) entity;
            WorldServer world = playerMP.getServerWorld();
            return TeleportUtil.teleportPlayerTo(playerMP, world, destination);
        } else {
            return TeleportUtil.teleportEntityTo(entity, destination);
        }
    }
}
