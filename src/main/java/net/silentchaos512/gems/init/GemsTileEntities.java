package net.silentchaos512.gems.init;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.silentchaos512.gems.block.altar.AltarTileEntity;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotTileEntity;
import net.silentchaos512.gems.block.flowerpot.PhantomLightTileEntity;
import net.silentchaos512.gems.block.pedestal.PedestalBlock;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.gems.block.purifier.PurifierTileEntity;
import net.silentchaos512.gems.block.supercharger.SuperchargerTileEntity;
import net.silentchaos512.gems.block.teleporter.GemTeleporterTileEntity;
import net.silentchaos512.gems.block.teleporter.TeleporterBaseBlock;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterTileEntity;
import net.silentchaos512.gems.block.urn.SoulUrnTileEntity;
import net.silentchaos512.gems.client.render.tile.PedestalRenderer;
import net.silentchaos512.gems.client.render.tile.SoulUrnRenderer;
import net.silentchaos512.lib.block.IBlockProvider;

import java.util.Collection;
import java.util.function.Supplier;

public final class GemsTileEntities {
    public static final RegistryObject<TileEntityType<LuminousFlowerPotTileEntity>> CHAOS_FLOWER_POT = register("luminous_flower_pot",
            LuminousFlowerPotTileEntity::new,
            GemsBlocks.LUMINOUS_FLOWER_POT);
    public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL = register("pedestal",
            PedestalTileEntity::new,
            () -> Registration.getBlocks(PedestalBlock.class));
    public static final RegistryObject<TileEntityType<PhantomLightTileEntity>> PHANTOM_LIGHT = register("phantom_light",
            PhantomLightTileEntity::new,
            GemsBlocks.PHANTOM_LIGHT);
    public static final RegistryObject<TileEntityType<PurifierTileEntity>> PURIFIER = register("purifier",
            PurifierTileEntity::new,
            GemsBlocks.PURIFIER);
    public static final RegistryObject<TileEntityType<SoulUrnTileEntity>> SOUL_URN = register("soul_urn",
            SoulUrnTileEntity::new,
            GemsBlocks.SOUL_URN);
    public static final RegistryObject<TileEntityType<SuperchargerTileEntity>> SUPERCHARGER = register("supercharger",
            SuperchargerTileEntity::new,
            GemsBlocks.SUPERCHARGER);
    public static final RegistryObject<TileEntityType<GemTeleporterTileEntity>> TELEPORTER = register("teleporter",
            GemTeleporterTileEntity::new,
            () -> Registration.getBlocks(TeleporterBaseBlock.class));
    public static final RegistryObject<TileEntityType<TokenEnchanterTileEntity>> TOKEN_ENCHANTER = register("token_enchanter",
            TokenEnchanterTileEntity::new,
            GemsBlocks.TOKEN_ENCHANTER);
    public static final RegistryObject<TileEntityType<AltarTileEntity>> TRANSMUTATION_ALTAR = register("transmutation_altar",
            AltarTileEntity::new,
            GemsBlocks.TRANSMUTATION_ALTAR);

    private GemsTileEntities() {throw new IllegalAccessError("Utility class");}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(PEDESTAL.get(), PedestalRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SOUL_URN.get(), SoulUrnRenderer::new);
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factoryIn, IBlockProvider validBlock) {
        return register(name, factoryIn, () -> ImmutableList.of(validBlock.asBlock()));
    }

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factoryIn, Supplier<Collection<? extends Block>> validBlocksSupplier) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            Block[] validBlocks = validBlocksSupplier.get().toArray(new Block[0]);
            //noinspection ConstantConditions -- null in build
            return TileEntityType.Builder.create(factoryIn, validBlocks).build(null);
        });
    }
}
