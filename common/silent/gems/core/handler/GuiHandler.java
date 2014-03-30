package silent.gems.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import silent.gems.client.gui.inventory.GuiShinyCrafter;
import silent.gems.core.util.LogHelper;
import silent.gems.inventory.ContainerShinyCrafter;
import silent.gems.lib.GuiIds;
import silent.gems.tileentity.TileShinyCrafter;
import cpw.mods.fml.common.network.IGuiHandler;


public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        //LogHelper.debug(ID);
        if (ID == GuiIds.SHINY_CRAFTER) {
            TileShinyCrafter tileShinyCrafter = (TileShinyCrafter) world.getBlockTileEntity(x, y, z);
            return new ContainerShinyCrafter(player.inventory, tileShinyCrafter);
        }
        
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        //LogHelper.debug(ID);
        if (ID == GuiIds.SHINY_CRAFTER) {
            TileShinyCrafter tileShinyCrafter = (TileShinyCrafter) world.getBlockTileEntity(x, y, z);
            return new GuiShinyCrafter(player.inventory, tileShinyCrafter);
        }
        
        return null;
    }

}
