package net.silentchaos512.gems.tile;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class TileTeleporter extends TileEntity {
//    protected DimensionalPosition destination = null;
//    protected int chaosStored = 0;
    public boolean isAnchor = false;

    public TileTeleporter(boolean isAnchor) {
        super(null); // FIXME
        this.isAnchor = isAnchor;
    }

//    public DimensionalPosition getDestination() {
//        return destination;
//    }

    @Override
    public void read(NBTTagCompound tags) {
        super.read(tags);

//        destination = DimensionalPosition.readFromNBT(tags);
//        chaosStored = tags.getInteger("Energy");
        isAnchor = tags.getBoolean("IsAnchor");
    }

    @Override
    public NBTTagCompound write(NBTTagCompound tags) {
        super.write(tags);

//        if (destination != null) {
//            destination.writeToNBT(tags);
//        }
//        tags.setInteger("Energy", chaosStored);
        tags.setBoolean("IsAnchor", isAnchor);
        return tags;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tags = new NBTTagCompound();
//        if (destination != null && !destination.equals(DimensionalPosition.ZERO)) {
//            destination.writeToNBT(tags);
//        }
        return new SPacketUpdateTileEntity(pos, 0, tags);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound tags = super.getUpdateTag();
//        if (destination != null && !destination.equals(DimensionalPosition.ZERO)) {
//            destination.writeToNBT(tags);
//        }
        return tags;
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
//        destination = DimensionalPosition.readFromNBT(packet.getNbtCompound());
    }

    public boolean linkReturnHomeCharm(EntityPlayer player, World world, BlockPos pos, ItemStack heldItem, EnumHand hand) {
        if (world.isRemote) {
            return true;
        }

//        if (!heldItem.hasTagCompound()) {
//            heldItem.setTagCompound(new NBTTagCompound());
//        }

//        DimensionalPosition position = new DimensionalPosition(pos, player.dimension);
//        position.writeToNBT(heldItem.getTagCompound());
//        ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "returnHomeBound"));
        if (player instanceof EntityPlayerMP)
            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, heldItem);
        return true;
    }

    public boolean linkTeleporters(EntityPlayer player, World world, BlockPos pos, ItemStack heldItem, EnumHand hand) {
//        if (world.isRemote) {
//            return true;
//        }
//
//        TeleporterLinker linker = ModItems.teleporterLinker;
//
//        if (heldItem.isEmpty() || heldItem.getItem() != linker) {
//            SilentGems.LOGGER.warn("TileTeleporter.linkTeleporters: heldItem is not a linker?");
//            return false;
//        }
//
//        if (linker.isLinked(heldItem)) {
//            // Active state: link teleporters and set inactive.
//            DimensionalPosition position1 = linker.getLinkedPosition(heldItem);
//            DimensionalPosition position2 = new DimensionalPosition(pos, player.dimension);
//
//            if (position1 == null) {
//                log.warn("Teleporter Linker tried to link with no position set?");
//                return true;
//            }
//
//            TileTeleporter tile1 = (TileTeleporter) player.getServer()
//                    .getWorld(position1.dim).getTileEntity(position1.toBlockPos());
//            TileTeleporter tile2 = (TileTeleporter) player.getServer()
//                    .getWorld(position2.dim).getTileEntity(position2.toBlockPos());
//
//            if (tile1 == null || tile2 == null) {
//                // Could not find a teleporter?
//                ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "linkFail"));
//                log.warn("Could not find teleporter when linking:" + "\nTeleporter1 @ "
//                        + position1.toString() + "\nTeleporter2 @ " + position2.toString());
//                linker.setLinked(heldItem, false);
//                return false;
//            }
//
//            // Create "link"
//            tile1.destination = position2;
//            tile2.destination = position1;
//            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "linkSuccess"));
//            linker.setLinked(heldItem, false);
//            tile1.markDirty();
//            tile2.markDirty();
//
//            if (player instanceof EntityPlayerMP) {
//                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, heldItem);
//            }
//        } else {
//            // Inactive state: set active and location.
//            linker.setLinkedPosition(heldItem, new DimensionalPosition(pos, player.dimension));
//            linker.setLinked(heldItem, true);
//            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "linkStart"));
//        }

        return true;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        Chunk chunk = world.getChunk(pos);
        IBlockState state = world.getBlockState(pos);
        world.markAndNotifyBlock(pos, chunk, state, state, 2);
    }

    @Deprecated
    public boolean checkAndDrainChaos(EntityPlayer player) {
//        if (player == null || player.capabilities.isCreativeMode) {
//            return true;
//        }
//
//        // Is there enough Chaos to teleport?
//        int cost = getRequiredChaos(player);
//        int available = getCharge() + ChaosUtil.getTotalChaosAvailable(player);
//        if (cost > available) {
//            String str = SilentGems.i18n.blockSubText(Names.TELEPORTER, "notEnoughChaos");
//            str = String.format(str, getCharge(), cost);
//            ChatHelper.sendMessage(player, str);
//            return false;
//        }
//
//        // Drain Chaos
//        if (cost > chaosStored) {
//            cost -= chaosStored;
//            chaosStored = 0;
//            ChaosUtil.drainChaosFromPlayerAndInventory(player, cost);
//        } else {
//            chaosStored -= cost;
//        }

        return true;
    }

    @Deprecated
    public int getRequiredChaos(EntityPlayer player) {
//        if (destination == null || destination.equals(DimensionalPosition.ZERO)) {
//            return 0;
//        }
//
//        DimensionalPosition source = new DimensionalPosition(pos, player.dimension);
//
//        if (source.dim != destination.dim) {
//            return GemsConfig.TELEPORTER_COST_CROSS_DIMENSION;
//        }
//
//        double distance = getDistanceBetweenTeleporters(source);
//        if (distance < GemsConfig.TELEPORTER_FREE_RANGE) {
//            return 0;
//        }
//        return MathHelper.clamp((int) (GemsConfig.TELEPORTER_COST_PER_BLOCK * distance), 0,
//                getMaxCharge());
        return 0;
    }

//    public double getDistanceBetweenTeleporters(DimensionalPosition source) {
//        if (destination == null || destination.equals(DimensionalPosition.ZERO)) {
//            return 0;
//        }
//
//        double x = source.x - destination.x;
//        double z = source.z - destination.z;
//        return Math.sqrt(x * x + z * z);
//    }

    public boolean isDestinationSafe(EntityPlayer player) {
        // Check for obstruction at head level. Could use some additional checks maybe...
//        if (player == null || destination == null || destination.equals(DimensionalPosition.ZERO)) {
//            return true;
//        }
//        return !player.getServer().getWorld(destination.dim)
//                .isBlockNormalCube(destination.toBlockPos().up(2), false);
        return true;
    }

    public boolean isDestinationSane(EntityPlayer player) {
        // Checks that the given destination makes sense.
//        if (player == null || destination == null || destination.equals(DimensionalPosition.ZERO)) {
//            return true;
//        }
//        return destination.y >= 0 && destination.y < 256;
        return true;
    }

    public boolean isDestinationAllowedIfDumb(EntityPlayer player) {
        // Checks for a teleporter at the destination if dumb teleports are not allowed.
//        if (player == null || GemsConfig.TELEPORTER_ALLOW_DUMB || destination == null
//                || destination.equals(DimensionalPosition.ZERO)) {
//            return true;
//        }
//        return player.getServer().getWorld(destination.dim)
//                .getBlockState(destination.toBlockPos()).getBlock() instanceof BlockTeleporter;
        return true;
    }

    public boolean isDestinationSet() {
//        return destination != null && !destination.equals(DimensionalPosition.ZERO);
        return false;
    }

    public boolean teleportEntityToDestination(Entity entity) {
//        if (destination == null || destination.equals(DimensionalPosition.ZERO)) {
//            return false;
//        }
//
//        if (entity instanceof EntityPlayerMP) {
//            return TeleportUtil.teleportPlayerTo((EntityPlayerMP) entity, destination);
//        } else {
//            return TeleportUtil.teleportEntityTo(entity, destination);
//        }
        return false;
    }
}
