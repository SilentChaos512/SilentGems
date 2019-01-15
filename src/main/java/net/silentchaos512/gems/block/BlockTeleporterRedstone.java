package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.event.ServerTickHandler;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileTeleporter;
import net.silentchaos512.lib.util.DimensionalPosition;

public class BlockTeleporterRedstone extends BlockTeleporter {
    public BlockTeleporterRedstone(Gems.Set set) {
        super(set, false, Names.TELEPORTER_REDSTONE);
    }

    private ItemStack getBasicTeleporter(int meta) {
        Block block;
        if (this == ModBlocks.teleporterRedstone)
            block = ModBlocks.teleporter;
        else if (this == ModBlocks.teleporterRedstoneDark)
            block = ModBlocks.teleporterDark;
        else
            block = ModBlocks.teleporterLight;
        return new ItemStack(block, 1, meta);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos p_189540_5_) {
        final double searchRange = GemsConfig.TELEPORTER_REDSTONE_SEARCH_RADIUS * GemsConfig.TELEPORTER_REDSTONE_SEARCH_RADIUS;
        TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
        if (tile == null) return;

        if (!world.isRemote && world.getRedstonePowerFromNeighbors(pos) != 0) {
            // Is this a "dumb" teleport and are they allowed if so?
            if (!tile.isDestinationAllowedIfDumb(null)) return;

            // Destination safe?
            if (!tile.isDestinationSafe(null)) return;

            boolean playSound = false;
            // Check all entities, teleport those close to the teleporter.
            DimensionalPosition source = null;
            for (Entity entity : world.getEntities(Entity.class, e -> e.getDistanceSqToCenter(pos) < searchRange)) {
                // Get source position (have to have the entity for this because dim?)
                if (source == null) {
                    source = new DimensionalPosition(pos, entity.dimension);
                }
                if (entity instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) entity;
                    // Chaos drain.
                    if (!tile.checkAndDrainChaos(player)) {
                        continue;
                    }
                }
                if (tile.teleportEntityToDestination(entity)) {
                    ServerTickHandler.schedule(() -> tile.teleportEntityToDestination(entity));
                    playSound = true;
                }
            }

            if (playSound) {
                float pitch = 0.7f + 0.3f * SilentGems.random.nextFloat();
                for (BlockPos p : new BlockPos[]{pos, tile.getDestination().toBlockPos()}) {
                    world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0f, pitch);
                }
            }
        }
    }
}
