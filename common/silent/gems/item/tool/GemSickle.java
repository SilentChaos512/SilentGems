package silent.gems.item.tool;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;
import silent.gems.material.ModMaterials;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.registry.GameRegistry;

public class GemSickle extends ItemTool {

    private int gemId;
    private boolean supercharged;
    private IIcon iconRod, iconHead;

    public static IIcon iconBlank = null;
    public static IIcon[] iconToolDeco = null;
    public static IIcon[] iconToolRod = null;
    public static IIcon[] iconToolHeadL = null;
    public static IIcon[] iconToolHeadM = null;
    public static IIcon[] iconToolHeadR = null;

    public static final Material[] effectiveMaterials = new Material[] { Material.cactus, Material.leaves, Material.plants, Material.vine,
            Material.web };

    public GemSickle(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

        super(2.0f, toolMaterial, Sets.newHashSet(new Block[] {}));

        this.gemId = gemId;
        this.supercharged = supercharged;
        this.setMaxDamage(toolMaterial.getMaxUses());
        addRecipe(new ItemStack(this), gemId, supercharged);
    }

    public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

        ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId + (supercharged ? 16 : 0));

        // Fish tools
        if (gemId == ModMaterials.FISH_GEM_ID) {
            material = new ItemStack(Items.fish);
        }

        if (supercharged) {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g", "gg", "s ", 'g', material, 's',
                    CraftingMaterial.getStack(Names.ORNATE_STICK) }));
        }
        else {
            GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { " g", "gg", "s ", 'g', material, 's', "stickWood" }));
        }
    }

    @Override
    public float func_150893_a(ItemStack stack, Block block) {

        for (Material material : effectiveMaterials) {
            if (block.getMaterial() == material) {
                return this.efficiencyOnProperMaterial;
            }
        }
        return super.func_150893_a(stack, block);
    }
    
    @Override
    public boolean func_150897_b(Block block) {
        
        return block.getMaterial() == Material.web;
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

        return LocalizationHelper.TOOL_PREFIX + "Sickle" + gemId + (supercharged ? "Plus" : "");
    }

    @Override
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.isItemEnchanted() && pass == 5;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {

        if (block.getMaterial() != Material.leaves && block != Blocks.web && block != Blocks.tallgrass && block != Blocks.vine
                && block != Blocks.tripwire && !(block instanceof IShearable)) {
            return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
        }
        else {
            return true;
        }
    }
    
    @Override
    public boolean onBlockStartBreak(ItemStack sickle, int x, int y, int z, EntityPlayer player) {
        
        if (player.worldObj.isRemote) {
            return false;
        }
        
        Block block = player.worldObj.getBlock(x, y, z);
        if (block instanceof IShearable) {
            IShearable target = (IShearable) block;
            if (target.isShearable(sickle, player.worldObj, x, y, z) && EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, sickle) > 0) {
                ArrayList<ItemStack> drops = target.onSheared(sickle, player.worldObj, x, y, z,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, sickle));
                Random rand = new Random();

                for(ItemStack stack : drops)
                {
                    float f = 0.7F;
                    double d  = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d1 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    double d2 = (double)(rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(player.worldObj, (double)x + d, (double)y + d1, (double)z + d2, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    player.worldObj.spawnEntityInWorld(entityitem);
                }

                sickle.damageItem(1, player);
                player.addStat(StatList.mineBlockStatArray[Block.getIdFromBlock(block)], 1);
            }
        }
        return false;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {

        String s = Strings.RESOURCE_PREFIX + "Sickle";

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
        str = Strings.RESOURCE_PREFIX + "SickleWool";
        iconToolRod = new IIcon[16];
        for (int i = 0; i < 16; ++i) {
            iconToolRod[i] = iconRegister.registerIcon(str + i);
        }

        // Blank texture
        iconBlank = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + "Blank");

        // HeadL
        str = Strings.RESOURCE_PREFIX + "Sickle";
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
