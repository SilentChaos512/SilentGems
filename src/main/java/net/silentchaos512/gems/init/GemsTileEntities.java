package net.silentchaos512.gems.init;

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
import net.silentchaos512.gems.block.altar.AltarTileEntity;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotTileEntity;
import net.silentchaos512.gems.block.flowerpot.PhantomLightTileEntity;
import net.silentchaos512.gems.block.pedestal.PedestalTileEntity;
import net.silentchaos512.gems.block.supercharger.SuperchargerTileEntity;
import net.silentchaos512.gems.block.teleporter.GemTeleporterTileEntity;
import net.silentchaos512.gems.block.tokenenchanter.TokenEnchanterTileEntity;
import net.silentchaos512.gems.block.urn.SoulUrnTileEntity;
import net.silentchaos512.gems.client.render.tile.PedestalRenderer;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;
import java.util.function.Supplier;

public enum GemsTileEntities {
    CHAOS_FLOWER_POT(LuminousFlowerPotTileEntity::new),
    PEDESTAL(PedestalTileEntity::new),
    PHANTOM_LIGHT(PhantomLightTileEntity::new),
    SOUL_URN(SoulUrnTileEntity::new),
    SUPERCHARGER(SuperchargerTileEntity::new),
    TELEPORTER(GemTeleporterTileEntity::new),
    TOKEN_ENCHANTER(TokenEnchanterTileEntity::new),
    TRANSMUTATION_ALTAR(AltarTileEntity::new);

    private final Lazy<TileEntityType<?>> type;

    GemsTileEntities(Supplier<TileEntity> factory) {
        //noinspection ConstantConditions -- null parameter in build
        this.type = Lazy.of(() -> TileEntityType.Builder.create(factory).build(null));
    }

    public TileEntityType<?> type() {
        return type.get();
    }

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        if (!event.getName().equals(ForgeRegistries.TILE_ENTITIES.getRegistryName())) return;

        for (GemsTileEntities tileEnum : values()) {
            register(tileEnum.name().toLowerCase(Locale.ROOT), tileEnum.type());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(PedestalTileEntity.class, new PedestalRenderer());
    }

    private static <T extends TileEntity> void register(String name, TileEntityType<T> type) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        type.setRegistryName(id);
        ForgeRegistries.TILE_ENTITIES.register(type);
    }
}
