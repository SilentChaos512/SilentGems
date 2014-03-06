package silent.gems.core.proxy;

import silent.gems.client.renderer.item.RenderSigil;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import net.minecraftforge.client.MinecraftForgeClient;

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
//        TickRegistry.registerTickHandler(new PlayerClientTickHandler(), Side.CLIENT);
//        TickRegistry.registerTickHandler(new RenderTickHandler(), Side.CLIENT);
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
