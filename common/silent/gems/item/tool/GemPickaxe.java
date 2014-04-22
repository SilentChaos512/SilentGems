package silent.gems.item.tool;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GemPickaxe extends ItemToolSG {
    
    protected static boolean pickaxeTexturesLoaded = false;
    public static Icon[] iconToolHeadL = null;
    public static Icon[] iconToolHeadM = null;
    public static Icon[] iconToolHeadR = null;

    public GemPickaxe(int id, EnumToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(id, 2.0f, toolMaterial, gemId, supercharged, ItemPickaxe.blocksEffectiveAgainst);
        MinecraftForge.setToolClass(this, "pickaxe", toolMaterial.getHarvestLevel());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        
        if (pass == 2) {
            // HeadM
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_MIDDLE)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_MIDDLE);
                if (b >= 0 && b < iconToolHeadM.length) {
                    //LogHelper.debug(iconToolHeadM[b]);
                    return iconToolHeadM[b];
                }
            }
            return iconHead;
        }
        else if (pass == 3) {
            // HeadL
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_LEFT)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_LEFT);
                if (b >= 0 && b < iconToolHeadL.length) {
                    return iconToolHeadL[b];
                }
            }
            return iconBlank;
        }
        else if (pass == 4) {
            // HeadR
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_RIGHT)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_RIGHT);
                if (b >= 0 && b < iconToolHeadR.length) {
                    return iconToolHeadR[b];
                }
            }
            return iconBlank;
        }
        else {
            return super.getIcon(stack, pass);
        }
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
                && (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock || block.blockMaterial == Material.glass) ? this.efficiencyOnProperMaterial
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
        
        // Shared decoration textures.
        // Deco
        String str = Strings.RESOURCE_PREFIX + "ToolDeco";
        iconToolDeco = new Icon[EnumGem.all().length + 1];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolDeco[i] = iconRegister.registerIcon(str + i);
        }
        iconToolDeco[iconToolDeco.length - 1] = iconRegister.registerIcon(str);
        
        // Rod
        str = Strings.RESOURCE_PREFIX + "RodWool";
        iconToolRod = new Icon[16];
        for (int i = 0; i < 16; ++i) {
            iconToolRod[i] = iconRegister.registerIcon(str + i);
        }
        
        // Blank texture
        iconBlank = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + "Blank");
        
        // Pickaxe decoration textures
        // HeadL
        str = Strings.RESOURCE_PREFIX + "Pickaxe";
        iconToolHeadL = new Icon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadL[i] = iconRegister.registerIcon(str + i + "L");
        }
        // HeadM
        iconToolHeadM = new Icon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadM[i] = iconRegister.registerIcon(str + i);
        }
        // HeadR
        iconToolHeadR = new Icon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadR[i] = iconRegister.registerIcon(str + i + "R");
        }
    }

    public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));

        // Fish tools
        if (gemId == ModMaterials.FISH_GEM_ID) {
            material = new ItemStack(Item.fishRaw);
        }

        if (supercharged) {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "ggg", " s ", " s ", 'g', material, 's',
                    new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "ggg", " s ", " s ", 'g', material, 's', "stickWood" }));
        }
    }
}
