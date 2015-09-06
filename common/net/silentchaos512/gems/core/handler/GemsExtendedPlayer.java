package net.silentchaos512.gems.core.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.silentchaos512.gems.SilentGems;

public class GemsExtendedPlayer implements IExtendedEntityProperties {

  public static final String PROPERTY_NAME = SilentGems.MOD_ID + "ExtendedPlayer";
  public static final int FLIGHT_TIME_AMOUNT = 40;

  private final EntityPlayer player;

  private int flightTime;

  public GemsExtendedPlayer(EntityPlayer player) {

    this.player = player;
    flightTime = 0;
  }

  public static final void register(EntityPlayer player) {

    player.registerExtendedProperties(PROPERTY_NAME, new GemsExtendedPlayer(player));
  }

  public static final GemsExtendedPlayer get(EntityPlayer player) {

    return (GemsExtendedPlayer) player.getExtendedProperties(PROPERTY_NAME);
  }

  @Override
  public void saveNBTData(NBTTagCompound compound) {

    NBTTagCompound properties = new NBTTagCompound();

    properties.setByte("FlightTime", (byte) flightTime);

    compound.setTag(PROPERTY_NAME, properties);
  }

  @Override
  public void loadNBTData(NBTTagCompound compound) {

    NBTTagCompound properties = (NBTTagCompound) compound.getTag(PROPERTY_NAME);

    if (properties != null) {
      flightTime = (int) properties.getByte("FlightTime");
    }
  }

  @Override
  public void init(Entity entity, World world) {

  }

  public void refreshFlightTime() {

    flightTime = FLIGHT_TIME_AMOUNT;
  }

  public boolean tickFlightTime() {

    if (flightTime > 0) {
      --flightTime;
      return flightTime == 0;
    }
    return false;
  }

  public int getFlightTime() {

    return flightTime;
  }

  public void setFlightTime(int value) {

    flightTime = value;
  }
}
