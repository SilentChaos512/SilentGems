package silent.gems.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class MiscBlock extends BlockSG {

    public static final String[] names = { Names.CHAOS_ESSENCE_BLOCK };

    public MiscBlock(int id) {

        super(id, Material.iron);
        icons = new Icon[names.length];
        setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);
        setUnlocalizedName(Names.MISC_BLOCKS);
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

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        for (int i = 0; i < names.length; ++i) {
            icons[i] = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + names[i]);
        }
    }

    @Override
    public void addRecipes() {

        GameRegistry.addShapedRecipe(getStack(Names.CHAOS_ESSENCE_BLOCK), "ccc", "ccc", "ccc", 'c',
                CraftingMaterial.getStack(Names.CHAOS_ESSENCE));
    }
}
