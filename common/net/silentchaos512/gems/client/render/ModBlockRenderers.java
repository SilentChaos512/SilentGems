package net.silentchaos512.gems.client.render;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.BlockChaosPylon;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.client.render.tile.RenderTileChaosAltar;
import net.silentchaos512.gems.client.render.tile.RenderTileChaosNode;
import net.silentchaos512.gems.client.render.tile.RenderTileChaosPylon;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosAltar;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.gems.tile.TileChaosPylon;
import net.silentchaos512.lib.registry.SRegistry;

public class ModBlockRenderers {

  public static void init(SRegistry reg) {

    // Node
//    ClientRegistry.bindTileEntitySpecialRenderer(TileChaosNode.class, new RenderTileChaosNode());
    reg.registerTileEntitySpecialRenderer(TileChaosNode.class, new RenderTileChaosNode());

    // Altar
    Item itemAltar = Item.getItemFromBlock(ModBlocks.chaosAltar);
    register(itemAltar, 0, "ChaosAltar");
//    ClientRegistry.bindTileEntitySpecialRenderer(TileChaosAltar.class, new RenderTileChaosAltar());
    reg.registerTileEntitySpecialRenderer(TileChaosAltar.class, new RenderTileChaosAltar());

    // Pylons
//    ClientRegistry.bindTileEntitySpecialRenderer(TileChaosPylon.class, new RenderTileChaosPylon());
    //Item itemPylon = Item.getItemFromBlock(ModBlocks.chaosPylon);
    //register(itemPylon,0,"ChaosPylon");
    for (BlockChaosPylon.VariantType pylonType : BlockChaosPylon.VariantType.values())
    {
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.chaosPylon),pylonType.ordinal(),new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + Names.CHAOS_PYLON,"variant="+pylonType.getName()));
      SilentGems.instance.logHelper.info("Registered ChaosPylon with meta " + pylonType.ordinal() + " to resource location: " + SilentGems.RESOURCE_PREFIX + Names.CHAOS_PYLON + " and variants: variant=" + pylonType.getName() );
    }
//    for (int i=0;i<2;i++)
//    {
//      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.chaosPylon),i,new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + Names.CHAOS_PYLON,"variant="+ BlockChaosPylon.VariantType.values()[i].getName()));
//    }
    reg.registerTileEntitySpecialRenderer(TileChaosPylon.class, new RenderTileChaosPylon());
  }

  private static void register(Item item, int meta, String name) {

    ResourceLocation resName = new ResourceLocation(SilentGems.RESOURCE_PREFIX + name);
    ModelBakery.registerItemVariants(item, resName);
    ModelResourceLocation model = new ModelResourceLocation(resName, "inventory");
    ModelLoader.setCustomModelResourceLocation(item, meta, model);
  }
}
