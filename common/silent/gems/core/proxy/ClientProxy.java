package silent.gems.core.proxy;

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

		// TODO
    }
}
