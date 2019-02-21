package net.silentchaos512.gems.block.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.Random;

public class PhantomLightBlock extends Block implements ITileEntityProvider {
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3, 0.3, 0.3, 0.7, 0.7, 0.7);

    public static final Lazy<PhantomLightBlock> INSTANCE = Lazy.of(PhantomLightBlock::new);

    private PhantomLightBlock() {
        super(Properties.create(Material.CIRCUITS)
                .hardnessAndResistance(0.5f, 6000000)
                .lightValue(15));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new PhantomLightTileEntity();
    }

    @Override
    public void animateTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
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

    @Override
    public boolean canBeReplacedByLeaves(IBlockState state, IWorldReaderBase world, BlockPos pos) {
        return true;
    }

    @Override
    public void dropBlockAsItemWithChance(IBlockState state, World worldIn, BlockPos pos, float chancePerItem, int fortune) {
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return Items.AIR;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof PhantomLightTileEntity) {
            ((PhantomLightTileEntity) tile).setPlacedByPlayer(true);
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return BOUNDING_BOX;
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
//        return null;
//    }

//    @Override
//    public boolean isOpaqueCube(IBlockState state) {
//        return false;
//    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
}
