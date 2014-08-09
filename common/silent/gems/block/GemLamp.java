package silent.gems.block;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;

public class GemLamp extends BlockSG {

    private final boolean lit;
    private final boolean inverted;

    public GemLamp(boolean lit, boolean inverted) {

        super(Material.redstoneLight);
        this.lit = lit;
        this.inverted = inverted;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHasGemSubtypes(true);
        this.setHasSubtypes(true);

        if (lit && inverted) {
            this.setLightLevel(1.0f);
            this.setUnlocalizedName(Names.GEM_LAMP_INV_LIT);
        }
        else if (lit) {
            this.setLightLevel(1.0f);
            this.setUnlocalizedName(Names.GEM_LAMP_LIT);
        }
        else if (inverted) {
            this.setUnlocalizedName(Names.GEM_LAMP_INV);
        }
        else {
            this.setUnlocalizedName(Names.GEM_LAMP);
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

        if (!world.isRemote) {
            if (this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.scheduleBlockUpdate(x, y, z, this, 4);
            }
            else if (!this.lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_LIT), world.getBlockMetadata(x, y, z), 2);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        if (!world.isRemote) {
            if (this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.scheduleBlockUpdate(x, y, z, this, 4);
            }
            else if (!this.lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_LIT), world.getBlockMetadata(x, y, z), 2);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {

        if (!world.isRemote && this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
            world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP), world.getBlockMetadata(x, y, z), 2);
        }
    }

    @Override
    public Item getItemDropped(int par1, Random random, int par3) {

        return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP));
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {

        return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP));
    }

    @Override
    protected ItemStack createStackedBlock(int par1) {

        return new ItemStack(SRegistry.getBlock(Names.GEM_LAMP));
    }

    @Override
    public void addRecipes() {

        if (!lit && !inverted) {
            for (int i = 0; i < EnumGem.all().length; ++i) {
                RecipeHelper.addSurround(new ItemStack(this, 1, i), EnumGem.all()[i].getItem(), new Object[] { Items.redstone,
                        Items.glowstone_dust });
            }
        }
        else if (!lit && inverted) {
            for (int i = 0; i < EnumGem.all().length; ++i) {
                GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i), new ItemStack(SRegistry.getBlock(Names.GEM_LAMP), 1, i),
                        Blocks.redstone_torch);
            }
        }
    }
}
