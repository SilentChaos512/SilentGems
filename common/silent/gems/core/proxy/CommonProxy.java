package silent.gems.core.proxy;

import silent.gems.core.handler.PlayerTickHandler;
import silent.gems.core.tick.ItemTickHandler;
import silent.gems.lib.Names;
import silent.gems.tileentity.TileEntityTeleporter;
import silent.gems.tileentity.TileShinyCrafter;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

    public CommonProxy() {

    }

    public void registerRenderers() {

    }

    public void registerTickHandlers() {

        TickRegistry.registerScheduledTickHandler(new ItemTickHandler(), Side.SERVER);
        TickRegistry.registerTickHandler(new PlayerTickHandler(), Side.SERVER);
    }

    public void registerTileEntities() {

        String prefix = "tile.silentgems:";
        GameRegistry.registerTileEntity(TileEntityTeleporter.class, prefix + Names.TELEPORTER);
        GameRegistry.registerTileEntity(TileShinyCrafter.class, prefix + Names.SHINY_CRAFTER);
    }

    public void registerKeyHandlers() {

    }
}
