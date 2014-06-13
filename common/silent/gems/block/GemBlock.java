package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import cpw.mods.fml.common.registry.GameRegistry;

public class GemBlock extends BlockSG {

    public GemBlock() {

        super(Material.iron);
        icons = new IIcon[EnumGem.all().length];
        setHardness(3.0f);
        setResistance(5.0f);
        setStepSound(Block.soundTypeStone);
        setCreativeTab(CreativeTabs.tabBlock);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_BLOCK);
    }

    @Override
    public void addRecipes() {

        String s = "ggg";
        for (int i = 0; i < EnumGem.all().length; ++i) {
            GameRegistry.addShapedRecipe(new ItemStack(this, 1, i), s, s, s, 'g', EnumGem.all()[i].getItem());
            GameRegistry.addShapelessRecipe(new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 9, i), new ItemStack(this, 1, i));
        }
    }
}
