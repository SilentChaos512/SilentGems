package net.silentchaos512.gems.core.proxy;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.client.key.KeyTracker;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {

        registerRenderersBlocks();
        registerRenderersItems();
        registerRenderersMobs();
        registerRenderersProjectiles();
    }

    private void registerRenderersProjectiles() {

        // TODO Auto-generated method stub

    }

    private void registerRenderersMobs() {

        // TODO Auto-generated method stub

    }

    private void registerRenderersItems() {

        // TODO Auto-generated method stub

    }

    private void registerRenderersBlocks() {

        // TODO Auto-generated method stub

    }

    @Override
    public void registerTileEntities() {

        super.registerTileEntities();
    }

    @Override
    public void registerKeyHandlers() {

        KeyTracker.init();
    }

    @Override
    public void doNEICheck(ItemStack stack) {

//        if (Minecraft.getMinecraft().thePlayer != null) {
//            Iterator modIT = Loader.instance().getModList().iterator();
//            ModContainer modc;
//            while (modIT.hasNext()) {
//                modc = (ModContainer) modIT.next();
//                if ("Not Enough Items".equals(modc.getName().trim())) {
//                    codechicken.nei.api.API.hideItem(new ItemStack(SRegistry.getBlock(Names.FLUFFY_PLANT)));
//                    for (int i = 0; i < EnumGem.all().length; ++i) {
//                        codechicken.nei.api.API.hideItem(new ItemStack(SRegistry.getBlock(Names.GEM_LAMP_LIT), 1, i));
//                    }
//                    return;
//                }
//            }
//        }
    }
}
