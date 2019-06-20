package net.silentchaos512.gems.block.teleporter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Gems;

public class RedstoneGemTeleporterBlock extends GemTeleporterBlock {
    public RedstoneGemTeleporterBlock(Gems gem, boolean isAnchor) {
        super(gem, isAnchor);
    }

    @Override
    public ITextComponent getNameTextComponent() {
        if (this.gem == null) return super.getNameTextComponent();
        return new TranslationTextComponent("block.silentgems.redstone_teleporter", this.gem.getDisplayName());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean p_220069_6_) {
        GemTeleporterTileEntity tile = (GemTeleporterTileEntity) world.getTileEntity(pos);
        if (tile == null) return;

        if (!world.isRemote && world.getRedstonePowerFromNeighbors(pos) != 0) {
            final int searchRadius = GemsConfig.COMMON.teleporterSearchRadius.get();
            boolean playSound = false;

            // Check all entities, teleport those close to the teleporter.
            for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos).grow(searchRadius))) {
                if (tile.tryTeleport(entity, false)) {
//                    ServerTicks.scheduleAction(() -> tile.teleportEntityToDestination(entity));
                    playSound = true;
                }
            }

            if (playSound) {
                tile.playSound();
            }
        }
    }
}
