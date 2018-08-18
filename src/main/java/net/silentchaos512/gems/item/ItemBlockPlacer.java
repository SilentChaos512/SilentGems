package net.silentchaos512.gems.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IBlockPlacer;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.lib.item.ILeftClickItem;
import net.silentchaos512.lib.registry.IAddRecipes;
import net.silentchaos512.lib.util.*;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemBlockPlacer extends Item implements IBlockPlacer, ILeftClickItem, IAddRecipes {
    private static final int ABSORB_DELAY = 20;
    private static final String NBT_AUTO_FILL = "AutoFill";
    private static final String NBT_BLOCK_COUNT = "BlockCount";

    private int maxBlocks;

    ItemBlockPlacer(int maxBlocks) {
        this.maxBlocks = maxBlocks;
        setMaxDamage(0);
        setNoRepair();
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flag) {
        boolean autoFillOn = getAutoFillMode(stack);
        int currentBlocks = getRemainingBlocks(stack);
        int maxBlocks = getMaxBlocksStored(stack);

        list.add(SilentGems.i18n.subText("blockplacer", "item", "count", currentBlocks, maxBlocks));
        String onOrOff = SilentGems.i18n.miscText("state." + (autoFillOn ? "on" : "off"));
        list.add(SilentGems.i18n.subText("blockplacer", "item", "autoFill", onOrOff));
    }

    @Override
    @Nullable
    public abstract IBlockState getBlockPlaced(ItemStack stack);

    public int getBlockMetaDropped(ItemStack stack) {
        IBlockState state = getBlockPlaced(stack);
        return state.getBlock().getMetaFromState(state);
    }

    @Override
    public int getRemainingBlocks(ItemStack stack) {
        return NBTHelper.getTagInt(stack, NBT_BLOCK_COUNT);
    }

    @Override
    public void setRemainingBlocks(ItemStack stack, int value) {
        value = MathHelper.clamp(value, 0, getMaxBlocksStored(stack));
        NBTHelper.setTagInt(stack, NBT_BLOCK_COUNT, value);
    }

    @Override
    public int getMaxBlocksStored(ItemStack stack) {
        return maxBlocks;
    }

    public boolean getAutoFillMode(ItemStack stack) {
        if (!NBTHelper.hasKey(stack, NBT_AUTO_FILL)) {
            NBTHelper.setTagBoolean(stack, NBT_AUTO_FILL, true);
        }
        return NBTHelper.getTagBoolean(stack, NBT_AUTO_FILL);
    }

    public void setAutoFillMode(ItemStack stack, boolean value) {
        NBTHelper.setTagBoolean(stack, NBT_AUTO_FILL, value);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        // Absorb blocks from inventory.
        if (!world.isRemote && world.getTotalWorldTime() % ABSORB_DELAY == 0) {
            if (entity instanceof EntityPlayer && getAutoFillMode(stack)) {
                absorbBlocksFromPlayer(stack, (EntityPlayer) entity);
            }
        }

        // Convert to new NBT block count;
        if (stack.getItemDamage() > 0 && !NBTHelper.hasKey(stack, NBT_BLOCK_COUNT)) {
            int blockCount = getMaxBlocksStored(stack) - stack.getItemDamage();
            NBTHelper.setTagInt(stack, NBT_BLOCK_COUNT, blockCount);
            stack.setItemDamage(0);
        }
    }

    private ItemStack absorbBlocksFromPlayer(ItemStack stack, EntityPlayer player) {
        int maxBlocksStored = getMaxBlocksStored(stack);
        if (getRemainingBlocks(stack) >= maxBlocksStored) {
            return stack;
        }

        IBlockState statePlaced = getBlockPlaced(stack);
        if (statePlaced == null)
            return stack;
        Block blockPlaced = statePlaced.getBlock();
        int metaDropped = getBlockMetaDropped(stack);
        Item itemBlock = Item.getItemFromBlock(blockPlaced);

        for (ItemStack invStack : PlayerHelper.getNonEmptyStacks(player, true, true, false)) {
            if (invStack.getItem() == itemBlock && invStack.getItemDamage() == metaDropped) {
                int currentBlocks = getRemainingBlocks(stack);

                // Add blocks to block placer, reduce stack size of block stack.
                if (currentBlocks + invStack.getCount() > maxBlocksStored) {
                    setRemainingBlocks(stack, maxBlocks);
                    invStack.shrink(maxBlocksStored - currentBlocks);
                    return stack;
                } else {
                    setRemainingBlocks(stack, currentBlocks + invStack.getCount());
                    invStack.setCount(0);
                }

                // Remove empty block stacks.
                if (invStack.getCount() <= 0) {
                    PlayerHelper.removeItem(player, invStack);
                }
            }
        }

        return stack;
    }

    public int absorbBlocks(ItemStack placer, ItemStack blockStack) {
        int placerCount = getRemainingBlocks(placer);
        int blockCount = blockStack.getCount();
        int maxBlocksStored = getMaxBlocksStored(placer);

        if (placerCount + blockCount > maxBlocksStored) {
            setRemainingBlocks(placer, maxBlocksStored);
            return maxBlocksStored - placerCount;
        } else {
            setRemainingBlocks(placer, placerCount + blockCount);
            return blockCount;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.world.isRemote && player.isSneaking()) {
            // Toggle auto-fill mode.
            boolean mode = !getAutoFillMode(stack);
            setAutoFillMode(stack, mode);

            String onOrOff = SilentGems.i18n.miscText("state." + (mode ? "on" : "off"));
            onOrOff = (mode ? TextFormatting.GREEN : TextFormatting.RED) + onOrOff;
            String line = SilentGems.i18n.subText("blockplacer", "item", "autoFill", onOrOff);
            ChatHelper.sendStatusMessage(player, line, true);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (getRemainingBlocks(stack) <= 0 && !player.capabilities.isCreativeMode) {
            // Empty and not in creative mode.
            return EnumActionResult.PASS;
        }

        // Create fake block stack and use it.
        IBlockState state = getBlockPlaced(stack);
        if (state == null)
            return EnumActionResult.PASS;
        Block block = state.getBlock();
        ItemStack fakeBlockStack = new ItemStack(block, 1, block.getMetaFromState(state));

        // In 1.11+, we must place the fake stack in the player's hand!
        ItemStack currentOffhand = player.getHeldItemOffhand();
        player.setHeldItem(EnumHand.OFF_HAND, fakeBlockStack);

        // Use the fake stack.
        EnumActionResult result = ItemHelper.onItemUse(fakeBlockStack.getItem(), player, world, pos,
                EnumHand.OFF_HAND, facing, hitX, hitY, hitZ);

        // Return the player's offhand stack.
        player.setHeldItem(EnumHand.OFF_HAND, currentOffhand);

        if (result == EnumActionResult.SUCCESS) {
            setRemainingBlocks(stack, getRemainingBlocks(stack) - 1);
        }
        return result;
    }

    @Override
    public ActionResult<ItemStack> onItemLeftClickSL(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!player.world.isRemote && player.isSneaking() && getRemainingBlocks(stack) > 0) {
            // Get the block this placer stores.
            IBlockState state = getBlockPlaced(stack);
            int meta = getBlockMetaDropped(stack);

            // Create block stack to drop.
            ItemStack toDrop = new ItemStack(state.getBlock(), 1, meta);
            toDrop.setCount(Math.min(getRemainingBlocks(stack), toDrop.getMaxStackSize()));
            setRemainingBlocks(stack, getRemainingBlocks(stack) - toDrop.getCount());

            // Make the EntityItem and spawn in world.
            Vec3d vec = player.getLookVec().scale(2.0);
            EntityItem entity = new EntityItem(world, player.posX + vec.x, player.posY + 1 + vec.y, player.posZ + vec.x, toDrop);
            vec = vec.scale(-0.125);
            entity.motionX = vec.x;
            entity.motionY = vec.y;
            entity.motionZ = vec.z;
            EntityHelper.safeSpawn(entity);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
        if (!isInCreativeTab(tab)) return;
        ItemStack stackEmpty = new ItemStack(this);
        ItemStack stackFull = new ItemStack(this);
        setRemainingBlocks(stackFull, getMaxBlocksStored(stackFull));
        list.add(stackEmpty);
        list.add(stackFull);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int maxBlocksStored = getMaxBlocksStored(stack);
        if (maxBlocksStored <= 0)
            return 1.0;
        return 1.0 - (double) getRemainingBlocks(stack) / (double) maxBlocksStored;
    }
}
