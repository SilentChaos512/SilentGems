package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.RecipeMaker;

import javax.annotation.Nullable;
import java.util.List;

public class ItemToolSoul extends Item implements IAddRecipes {
    private static final String NBT_KEY = "ToolSoul";

    public IRecipe recipe;

    public ItemToolSoul() {
        setMaxStackSize(1);
    }

    @Nullable
    public ToolSoul getSoul(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_KEY)) {
            return null;
        }
        return ToolSoul.readFromNBT(stack.getTagCompound().getCompoundTag(NBT_KEY));
    }

    public void setSoul(ItemStack stack, ToolSoul soul) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.getTagCompound().hasKey(NBT_KEY)) {
            stack.getTagCompound().setTag(NBT_KEY, new NBTTagCompound());
        }
        soul.writeToNBT(stack.getTagCompound().getCompoundTag(NBT_KEY));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        ToolSoul soul = getSoul(stack);
        if (soul != null) {
            soul.addInformation(stack, world, list, flag.isAdvanced());
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!stack.isEmpty() && !stack.hasTagCompound()) {
            // Randomize souls with no data!
            ToolSoul soul = ToolSoul.randomSoul();
            setSoul(stack, soul);
        }
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        recipe = recipes.addShaped("tool_soul", new ItemStack(this), " s ", "scs", " s ",
                's', new ItemStack(ModItems.soulGem), 'c', CraftingItems.SOUL_SHELL.getStack());
//        if (ModRecipes.ADD_SOUL_RECIPES) {
//            // FIXME? JEI only shows wheat souls?
//            NonNullList<ItemStack> listSouls = NonNullList.create();
//            ModItems.soulGem.getSubItems(ModItems.soulGem.getCreativeTab(), listSouls);
//            Ingredient ingSouls = Ingredient
//                    .fromStacks(listSouls.toArray(new ItemStack[listSouls.size()]));
//
//            recipe = recipes.addShaped("tool_soul", new ItemStack(this), " s ", "scs", " s ", 's',
//                    ingSouls, 'c', ModItems.craftingMaterial.soulShell);
//        }
    }
}
