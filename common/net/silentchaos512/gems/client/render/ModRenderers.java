package net.silentchaos512.gems.client.render;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosPylon;

/**
 * Created by M4thG33k on 9/13/2015.
 */
public class ModRenderers {

  public static void init() {

    OBJLoader.instance.addDomain(SilentGems.MOD_ID);

    // altar renderers (block and item)
    Item itemAltar = Item.getItemFromBlock(ModBlocks.chaosAltar);
    register(itemAltar, 0, "ChaosAltar");
    ClientRegistry.bindTileEntitySpecialRenderer(TileChaosAltar.class, new RenderChaosAltar());

    // ClientRegistry.bindTileEntitySpecialRenderer(TileChaosAltar.class, new RendererChaosAltar());
    // MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.chaosAltar),
    // new ItemRendererChaosAltar(new RendererChaosAltar(), new TileChaosAltar()));

    // pylon renderers (block and item)
    // Item itemPylon = Item.getItemFromBlock(ModBlocks.chaosPylon);
    // ModelLoader.setCustomModelResourceLocation(itemPylon, 0, new ModelResourceLocation(
    // SilentGems.MOD_ID + ":" + "SilentChaosPylon", "inventory"));
    // ModelLoader.setCustomModelResourceLocation(itemPylon, 1, new ModelResourceLocation(
    // SilentGems.MOD_ID + ":" + "SilentChaosPylon", "inventory"));

    // ClientRegistry.bindTileEntitySpecialRenderer(TileChaosPylon.class, new RendererChaosPylon());
    // MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.chaosPylon),
    // new ItemRendererChaosPylon(new RendererChaosPylon(), new TileChaosPylon()));
  }

  private static void register(Item item, int meta, String name) {

    ResourceLocation resName = new ResourceLocation(Strings.RESOURCE_PREFIX + name);
    ModelBakery.registerItemVariants(item, resName);
    ModelResourceLocation model = new ModelResourceLocation(resName, "inventory");
    ModelLoader.setCustomModelResourceLocation(item, meta, model);
  }
}
