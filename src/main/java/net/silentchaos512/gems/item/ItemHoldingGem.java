package net.silentchaos512.gems.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class ItemHoldingGem extends ItemBlockPlacer {
    public static final String NBT_BLOCK_PLACED = "sg_block_placed";
    public static final String NBT_GEM_ID = "sg_gem_id";

    public ItemHoldingGem() {
        super(4096);
    }

    @Override
    public IBlockState getBlockPlaced(ItemStack stack) {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_BLOCK_PLACED))
            return null;

        int stateId = stack.getTagCompound().getInteger(NBT_BLOCK_PLACED);
        return Block.getStateById(stateId);
    }

    public void setBlockPlaced(ItemStack stack, IBlockState state) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger(NBT_BLOCK_PLACED, Block.getStateId(state));
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        IBlockState state = getBlockPlaced(stack);
        if (state == null) {
            list.add(TextFormatting.ITALIC + SilentGems.i18n.subText(this, "desc"));
            return;
        }

        Block block = state.getBlock();
        ItemStack placedStack = new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(state));
        list.add(placedStack.getDisplayName());

        super.addInformation(stack, world, list, flag);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        for (EnumGem gem : EnumGem.values()) {
            ItemStack stack = new ItemStack(this);
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setShort(NBT_GEM_ID, (short) gem.ordinal());
            recipes.addShapedOre("holding_gem_" + gem.name(), stack, "gcg", "s s", "gcg", 'g', "ingotGold", 'c',
                    ModItems.craftingMaterial.chaosEssenceEnriched, 's', gem.getItemOreName());
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        for (EnumGem gem : EnumGem.values()) {
            list.add(construct(gem));
        }
    }

    public ItemStack construct(EnumGem gem) {
        ItemStack stack = new ItemStack(this);
        setRemainingBlocks(stack, 0);
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setShort(NBT_GEM_ID, (short) gem.ordinal());
        return stack;
    }
}
