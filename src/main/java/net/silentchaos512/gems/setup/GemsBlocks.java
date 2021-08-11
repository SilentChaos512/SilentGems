package net.silentchaos512.gems.setup;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
            getSilverOre(BlockBehaviour.Properties.of(Material.STONE).strength(3)));

    public static final BlockRegistryObject<Block> DEEPSLATE_SILVER_ORE = register("deepslate_silver_ore", () ->
            getSilverOre(BlockBehaviour.Properties.copy(SILVER_ORE.get()).strength(4.5f, 3f).sound(SoundType.DEEPSLATE)));

    public static final BlockRegistryObject<Block> SILVER_BLOCK = register("silver_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.METAL).strength(4, 30).sound(SoundType.METAL)));

    private GemsBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    static void registerRenderTypes(FMLClientSetupEvent event) {
        for (Gems gem : Gems.values()) {
            ItemBlockRenderTypes.setRenderLayer(gem.getGlass(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(gem.getGlowrose(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(gem.getPottedGlowrose(), RenderType.cutout());
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
        return () -> new BlockItem(block.get(), new Item.Properties().tab(GemsBase.ITEM_GROUP));
    }

    private static OreBlockSG getSilverOre(final BlockBehaviour.Properties properties) {
        return new OreBlockSG(GemsItems.RAW_SILVER, 2, properties) {
            @Override
            public int getExpRandom() {
                return 0;
            }
        };
    }
}
