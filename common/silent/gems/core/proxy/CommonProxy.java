package silent.gems.core.proxy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.lib.Names;
import silent.gems.tile.TileTeleporter;

public class CommonProxy {

  public CommonProxy() {

  }
  
  public void preInit() {
    
  }
  
  public void init() {
    
    registerTileEntities();
    registerRenderers();
    registerKeyHandlers();
  }
  
  public void postInit() {
    
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
