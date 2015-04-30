package silent.gems.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;


public class MiscBlock extends BlockSG {

    public final static String[] names = { Names.CHAOS_ESSENCE_BLOCK };
    public MiscBlock() {

        super(Material.iron);
        icons = new IIcon[names.length];
        setHasSubtypes(true);
        setUnlocalizedName(Names.MISC_BLOCKS);
    }
    
    @Override
    public void addRecipes() {

        GameRegistry.addShapedRecipe(getStack(Names.CHAOS_ESSENCE_BLOCK), "ccc", "ccc", "ccc", 'c',
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE));
        GameRegistry.addShapelessRecipe(CraftingMaterial.getStack(Names.CHAOS_ESSENCE, 9), getStack(Names.CHAOS_ESSENCE_BLOCK));
    }

    public static ItemStack getStack(String name) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getBlock(Names.MISC_BLOCKS), 1, i);
            }
        }

        return null;
    }

    public static ItemStack getStack(String name, int count) {

        for (int i = 0; i < names.length; ++i) {
            if (names[i].equals(name)) {
                return new ItemStack(SRegistry.getBlock(Names.MISC_BLOCKS), count, i);
            }
        }

        return null;
    }
    
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }
}
