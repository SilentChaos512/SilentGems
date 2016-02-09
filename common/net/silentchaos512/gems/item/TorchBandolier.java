package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.api.IPlaceable;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class TorchBandolier extends ItemSG implements IPlaceable {

  public static final String AUTO_FILL_OFF = "AutoFillOff";
  public static final String AUTO_FILL_ON = "AutoFillOn";
  public static final int MAX_DAMAGE = 1024;
  public static final int ABSORB_DELAY = 20;

  protected static ShapedOreRecipe recipe1;
  protected static ShapedOreRecipe recipe2;

  public TorchBandolier() {

    this.setMaxDamage(MAX_DAMAGE);
    this.setNoRepair();
    this.setMaxStackSize(1);
    this.setUnlocalizedName(Names.TORCH_BANDOLIER);
  }

  @Override
  public Block getBlockPlaced(ItemStack stack) {

    return Blocks.torch;
  }

  @Override
  public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {

    if (!world.isRemote && world.getTotalWorldTime() % ABSORB_DELAY == 0) {
      if (entity instanceof EntityPlayer) {
        absorbTorches(stack, (EntityPlayer) entity);
      }
    }
  }

  public ItemStack absorbTorches(ItemStack stack, EntityPlayer player) {

    NBTTagCompound root = stack.getTagCompound();
    if (root == null) {
      root = new NBTTagCompound();
      root.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
    }

    if (root.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
      ItemStack torches;
      for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
        torches = player.inventory.getStackInSlot(i);
        if (torches != null
            && Item.getIdFromItem(torches.getItem()) == Block.getIdFromBlock(Blocks.torch)) {
          int damage = stack.getItemDamage();

          // Decrease damage value of torch bandolier, reduce stack size of torch stack.
          if (damage - torches.stackSize < 0) {
            stack.setItemDamage(0);
            torches.stackSize -= damage;
          } else {
            stack.setItemDamage(damage - torches.stackSize);
            torches.stackSize = 0;
          }

          // If torch stack is empty, get rid of it.
          if (torches.stackSize <= 0) {
            player.inventory.setInventorySlotContents(i, null);
          }
        }
      }
    }

    return stack;
  }

  public int getTorchCount(ItemStack stack) {

    return this.MAX_DAMAGE - stack.getItemDamage();
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    if (!stack.hasTagCompound()) {
      resetTagCompound(stack);
    }

    NBTTagCompound root = stack.getTagCompound();

    // Item description
    list.add(LocalizationHelper.getItemDescription(itemName, 1));
    // Auto-fill mode
    if (root.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)
        && root.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
      list.add(
          EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_ON));
    } else {
      list.add(
          EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_OFF));
    }
    if (stack.getItemDamage() < MAX_DAMAGE) {
      list.add((new StringBuilder()).append(EnumChatFormatting.YELLOW)
          .append(MAX_DAMAGE - stack.getItemDamage()).append(" / ").append(MAX_DAMAGE).toString());
    }

    if (shifted) {
      // How to use
      list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 2));
      list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 3));
    } else {
      list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getMiscText(Strings.PRESS_SHIFT));
    }
  }

  @Override
  public void addRecipes() {

    ItemStack bandolier = new ItemStack(this, 1, MAX_DAMAGE);
    ItemStack wool = new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE);
    ItemStack anyGem = new ItemStack(ModItems.gem, 1, OreDictionary.WILDCARD_VALUE);

    // Basic leather recipe (OreDict recipe removed, it was pointless)
    recipe1 = new ShapedOreRecipe(bandolier, true, "lll", "sgs", "lll", 'l', Items.leather, 's',
        "stickWood", 'g', anyGem);
    // Wool recipe
    recipe2 = new ShapedOreRecipe(bandolier, true, "lll", "sgs", "lll", 'l', wool, 's', "stickWood",
        'g', anyGem);

    GameRegistry.addRecipe(recipe1);
    GameRegistry.addRecipe(recipe2);
  }

  @Override
  public void addThaumcraftStuff() {

//    ThaumcraftApi.registerObjectTag(new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE),
//        (new AspectList()).add(Aspect.LIGHT, 8).add(Aspect.VOID, 4).add(Aspect.CRYSTAL, 2));
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    // Both a full and empty version, for cheaty purposes.
    list.add(new ItemStack(this, 1, MAX_DAMAGE));
    list.add(new ItemStack(this, 1, 0));
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (player.isSneaking()) {
      if (!stack.hasTagCompound()) {
        stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound root = stack.getTagCompound();
      boolean autoFill = true;
      if (root.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
        autoFill = !root.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL);
      }

      root.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, autoFill);

      if (world.isRemote) {
        if (autoFill) {
          PlayerHelper.addChatMessage(player, EnumChatFormatting.GREEN
              + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_ON));
        } else {
          PlayerHelper.addChatMessage(player,
              EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_OFF));
        }
      }
    }

    return stack;
  }

  /**
   * Place a torch, if possible. Mostly the same code gem tools use to place blocks.
   */
  @Override
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos,
      EnumFacing side, float hitX, float hitY, float hitZ) {

    if (stack.getItemDamage() == MAX_DAMAGE && !player.capabilities.isCreativeMode) {
      return false; // It's empty and the player is not in creative mode.
    }

    boolean used = false;

    // Basically, we're just creating a "fake" torch stack and using it.
    ItemStack fakeTorchStack = new ItemStack(Blocks.torch);
    Item torch = fakeTorchStack.getItem();

    used = torch.onItemUse(fakeTorchStack, player, world, pos, side, hitX, hitY, hitZ);
    if (used) {
      stack.damageItem(1, player);
    }

    return used;
  }

  private void resetTagCompound(ItemStack stack) {

    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }

    stack.getTagCompound().setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
  }

  public static boolean matchesRecipe(InventoryCrafting inv, World world) {

    return recipe1.matches(inv, world) || recipe2.matches(inv, world);
  }
}
