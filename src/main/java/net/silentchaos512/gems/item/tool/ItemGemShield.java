package net.silentchaos512.gems.item.tool;

import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.api.ITool;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGemShield extends ItemShield implements ITool, IAddRecipes {
    public ItemGemShield() {
        setNoRepair();
    }

    public boolean shouldBlockDamage(EntityLivingBase entityLiving) {
        if (!(entityLiving instanceof EntityPlayer))
            return false;

        EntityPlayer player = (EntityPlayer) entityLiving;
        if (!player.isActiveItemStackBlocking() || player.getActiveItemStack().getItem() != this)
            return false;

        return !ToolHelper.isBroken(player.getActiveItemStack());
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return ToolHelper.getAttributeModifiers(slot, stack);
    }

    // ================
    // = Construction =
    // ================

    @Override
    public ConfigOptionToolClass getConfig() {
        return GemsConfig.shield;
    }

    @Override
    public ItemStack constructTool(ItemStack rod, ItemStack... materials) {
        if (getConfig().isDisabled)
            return ItemStack.EMPTY;

        if (materials.length == 1)
            return constructTool(rod, materials[0], materials[0], materials[0]);
        return ToolHelper.constructTool(this, rod, materials);
    }

    @Override
    public float getMeleeDamage(ItemStack tool) {
        return Math.max(0, (getMeleeDamageModifier() + ToolHelper.getMeleeDamage(tool)) / 2);
    }

    @Override
    public float getMagicDamage(ItemStack tool) {
        return 0.0f;
    }

    @Override
    public float getMeleeDamageModifier() {
        return -4.0f;
    }

    @Override
    public float getMagicDamageModifier() {
        return 0.0f;
    }

    @Override
    public float getMeleeSpeedModifier() {
        return -3.2f;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (getConfig().isDisabled)
            return;

        String[] lines = {"hwh", "wrw", " h "};
        ToolHelper.addExampleRecipe(this, EnumMaterialTier.values(), lines, 'w', "plankWood");
    }

    // ========================
    // = ItemShield overrides =
    // ========================

    public EnumAction getItemUseAction(ItemStack stack) {
        return ToolHelper.isBroken(stack) ? EnumAction.NONE : EnumAction.BLOCK;
    }

    // ==================
    // = Item overrides =
    // ==================

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

    @Override
    public boolean isShield(ItemStack stack, @Nullable EntityLivingBase entity) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List list, ITooltipFlag flag) {
        ToolRenderHelper.getInstance().addInformation(stack, world, list, flag);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        list.addAll(ToolHelper.getSubItems(this, 3));
    }
}
