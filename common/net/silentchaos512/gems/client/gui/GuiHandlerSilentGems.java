package net.silentchaos512.gems.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.inventory.ContainerBurnerPylon;
import net.silentchaos512.gems.inventory.ContainerChaosAltar;
import net.silentchaos512.gems.inventory.ContainerMaterialGrader;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.gems.tile.TileMaterialGrader;

public class GuiHandlerSilentGems implements IGuiHandler {

  public static final int ID_ALTAR = 0;
  public static final int ID_BURNER_PYLON = 1;
  public static final int ID_MATERIAL_GRADER = 2;

  @Override
  public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

    switch (ID) {
      case ID_ALTAR:
        if (tile instanceof TileChaosAltar) {
          TileChaosAltar tileAltar = (TileChaosAltar) tile;
          return new ContainerChaosAltar(player.inventory, tileAltar);
        }
        return null;
      case ID_BURNER_PYLON:
        if (tile instanceof TileChaosPylon) {
          return new ContainerBurnerPylon(player.inventory, (TileChaosPylon) tile);
        }
        return null;
      case ID_MATERIAL_GRADER:
        if (tile instanceof TileMaterialGrader) {
          return new ContainerMaterialGrader(player.inventory, (TileMaterialGrader) tile);
        }
        return null;
      default:
        SilentGems.instance.logHelper.warning("No GUI with ID " + ID + "!");
        return null;
    }
  }

  @Override
  public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

    TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

    switch (ID) {
      case ID_ALTAR:
        if (tile instanceof TileChaosAltar) {
          TileChaosAltar tileAltar = (TileChaosAltar) tile;
          return new GuiChaosAltar(player.inventory, tileAltar);
        }
        return null;
      case ID_BURNER_PYLON:
        if (tile instanceof TileChaosPylon) {
          return new GuiBurnerPylon(player.inventory, (TileChaosPylon) tile);
        }
        return null;
      case ID_MATERIAL_GRADER:
        if (tile instanceof TileMaterialGrader) {
          return new GuiMaterialGrader(player.inventory, (TileMaterialGrader) tile);
        }
        return null;
      default:
        SilentGems.instance.logHelper.warning("No GUI with ID " + ID + "!");
        return null;
    }
  }
}
