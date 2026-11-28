package net.silentchaos512.gems.block.teleporter;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.silentchaos512.gems.block.IGemBlock;
import net.silentchaos512.gems.setup.Gems;

public class GemTeleporterBlock extends AbstractTeleporterBlock implements IGemBlock {
    protected final Gems gem;

    public GemTeleporterBlock(Gems gem, Properties properties) {
        super(properties);
        this.gem = gem;
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
}
