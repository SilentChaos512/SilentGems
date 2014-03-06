package silent.gems.core.proxy;

import silent.gems.core.tick.ItemTickHandler;
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

    }

    public void registerKeyHandlers() {

    }
}
