package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.ModBlocks;
import net.silentchaos512.gems.configuration.Config;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.lib.Names;

public class HoldingGem extends ItemBlock implements IAddRecipe {

  public static final String NBT_BASE = "HoldingGem";
  public static final String NBT_COUNT = "Count";
  public static final String NBT_AUTO_FILL = "AutoFill";
  public static final String NBT_BLOCK_NAME = "BlockName";
  public static final String NBT_BLOCK_META = "BlockMeta";

  protected String itemName;
  
  public HoldingGem(Block block) {

    super(block);
    // this.icons = new IIcon[EnumGem.all().length];
    this.setMaxStackSize(1);
    this.setMaxDamage(0);
    this.setNoRepair();
    // this.setHasSubtypes(true);
    // this.setHasGemSubtypes(true);
    this.setUnlocalizedName(Names.HOLDING_GEM);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {

    int count = this.getTag(stack, NBT_COUNT);
    boolean autoFill = this.getTagBoolean(stack, NBT_AUTO_FILL);
    String itemName = this.getTagString(stack, NBT_BLOCK_NAME);

    list.add("Count: " + count);
    list.add("AutoFill: " + autoFill);
    list.add("ItemName: " + itemName == null ? "null" : itemName);
  }

  @Override
  public void addRecipes() {

    // TODO
  }
  
  @Override
  public void addOreDict() {

  }

//  @Override
//  public Block getBlockPlaced(ItemStack stack) {
//
//    return this.getContainedBlock(stack);
//  }

  public Block getContainedBlock(ItemStack stack) {

    Item item = this.getContainedItem(stack);
    if (item != null) {
      return Block.getBlockFromItem(item);
    }
    return null;
  }

  public Item getContainedItem(ItemStack stack) {

    String name = this.getContainedItemName(stack);
    if (name != null) {
      return (Item) Item.itemRegistry.getObject(this.getContainedItemName(stack));
    }
    return null;
  }

  public ItemStack getContainedStack(ItemStack stack) {

    Item item = this.getContainedItem(stack);
    if (item != null) {
      return new ItemStack(item, 1, this.getContainedItemMeta(stack));
    }
    return null;
  }

  public int getContainedItemCount(ItemStack stack) {

    return this.getTag(stack, NBT_COUNT);
  }

  public int getMaxContainedItemCount(ItemStack stack) {

    return 4096;
//    return Config.HOLDING_GEM_MAX_ITEMS.value;
  }

  public String getContainedItemName(ItemStack stack) {

    return this.getTagString(stack, NBT_BLOCK_NAME);
  }

  public int getContainedItemMeta(ItemStack stack) {

    return this.getTag(stack, NBT_BLOCK_META);
  }
  
  public void setContainedItemCount(ItemStack stack, int count) {
    
    this.setTag(stack, NBT_COUNT, count);
  }

  public void setContainedItemName(ItemStack stack, String name) {

    this.setTagString(stack, NBT_BLOCK_NAME, name);
  }
  
  public void setContainedItemMeta(ItemStack stack, int meta) {
    
    this.setTag(stack, NBT_BLOCK_META, meta);
  }

  public ItemStack absorbItems(ItemStack stack, EntityPlayer player) {

    int maxCount = this.getMaxContainedItemCount(stack);
    String itemName = this.getContainedItemName(stack);
    if (itemName != null && this.getTagBoolean(stack, NBT_AUTO_FILL)) {
      ItemStack itemStack;
      for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
        itemStack = player.inventory.getStackInSlot(i);
        if (itemName.equals(itemStack.getItem().getUnlocalizedName())) {
          int count = this.getTag(stack, NBT_COUNT);

          if (count + itemStack.stackSize > maxCount) {
            this.setTag(stack, NBT_COUNT, maxCount);
            itemStack.stackSize -= maxCount - count;
          } else {
            this.setTag(stack, NBT_COUNT, count + itemStack.stackSize);
            itemStack.stackSize = 0;
          }

          // If itemStack is empty, remove it.
          if (itemStack.stackSize <= 0) {
            player.inventory.setInventorySlotContents(i, null);
          }
        }
      }
    }

    return stack;
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (player.isSneaking()) {
      boolean autoFill = this.getTagBoolean(stack, NBT_AUTO_FILL);
      this.setTagBoolean(stack, NBT_AUTO_FILL, !autoFill);
      // TODO: Change something to indicate this?
    }

    return stack;
  }

  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
      int side, float hitX, float hitY, float hitZ) {

    // Debug code:
//    this.setTagString(stack, NBT_BLOCK_NAME, "SilentGems:GemBlock");
//    this.setTag(stack, NBT_BLOCK_META, 6);
//    this.setTag(stack, NBT_COUNT, 1000);

    String itemName = this.getContainedItemName(stack);
    int count = this.getContainedItemCount(stack);

    if (itemName == null || (count == 0 && !player.capabilities.isCreativeMode)) {
      return false;
    }

    boolean used = false;

    try {
      ItemStack blockStack = this.getContainedStack(stack);
      ItemBlock itemBlock = (ItemBlock) blockStack.getItem();
      used = itemBlock.onItemUse(blockStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    } catch (Exception ex) {
      LogHelper.debug(ex);
    }

    return used;
  }

  /*
   * ----------------- Rendering methods -----------------
   */

  @Override
  public void registerIcons(IIconRegister reg) {

    // TODO
  }

  @Override
  public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem,
      int useRemaining) {

    Block block = this.getContainedBlock(stack);
    if (block != null) {
      return block.getBlockTextureFromSide(1);
    }
    return null;
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return false; // TODO: Change me?
  }

  @Override
  public int getRenderPasses(int meta) {

    return 1; // TODO: Change me?
  }
  
  @Override
  public int getSpriteNumber() {
    
    return 0;
  }

  @Override
  public double getDurabilityForDisplay(ItemStack stack) {

    double count = this.getContainedItemCount(stack);
    double maxCount = this.getMaxContainedItemCount(stack);
    return (maxCount - count) / maxCount;
  }
  
  /* --------------------
   * Localization methods
   * --------------------
   */
  
  @Override
  public String getUnlocalizedName(ItemStack stack) {
    
    return LocalizationHelper.ITEM_PREFIX + this.itemName;
  }
  
  @Override
  public String getUnlocalizedName() {
    
    return LocalizationHelper.ITEM_PREFIX + this.itemName;
  }
  
  @Override
  public ItemBlock setUnlocalizedName(String name) {
    
    this.itemName = name;
    return super.setUnlocalizedName(name);
  }

  /*
   * ------------------ NBT helper methods ------------------
   */

  public void createTagCompoundIfNeeded(ItemStack stack) {

    if (stack.stackTagCompound == null) {
      stack.setTagCompound(new NBTTagCompound());
    }
    if (!stack.stackTagCompound.hasKey(NBT_BASE)) {
      stack.stackTagCompound.setTag(NBT_BASE, new NBTTagCompound());
    }
  }

  public boolean hasTag(ItemStack stack, String key) {

    if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey(NBT_BASE)) {
      return false;
    }
    return ((NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE)).hasKey(key);
  }

  public int getTag(ItemStack stack, String key) {

    if (stack == null) {
      return -1;
    }
    this.createTagCompoundIfNeeded(stack);

    NBTTagCompound tags = (NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE);
    return tags.hasKey(key) ? tags.getInteger(key) : 0;
  }

  public boolean getTagBoolean(ItemStack stack, String key) {

    if (stack == null) {
      return false;
    }
    this.createTagCompoundIfNeeded(stack);

    NBTTagCompound tags = (NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE);
    return tags.hasKey(key) ? tags.getBoolean(key) : false;
  }

  public String getTagString(ItemStack stack, String key) {

    if (stack == null) {
      return null;
    }
    this.createTagCompoundIfNeeded(stack);

    NBTTagCompound tags = (NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE);
    return tags.hasKey(key) ? tags.getString(key) : null;
  }

  public void setTag(ItemStack stack, String key, int value) {

    if (stack == null) {
      return;
    }
    this.createTagCompoundIfNeeded(stack);

    NBTTagCompound tags = (NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE);
    tags.setInteger(key, value);
  }

  public void setTagBoolean(ItemStack stack, String key, boolean value) {

    if (stack == null) {
      return;
    }
    this.createTagCompoundIfNeeded(stack);

    NBTTagCompound tags = (NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE);
    tags.setBoolean(key, value);
  }

  public void setTagString(ItemStack stack, String key, String value) {

    if (stack == null) {
      return;
    }
    this.createTagCompoundIfNeeded(stack);

    NBTTagCompound tags = (NBTTagCompound) stack.stackTagCompound.getTag(NBT_BASE);
    tags.setString(key, value);
  }
}
