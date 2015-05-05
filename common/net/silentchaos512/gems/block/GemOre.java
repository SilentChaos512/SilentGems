package net.silentchaos512.gems.block;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.SRegistry;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import thaumcraft.api.ThaumcraftApi;

public class GemOre extends BlockSG {

    public GemOre() {

        super(Material.rock);

        icons = new IIcon[EnumGem.all().length];
        setHardness(3.0f);
        setResistance(5.0f);
        setStepSound(Block.soundTypeStone);
        setHarvestLevel("pickaxe", 2);

        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_ORE);
    }

    @Override
    public void addOreDict() {

        OreDictionary.registerOre("oreRuby", new ItemStack(this, 1, EnumGem.RUBY.id));
        OreDictionary.registerOre("oreGarnet", new ItemStack(this, 1, EnumGem.GARNET.id));
        OreDictionary.registerOre("oreTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.id));
        OreDictionary.registerOre("oreHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.id));
        OreDictionary.registerOre("orePeridot", new ItemStack(this, 1, EnumGem.PERIDOT.id));
        OreDictionary.registerOre("oreBeryl", new ItemStack(this, 1, EnumGem.EMERALD.id));
        OreDictionary.registerOre("oreAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
        OreDictionary.registerOre("oreSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
        OreDictionary.registerOre("oreIolite", new ItemStack(this, 1, EnumGem.IOLITE.id));
        OreDictionary.registerOre("oreAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.id));
        OreDictionary.registerOre("oreMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.id));
        OreDictionary.registerOre("oreOnyx", new ItemStack(this, 1, EnumGem.ONYX.id));
    }

    @Override
    public void addRecipes() {

        for (int i = 0; i < EnumGem.all().length; ++i) {
            GameRegistry.addSmelting(new ItemStack(this, 1, i), EnumGem.all()[i].getItem(), 0.5f);
        }
    }

    @Override
    public void addThaumcraftStuff() {

        for (int i = 0; i < EnumGem.all().length; ++i) {
            ThaumcraftApi.addSmeltingBonus(new ItemStack(this, 1, i), new ItemStack(EnumGem.all()[i].getShard().getItem(), 0, i));
        }
    }

    @Override
    public int damageDropped(int meta) {

        return meta;
    }

    // @Override
    // public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7) {
    //
    // super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, par7);
    //
    // if (getItemDropped(par5, world.rand, par7) != blockID) {
    // int j1 = 1 + world.rand.nextInt(5);
    // dropXpOnBlockBreak(world, x, y, z, j1);
    // }
    // }

    @Override
    public int getExpDrop(IBlockAccess world, int meta, int fortune) {

        if (this.getItemDropped(meta, SilentGems.instance.random, fortune) != Item.getItemFromBlock(this)) {
            return 1 + SilentGems.instance.random.nextInt(5);
        }
        return 0;
    }

    @Override
    public Item getItemDropped(int par1, Random random, int par2) {

        return SRegistry.getItem(Names.GEM_ITEM);
    }

    @Override
    public int quantityDropped(Random random) {

        return 1;
    }

    @Override
    public int quantityDroppedWithBonus(int par1, Random random) {

        if (par1 > 0) {
            int j = random.nextInt(par1 + 2) - 1;

            if (j < 0) {
                j = 0;
            }

            return quantityDropped(random) * (j + 1);
        }
        else {
            return quantityDropped(random);
        }
    }
}
