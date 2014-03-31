package silent.gems.core.handler;

import java.util.Random;

import silent.gems.block.GlowRose;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;


public class GemsEventHandler {

    private Random random = new Random();
    
    @ForgeSubscribe
    public void onUseBonemeal(BonemealEvent event) {
        
        if (event.ID == Block.grass.blockID) {
            if (!event.world.isRemote) {
                // Spawn some Glow Roses?
                int k = random.nextInt(6) - 1;
                int x, y, z, m;
                GlowRose flower = (GlowRose) SRegistry.getBlock(Names.GLOW_ROSE);
                for (int i = 0; i < k; ++i) {
                    x = event.X + random.nextInt(9) - 4;
                    y = event.Y + 1;
                    z = event.Z + random.nextInt(9) - 4;
                    LogHelper.debug(LogHelper.coord(x, y, z));
                    // Get rid of tall grass, it seems to spawn first.
                    if (event.world.getBlockId(x, y, z) == Block.tallGrass.blockID) {
                        event.world.setBlockToAir(x, y, z);
                    }
                    if (event.world.isAirBlock(x, y, z) && flower.canBlockStay(event.world, x, y, z)) {
                        m = random.nextInt(EnumGem.all().length);
                        event.world.setBlock(x, y, z, flower.blockID, m, 2);
                    }
                }
            }
        }
    }
}
