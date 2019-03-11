package net.silentchaos512.gems.block.teleporter;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Gems;

public class GemTeleporterRedstone extends GemTeleporter {
    public GemTeleporterRedstone(Gems gem, boolean isAnchor) {
        super(gem, isAnchor);
    }

    @Override
    public ITextComponent getNameTextComponent() {
        if (this.gem == null) return super.getNameTextComponent();
        return new TextComponentTranslation("block.silentgems.redstone_teleporter", this.gem.getDisplayName());
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos p_189540_5_) {
//        final double searchRange = GemsConfig.TELEPORTER_REDSTONE_SEARCH_RADIUS * GemsConfig.TELEPORTER_REDSTONE_SEARCH_RADIUS;
//        GemTeleporterTileEntity tile = (GemTeleporterTileEntity) world.getTileEntity(pos);
//        if (tile == null) return;
//
//        if (!world.isRemote && world.getRedstonePowerFromNeighbors(pos) != 0) {
//            // Is this a "dumb" teleport and are they allowed if so?
//            if (!tile.isDestinationAllowedIfDumb(null)) return;
//
//            // Destination safe?
//            if (!tile.isDestinationSafe(null)) return;
//
//            boolean playSound = false;
//            // Check all entities, teleport those close to the teleporter.
//            DimensionalPosition source = null;
//            for (Entity entity : world.getEntities(Entity.class, e -> e.getDistanceSqToCenter(pos) < searchRange)) {
//                // Get source position (have to have the entity for this because dim?)
//                if (source == null) {
//                    source = new DimensionalPosition(pos, entity.dimension);
//                }
//                if (entity instanceof EntityPlayer) {
//                    EntityPlayer player = (EntityPlayer) entity;
//                    // Chaos drain.
//                    if (!tile.checkAndDrainChaos(player)) {
//                        continue;
//                    }
//                }
//                if (tile.teleportEntityToDestination(entity)) {
//                    ServerTickHandler.schedule(() -> tile.teleportEntityToDestination(entity));
//                    playSound = true;
//                }
//            }
//
//            if (playSound) {
//                float pitch = 0.7f + 0.3f * SilentGems.random.nextFloat();
//                for (BlockPos p : new BlockPos[]{pos, tile.getDestination().toBlockPos()}) {
//                    world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0f, pitch);
//                }
//            }
//        }
    }
}
