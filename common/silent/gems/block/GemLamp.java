package silent.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import silent.gems.SilentGems;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.RecipeHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;

public class GemLamp extends BlockSG {

    private final boolean lit;
    private final boolean inverted;

    public GemLamp(boolean lit, boolean inverted) {

        super(Material.redstoneLight);
        this.setHardness(0.3f);
        this.lit = lit;
        this.inverted = inverted;
        if (this.lit == this.inverted) {
            this.setCreativeTab(SilentGems.tabSilentGems);
        }
        else {
            this.setCreativeTab(null);
        }
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
            if (this.inverted) {
                if (!this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.scheduleBlockUpdate(x, y, z, this, 4);
                }
                else if (this.lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_INV), world.getBlockMetadata(x, y, z), 2);
                }
            }
            else {
                if (this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.scheduleBlockUpdate(x, y, z, this, 4);
                }
                else if (!this.lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_LIT), world.getBlockMetadata(x, y, z), 2);
                }
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        if (!world.isRemote) {
            if (this.inverted) {
                if (!this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.scheduleBlockUpdate(x, y, z, this, 4);
                }
                else if (this.lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_INV), world.getBlockMetadata(x, y, z), 2);
                }
            }
            else {
                if (this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.scheduleBlockUpdate(x, y, z, this, 4);
                }
                else if (!this.lit && world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_LIT), world.getBlockMetadata(x, y, z), 2);
                }
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {

        if (!world.isRemote) {
            if (inverted) {
                if (!this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP_INV_LIT), world.getBlockMetadata(x, y, z), 2);
                }
            }
            else {
                if (this.lit && !world.isBlockIndirectlyGettingPowered(x, y, z)) {
                    world.setBlock(x, y, z, SRegistry.getBlock(Names.GEM_LAMP), world.getBlockMetadata(x, y, z), 2);
                }
            }
        }
    }

    @Override
    public Item getItemDropped(int par1, Random random, int par3) {

        if (this.inverted) {
            return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP_INV_LIT));
        }
        else {
            return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP));
        }
    }

    @Override
    public Item getItem(World world, int x, int y, int z) {

        if (this.inverted) {
            return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP_INV_LIT));
        }
        else {
            return Item.getItemFromBlock(SRegistry.getBlock(Names.GEM_LAMP));
        }
    }

    @Override
    protected ItemStack createStackedBlock(int par1) {

        if (this.inverted) {
            return new ItemStack(SRegistry.getBlock(Names.GEM_LAMP_INV_LIT));
        }
        else {
            return new ItemStack(SRegistry.getBlock(Names.GEM_LAMP));
        }
    }

    @Override
    public void addRecipes() {

        if (!lit && !inverted) {
            for (int i = 0; i < EnumGem.all().length; ++i) {
                RecipeHelper.addSurround(new ItemStack(this, 1, i), EnumGem.all()[i].getItem(), new Object[] { Items.redstone,
                        Items.glowstone_dust });
            }
        }
        else if (lit && inverted) {
            for (int i = 0; i < EnumGem.all().length; ++i) {
                GameRegistry.addShapelessRecipe(new ItemStack(this, 1, i), new ItemStack(SRegistry.getBlock(Names.GEM_LAMP), 1, i),
                        Blocks.redstone_torch);
            }
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {

        if (icons == null || icons.length != EnumGem.all().length) {
            icons = new IIcon[EnumGem.all().length];
        }

        for (int i = 0; i < EnumGem.all().length; ++i) {
            icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + (this.lit ? Names.GEM_LAMP_LIT : Names.GEM_LAMP) + i);
        }
    }
}
