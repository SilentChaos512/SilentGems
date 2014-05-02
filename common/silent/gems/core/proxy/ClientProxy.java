package silent.gems.core.proxy;

import silent.gems.core.handler.ClientTickHandler;
import silent.gems.core.handler.PlayerTickHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerRenderers() {

        registerRenderersBlocks();
        registerRenderersItems();
        registerRenderersMobs();
        registerRenderersProjectiles();
    }

    private void registerRenderersBlocks() {

//        RenderIds.markerRender = RenderingRegistry.getNextAvailableRenderId();
//        RenderIds.testRender = RenderingRegistry.getNextAvailableRenderId();
    }

    private void registerRenderersItems() {

//        MinecraftForgeClient.registerItemRenderer(SRegistry.getItem(Names.SIGIL).itemID, new RenderSigil());
//        MinecraftForgeClient.registerItemRenderer(SARegistry.getItem(Names.GEM_ITEM).itemID, new RenderGem());
//        MinecraftForgeClient.registerItemRenderer(SARegistry.getItem("Pickaxe Ruby").itemID, new RenderPickaxe());
//        MinecraftForgeClient.registerItemRenderer(ModBlocks.blockTest.blockID, new ItemTestRenderer());
    }

    private void registerRenderersMobs() {

//        RenderingRegistry.registerEntityRenderingHandler(EntityGrumbling.class, new RenderGrumbling(new ModelGrumbling(), 0.5F));
//        RenderingRegistry.registerEntityRenderingHandler(CrimsonCreeper.class, new RenderCrimsonCreeper());
    }

    private void registerRenderersProjectiles() {

//        RenderingRegistry.registerEntityRenderingHandler(EntityProjectileMagic.class, new RenderProjectileMagic());
//        RenderingRegistry.registerEntityRenderingHandler(EntityMeteor.class, new RenderFireball(0));
    }

    @Override
    public void registerTickHandlers() {

        super.registerTickHandlers();
        TickRegistry.registerTickHandler(new PlayerTickHandler(), Side.CLIENT);
        TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
    }

    @Override
    public void registerTileEntities() {

        super.registerTileEntities();

//        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMarker.class, new TileEntityMarkerRenderer());
    }

    @Override
    public void registerKeyHandlers() {

//        KeyHelper.init();
    }
}
