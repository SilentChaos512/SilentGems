package net.silentchaos512.gems.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModBlocks;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.tile.TileChaosNode;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.registry.ICustomModel;
import net.silentchaos512.lib.registry.RecipeMaker;

public class ItemNodeMover extends Item implements IAddRecipes, ICustomModel {
    private static final int META_EMPTY = 0;
    private static final int META_FILLED = 1;
    private static final int META_USED = 2;

    public ItemNodeMover() {
        setMaxStackSize(1);
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        ItemStack empty = new ItemStack(this, 1, META_EMPTY);
        ItemStack spent = new ItemStack(this, 1, META_USED);
        ItemStack chaosCore = CraftingItems.CHAOS_CORE.getStack();
        ItemStack netherShard = CraftingItems.NETHER_SHARD.getStack();
        ItemStack enderFrost = CraftingItems.ENDER_FROST.getStack();

        recipes.addSurroundOre("node_mover", empty, chaosCore, netherShard, enderFrost);
        recipes.addShaped("node_mover_recharge", empty, "sms", 's', netherShard, 'm', spent);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);

        if (stack.getItemDamage() >= META_USED) {
            return EnumActionResult.PASS;
        }

        if (stack.hasTagCompound()) {
            EnumActionResult result = releaseNode(stack, worldIn, pos, facing);
            if (result == EnumActionResult.SUCCESS && playerIn instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, pos, stack);
            return result;
        }

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileChaosNode) {
            return captureNode(stack, worldIn, pos, (TileChaosNode) tile);
        }

        return EnumActionResult.PASS;
    }

    private EnumActionResult releaseNode(ItemStack stack, World worldIn, BlockPos pos, EnumFacing facing) {
        pos = pos.offset(facing);
        if (!worldIn.isAirBlock(pos)) {
            return EnumActionResult.PASS;
        }

        worldIn.setBlockState(pos, ModBlocks.chaosNode.getDefaultState(), 2);
        TileChaosNode tileNode = (TileChaosNode) worldIn.getTileEntity(pos);
        NBTTagCompound compound = stack.getTagCompound();
        compound.setInteger("x", pos.getX());
        compound.setInteger("y", pos.getY());
        compound.setInteger("z", pos.getZ());
        tileNode.readFromNBT(compound);

        stack.setTagCompound(null);
        stack.setItemDamage(META_USED);

        return EnumActionResult.SUCCESS;
    }

    private EnumActionResult captureNode(ItemStack stack, World worldIn, BlockPos pos, TileChaosNode tileNode) {
        NBTTagCompound compound = new NBTTagCompound();
        tileNode.writeToNBT(compound);
        stack.setTagCompound(compound);
        stack.setItemDamage(META_FILLED);

        worldIn.setBlockToAir(pos);

        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + stack.getItemDamage();
    }

    @Override
    public void registerModels() {
        for (int i = 0; i < 3; ++i) {
            SilentGems.registry.setModel(this, i, Names.NODE_MOVER + i);
        }
    }
}
