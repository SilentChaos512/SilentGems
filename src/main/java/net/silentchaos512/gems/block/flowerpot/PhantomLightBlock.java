package net.silentchaos512.gems.block.flowerpot;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.Random;

public class PhantomLightBlock extends Block implements ITileEntityProvider {
    public static final Lazy<PhantomLightBlock> INSTANCE = Lazy.of(PhantomLightBlock::new);

    private PhantomLightBlock() {
        super(Properties.create(Material.MISCELLANEOUS)
                .hardnessAndResistance(0.5f, 6000000)
                .lightValue(15));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
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

    @Override
    public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof PhantomLightTileEntity) {
            ((PhantomLightTileEntity) tile).setPlacedByPlayer(true);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
