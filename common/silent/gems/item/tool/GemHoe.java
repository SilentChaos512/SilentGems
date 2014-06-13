package silent.gems.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class GemHoe extends ItemHoe {

    private int gemId;
    private boolean supercharged;
    private EnumToolMaterial toolMaterial;
    private Icon iconRod, iconHead;
    
    public static Icon iconBlank = null;
    public static Icon[] iconToolDeco = null;
    public static Icon[] iconToolRod = null;
    public static Icon[] iconToolHeadL = null;
    public static Icon[] iconToolHeadM = null;
    public static Icon[] iconToolHeadR = null;

    public GemHoe(int id, EnumToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(id - Reference.SHIFTED_ID_RANGE_CORRECTION, toolMaterial);
        this.gemId = gemId;
        this.supercharged = supercharged;
        this.toolMaterial = toolMaterial;
        this.setMaxDamage(toolMaterial.getMaxUses());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        StringBuilder s = new StringBuilder();
        s.append("tool.");
        s.append(Strings.RESOURCE_PREFIX);
        s.append("Hoe");
        s.append(gemId);
        if (supercharged) {
            s.append("Plus");
        }

        return s.toString();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        
        if (pass == 0) {
            // Rod
            return iconRod;
        }
        else if (pass == 1) {
            // Rod decoration
            if (supercharged) {
                if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_DECO)) {
                    byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_DECO);
                    if (b >= 0 && b < iconToolDeco.length - 1) {
                        return iconToolDeco[b];
                    }
                }
                return iconToolDeco[iconToolDeco.length - 1];
            }
            return iconBlank;
        }
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
        else if (pass == 5) {
            // Rod wool
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_ROD)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_ROD);
                if (b >= 0 && b < iconToolRod.length) {
                    return iconToolRod[b];
                }
            }
            return iconBlank;
        }
        else {
            return iconBlank;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        String s = Strings.RESOURCE_PREFIX + "Hoe";
        
        if (supercharged) {
            iconRod = iconRegister.registerIcon(s + "_RodOrnate");
        }
        else {
            iconRod = iconRegister.registerIcon(s + "_RodNormal");
        }
        
        s += gemId;
        
        iconHead = iconRegister.registerIcon(s);
        
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
        
        // HeadL
        str = Strings.RESOURCE_PREFIX + "Hoe";
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderPasses(int meta) {
        
        return 6;
    }

    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));
        if (material.itemID == stack2.itemID && material.getItemDamage() == stack2.getItemDamage()) {
            return true;
        }
        else {
            return super.getIsRepairable(stack1, stack2);
        }
    }

    public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));
        
        // Fish tools
        if (gemId == ModMaterials.FISH_GEM_ID) {
            material = new ItemStack(Item.fishRaw);
        }
        
        if (supercharged) {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg ", " s ", " s ", 'g', material, 's',
                    new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg ", " s ", " s ", 'g', material, 's', "stickWood" }));
        }
    }
    
    public int getGemId() {
        
        return gemId;
    }
}
