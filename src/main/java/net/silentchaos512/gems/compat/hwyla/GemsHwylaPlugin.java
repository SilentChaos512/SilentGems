package net.silentchaos512.gems.compat.hwyla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraftforge.common.MinecraftForge;
import net.silentchaos512.gems.SilentGems;

@WailaPlugin
public class GemsHwylaPlugin implements IWailaPlugin {
    @Override
    public void register(IRegistrar registrar) {
        SilentGems.LOGGER.info("Register HWYLA plugin");
        MinecraftForge.EVENT_BUS.register(new GemsHwylaEvents());
    }
}
