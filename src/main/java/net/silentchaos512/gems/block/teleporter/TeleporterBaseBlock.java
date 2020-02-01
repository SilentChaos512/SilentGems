package net.silentchaos512.gems.block.teleporter;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.item.ReturnHomeCharmItem;
import net.silentchaos512.gems.item.TeleporterLinkerItem;

public class TeleporterBaseBlock extends Block {
    private final boolean isAnchor;

    public TeleporterBaseBlock(boolean isAnchor) {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(15, 2000)
                .sound(SoundType.METAL));
        this.isAnchor = isAnchor;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new GemTeleporterTileEntity(this.isAnchor);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(hand);
        boolean holdingLinker = !heldItem.isEmpty() && heldItem.getItem() == TeleporterLinkerItem.INSTANCE.get();
        boolean holdingReturnHome = !heldItem.isEmpty() && heldItem.getItem() instanceof ReturnHomeCharmItem;

        if (world.isRemote)
            return (holdingLinker || holdingReturnHome) || !isAnchor ? ActionResultType.SUCCESS : ActionResultType.PASS;

        GemTeleporterTileEntity tile = (GemTeleporterTileEntity) world.getTileEntity(pos);
        if (tile == null) {
            SilentGems.LOGGER.warn("Teleporter tile at {} not found!", pos);
            return ActionResultType.PASS;
        }

        return tile.interact(player, heldItem, hand) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }
}
