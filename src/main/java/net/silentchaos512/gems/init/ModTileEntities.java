package net.silentchaos512.gems.init;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.supercharger.TileSupercharger;
import net.silentchaos512.gems.block.urn.TileSoulUrn;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModTileEntities {
    SUPERCHARGER(() -> TileEntityType.Builder.create(TileSupercharger::new).build(null)),
    SOUL_URN(() -> TileEntityType.Builder.create(TileSoulUrn::new).build(null));

    private final LazyLoadBase<TileEntityType<?>> type;

    ModTileEntities(Supplier<TileEntityType<?>> typeFactory) {
        this.type = new LazyLoadBase<>(typeFactory);
    }

    public TileEntityType<?> type() {
        return type.getValue();
    }

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        if (!event.getName().equals(GameData.TILEENTITIES)) return;

        IForgeRegistry<TileEntityType<?>> reg = ForgeRegistries.TILE_ENTITIES;

        for (ModTileEntities tileEnum : values()) {
            register(reg, tileEnum.name().toLowerCase(Locale.ROOT), tileEnum.type());
        }
    }

    private static <T extends TileEntity> TileEntityType<T> register(IForgeRegistry<TileEntityType<?>> reg, String name, TileEntityType<T> type) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        type.setRegistryName(id);
        reg.register(type);
        return type;
    }
}
