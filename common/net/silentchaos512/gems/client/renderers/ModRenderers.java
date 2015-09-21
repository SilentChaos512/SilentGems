package net.silentchaos512.gems.client.renderers;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;

/**
 * Created by M4thG33k on 9/13/2015.
 */
public class ModRenderers {

    public static void init()
    {
        //altar renderers (block and item)
        ClientRegistry.bindTileEntitySpecialRenderer(TileChaosAltar.class,new RendererChaosAltar());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.chaosAltar), new ItemRendererChaosAltar(new RendererChaosAltar(),new TileChaosAltar()));

        //pylon renderers (block and item)
        ClientRegistry.bindTileEntitySpecialRenderer(TileChaosPylon.class,new RendererChaosPylon());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.chaosPylon), new ItemRendererChaosPylon(new RendererChaosPylon(), new TileChaosPylon()));
    }
}
