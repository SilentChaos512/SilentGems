package silent.gems.item.tool;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import silent.gems.core.registry.SRegistry;
import silent.gems.item.ItemSG;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.Icon;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class GemSword extends ItemSword {

    private int gemId;
    private boolean supercharged;
    private EnumToolMaterial toolMaterial;
    private Icon iconRod, iconHead;

    public GemSword(int id, EnumToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(id - Reference.SHIFTED_ID_RANGE_CORRECTION, toolMaterial);
        this.gemId = gemId;
        this.supercharged = supercharged;
        this.toolMaterial = toolMaterial;
        this.setMaxDamage(toolMaterial.getMaxUses());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }
    
//    @Override
//    public float func_82803_g() {
//        
//        return this.weaponDamage;
//    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        StringBuilder s = new StringBuilder();
        s.append("tool.");
        s.append(Strings.RESOURCE_PREFIX);
        s.append("Sword");
        s.append(gemId);
        if (supercharged) {
            s.append("Plus");
        }

        return s.toString();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {

        String s = Strings.RESOURCE_PREFIX + "Sword";
        
        if (supercharged) {
            iconRod = iconRegister.registerIcon(s + "_RodOrnate");
        }
        else {
            iconRod = iconRegister.registerIcon(s + "_RodNormal");
        }
        
        s += gemId;
        
        iconHead = iconRegister.registerIcon(s);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamageForRenderPass(int meta, int pass) {
        
        if (pass == 0) {
            return iconRod;
        }
        else {
            return iconHead;
        }
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
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g ", " g ", " s ", 'g', material, 's',
                    new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g ", " g ", " s ", 'g', material, 's', "stickWood" }));
        }
    }

//    @Override
//    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player) {
//
//        int lvl;
//
//        // Nihil enchantment
//        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.nihil.effectId, stack) > 0) {
//            target.curePotionEffects(new ItemStack(Item.bucketMilk));
//            // Chaos cost of Nihil
//            ChaosHandler.addChaos(Config.CHAOS_COST_NIHIL.value);
//        }
//
//        // Ice aspect enchantment
//        lvl = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.iceAspect.effectId, stack);
//        if (lvl > 0) {
//            // Slow down the target.
//            target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, lvl, false));
//        }
//
//        return super.hitEntity(stack, target, player);
//    }
}
