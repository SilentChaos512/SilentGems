package silent.gems.item.tool;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LogHelper;
import silent.gems.core.util.NBTHelper;
import silent.gems.item.ItemSG;
import silent.gems.item.TorchBandolier;
import silent.gems.lib.EnumGem;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemToolSG extends ItemSG {

    private Block[] blocksEffectiveAgainst;
    public float efficiencyOnProperMaterial = 4.0f;
    public float damageVsEntity;
    protected EnumToolMaterial toolMaterial;
    protected boolean supercharged;
    protected int gemId;
    protected Icon iconRod, iconHead;
    
    protected static boolean sharedTexturesLoaded = false;
    public static Icon iconBlank = null;
    public static Icon[] iconToolDeco = null;
    public static Icon[] iconToolRod = null;
    
    public ItemToolSG(int id, float baseDamage, EnumToolMaterial toolMaterial, int gemId, boolean supercharged, Block[] blocksEffectiveAgainst) {

        super(id);
        this.supercharged = supercharged;
        this.gemId = gemId;
        this.toolMaterial = toolMaterial;
        this.blocksEffectiveAgainst = blocksEffectiveAgainst;
        this.maxStackSize = 1;
        //this.setMaxDamage(supercharged ? 4 * toolMaterial.getMaxUses() : toolMaterial.getMaxUses());
        //this.efficiencyOnProperMaterial = toolMaterial.getEfficiencyOnProperMaterial() + (supercharged ? 4.0f : 0.0f);
        //this.damageVsEntity = baseDamage + toolMaterial.getDamageVsEntity() + (supercharged ? 1 : 0);
        this.setMaxDamage(toolMaterial.getMaxUses());
        this.efficiencyOnProperMaterial = toolMaterial.getEfficiencyOnProperMaterial();
        this.damageVsEntity = baseDamage + toolMaterial.getDamageVsEntity();
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
            float hitZ) {

        boolean used = false;
        int toolSlot = player.inventory.currentItem;
        int itemSlot = toolSlot + 1;
        ItemStack nextStack = null;

        if (toolSlot < 8) {
            nextStack = player.inventory.getStackInSlot(itemSlot);
            if (nextStack != null) {
                Item item = nextStack.getItem();
                if (item instanceof ItemBlock || item instanceof TorchBandolier) {
                    ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];

                    int px = x + d.offsetX;
                    int py = y + d.offsetY;
                    int pz = z + d.offsetZ;
                    int playerX = (int) Math.floor(player.posX);
                    int playerY = (int) Math.floor(player.posY);
                    int playerZ = (int) Math.floor(player.posZ);

                    // Check for overlap with player, except for torches and
                    // torch bandolier
                    if (item.itemID != Block.torchWood.blockID && item.itemID != SRegistry.getItem(Names.TORCH_BANDOLIER).itemID
                            && px == playerX && (py == playerY || py == playerY + 1 || py == playerY - 1) && pz == playerZ) {
                        return false;
                    }

                    used = item.onItemUse(nextStack, player, world, x, y, z, side, hitX, hitY, hitZ);
                    if (nextStack.stackSize < 1) {
                        nextStack = null;
                        player.inventory.setInventorySlotContents(itemSlot, null);
                    }
                }
            }
        }

        return used;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {

        return stack.isItemEnchanted() ? EnumRarity.rare : EnumRarity.common;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {

        return stack.isItemEnchanted() && pass == 5;
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
    
    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(ItemStack stack, int pass) {
        
        if (pass == 0) {
            return iconRod;
        }
        else if (pass == 1) {
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
        else if (pass == 5) {
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

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return getUnlocalizedName(Integer.toString(itemID), "tool");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {

        stack.damageItem(2, entity2);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int id, int x, int y, int z, EntityLivingBase entity) {

        if ((double) Block.blocksList[id].getBlockHardness(world, x, y, z) != 0.0D && Block.blocksList[id].blockMaterial != Material.leaves) {
            stack.damageItem(1, entity);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {

        return true;
    }

    @Override
    public int getItemEnchantability() {

        return this.toolMaterial.getEnchantability();
    }

    public String getToolMaterialName() {

        return this.toolMaterial.toString();
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

    @Override
    public Multimap getItemAttributeModifiers() {

        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e,
                "Tool modifier", (double) this.damageVsEntity, 0));
        return multimap;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block, int meta) {

        if (ForgeHooks.isToolEffective(stack, block, meta)) {
            return efficiencyOnProperMaterial;
        }
        return getStrVsBlock(stack, block);
    }
}
