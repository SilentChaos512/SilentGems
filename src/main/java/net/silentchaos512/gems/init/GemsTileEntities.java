package net.silentchaos512.gems.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.altar.AltarBlock;
import net.silentchaos512.gems.block.altar.AltarTileEntity;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotBlock;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotTileEntity;
import net.silentchaos512.gems.block.flowerpot.PhantomLightBlock;
import net.silentchaos512.gems.block.flowerpot.PhantomLightTileEntity;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.gems.block.purifier.PurifierBlock;
import net.silentchaos512.gems.block.purifier.PurifierTileEntity;
import net.silentchaos512.gems.block.supercharger.SuperchargerBlock;
import net.silentchaos512.gems.block.supercharger.SuperchargerTileEntity;
import net.silentchaos512.gems.block.teleporter.GemTeleporterTileEntity;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterBlock;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterTileEntity;
import net.silentchaos512.gems.block.urn.SoulUrnBlock;
import net.silentchaos512.gems.block.urn.SoulUrnTileEntity;
import net.silentchaos512.gems.client.render.tile.PedestalRenderer;
import net.silentchaos512.gems.client.render.tile.SoulUrnRenderer;
import net.silentchaos512.utils.Lazy;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public enum GemsTileEntities {
    CHAOS_FLOWER_POT(LuminousFlowerPotTileEntity::new, LuminousFlowerPotBlock.INSTANCE::get),
    PEDESTAL(PedestalTileEntity::new, GemsBlocks.pedestals),
    PHANTOM_LIGHT(PhantomLightTileEntity::new, PhantomLightBlock.INSTANCE::get),
    PURIFIER(PurifierTileEntity::new, PurifierBlock.INSTANCE::get),
    SOUL_URN(SoulUrnTileEntity::new, SoulUrnBlock.INSTANCE::get),
    SUPERCHARGER(SuperchargerTileEntity::new, SuperchargerBlock.INSTANCE::get),
    TELEPORTER(GemTeleporterTileEntity::new, GemsBlocks.teleporters),
    TOKEN_ENCHANTER(TokenEnchanterTileEntity::new, TokenEnchanterBlock.INSTANCE::get),
    TRANSMUTATION_ALTAR(AltarTileEntity::new, AltarBlock.INSTANCE::get);

    private final Lazy<TileEntityType<?>> type;

    GemsTileEntities(Supplier<TileEntity> factory, Supplier<? extends Block> block) {
        //noinspection ConstantConditions -- null parameter in build
        this.type = Lazy.of(() -> TileEntityType.Builder.create(factory, block.get()).build(null));
    }

    GemsTileEntities(Supplier<TileEntity> factory, List<? extends Block> blocks) {
        //noinspection ConstantConditions -- null parameter in build
        this.type = Lazy.of(() -> TileEntityType.Builder.create(factory, blocks.toArray(new Block[0])).build(null));
    }

    public TileEntityType<?> type() {
        return type.get();
    }

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        for (GemsTileEntities tileEnum : values()) {
            register(tileEnum.name().toLowerCase(Locale.ROOT), tileEnum.type());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(PedestalTileEntity.class, new PedestalRenderer());
        ClientRegistry.bindTileEntityRenderer(SoulUrnTileEntity.class, new SoulUrnRenderer());
    }

    private static <T extends TileEntity> void register(String name, TileEntityType<T> type) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        type.setRegistryName(id);
        ForgeRegistries.TILE_ENTITIES.register(type);
    }
}
