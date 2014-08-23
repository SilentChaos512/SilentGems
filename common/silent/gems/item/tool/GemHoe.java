package silent.gems.item.tool;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;
import cpw.mods.fml.common.registry.GameRegistry;

public class GemHoe extends ItemHoe {

    private int gemId;
    private boolean supercharged;
    private IIcon iconRod, iconHead;

    public static IIcon iconBlank = null;
    public static IIcon[] iconToolDeco = null;
    public static IIcon[] iconToolRod = null;
    public static IIcon[] iconToolHeadL = null;
    public static IIcon[] iconToolHeadM = null;
    public static IIcon[] iconToolHeadR = null;

    public GemHoe(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(toolMaterial);

        this.gemId = gemId;
        this.supercharged = supercharged;
        this.setMaxDamage(toolMaterial.getMaxUses());
        addRecipe(new ItemStack(this), gemId, supercharged);
        this.setCreativeTab(SilentGems.tabSilentGems);
    }

    public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));

        // Fish tools
        if (gemId == ModMaterials.FISH_GEM_ID) {
            material = new ItemStack(Items.fish);
        }

        if (supercharged) {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg", " s", " s", 'g', material, 's',
                    CraftingMaterial.getStack(Names.ORNATE_STICK) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg", " s", " s", 'g', material, 's', "stickWood" }));
        }
    }

    public int getGemId() {

        return gemId;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {

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
        else if (pass == 2) {
            // Rod wool
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_ROD)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_ROD);
                if (b >= 0 && b < iconToolRod.length) {
                    return iconToolRod[b];
                }
            }
            return iconBlank;
        }
        else if (pass == 3) {
            // HeadM
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_MIDDLE)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_MIDDLE);
                if (b >= 0 && b < iconToolHeadM.length) {
                    return iconToolHeadM[b];
                }
            }
            return iconHead;
        }
        else if (pass == 4) {
            // HeadL
            if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_LEFT)) {
                byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_LEFT);
                if (b >= 0 && b < iconToolHeadL.length) {
                    return iconToolHeadL[b];
                }
            }
            return iconBlank;
        }
        else if (pass == 5) {
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
            return iconBlank;
        }
    }

    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));
        if (material.getItem() == stack2.getItem() && material.getItemDamage() == stack2.getItemDamage()) {
            return true;
        }
        else {
            return super.getIsRepairable(stack1, stack2);
        }
    }

    @Override
    public int getRenderPasses(int meta) {

        return 6;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return LocalizationHelper.TOOL_PREFIX + "Hoe" + gemId + (supercharged ? "Plus" : "");
    }
    
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.isItemEnchanted() && pass == 5;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {

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
        iconToolDeco = new IIcon[EnumGem.all().length + 1];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolDeco[i] = iconRegister.registerIcon(str + i);
        }
        iconToolDeco[iconToolDeco.length - 1] = iconRegister.registerIcon(str);

        // Rod
        str = Strings.RESOURCE_PREFIX + "RodWool";
        iconToolRod = new IIcon[16];
        for (int i = 0; i < 16; ++i) {
            iconToolRod[i] = iconRegister.registerIcon(str + i);
        }

        // Blank texture
        iconBlank = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + "Blank");

        // HeadL
        str = Strings.RESOURCE_PREFIX + "Hoe";
        iconToolHeadL = new IIcon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadL[i] = iconRegister.registerIcon(str + i + "L");
        }
        // HeadM
        iconToolHeadM = new IIcon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadM[i] = iconRegister.registerIcon(str + i);
        }
        // HeadR
        iconToolHeadR = new IIcon[EnumGem.all().length];
        for (int i = 0; i < EnumGem.all().length; ++i) {
            iconToolHeadR[i] = iconRegister.registerIcon(str + i + "R");
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses() {

        return true;
    }
}
