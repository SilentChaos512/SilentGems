package silent.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MushroomBlock extends BlockSG implements IPlantable {

    public final static String[] names = { Names.FLY_AMANITA };

    public MushroomBlock(int id) {

        super(id, Material.plants);
        icons = new Icon[names.length];
        this.setHardness(0.0f);
        this.setStepSound(Block.soundGrassFootstep);
        this.setTickRandomly(true);
        float f = 0.2F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        setHasSubtypes(true);
        setUnlocalizedName(Names.MUSHROOM_BLOCK);
    }

    public static ItemStack getStack(String name) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getBlock(Names.MUSHROOM_BLOCK), 1, i);
            }
        }

        return null;
    }

    public static ItemStack getStack(String name, int count) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getBlock(Names.MUSHROOM_BLOCK), count, i);
            }
        }

        return null;
    }
    
    public static int getDamageFromBlockName(String name) {
        
        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return i;
            }
        }
        
        return -1;
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {

        if (random.nextInt(10) == 0) { // was 25 and 0
            byte b0 = 4;
            int l = 10; // was 5
            int i1;
            int j1;
            int k1;

            for (i1 = x - b0; i1 <= x + b0; ++i1)
            {
                for (j1 = z - b0; j1 <= z + b0; ++j1)
                {
                    for (k1 = y - 1; k1 <= y + 1; ++k1)
                    {
                        if (world.getBlockId(i1, k1, j1) == this.blockID)
                        {
                            --l;

                            if (l <= 0)
                            {
                                return;
                            }
                        }
                    }
                }
            }

            i1 = x + random.nextInt(3) - 1;
            j1 = y + random.nextInt(2) - random.nextInt(2);
            k1 = z + random.nextInt(3) - 1;

            for (int l1 = 0; l1 < 4; ++l1)
            {
                if (world.isAirBlock(i1, j1, k1) && this.canBlockStay(world, i1, j1, k1))
                {
                    x = i1;
                    y = j1;
                    z = k1;
                }

                i1 = x + random.nextInt(3) - 1;
                j1 = y + random.nextInt(2) - random.nextInt(2);
                k1 = z + random.nextInt(3) - 1;
            }

            if (world.isAirBlock(i1, j1, k1) && this.canBlockStay(world, i1, j1, k1))
            {
                world.setBlock(i1, j1, k1, this.blockID, 0, 2);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {

        return null;
    }
    
    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

    protected boolean canThisPlantGrowOnThisBlockID(int id) {

        return Block.opaqueCubeLookup[id];
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int id) {

        super.onNeighborBlockChange(world, x, y, z, id);
        this.checkFlowerChange(world, x, y, z);
    }
    
    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        
        if (y >= 0 && y < 256) {
            int l = world.getBlockId(x, y - 1, z);
            Block soil = Block.blocksList[l];
            return (l == Block.mycelium.blockID || world.getFullBlockLightValue(x, y, z) < 13) &&
                    (soil != null && soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this));
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {

        return false;
    }
    
    @Override
    public int getRenderType() {
        
        return 1;
    }
    
    protected final void checkFlowerChange(World world, int x, int y, int z) {

        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, 0, 0, 2);
        }
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {

        return EnumPlantType.Cave;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {

        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {

        return world.getBlockMetadata(x, y, z);
    }
}
