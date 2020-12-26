package net.silentchaos512.gems.setup;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public final class GemsBlocks {
    static {
        Gems.registerBlocks();
    }

    private GemsBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(FMLClientSetupEvent event) {
//        for (Gems gem : Gems.values()) {
//            RenderTypeLookup.setRenderLayer(gem.getGlass(), RenderType.getTranslucent());
//            RenderTypeLookup.setRenderLayer(gem.getGlowrose(), RenderType.getCutout());
//            RenderTypeLookup.setRenderLayer(gem.getPottedGlowrose(), RenderType.getCutout());
//        }
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

    public static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(GemsBase.ITEM_GROUP));
    }
}
