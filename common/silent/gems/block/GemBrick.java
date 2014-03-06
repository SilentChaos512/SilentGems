package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;


public class GemBrick extends BlockSG {

    public GemBrick(int id) {

        super(id, Material.rock);
        icons = new Icon[EnumGem.all().length];
        setHardness(3.0f);
        setResistance(5.0f);
        setStepSound(Block.soundStoneFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_BRICK);
    }

    @Override
    public String getUnlocalizedName() {

        return getUnlocalizedName(Names.GEM_BRICK);
    }

    @Override
    public void addRecipes() {

        String s1 = "sss";
        String s2 = "sgs";
        for (int i = 0; i < EnumGem.all().length; ++i) {
            GameRegistry.addShapedRecipe(new ItemStack(this, 8, i), s1, s2, s1, 'g', EnumGem.all()[i].getShard(), 's', Block.stoneBrick);
        }
    }
}
