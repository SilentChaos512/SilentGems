package net.silentchaos512.gems.init;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.supercharger.TileSupercharger;
import net.silentchaos512.gems.block.urn.TileSoulUrn;

import java.util.Locale;
import java.util.function.Supplier;

public enum ModTileEntities {
    SUPERCHARGER(() -> TileEntityType.Builder.create(TileSupercharger::new).build(null)),
    SOUL_URN(() -> TileEntityType.Builder.create(TileSoulUrn::new).build(null));

    private final Supplier<TileEntityType<?>> typeFactory;
    private TileEntityType<?> type;

    ModTileEntities(Supplier<TileEntityType<?>> typeFactory) {
        this.typeFactory = typeFactory;
    }

    public TileEntityType<?> type() {
        if (type == null) type = typeFactory.get();
        return type;
    }

    public static void registerAll(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> reg = event.getRegistry();
        for (ModTileEntities tileEnum : values()) {
            register(reg, tileEnum.name().toLowerCase(Locale.ROOT), tileEnum.type().get());
        }
    }

    private static <T extends TileEntity> TileEntityType<T> register(IForgeRegistry<TileEntityType<?>> reg, String name, TileEntityType<T> type) {
        ResourceLocation id = new ResourceLocation(SilentGems.MOD_ID, name);
        type.setRegistryName(id);
        reg.register(type);
        return type;
    }
}
