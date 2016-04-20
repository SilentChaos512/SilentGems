package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IBlockPlacer;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.PlayerHelper;

public class ItemBlockPlacer extends ItemSL implements IBlockPlacer {

  protected static final int ABSORB_DELAY = 20;
  protected static final String NBT_AUTO_FILL = "AutoFill";

  public ItemBlockPlacer(String name, int maxDamage) {

    super(1, SilentGems.MOD_ID, name);
    setMaxDamage(maxDamage);
    setNoRepair();
    setMaxStackSize(1);
    setUnlocalizedName(name);
  }

  @Override
  public IBlockState getBlockPlaced(ItemStack stack) {

    return Blocks.AIR.getDefaultState();
  }

  @Override
  public int getRemainingBlocks(ItemStack stack) {

    return stack.getMaxDamage() - stack.getItemDamage();
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
  public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot,
      boolean isSelected) {

    if (!world.isRemote && world.getTotalWorldTime() % ABSORB_DELAY == 0) {
      if (entity instanceof EntityPlayer && getAutoFillMode(stack)) {
        absorbBlocksFromPlayer(stack, (EntityPlayer) entity);
      }
    }
  }

  protected ItemStack absorbBlocksFromPlayer(ItemStack stack, EntityPlayer player) {

    if (stack.getItemDamage() == 0) {
      return stack;
    }

    IBlockState statePlaced = getBlockPlaced(stack);
    Block blockPlaced = statePlaced.getBlock();
    Item itemBlock = Item.getItemFromBlock(blockPlaced);

    for (ItemStack invStack : PlayerHelper.getNonNullStacks(player, true, true, false)) {
      if (invStack.getItem() == itemBlock) {
        int damage = stack.getItemDamage();

        // Decrease damage of block placer, reduce stack size of block stack.
        if (damage - invStack.stackSize < 0) {
          stack.setItemDamage(0);
          invStack.stackSize -= damage;
          return stack;
        } else {
          stack.setItemDamage(damage - invStack.stackSize);
          invStack.stackSize = 0;
        }

        // Remove empty stacks.
        if (invStack.stackSize <= 0) {
          PlayerHelper.removeItem(player, invStack);
        }
      }
    }

    return stack;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player,
      EnumHand hand) {

    if (player.isSneaking()) {
      setAutoFillMode(stack, !getAutoFillMode(stack));
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }

  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world,
      BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    if (stack.getItemDamage() == getMaxDamage(stack) && !player.capabilities.isCreativeMode) {
      return EnumActionResult.PASS; // Empty and not in creative mode.
    }

    // Create fake block stack and use it.
    IBlockState state = getBlockPlaced(stack);
    Block block = state.getBlock();
    ItemStack fakeBlockStack = new ItemStack(block, 1, block.getMetaFromState(state));

    EnumActionResult result = fakeBlockStack.getItem().onItemUse(fakeBlockStack, player, world, pos,
        hand, facing, hitX, hitY, hitZ);

    if (result == EnumActionResult.SUCCESS) {
      stack.damageItem(1, player);
    }
    return result;
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    list.add(new ItemStack(item, 1, getMaxDamage()));
    list.add(new ItemStack(item, 1, 0));
  }
}
