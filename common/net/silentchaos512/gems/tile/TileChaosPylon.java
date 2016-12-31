package net.silentchaos512.gems.tile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.energy.IChaosAccepter;
import net.silentchaos512.gems.api.energy.IChaosProvider;
import net.silentchaos512.gems.block.BlockChaosPylon;
import net.silentchaos512.gems.entity.packet.EntityPacketChaos;
import net.silentchaos512.gems.lib.EnumPylonType;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ChaosUtil;
import net.silentchaos512.lib.tile.TileInventorySL;

public class TileChaosPylon extends TileInventorySL implements ITickable, IChaosProvider {

  public static final double SEARCH_RADIUS_PLAYER_SQUARED = 16 * 16;
  public static final int SEARCH_RADIUS_BLOCK = 6;
  public static final int SEND_CHAOS_DELAY = 100;
  public static final int MAX_CHAOS_STORED = 100000;
  public static final int MAX_CHAOS_TRANSFERED = 10000;
  public static final int CHAOS_GENERATION_RATE_PASSIVE = 10;
  public static final int CHAOS_GENERATION_RATE_BURNER = 100;

  protected int chaosStored;
  protected int burnTimeRemaining;
  protected int currentItemBurnTime;

  protected EnumPylonType pylonType = EnumPylonType.NONE;

  public EnumPylonType getPylonType() {

    return pylonType;
  }

  public void setPylonType(EnumPylonType type) {

    pylonType = type;
  }

  @Override
  public void update() {

    if (!world.isRemote) {
      // Generate chaos.
      chaosStored = Math.min(getCharge() + getEnergyProduced(), getMaxCharge());

      // Burner pylon fuel
      if (pylonType == EnumPylonType.BURNER) {
        burnFuel();
      }

      // Transfer energy to blocks, then players.
      if (world.getTotalWorldTime() % SEND_CHAOS_DELAY == 0) {
        List<IChaosAccepter> accepters = ChaosUtil.getNearbyAccepters(world, pos,
            SEARCH_RADIUS_BLOCK, SEARCH_RADIUS_BLOCK);
        List<EntityPlayer> players = world.getEntities(EntityPlayer.class,
            e -> e.getDistanceSq(pos) < SEARCH_RADIUS_PLAYER_SQUARED);

        if (!players.isEmpty() || !accepters.isEmpty()) {
          final int amountForEach = Math.min(MAX_CHAOS_TRANSFERED,
              getCharge() / (accepters.size() + players.size()));

          sendEnergyToAccepters(accepters, amountForEach);
          sendEnergyToPlayers(players, amountForEach);
        }
      }
    }
  }

  protected void sendEnergyToAccepters(List<IChaosAccepter> list, int amountForEach) {

    if (getCharge() <= 0)
      return;

    int amount;

    for (IChaosAccepter accepter : list) {
      if (getCharge() <= 0) {
        return;
      }

      amount = Math.min(getCharge(), amountForEach);
      amount = accepter.receiveCharge(amount, true);
      if (amount > 0) {
        extractEnergy(amount, false);
        EntityPacketChaos packet = ChaosUtil.spawnPacketToBlock(world, pos,
            ((TileEntity) accepter).getPos(), amount);
        // Set packets velocity to something less random.
        if (packet != null) {
          Vec3d vel = new Vec3d(0.1, 0.25, 0.0);
          vel = vel.rotateYaw(2 * (float) Math.PI * SilentGems.random.nextFloat());
          packet.setVelocity(vel);
        }
      }
    }
  }

  protected void sendEnergyToPlayers(List<EntityPlayer> list, int amountForEach) {

    if (getCharge() <= 0) {
      return;
    }

    int amount;

    for (EntityPlayer player : list) {
      if (getCharge() <= 0) {
        return;
      }

      amount = Math.min(getCharge(), amountForEach);
      int amountPlayerCanAccept = ChaosUtil.getAmountPlayerCanAccept(player, amount);
      if (amountPlayerCanAccept > 0) {
        amount = Math.min(amount, amountPlayerCanAccept);
        EntityPacketChaos packet = ChaosUtil.spawnPacketToEntity(world, pos, player, amount);
        // Set packets velocity to something less random.
        if (packet != null) {
          Vec3d vel = new Vec3d(0.1, 0.25, 0.0);
          vel = vel.rotateYaw(2 * (float) Math.PI * SilentGems.random.nextFloat());
          packet.setVelocity(vel);
        }
        extractEnergy(amount, false);
      }
    }
  }

  private void burnFuel() {

    boolean markForUpdate = false;

    if (burnTimeRemaining > 0) {
      --burnTimeRemaining;
      markForUpdate = true;
    }

    if (burnTimeRemaining <= 0 && getCharge() < getMaxCharge()) {
      ItemStack stack = getStackInSlot(0);
      if (!stack.isEmpty()) {
        int fuelBurnTime = TileEntityFurnace.getItemBurnTime(stack);
        if (fuelBurnTime > 0) {
          currentItemBurnTime = burnTimeRemaining = fuelBurnTime;
          stack.shrink(1);
          if (stack.isEmpty())
            setInventorySlotContents(0, stack.getItem().getContainerItem(stack));
          else
            setInventorySlotContents(0, stack);
          markForUpdate = true;
        }
      }
    }

    if (markForUpdate) {
      markDirty();
      // worldObj.markBlockForUpdate(pos);
    }
  }

  public boolean isBurningFuel() {

    return burnTimeRemaining > 0;
  }

  public int getBurnTimeRemaining() {

    return burnTimeRemaining;
  }

  public int getCurrentItemBurnTime() {

    return currentItemBurnTime;
  }

  public int getBurnTimeRemainingScaled(int k) {

    if (currentItemBurnTime == 0) {
      currentItemBurnTime = 200;
    }

    return burnTimeRemaining * k / currentItemBurnTime;
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {

    return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {

    return writeToNBT(super.getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager network, SPacketUpdateTileEntity packet) {

    readFromNBT(packet.getNbtCompound());
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {

    // Load pylon type before loading inventory, because this determines inventory size!
    pylonType = EnumPylonType.getByMeta(tags.getInteger("MyPylonType"));

    super.readFromNBT(tags);

    chaosStored = tags.getInteger("Energy");
    burnTimeRemaining = tags.getInteger("BurnTime");
    if (getSizeInventory() > 0)
      currentItemBurnTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(0));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {

    super.writeToNBT(tags);
    tags.setInteger("Energy", chaosStored);
    tags.setInteger("BurnTime", burnTimeRemaining);

    // save pylon type
    tags.setInteger("MyPylonType", pylonType.getMeta());
    return tags;
  }

  @Override
  public String getName() {

    return Names.CHAOS_PYLON;
  }

  public int getEnergyProduced() {

    if (pylonType == EnumPylonType.BURNER && burnTimeRemaining <= 0) {
      return 0;
    }
    return pylonType.getChaosGenerationRate();

  }

  @Override
  public int getCharge() {

    return chaosStored;
  }

  @Override
  public int getMaxCharge() {

    return MAX_CHAOS_STORED;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {

    int amount = Math.min(getCharge(), maxExtract);
    if (!simulate) {
      chaosStored -= amount;
    }
    return amount;
  }

  @Override
  public int getSizeInventory() {

    return getPylonType() == EnumPylonType.BURNER ? 1 : 0;
  }
}
