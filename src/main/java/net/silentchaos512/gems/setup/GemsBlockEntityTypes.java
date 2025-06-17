package net.silentchaos512.gems.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.teleporter.TeleporterBlockEntity;
import net.silentchaos512.gems.util.Gems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GemsBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SilentGems.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TeleporterBlockEntity>> TELEPORTER = register(
            "teleporter",
            TeleporterBlockEntity::new,
            getAllTeleporters().toArray(DeferredBlock[]::new)
    );

    private static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, DeferredBlock<?>... blocks) {
        return BLOCK_ENTITY_TYPES.register(name, () -> {
            Block[] validBlocks = Arrays.stream(blocks).map(DeferredBlock::get).toArray(Block[]::new);
            //noinspection ConstantConditions - null in build
            return BlockEntityType.Builder.of(factory, validBlocks).build(null);
        });
    }

    private static List<DeferredBlock<?>> getAllTeleporters() {
        List<DeferredBlock<?>> list = new ArrayList<>();
        for (Gems gem : Gems.values()) {
            list.add(gem.getTeleporter());
            list.add(gem.getRedstoneTeleporter());
        }
        list.add(GemsBlocks.TELEPORTER_ANCHOR);
        return list;
    }
}
