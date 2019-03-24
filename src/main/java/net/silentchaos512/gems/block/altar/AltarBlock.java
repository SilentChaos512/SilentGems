package net.silentchaos512.gems.block.altar;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.silentchaos512.gems.block.BaseContainerBlock;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.utils.Lazy;

public final class AltarBlock extends BaseContainerBlock {
    public static final Lazy<AltarBlock> INSTANCE = Lazy.of(AltarBlock::new);

    private static final VoxelShape SHAPE = Block.makeCuboidShape(0, 0, 0, 16, 12, 16);

    private AltarBlock() {
        super(AltarTileEntity.class, GuiTypes.TRANSMUTATION_ALTAR,
                Properties.create(Material.IRON)
                        .hardnessAndResistance(5, 50)
                        .sound(SoundType.METAL));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new AltarTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return SHAPE;
    }
}
