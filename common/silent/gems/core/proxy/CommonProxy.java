package silent.gems.core.proxy;

import silent.gems.core.tick.ItemTickHandler;
import silent.gems.tileentity.TileEntityTeleporter;
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
    }

    public void registerTileEntities() {

        GameRegistry.registerTileEntity(TileEntityTeleporter.class, "tileEntity.silentgems:Teleporter");
    }

    public void registerKeyHandlers() {

    }
}
