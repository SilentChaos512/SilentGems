package silent.gems.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSG extends Block {

    public Icon[] icons = null;
    public boolean hasSubtypes = false;
    /**
     * If set to true, the block will automatically get unlocalized names and textures for each of the basic gem types.
     */
    protected boolean gemSubtypes = false;
    /**
     * This is here because Block.unlocalizedName is private.
     */
    protected String blockName = "";

    public BlockSG(int id, Material material) {

        super(id, material);

        setHardness(3.0f);
        setResistance(10.0f);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        setUnlocalizedName(Integer.toString(id));
    }

    /**
     * Registers blockIcon using the unlocalized name for the texture name.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister reg) {

        if (gemSubtypes) {
            registerIconsForGemSubtypes(reg);
        }
        else {
            blockIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + blockName);
        }
    }

    /**
     * Fills the icon array (and creates it, if necessary) with icons corresponding to the six basic gems.
     * 
     * @param reg
     * @param blockName
     */
    @SideOnly(Side.CLIENT)
    public void registerIconsForGemSubtypes(IconRegister reg) {

        if (icons == null || icons.length != EnumGem.all().length) {
            icons = new Icon[EnumGem.all().length];
        }

        for (int i = 0; i < EnumGem.all().length; ++i) {
            icons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + this.blockName + EnumGem.all()[i].id);
        }
    }

    /**
     * If the block hasSubtypes, tries to get an icon from the icons array (does not error check the array). Otherwise,
     * the blockIcon is returned.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int meta) {

        if (hasSubtypes) {
            return icons[meta];
        }
        else {
            return blockIcon;
        }
    }

    /**
     * If the block hasSubtypes, this will add one ItemStack to the subItems list for each icon.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(int par1, CreativeTabs tab, List subItems) {

        if (hasSubtypes) {
            for (int i = 0; i < icons.length; ++i) {
                subItems.add(new ItemStack(this, 1, i));
            }
        }
        else {
            super.getSubBlocks(par1, tab, subItems);
        }
    }

    /**
     * Returns meta if the block hasSubtypes, otherwise 0.
     */
    @Override
    public int damageDropped(int meta) {

        if (hasSubtypes) {
            return meta;
        }
        else {
            return 0;
        }
    }

    /**
     * Returns an unlocalized name based on mod id and blockName.
     */
    @Override
    public String getUnlocalizedName() {

        return getUnlocalizedName(this.blockName);
    }

    /**
     * Returns an unlocalized name based on mod id and blockName.
     * 
     * @param tileName
     *            The String to appear after the resource prefix
     * @return
     */
    public String getUnlocalizedName(String tileName) {

        return (new StringBuilder()).append("tile.").append(Strings.RESOURCE_PREFIX).append(tileName).toString();
    }

    /**
     * Sets Block.unlocalizedName and blockName (because unlocalizedName is private).
     */
    @Override
    public Block setUnlocalizedName(String blockName) {

        this.blockName = blockName;
        return super.setUnlocalizedName(blockName);
    }

    public Block setHasGemSubtypes(boolean value) {

        gemSubtypes = value;
        return this;
    }

    public Block setHasSubtypes(boolean value) {

        hasSubtypes = true;
        return this;
    }

    /**
     * This should be overridden in any deriving class and called in ModBlocks.initBlockRecipes.
     */
    public void addRecipes() {

    }

    /**
     * Should be overridden if the deriving class needs ore dictionary entries.
     */
    public void addOreDict() {

    }
}
