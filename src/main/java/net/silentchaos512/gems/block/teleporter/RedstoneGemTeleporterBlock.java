package net.silentchaos512.gems.block.teleporter;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Gems;

public class GemTeleporterRedstone extends GemTeleporterBlock {
    public GemTeleporterRedstone(Gems gem, boolean isAnchor) {
        super(gem, isAnchor);
    }

    @Override
    public ITextComponent getNameTextComponent() {
        if (this.gem == null) return super.getNameTextComponent();
        return new TextComponentTranslation("block.silentgems.redstone_teleporter", this.gem.getDisplayName());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos p_189540_5_) {
        GemTeleporterTileEntity tile = (GemTeleporterTileEntity) world.getTileEntity(pos);
        if (tile == null) return;

        if (!world.isRemote && world.getRedstonePowerFromNeighbors(pos) != 0) {
            final int searchRadius = GemsConfig.COMMON.teleporterSearchRadius.get();
            final int searchRadiusSq = searchRadius * searchRadius;
            boolean playSound = false;

            // Check all entities, teleport those close to the teleporter.
            for (Entity entity : world.getEntities(Entity.class, e -> e.getDistanceSqToCenter(pos) < searchRadiusSq)) {
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
