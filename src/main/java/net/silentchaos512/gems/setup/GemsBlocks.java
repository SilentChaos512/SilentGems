package net.silentchaos512.gems.setup;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.OreBlockSG;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public final class GemsBlocks {
    static {
        Gems.registerBlocks();
    }

    public static final BlockRegistryObject<Block> SILVER_ORE = register("silver_ore", () ->
            new OreBlockSG(null, 2, AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3)) {
                @Override
                public int getExpRandom() {
                    return 0;
                }
            });
    public static final BlockRegistryObject<Block> SILVER_BLOCK = register("silver_block", () ->
            new Block(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(4, 30).sound(SoundType.METAL)));

    private GemsBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    static void registerRenderTypes(FMLClientSetupEvent event) {
        for (Gems gem : Gems.values()) {
            RenderTypeLookup.setRenderLayer(gem.getGlass(), RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(gem.getGlowrose(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(gem.getPottedGlowrose(), RenderType.getCutout());
        }
//        RenderTypeLookup.setRenderLayer(FLUFFY_PUFF_PLANT.get(), RenderType.getCutout());
//        RenderTypeLookup.setRenderLayer(WILD_FLUFFY_PUFF_PLANT.get(), RenderType.getCutout());
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, GemsBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        BlockRegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

//    private static BlockRegistryObject<FluffyBlock> registerFluffyBlock(DyeColor color) {
//        return register(color.getTranslationKey() + "_fluffy_block", () -> new FluffyBlock(color));
//    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(GemsBase.ITEM_GROUP));
    }
}
