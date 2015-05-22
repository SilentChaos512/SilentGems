package net.silentchaos512.gems.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.api.IPlaceable;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.PlayerHelper;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import cpw.mods.fml.common.registry.GameRegistry;

public class TorchBandolier extends ItemSG implements IPlaceable {

  public final static String AUTO_FILL_OFF = "AutoFillOff";
  public final static String AUTO_FILL_ON = "AutoFillOn";
  public final static int MAX_DAMAGE = 1024;

  protected static ShapedOreRecipe recipe1;
  protected static ShapedOreRecipe recipe2;

  public final static IIcon[] gemIcons = new IIcon[EnumGem.all().length];
  public static IIcon iconBlank;

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

  public ItemStack absorbTorches(ItemStack stack, EntityPlayer player) {

    if (stack.stackTagCompound == null) {
      stack.stackTagCompound = new NBTTagCompound();
      stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
    }

    if (stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
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

  public int getTorchCount(ItemStack stack) {

    return this.MAX_DAMAGE - stack.getItemDamage();
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)
        || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);

    if (stack.stackTagCompound == null) {
      resetTagCompound(stack);
    }

    // Item description
    list.add(LocalizationHelper.getItemDescription(itemName, 1));
    // Auto-fill mode
    if (stack.stackTagCompound.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)
        && stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
      list.add(EnumChatFormatting.GREEN
          + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_ON));
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, AUTO_FILL_OFF));
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
    String anyGem = Strings.ORE_DICT_GEM_BASIC;

    // Basic leather recipe (OreDict recipe removed, it was pointless)
    recipe1 = new ShapedOreRecipe(bandolier, true, "lll", "sgs", "lll", 'l', Items.leather, 's',
        "stickWood", 'g', anyGem);
    // Wool recipe
    recipe2 = new ShapedOreRecipe(bandolier, true, "lll", "sgs", "lll", 'l', wool, 's',
        "stickWood", 'g', anyGem);

    GameRegistry.addRecipe(recipe1);
    GameRegistry.addRecipe(recipe2);
  }

  @Override
  public void addThaumcraftStuff() {

    ThaumcraftApi.registerObjectTag(new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE),
        (new AspectList()).add(Aspect.LIGHT, 8).add(Aspect.VOID, 4).add(Aspect.CRYSTAL, 2));
  }

  @Override
  public void getSubItems(Item item, CreativeTabs tab, List list) {

    // Both a full and empty version, for cheaty purposes.
    list.add(new ItemStack(this, 1, 0));
    list.add(new ItemStack(this, 1, MAX_DAMAGE));
  }

  @Override
  public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

    if (player.isSneaking()) {
      if (stack.stackTagCompound == null) {
        stack.stackTagCompound = new NBTTagCompound();
      }

      boolean autoFill = true;
      if (stack.stackTagCompound.hasKey(Strings.TORCH_BANDOLIER_AUTO_FILL)) {
        autoFill = !stack.stackTagCompound.getBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL);
      }

      stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, autoFill);

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
  public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
      int side, float hitX, float hitY, float hitZ) {

    if (stack.getItemDamage() == MAX_DAMAGE && !player.capabilities.isCreativeMode) {
      return false; // It's empty and the player is not in creative mode.
    }

    boolean used = false;

    // Basically, we're just creating a "fake" torch stack and using it.
    ItemStack fakeTorchStack = new ItemStack(Blocks.torch);
    Item torch = fakeTorchStack.getItem();

    used = torch.onItemUse(fakeTorchStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    if (used) {
      stack.damageItem(1, player);
    }

    return used;
  }

  private void resetTagCompound(ItemStack stack) {

    if (stack.stackTagCompound == null) {
      stack.stackTagCompound = new NBTTagCompound();
    }

    stack.stackTagCompound.setBoolean(Strings.TORCH_BANDOLIER_AUTO_FILL, true);
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName);
    iconBlank = reg.registerIcon(Strings.RESOURCE_PREFIX + "Blank");
    for (int i = 0; i < gemIcons.length; ++i) {
      gemIcons[i] = reg.registerIcon(Strings.RESOURCE_PREFIX + itemName + "_Gem" + i);
    }
  }

  @Override
  public boolean requiresMultipleRenderPasses() {

    return true;
  }

  @Override
  public int getRenderPasses(int meta) {

    return 2;
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {

    if (pass == 0) {
      return itemIcon; // The main texture
    } else if (pass == 1) {
      // The decoration, if there is one.
      if (stack != null && stack.stackTagCompound != null
          && stack.stackTagCompound.hasKey(Strings.TORCH_BANDOLIER_GEM)) {
        int k = stack.stackTagCompound.getByte(Strings.TORCH_BANDOLIER_GEM);
        if (k >= 0 && k < gemIcons.length) {
          return gemIcons[k];
        }
      }
    }

    return iconBlank;
  }

  public static boolean matchesRecipe(InventoryCrafting inv, World world) {

    return recipe1.matches(inv, world) || recipe2.matches(inv, world);
  }
}
