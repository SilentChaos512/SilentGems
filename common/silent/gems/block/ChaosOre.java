package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import silent.gems.configuration.Config;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class ChaosOre extends BlockSG {

    public ChaosOre(int id) {

        super(id, Material.rock);
        setHardness(3.0f);
        setResistance(5.0f);
        setStepSound(Block.soundStoneFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        setUnlocalizedName(Names.CHAOS_ORE);
    }

    @Override
    public void addRecipes() {

        GameRegistry.addSmelting(blockID,
                new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), Config.CHAOS_ESSENCE_PER_ORE.value, 3), 0.5f);
    }

    @Override
    public void addOreDict() {

        OreDictionary.registerOre("oreChaos", this);
    }
}
