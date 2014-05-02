package silent.gems.block;

import static net.minecraftforge.common.EnumPlantType.Plains;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GlowRose extends BlockSG implements IPlantable {

    public GlowRose(int id) {

        super(id, Material.plants);
        this.setHardness(0.0f);
        this.setStepSound(Block.soundGrassFootstep);
        this.setTickRandomly(true);
        float f = 0.2F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setLightValue(0.7f);
        setHasGemSubtypes(true);
        setHasSubtypes(true);
        setUnlocalizedName(Names.GLOW_ROSE);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {

        return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int id) {

        return id == Block.grass.blockID || id == Block.dirt.blockID || id == Block.tilledField.blockID;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int id) {

        super.onNeighborBlockChange(world, x, y, z, id);
        this.checkFlowerChange(world, x, y, z);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World world, int x, int y, int z, Random par5Random) {

        this.checkFlowerChange(world, x, y, z);
    }

    protected final void checkFlowerChange(World world, int x, int y, int z) {

        if (!this.canBlockStay(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, 0, 0, 2);
        }
    }

    /**
     * Can this block stay at this position. Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World world, int x, int y, int z) {

        Block soil = blocksList[world.getBlockId(x, y - 1, z)];
        return (world.getFullBlockLightValue(x, y, z) >= 8 || world.canBlockSeeTheSky(x, y, z))
                && (soil != null && soil.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this));
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {

        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {

        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {

        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType() {

        return 1;
    }

    @Override
    public EnumPlantType getPlantType(World world, int x, int y, int z) {
        
        return Plains;
    }

    @Override
    public int getPlantID(World world, int x, int y, int z) {

        return blockID;
    }

    @Override
    public int getPlantMetadata(World world, int x, int y, int z) {

        return world.getBlockMetadata(x, y, z);
    }
    
    @Override
    public void addRecipes() {
        
        for (int i = 0; i < EnumGem.all().length; ++i) {
            GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i), Block.plantRed, Item.glowstone, EnumGem.all()[i].getShard());
        }
        
        // Flowers to dye.
        int k = 2;
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 0), new ItemStack(this, 1, EnumGem.ONYX.id));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 1), new ItemStack(this, 1, EnumGem.RUBY.id));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 2), new ItemStack(this, 1, EnumGem.EMERALD.id));
        // 3-brown
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 4), new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 5), new ItemStack(this, 1, EnumGem.AMETHYST.id));
        // 6-cyan
        // 7-light gray
        // 8-gray
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 9), new ItemStack(this, 1, EnumGem.MORGANITE.id));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 10), new ItemStack(this, 1, EnumGem.PERIDOT.id));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 11), new ItemStack(this, 1, EnumGem.HELIODOR.id));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 12), new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
        // 13-magenta
        GameRegistry.addShapelessRecipe(new ItemStack(Item.dyePowder, k, 14), new ItemStack(this, 1, EnumGem.TOPAZ.id));
    }
}
