package net.silentchaos512.gems.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.inventory.ContainerBurnerPylon;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;

public class GuiHandlerSilentGems implements IGuiHandler {
  
  public static final int ID_ALTAR = 0;
  public static final int ID_BURNER_PYLON = 1;
  public static final int ID_MANUAL = 2;

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

    TileEntity tile = world.getTileEntity(x, y, z);

    switch (ID) {
      case ID_ALTAR:
        if (tile instanceof TileChaosAltar) {
          TileChaosAltar tileAltar = (TileChaosAltar) tile;
          return new ContainerChaosAltar(player.inventory, tileAltar);
        }
        return null;
      case ID_BURNER_PYLON:
        if (tile instanceof TileChaosPylon) {
          TileChaosPylon tilePylon = (TileChaosPylon) tile;
          return new ContainerBurnerPylon(player.inventory, tilePylon);
        }
        return null;
      case ID_MANUAL:
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
      case ID_ALTAR:
        if (tile instanceof TileChaosAltar) {
          TileChaosAltar tileAltar = (TileChaosAltar) tile;
          return new GuiChaosAltar(player.inventory, tileAltar);
        }
        return null;
      case ID_BURNER_PYLON:
        if (tile instanceof TileChaosPylon) {
          TileChaosPylon tilePylon = (TileChaosPylon) tile;
          return new GuiBurnerPylon(player.inventory, tilePylon);
        }
        return null;
      case ID_MANUAL:
        return GuiGemsManual.currentOpenManual;
      default:
        LogHelper.warning("No GUI with ID " + ID + "!");
        return null;
    }
  }
}
