package silent.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
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
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.PlayerHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class TorchBandolier extends ItemSG {

  public final static String AUTO_FILL_OFF = "AutoFillOff";
  public final static String AUTO_FILL_ON = "AutoFillOn";
  public final static int MAX_DAMAGE = 1024;

  protected static ShapedOreRecipe recipe1;
  protected static ShapedOreRecipe recipe2;
  protected static ShapedOreRecipe recipe3;

  public TorchBandolier() {

    super(1);
    setMaxDamage(MAX_DAMAGE);
    setMaxStackSize(1);
    setUnlocalizedName(Names.TORCH_BANDOLIER);
  }

  public ItemStack absorbTorches(ItemStack stack, EntityPlayer player) {

    if (stack.getTagCompound() == null) {
      stack.setTagCompound(new NBTTagCompound());
      stack.getTagCompound().setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
    }

    if (stack.getTagCompound().getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
      ItemStack torches;
      for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
        torches = player.inventory.getStackInSlot(i);
        // Is the stack torches? Is this the best way? It's not pretty.
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

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    if (stack.getTagCompound() == null) {
      resetTagCompound(stack);
    }

    // Item description
    list.add(LocalizationHelper.getItemDescription(itemName, 1));
    // Auto-fill mode
    if (stack.getTagCompound().hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)
        && stack.getTagCompound().getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
      list.add(EnumChatFormatting.GREEN
          + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_ON));
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_OFF));
    }
    if (stack.getItemDamage() < MAX_DAMAGE) {
      list.add((new StringBuilder()).append(EnumChatFormatting.YELLOW)
          .append(MAX_DAMAGE - stack.getItemDamage()).append(" / ").append(MAX_DAMAGE).toString());
    }
    // How to use
    list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 2));
  }

  @Override
  public void addRecipes() {

    recipe1 = new ShapedOreRecipe(new ItemStack(this, 1, MAX_DAMAGE), true, new Object[] { "lll",
        "sgs", "lll", 'l', Items.leather, 's', "stickWood", 'g', Strings.ORE_DICT_GEM_BASIC });
    recipe2 = new ShapedOreRecipe(new ItemStack(this, 1, MAX_DAMAGE), true, new Object[] { "lll",
        "sgs", "lll", 'l', "materialLeather", 's', "stickWood", 'g', Strings.ORE_DICT_GEM_BASIC });
    recipe3 = new ShapedOreRecipe(new ItemStack(this, 1, MAX_DAMAGE), true, new Object[] { "lll",
        "sgs", "lll", 'l', new ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE), 's',
        "stickWood", 'g', Strings.ORE_DICT_GEM_BASIC });

    GameRegistry.addRecipe(recipe1);
    GameRegistry.addRecipe(recipe2);
    GameRegistry.addRecipe(recipe3);
  }

  @Override
  public void addThaumcraftStuff() {

//    ThaumcraftApi.registerObjectTag(new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE),
//        (new AspectList()).add(Aspect.LIGHT, 8).add(Aspect.VOID, 4).add(Aspect.CRYSTAL, 2));
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    list.add(new ItemStack(this, 1, 0));
    list.add(new ItemStack(this, 1, MAX_DAMAGE));
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (player.isSneaking()) {
      if (stack.getTagCompound() == null) {
        stack.setTagCompound(new NBTTagCompound());
      }

      boolean autoFill = true;
      if (stack.getTagCompound().hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
        autoFill = !stack.getTagCompound().getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL);
      }

      stack.getTagCompound().setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, autoFill);

      if (world.isRemote) {
        if (autoFill) {
          PlayerHelper
              .addChatMessage(
                  player,
                  EnumChatFormatting.GREEN
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
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

    if (stack.getItemDamage() == MAX_DAMAGE && !player.capabilities.isCreativeMode) {
      return false;
    }

    boolean used = false;

    ItemStack fakeTorchStack = new ItemStack(Blocks.torch);
    Item torch = fakeTorchStack.getItem();

    BlockPos torchPos = pos.offset(side);

    used = torch.onItemUse(fakeTorchStack, player, world, torchPos, side, hitX, hitY, hitZ);
    if (used) {
      stack.damageItem(1, player);
    }

    return used;
  }

  private void resetTagCompound(ItemStack stack) {

    if (stack.getTagCompound() == null) {
      stack.setTagCompound(new NBTTagCompound());
    }

    stack.getTagCompound().setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
  }

  public static boolean matchesRecipe(InventoryCrafting inv, World world) {

    return recipe1.matches(inv, world) || recipe2.matches(inv, world)
        || recipe3.matches(inv, world);
  }
}
