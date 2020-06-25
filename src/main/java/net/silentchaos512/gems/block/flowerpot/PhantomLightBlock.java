package net.silentchaos512.gems.block.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class PhantomLightBlock extends Block {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);

    public PhantomLightBlock() {
        super(Properties.create(Material.MISCELLANEOUS)
                .hardnessAndResistance(0.5f, 6000000)
                .lightValue(15));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PhantomLightTileEntity();
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
//        if (rand.nextInt(2 * (1 + 2 * SilentGems.proxy.getParticleSettings())) == 0) {
//            final float meanSpeed = 0.025f;
//            final double motionX = rand.nextGaussian() * meanSpeed;
//            final double motionY = rand.nextGaussian() * meanSpeed;
//            final double motionZ = rand.nextGaussian() * meanSpeed;
//            Color color = new Color(0xFFFFAA);
//            SilentGems.proxy.spawnParticles(EnumModParticles.PHANTOM_LIGHT, color, worldIn,
//                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, motionX, motionY, motionZ);
//        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof PhantomLightTileEntity) {
            ((PhantomLightTileEntity) tile).setPlacedByPlayer(true);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
