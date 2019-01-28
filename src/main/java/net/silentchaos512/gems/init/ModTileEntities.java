package net.silentchaos512.gems.init;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.flowerpot.LuminousFlowerPotTileEntity;
import net.silentchaos512.gems.block.flowerpot.PhantomLightTileEntity;
import net.silentchaos512.gems.block.supercharger.TileSupercharger;
import net.silentchaos512.gems.block.urn.TileSoulUrn;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModTileEntities {
    CHAOS_FLOWER_POT(() -> TileEntityType.Builder.create(LuminousFlowerPotTileEntity::new).build(null)),
    PHANTOM_LIGHT(() -> TileEntityType.Builder.create(PhantomLightTileEntity::new).build(null)),
    SOUL_URN(() -> TileEntityType.Builder.create(TileSoulUrn::new).build(null)),
    SUPERCHARGER(() -> TileEntityType.Builder.create(TileSupercharger::new).build(null));

    private final LazyLoadBase<TileEntityType<?>> type;

    ModTileEntities(Supplier<TileEntityType<?>> typeFactory) {
        this.type = new LazyLoadBase<>(typeFactory);
    }

    public TileEntityType<?> type() {
        return type.getValue();
    }

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        if (!event.getName().equals(ForgeRegistries.TILE_ENTITIES.getRegistryName())) return;

        for (ModTileEntities tileEnum : values()) {
            register(tileEnum.name().toLowerCase(Locale.ROOT), tileEnum.type());
        }
    }

    private static <T extends TileEntity> void register(String name, TileEntityType<T> type) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        type.setRegistryName(id);
        ForgeRegistries.TILE_ENTITIES.register(type);
    }
}
