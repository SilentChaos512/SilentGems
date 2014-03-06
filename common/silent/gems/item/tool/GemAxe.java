package silent.gems.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class GemAxe extends ItemToolSG {

    public GemAxe(int id, EnumToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(id, 3.0f, toolMaterial, gemId, supercharged, ItemAxe.blocksEffectiveAgainst);
        MinecraftForge.setToolClass(this, "axe", toolMaterial.getHarvestLevel());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {

        return block != null
                && (block.blockMaterial == Material.wood || block.blockMaterial == Material.plants || block.blockMaterial == Material.vine || block.blockMaterial == Material.leaves) ? this.efficiencyOnProperMaterial
                : super.getStrVsBlock(stack, block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        StringBuilder s = new StringBuilder();
        s.append("tool.");
        s.append(Strings.RESOURCE_PREFIX);
        s.append("Axe");
        s.append(gemId);
        if (supercharged) {
            s.append("Plus");
        }

        return s.toString();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {
        
        String s = Strings.RESOURCE_PREFIX + "Axe";
        
        if (supercharged) {
            iconRod = iconRegister.registerIcon(s + "_RodOrnate");
        }
        else {
            iconRod = iconRegister.registerIcon(s + "_RodNormal");
        }
        
        s += gemId;
        
        iconHead = iconRegister.registerIcon(s);
    }

    public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));
        if (supercharged) {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg ", "gs ", " s ", 'g', material, 's',
                    new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg ", "gs ", " s ", 'g', material, 's', "stickWood" }));
        }
    }
}
