package silent.gems.item.tool;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import cpw.mods.fml.common.registry.GameRegistry;


public class GemBow extends ItemBow {
    
    private final int gemId;

    public GemBow(ToolMaterial toolMaterial, int gemId) {
        
        super();
        this.gemId = gemId;
        this.setMaxDamage(toolMaterial.getMaxUses());
        addRecipe(new ItemStack(this), gemId);
    }
    
    public static void addRecipe(ItemStack tool, int gemId) {
        
        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ROD), 1, gemId);
        GameRegistry.addShapedRecipe(tool, " rs", "r s", " rs", 'r', material, 's', CraftingMaterial.getStack(Names.GILDED_STRING));
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        
        return super.getIcon(stack, pass);
        // TODO Fancy decorations!
    }
    
    @Override
    public int getRenderPasses(int meta) {

        // TODO: How many?
        return 6;
    }
    
    @Override
    public boolean requiresMultipleRenderPasses() {

        return true;
    }
    
    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId);
        if (material.getItem() == stack2.getItem() && material.getItemDamage() == stack2.getItemDamage()) {
            return true;
        }
        else {
            return super.getIsRepairable(stack1, stack2);
        }
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return LocalizationHelper.TOOL_PREFIX + "Bow" + gemId;
    }
    
    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        // TODO: Which render pass?
        return stack.isItemEnchanted() && pass == 0;
    }
    
    @Override
    public void registerIcons(IIconRegister reg) {
        
        // TODO register icons!
        
        String s = Strings.RESOURCE_PREFIX + "Bow";
        
        super.registerIcons(reg); // temp
    }
    
    public int getGemId() {

        return gemId;
    }
}
