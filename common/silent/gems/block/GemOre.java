package silent.gems.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;


public class GemOre extends BlockSG {

    public GemOre(int id) {

        super(id, Material.rock);
        icons = new Icon[EnumGem.all().length];
        setHardness(3.0f);
        setResistance(5.0f);
        setStepSound(Block.soundStoneFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_ORE);
    }

    @Override
    public int idDropped(int par1, Random random, int par2) {
        
        return SRegistry.getItem(Names.GEM_ITEM).itemID;
    }
    
    @Override
    public int damageDropped(int meta) {

        return meta;
    }

    @Override
    public int quantityDropped(Random random) {

        return 1;
    }

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

    @Override
    public void dropBlockAsItemWithChance(World world, int x, int y, int z, int par5, float par6, int par7) {

        super.dropBlockAsItemWithChance(world, x, y, z, par5, par6, par7);

        if (idDropped(par5, world.rand, par7) != blockID) {
            int j1 = 1 + world.rand.nextInt(5);
            dropXpOnBlockBreak(world, x, y, z, j1);
        }
    }

    @Override
    public String getUnlocalizedName() {

        return getUnlocalizedName(Names.GEM_ORE);
    }

    @Override
    public void addOreDict() {

        OreDictionary.registerOre("oreRuby", new ItemStack(this, 1, EnumGem.RUBY.id));
        OreDictionary.registerOre("oreGarnet", new ItemStack(this, 1, EnumGem.GARNET.id));
        OreDictionary.registerOre("oreTopaz", new ItemStack(this, 1, EnumGem.TOPAZ.id));
        OreDictionary.registerOre("oreHeliodor", new ItemStack(this, 1, EnumGem.HELIODOR.id));
        OreDictionary.registerOre("orePeridot", new ItemStack(this, 1, EnumGem.PERIDOT.id));
        OreDictionary.registerOre("oreEmerald", new ItemStack(this, 1, EnumGem.EMERALD.id));
        OreDictionary.registerOre("oreAquamarine", new ItemStack(this, 1, EnumGem.AQUAMARINE.id));
        OreDictionary.registerOre("oreSapphire", new ItemStack(this, 1, EnumGem.SAPPHIRE.id));
        OreDictionary.registerOre("oreIolite", new ItemStack(this, 1, EnumGem.IOLITE.id));
        OreDictionary.registerOre("oreAmethyst", new ItemStack(this, 1, EnumGem.AMETHYST.id));
        OreDictionary.registerOre("oreMorganite", new ItemStack(this, 1, EnumGem.MORGANITE.id));
        OreDictionary.registerOre("oreOnyx", new ItemStack(this, 1, EnumGem.ONYX.id));
    }
}
