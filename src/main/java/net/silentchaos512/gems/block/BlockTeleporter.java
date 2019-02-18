package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.Gems;

import javax.annotation.Nullable;

public class BlockTeleporter extends Block implements ITileEntityProvider {
    @Nullable private final Gems gem;
    private final boolean isAnchor;

    public BlockTeleporter(@Nullable Gems gem, boolean isAnchor) {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(15, 2000)
                .sound(SoundType.METAL));
        this.gem = gem;
        this.isAnchor = isAnchor;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        // FIXME
        return null;
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
//        ItemStack heldItem = player.getHeldItem(hand);
//        boolean holdingLinker = !heldItem.isEmpty() && heldItem.getItem() == ModItems.teleporterLinker;
//        boolean holdingReturnHome = !heldItem.isEmpty() && heldItem.getItem() == ModItems.returnHomeCharm;
//
//        if (world.isRemote)
//            return (holdingLinker || holdingReturnHome) || !isAnchor;
//
//        TileTeleporter tile = (TileTeleporter) world.getTileEntity(pos);
//        if (tile == null) {
//            SilentGems.logHelper.warn("Teleporter tile at {} not found!", pos);
//            return false;
//        }
//
//        // Link teleporters with linker.
//        if (holdingLinker) {
//            return tile.linkTeleporters(player, world, pos, heldItem, hand);
//        }
//
//        // Link return home charm.
//        if (holdingReturnHome) {
//            return tile.linkReturnHomeCharm(player, world, pos, heldItem, hand);
//        }
//
//        // If this is an anchor, we're done.
//        if (isAnchor) {
//            return false;
//        }
//
//        // Destination set?
//        if (!tile.isDestinationSet()) {
//            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "noDestination"));
//            return true;
//        }
//
//        // Safety checks before teleporting:
//        if (!tile.isDestinationSane(player)) {
//            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "notSane"));
//            return true;
//        }
//        if (!tile.isDestinationSafe(player)) {
//            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "notSafe"));
//            return true;
//        }
//        if (!tile.isDestinationAllowedIfDumb(player)) {
//            ChatHelper.sendMessage(player, SilentGems.i18n.blockSubText(Names.TELEPORTER, "noReceiver"));
//            return true;
//        }
//
//        // Check available charge, drain if there is enough.
//        if (!tile.checkAndDrainChaos(player)) {
//            return true;
//        }
//
//        // Teleport player
//        tile.teleportEntityToDestination(player);
//
//        // Play sounds
//        float pitch = 0.7f + 0.3f * SilentGems.random.nextFloat();
//        for (BlockPos p : new BlockPos[]{pos, tile.getDestination().toBlockPos()}) {
//            world.playSound(null, p, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0f,
//                    pitch);
//        }

        return true;
    }
}
