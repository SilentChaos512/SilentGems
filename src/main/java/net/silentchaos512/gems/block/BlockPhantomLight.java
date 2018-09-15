package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumModParticles;
import net.silentchaos512.gems.tile.TilePhantomLight;
import net.silentchaos512.lib.block.ITileEntityBlock;
import net.silentchaos512.lib.util.Color;

import java.util.Random;

public class BlockPhantomLight extends Block implements ITileEntityProvider, ITileEntityBlock {
    private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.3, 0.3, 0.3, 0.7, 0.7, 0.7);

    public BlockPhantomLight() {
        super(Material.CIRCUITS);
        setHardness(0.5f);
        setResistance(6000000.0F);
        lightValue = 15;
        translucent = true;
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TilePhantomLight.class;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(2 * (1 + 2 * SilentGems.proxy.getParticleSettings())) == 0) {
            final float meanSpeed = 0.025f;
            final double motionX = rand.nextGaussian() * meanSpeed;
            final double motionY = rand.nextGaussian() * meanSpeed;
            final double motionZ = rand.nextGaussian() * meanSpeed;
            Color color = new Color(0xFFFFAA);
            SilentGems.proxy.spawnParticles(EnumModParticles.PHANTOM_LIGHT, color, worldIn,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, motionX, motionY, motionZ);
        }
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.AIR;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TilePhantomLight) {
            ((TilePhantomLight) tile).setPlacedByPlayer(true);
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePhantomLight();
    }
}
