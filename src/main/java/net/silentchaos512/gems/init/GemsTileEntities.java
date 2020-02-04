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

import java.util.function.Supplier;

public final class GemsTileEntities {
    public static TileEntityType<LuminousFlowerPotTileEntity> CHAOS_FLOWER_POT;
    public static TileEntityType<PedestalTileEntity> PEDESTAL;
    public static TileEntityType<PhantomLightTileEntity> PHANTOM_LIGHT;
    public static TileEntityType<PurifierTileEntity> PURIFIER;
    public static TileEntityType<SoulUrnTileEntity> SOUL_URN;
    public static TileEntityType<SuperchargerTileEntity> SUPERCHARGER;
    public static TileEntityType<GemTeleporterTileEntity> TELEPORTER;
    public static TileEntityType<TokenEnchanterTileEntity> TOKEN_ENCHANTER;
    public static TileEntityType<AltarTileEntity> TRANSMUTATION_ALTAR;

    private GemsTileEntities() {throw new IllegalAccessError("Utility class");}

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        CHAOS_FLOWER_POT = register("luminous_flower_pot", LuminousFlowerPotTileEntity::new, LuminousFlowerPotBlock.INSTANCE.get());
        PEDESTAL = register("pedestal", PedestalTileEntity::new, GemsBlocks.pedestals.toArray(new Block[0]));
        PHANTOM_LIGHT = register("phantom_light", PhantomLightTileEntity::new, PhantomLightBlock.INSTANCE.get());
        PURIFIER = register("purifier", PurifierTileEntity::new, PurifierBlock.INSTANCE.get());
        SOUL_URN = register("soul_urn", SoulUrnTileEntity::new, SoulUrnBlock.INSTANCE.get());
        SUPERCHARGER = register("supercharger", SuperchargerTileEntity::new, SuperchargerBlock.INSTANCE.get());
        TELEPORTER = register("teleporter", GemTeleporterTileEntity::new, GemsBlocks.teleporters.toArray(new Block[0]));
        TOKEN_ENCHANTER = register("token_enchanter", TokenEnchanterTileEntity::new, TokenEnchanterBlock.INSTANCE.get());
        TRANSMUTATION_ALTAR = register("transmutation_altar", AltarTileEntity::new, AltarBlock.INSTANCE.get());
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(PEDESTAL, PedestalRenderer::new);
        ClientRegistry.bindTileEntityRenderer(SOUL_URN, SoulUrnRenderer::new);
    }

    private static <T extends TileEntity> TileEntityType<T> register(String name, Supplier<T> factoryIn, Block... validBlocks) {
        //noinspection ConstantConditions -- null parameter in builder
        TileEntityType<T> type = TileEntityType.Builder.create(factoryIn, validBlocks).build(null);
        ResourceLocation id = SilentGems.getId(name);
        type.setRegistryName(id);
        ForgeRegistries.TILE_ENTITIES.register(type);
        return type;
    }
}
