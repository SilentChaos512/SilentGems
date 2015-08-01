package net.silentchaos512.gems.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.tile.TileChaosAltar;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandlerSilentGems implements IGuiHandler {

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

    TileEntity tile = world.getTileEntity(x, y, z);

    switch (ID) {
      case 0:
        if (tile instanceof TileChaosAltar) {
          TileChaosAltar tileAltar = (TileChaosAltar) tile;
          return new ContainerChaosAltar(player.inventory, tileAltar);
        }
        return null;
      default:
        LogHelper.warning("No GUI with ID " + ID + "!");
        return null;
    }
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

    TileEntity tile = world.getTileEntity(x, y, z);

    switch (ID) {
      case 0:
        if (tile instanceof TileChaosAltar) {
          TileChaosAltar tileAltar = (TileChaosAltar) tile;
          return new GuiChaosAltar(player.inventory, tileAltar);
        }
        return null;
      default:
        LogHelper.warning("No GUI with ID " + ID + "!");
        return null;
    }
  }
}
