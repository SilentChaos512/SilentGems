package silent.gems.item.tool;

import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class GemPickaxe extends ItemToolSG {

    public GemPickaxe(int id, EnumToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(id, 2.0f, toolMaterial, gemId, supercharged, ItemPickaxe.blocksEffectiveAgainst);
        MinecraftForge.setToolClass(this, "pickaxe", toolMaterial.getHarvestLevel());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }

    @Override
    public boolean canHarvestBlock(Block block) {

        return block == Block.obsidian ? this.toolMaterial.getHarvestLevel() == 3
                : (block != Block.blockDiamond && block != Block.oreDiamond ? (block != Block.oreEmerald && block != Block.blockEmerald ? (block != Block.blockGold
                        && block != Block.oreGold ? (block != Block.blockIron && block != Block.oreIron ? (block != Block.blockLapis
                        && block != Block.oreLapis ? (block != Block.oreRedstone && block != Block.oreRedstoneGlowing ? (block.blockMaterial == Material.rock ? true
                        : (block.blockMaterial == Material.iron ? true : block.blockMaterial == Material.anvil))
                        : this.toolMaterial.getHarvestLevel() >= 2)
                        : this.toolMaterial.getHarvestLevel() >= 1)
                        : this.toolMaterial.getHarvestLevel() >= 1)
                        : this.toolMaterial.getHarvestLevel() >= 2)
                        : this.toolMaterial.getHarvestLevel() >= 2)
                        : this.toolMaterial.getHarvestLevel() >= 2);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {

        return block != null
                && (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock) ? this.efficiencyOnProperMaterial
                : super.getStrVsBlock(stack, block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        StringBuilder s = new StringBuilder();
        s.append("tool.");
        s.append(Strings.RESOURCE_PREFIX);
        s.append("Pickaxe");
        s.append(gemId);
        if (supercharged) {
            s.append("Plus");
        }

        return s.toString();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {
        
        String s = Strings.RESOURCE_PREFIX + "Pickaxe";
        
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
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "ggg", " s ", " s ", 'g', material, 's',
                    new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "ggg", " s ", " s ", 'g', material, 's', "stickWood" }));
        }
    }
}
