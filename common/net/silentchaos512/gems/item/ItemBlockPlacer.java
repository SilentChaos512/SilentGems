package net.silentchaos512.gems.item;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IBlockPlacer;
import net.silentchaos512.gems.util.NBTHelper;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.util.LocalizationHelper;
import net.silentchaos512.lib.util.PlayerHelper;

public abstract class ItemBlockPlacer extends ItemSL implements IBlockPlacer {

  protected static final int ABSORB_DELAY = 20;
  protected static final String NBT_AUTO_FILL = "AutoFill";

  public ItemBlockPlacer(String name, int maxDamage) {

    super(1, SilentGems.MODID, name);
    setMaxDamage(maxDamage);
    setNoRepair();
    setMaxStackSize(1);
    setUnlocalizedName(name);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List list, boolean advanced) {

    String blockPlacer = "BlockPlacer";
    LocalizationHelper loc = SilentGems.localizationHelper;

    boolean autoFillOn = getAutoFillMode(stack);
    int currentBlocks = getRemainingBlocks(stack);
    int maxBlocks = stack.getMaxDamage();

    list.add(loc.getItemSubText(blockPlacer, "count", currentBlocks, maxBlocks));
    String onOrOff = loc.getItemSubText(blockPlacer, "autoFill." + (autoFillOn ? "on" : "off"));
    list.add(loc.getItemSubText(blockPlacer, "autoFill", onOrOff));
  }

  @Override
  public abstract @Nullable IBlockState getBlockPlaced(ItemStack stack);

  public int getBlockMetaDropped(ItemStack stack) {

    IBlockState state = getBlockPlaced(stack);
    return state.getBlock().getMetaFromState(state);
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
    if (statePlaced == null)
      return stack;
    Block blockPlaced = statePlaced.getBlock();
    int metaDropped = getBlockMetaDropped(stack);
    Item itemBlock = Item.getItemFromBlock(blockPlaced);

    for (ItemStack invStack : PlayerHelper.getNonEmptyStacks(player, true, true, false)) {
      if (invStack.getItem() == itemBlock && invStack.getItemDamage() == metaDropped) {
        int damage = stack.getItemDamage();

        // Decrease damage of block placer, reduce stack size of block stack.
        if (damage - invStack.getCount() < 0) {
          stack.setItemDamage(0);
          invStack.shrink(damage);
          return stack;
        } else {
          stack.setItemDamage(damage - invStack.getCount());
          invStack.setCount(0);
        }

        // Remove empty stacks.
        if (invStack.getCount() <= 0) {
          PlayerHelper.removeItem(player, invStack);
        }
      }
    }

    return stack;
  }

  public int absorbBlocks(ItemStack placer, ItemStack blockStack) {

    // TODO
    return 0;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

    ItemStack stack = player.getHeldItem(hand);
    if (player.isSneaking()) {
      boolean mode = !getAutoFillMode(stack);
      setAutoFillMode(stack, mode);

      LocalizationHelper loc = SilentGems.localizationHelper;
      String onOrOff = loc.getItemSubText("BlockPlacer", "autoFill." + (mode ? "on" : "off"));
      String line = loc.getItemSubText("BlockPlacer", "autoFill", onOrOff);
      player.sendStatusMessage(new TextComponentString(line), true);
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
      EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack stack = player.getHeldItem(hand);
    if (stack.getItemDamage() == getMaxDamage(stack) && !player.capabilities.isCreativeMode) {
      return EnumActionResult.PASS; // Empty and not in creative mode.
    }

    // Create fake block stack and use it.
    IBlockState state = getBlockPlaced(stack);
    if (state == null)
      return EnumActionResult.PASS;
    Block block = state.getBlock();
    ItemStack fakeBlockStack = new ItemStack(block, 1, block.getMetaFromState(state));

    // In 1.11, we must place the fake stack in the player's hand!
    ItemStack currentOffhand = player.getHeldItemOffhand();
    player.setHeldItem(EnumHand.OFF_HAND, fakeBlockStack);

    // Use the fake stack.
    EnumActionResult result = fakeBlockStack.getItem().onItemUse(player, world, pos,
        EnumHand.OFF_HAND, facing, hitX, hitY, hitZ);

    // Return the player's offhand stack.
    player.setHeldItem(EnumHand.OFF_HAND, currentOffhand);

    if (result == EnumActionResult.SUCCESS) {
      stack.damageItem(1, player);
    }
    return result;
  }

  @Override
  public boolean onEntitySwing(EntityLivingBase player, ItemStack stack) {

    if (!player.world.isRemote && player.isSneaking() && getRemainingBlocks(stack) > 0 && Mouse.isButtonDown(0)) {
      // Get the block this placer stores.
      IBlockState state = getBlockPlaced(stack);
      int meta = getBlockMetaDropped(stack);

      // Create block stack to drop.
      ItemStack toDrop = new ItemStack(state.getBlock(), 1, meta);
      toDrop.setCount(Math.min(getRemainingBlocks(stack), toDrop.getMaxStackSize()));
      stack.damageItem(toDrop.getCount(), player);

      // Make the EntityItem and spawn in world.
      World world = player.world;
      Vec3d vec = player.getLookVec().scale(2.0);
      EntityItem entity = new EntityItem(world, player.posX + vec.xCoord,
          player.posY + 1 + vec.yCoord, player.posZ + vec.zCoord, toDrop);
      vec = vec.scale(-0.125);
      entity.setVelocity(vec.xCoord, vec.yCoord, vec.zCoord);
      world.spawnEntity(entity);
    }

    return super.onEntitySwing(player, stack);
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, NonNullList list) {

    ItemStack stack = new ItemStack(item);
    list.add(new ItemStack(item, 1, getMaxDamage(stack)));
    list.add(stack);
  }
}
