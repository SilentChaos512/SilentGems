package net.silentchaos512.gems.item.tool;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;
import java.util.Set;

public class ItemGemAxe extends ItemAxe implements ITool, IAddRecipes, ICustomModel {
    public static final Set<Material> BASE_EFFECTIVE_MATERIALS = Sets.newHashSet(Material.GOURD, Material.WOOD);
    public static final Material[] EXTRA_EFFECTIVE_MATERIALS = {Material.WOOD, Material.LEAVES, Material.PLANTS, Material.VINE};

    public ItemGemAxe() {
        super(ToolHelper.FAKE_MATERIAL, 0f, 0f);
        setNoRepair();
    }

    public ItemStack constructTool(boolean supercharged, ItemStack material) {
        return constructTool(supercharged, material, material, material);
    }

    public ItemStack constructTool(boolean supercharged, ItemStack... materials) {
        ItemStack rod = supercharged ? CraftingItems.ORNATE_GOLD_ROD.getStack() : new ItemStack(Items.STICK);
        return ToolHelper.constructTool(this, rod, materials);
    }

    // ===============
    // ITool overrides
    // ===============

    public ConfigOptionToolClass getConfig() {
        return GemsConfig.axe;
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {
        if (getConfig().isDisabled)
            return ItemStack.EMPTY;
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeSpeedModifier() {
        return -3.0f;
    }

    @Override
    public float getMagicDamageModifier() {
        return 0.0f;
    }

    @Override
    public float getMeleeDamageModifier() {
        return 5.0f;
    }

    @Override
    public boolean isDiggingTool() {
        return true;
    }

    @Override
    public Material[] getExtraEffectiveMaterials(ItemStack stack) {
        return EXTRA_EFFECTIVE_MATERIALS;
    }

    // ==============
    // Item overrides
    // ==============

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return ToolHelper.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        boolean canceled = super.onBlockStartBreak(stack, pos, player);
        if (!canceled) {
            ToolHelper.onBlockStartBreak(stack, pos, player);
        }
        return canceled;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {

        return ToolHelper.getMaxDamage(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return ToolRenderHelper.instance.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return ToolRenderHelper.instance.hasEffect(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return ToolRenderHelper.instance.getRarity(stack);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return ToolHelper.getItemEnchantability(stack);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        ToolHelper.onUpdate(tool, world, entity, itemSlot, isSelected);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        return ToolHelper.onEntityItemUpdate(entityItem);
    }

    // ==================
    // ItemTool overrides
    // ==================

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return ToolHelper.getDigSpeed(stack, state, EXTRA_EFFECTIVE_MATERIALS);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return ToolHelper.onBlockDestroyed(stack, world, state, pos, entityLiving);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, EntityPlayer player, IBlockState state) {
        if (super.getHarvestLevel(stack, toolClass, player, state) < 0 || ToolHelper.isBroken(stack)) {
            return -1;
        }
        return ToolHelper.getHarvestLevel(stack);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return ToolHelper.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity1, EntityLivingBase entity2) {
        return ToolHelper.hitEntity(stack, entity1, entity2);
    }

    @Override
    public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {
        return ToolHelper.getIsRepairable(stack1, stack2);
    }

    // Forge ItemStack-sensitive version
    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack tool) {
        return canHarvestBlock(state, ToolHelper.getHarvestLevel(tool));
    }

    // Vanilla version... Not good because we can't get the actual harvest level.
    @Override
    public boolean canHarvestBlock(IBlockState state) {
        // Assume a very high level since we can't get the actual value.
        return canHarvestBlock(state, 10);
    }

    private boolean canHarvestBlock(IBlockState state, int toolLevel) {
        // Wrong harvest level?
        if (state.getBlock().getHarvestLevel(state) > toolLevel)
            return false;
        // Included in base materials?
        if (BASE_EFFECTIVE_MATERIALS.contains(state.getMaterial()))
            return true;
        // Included in extra materials?
        for (Material mat : EXTRA_EFFECTIVE_MATERIALS)
            if (mat.equals(state.getMaterial()))
                return true;

        return super.canHarvestBlock(state);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (!getConfig().isDisabled)
            ToolHelper.addExampleRecipe(this, "hh", "hr", " r");
    }

    @Override
    public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {
        ToolRenderHelper.getInstance().addInformation(stack, world, list, flag);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        list.addAll(ToolHelper.getSubItems(this, 3));
    }

    @Override
    public void registerModels() {
        SilentGems.registry.setModel(this, 0, "tool");
    }
}
