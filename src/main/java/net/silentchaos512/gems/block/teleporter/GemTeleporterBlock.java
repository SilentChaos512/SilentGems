package net.silentchaos512.gems.block.teleporter;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.silentchaos512.gems.block.IGemBlock;
import net.silentchaos512.gems.util.Gems;

import java.util.function.BiFunction;

public class GemTeleporterBlock extends AbstractTeleporterBlock implements IGemBlock {
    protected final Gems gem;
    private final MapCodec<? extends GemTeleporterBlock> codec;

    public GemTeleporterBlock(Gems gem, Properties properties) {
        this(gem, properties, GemTeleporterBlock::new);
    }

    public GemTeleporterBlock(Gems gem, Properties properties, BiFunction<Gems, Properties, ? extends GemTeleporterBlock> constructor) {
        super(properties, p -> new GemTeleporterBlock(gem, p));
        this.gem = gem;
        this.codec = simpleCodec(p -> constructor.apply(gem, p));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(TRIGGERED, false));
    }

    @Override
    public Gems getGem() {
        return this.gem;
    }

    @Override
    public MutableComponent getGemBlockName() {
        return Component.translatable("block.silentgems.gem_teleporter", this.gem.getDisplayName());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return this.codec;
    }
}
