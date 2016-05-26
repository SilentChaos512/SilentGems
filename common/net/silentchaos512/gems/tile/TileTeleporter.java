package net.silentchaos512.gems.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.block.BlockTeleporter;
import net.silentchaos512.gems.config.Config;
import net.silentchaos512.gems.handler.PlayerDataHandler;
import net.silentchaos512.gems.handler.PlayerDataHandler.PlayerData;
import net.silentchaos512.gems.item.ItemTeleporterLinker;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ChaosUtil;
import net.silentchaos512.gems.util.TeleportUtil;
import net.silentchaos512.lib.util.DimensionalPosition;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.LogHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public class TileTeleporter extends TileEntity implements IChaosAccepter {

  protected DimensionalPosition destination = null;
  protected int chaosStored = 0;
  public boolean isAnchor = false;

  public TileTeleporter() {

  }

  public TileTeleporter(boolean isAnchor) {

    this.isAnchor = isAnchor;
  }

  public DimensionalPosition getDestination() {

    return destination;
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    super.readFromNBT(tags);

    destination = DimensionalPosition.readFromNBT(tags);
    chaosStored = tags.getInteger("Energy");
    isAnchor = tags.getBoolean("IsAnchor");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);

    if (destination != null) {
      destination.writeToNBT(tags);
    }
    tags.setInteger("Energy", chaosStored);
    tags.setBoolean("IsAnchor", isAnchor);
    return tags;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    NBTTagCompound tags = new NBTTagCompound();
    if (destination != null && !destination.equals(DimensionalPosition.ZERO)) {
      destination.writeToNBT(tags);
    }
    return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tags);
  }

  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {

    destination = DimensionalPosition.readFromNBT(packet.getNbtCompound());
  }

  public boolean linkReturnHomeCharm(EntityPlayer player, World world, BlockPos pos,
      ItemStack heldItem, EnumHand hand) {

    if (world.isRemote) {
      return true;
    }

    if (!heldItem.hasTagCompound()) {
      heldItem.setTagCompound(new NBTTagCompound());
    }

    DimensionalPosition position = new DimensionalPosition(pos, player.dimension);
    position.writeToNBT(heldItem.getTagCompound());
    PlayerHelper.addChatMessage(player, SilentGems.instance.localizationHelper
        .getBlockSubText(Names.TELEPORTER, "ReturnHomeBound"));
    return true;
  }

  public boolean linkTeleporters(EntityPlayer player, World world, BlockPos pos, ItemStack heldItem,
      EnumHand hand) {

    if (world.isRemote) {
      return true;
    }

    LogHelper log = SilentGems.instance.logHelper;
    LocalizationHelper loc = SilentGems.instance.localizationHelper;
    ItemTeleporterLinker linker = ModItems.teleporterLinker;

    if (heldItem == null || heldItem.getItem() != linker) {
      log.warning("TileTeleporter.linkTeleporters: heldItem is not a linker?");
      return false;
    }

    if (linker.isLinked(heldItem)) {
      // Active state: link teleporters and set inactive.
      DimensionalPosition position1 = linker.getLinkedPosition(heldItem);
      DimensionalPosition position2 = new DimensionalPosition(pos, player.dimension);

      if (position1 == null) {
        log.warning("Teleporter Linker tried to link with no position set?");
      }

      TileTeleporter tile1 = (TileTeleporter) player.getServer()
          .worldServerForDimension(position1.dim).getTileEntity(position1.toBlockPos());
      TileTeleporter tile2 = (TileTeleporter) player.getServer()
          .worldServerForDimension(position2.dim).getTileEntity(position2.toBlockPos());

      if (tile1 == null || tile2 == null) {
        // Could not find a teleporter?
        PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "LinkFail"));
        log.warning("Could not find teleporter when linking:" + "\nTeleporter1 @ "
            + position1.toString() + "\nTeleporter2 @ " + position2.toString());
        linker.setLinked(heldItem, false);
        return false;
      }

      // Create "link"
      tile1.destination = position2;
      tile2.destination = position1;
      PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "LinkSuccess"));
      linker.setLinked(heldItem, false);
      tile1.markDirty();
      tile2.markDirty();
    } else {
      // Inactive state: set active and location.
      linker.setLinkedPosition(heldItem, new DimensionalPosition(pos, player.dimension));
      linker.setLinked(heldItem, true);
      PlayerHelper.addChatMessage(player, loc.getBlockSubText(Names.TELEPORTER, "LinkStart"));
    }

    return true;
  }

  @Override
  public void markDirty() {

    super.markDirty();
    Chunk chunk = worldObj.getChunkFromBlockCoords(pos);
    IBlockState state = worldObj.getBlockState(pos);
    worldObj.markAndNotifyBlock(pos, chunk, state, state, 2);
  }

  public boolean checkAndDrainChaos(EntityPlayer player) {

    if (player == null || player.capabilities.isCreativeMode) {
      return true;
    }

    // Is there enough Chaos to teleport?
    int cost = getRequiredChaos(player);
    int available = getCharge() + ChaosUtil.getTotalChaosAvailable(player);
    if (cost > available) {
      String str = SilentGems.instance.localizationHelper.getBlockSubText(Names.TELEPORTER,
          "NotEnoughChaos");
      str = String.format(str, getCharge(), cost);
      PlayerHelper.addChatMessage(player, str);
      return false;
    }

    // Drain Chaos
    if (cost > chaosStored) {
      cost -= chaosStored;
      chaosStored = 0;
      ChaosUtil.drainChaosFromPlayerAndInventory(player, cost);
    } else {
      chaosStored -= cost;
    }

    return true;
  }

  public int getRequiredChaos(EntityPlayer player) {

    if (destination == null || destination.equals(DimensionalPosition.ZERO)) {
      return 0;
    }

    DimensionalPosition source = new DimensionalPosition(pos, player.dimension);

    if (source.dim != destination.dim) {
      return Config.TELEPORTER_COST_CROSS_DIMENSION;
    }

    double distance = getDistanceBetweenTeleporters(source);
    if (distance < Config.TELEPORTER_FREE_RANGE) {
      return 0;
    }
    return MathHelper.clamp_int((int) (Config.TELEPORTER_COST_PER_BLOCK * distance), 0,
        getMaxCharge());
  }

  public double getDistanceBetweenTeleporters(DimensionalPosition source) {

    if (destination == null || destination.equals(DimensionalPosition.ZERO)) {
      return 0;
    }

    double x = source.x - destination.x;
    double z = source.z - destination.z;
    return Math.sqrt(x * x + z * z);
  }

  public boolean isDestinationSafe(EntityPlayer player) {

    // Check for obstruction at head level. Could use some additional checks maybe...
    if (player == null || destination == null || destination.equals(DimensionalPosition.ZERO)) {
      return true;
    }
    return !player.getServer().worldServerForDimension(destination.dim)
        .isBlockNormalCube(destination.toBlockPos().up(2), false);
  }

  public boolean isDestinationSane(EntityPlayer player) {

    // Checks that the given destination makes sense.
    if (player == null || destination == null || destination.equals(DimensionalPosition.ZERO)) {
      return true;
    }
    return destination.y >= 0 && destination.y < 256;
  }

  public boolean isDestinationAllowedIfDumb(EntityPlayer player) {

    // Checks for a teleporter at the destination if dumb teleports are not allowed.
    if (player == null || Config.TELEPORTER_ALLOW_DUMB || destination == null
        || destination.equals(DimensionalPosition.ZERO)) {
      return true;
    }
    return player.getServer().worldServerForDimension(destination.dim)
        .getBlockState(destination.toBlockPos()).getBlock() instanceof BlockTeleporter;
  }

  public boolean isDestinationSet() {

    return destination != null && !destination.equals(DimensionalPosition.ZERO);
  }

  public boolean teleportEntityToDestination(Entity entity) {

    if (destination == null || destination.equals(DimensionalPosition.ZERO)) {
      return false;
    }

    if (entity instanceof EntityPlayerMP) {
      return TeleportUtil.teleportPlayerTo((EntityPlayerMP) entity, destination);
    } else {
      return TeleportUtil.teleportEntityTo(entity, destination);
    }
  }

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return isAnchor ? 0 : Config.TELEPORTER_MAX_CHARGE;
  }

  @Override
  public int receiveCharge(int maxReceive, boolean simulate) {

    int amount = Math.min(getMaxCharge() - getCharge(), maxReceive);
    if (!simulate) {
      chaosStored += amount;
    }
    return amount;
  }
}
