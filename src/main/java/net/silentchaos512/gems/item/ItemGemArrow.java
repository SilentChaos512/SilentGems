package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.ToolStats;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.entity.EntityGemArrow;
import net.silentchaos512.gems.util.ToolHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemGemArrow extends ItemArrow {
    public static final String NBT_STATS = "SGArrowStats";
    public static final String NBT_DAMAGE = "Damage";
    public static final String NBT_COLOR_HEAD = "ColorHead";
    public static final String NBT_COLOR_SHAFT = "ColorShaft";
    public static final String NBT_COLOR_FLETCHING = "ColorFletching";

    public ItemStack construct(ItemStack... stacks) {
        ItemStack result = new ItemStack(this);
        result.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbtStats = new NBTTagCompound();

        Map<ToolPart, EnumMaterialGrade> partMap = new LinkedHashMap<>();
        for (ItemStack stack : stacks) {
            ToolPart part = ToolPartRegistry.fromStack(stack);
            EnumMaterialGrade grade = EnumMaterialGrade.fromStack(stack);
            partMap.put(part, grade);

            // Set colors
            if (part instanceof ToolPartMain) {
                nbtStats.setInteger(NBT_COLOR_HEAD, part.getColor(result, ToolPartPosition.ROD_DECO, 0));
            } else if (part instanceof ToolPartRod) {
                nbtStats.setInteger(NBT_COLOR_SHAFT, part.getColor(result, ToolPartPosition.ROD_DECO, 0));
            }
        }

        nbtStats.setInteger(NBT_COLOR_FLETCHING, 0xFFFFFF);

        // Using standard tool stat calculations
        ToolStats stats = new ToolStats(result, partMap);
        stats.calculate();

        // Set stats NBT
        nbtStats.setFloat(NBT_DAMAGE, stats.meleeDamage);
        result.getTagCompound().setTag(NBT_STATS, nbtStats);

        // Count based on durability
        result.setCount(MathHelper.clamp((int) Math.floor(stats.durability / 48f), 4, 64));

        // Create name
        result.setStackDisplayName(ToolHelper.createToolName(this, stacks));

        return result;
    }

    @Override
    public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityArrow entity = new EntityGemArrow(worldIn, shooter, stack);
        entity.setDamage(getBaseDamage(stack));
        return entity;
    }

    public double getBaseDamage(ItemStack stack) {
        if (stack.isEmpty() || !stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_STATS)) {
            return 2.0;
        }
        float damage = stack.getTagCompound().getCompoundTag(NBT_STATS).getFloat(NBT_DAMAGE);
        return 2.0 + (damage - 1.5) / 2.0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(String.format("Damage: %.1f", getBaseDamage(stack)));
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;

        ItemStack rodWood = new ItemStack(Items.STICK);
        ItemStack rodGold = CraftingItems.ORNATE_GOLD_ROD.getStack();

        for (ToolPartMain part : ToolPartRegistry.getMains()) {
            ItemStack main = part.getCraftingStack();
            if (!main.isEmpty() && !part.isBlacklisted(main)) {
                ItemStack rod = part.getTier() == EnumMaterialTier.SUPER ? rodGold : rodWood;
                ItemStack arrow = construct(main, rod);
                if (!arrow.isEmpty()) {
                    arrow.setCount(1);
                    list.add(arrow);
                }
            }
        }
    }
}
