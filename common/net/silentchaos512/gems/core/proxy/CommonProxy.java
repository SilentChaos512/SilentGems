package net.silentchaos512.gems.core.proxy;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy {

    public CommonProxy() {

    }

    public void registerRenderers() {

    }

    public void registerTileEntities() {

        String prefix = "tile.silentgems:";
        GameRegistry.registerTileEntity(TileTeleporter.class, prefix + Names.TELEPORTER);
    }

    public void registerKeyHandlers() {

    }

    public void doNEICheck(ItemStack stack) {

    }
}