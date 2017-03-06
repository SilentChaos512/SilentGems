package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.util.LocalizationHelper;

public class ItemHoldingGem extends ItemBlockPlacer {

  public static final String NBT_BLOCK_PLACED = "sg_block_placed";
  public static final String NBT_GEM_ID = "sg_gem_id";

  public ItemHoldingGem() {

    super(Names.HOLDING_GEM, 4096);
  }

  @Override
  public IBlockState getBlockPlaced(ItemStack stack) {

    if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_BLOCK_PLACED))
      return null;

    int stateId = stack.getTagCompound().getInteger(NBT_BLOCK_PLACED);
    IBlockState state = Block.getStateById(stateId);
    return state;
  }

  public void setBlockPlaced(ItemStack stack, IBlockState state) {

    if (!stack.hasTagCompound())
      stack.setTagCompound(new NBTTagCompound());
    stack.getTagCompound().setInteger(NBT_BLOCK_PLACED, Block.getStateId(state));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List list, boolean advanced) {

    LocalizationHelper loc = SilentGems.localizationHelper;
    IBlockState state = getBlockPlaced(stack);
    if (state == null) {
      list.addAll(loc.getItemDescriptionLines(itemName));
      return;
    }

    Block block = state.getBlock();
    ItemStack placedStack = new ItemStack(Item.getItemFromBlock(block), 1,
        block.getMetaFromState(state));
    list.add(placedStack.getDisplayName());

    super.addInformation(stack, playerIn, list, advanced);
  }

  public void addRecipes() {

    for (EnumGem gem : EnumGem.values()) {
      ItemStack stack = new ItemStack(this);
      stack.setItemDamage(stack.getMaxDamage());
      stack.setTagCompound(new NBTTagCompound());
      stack.getTagCompound().setShort(NBT_GEM_ID, (short) gem.ordinal());
      GameRegistry.addRecipe(new ShapedOreRecipe(stack, "gcg", "s s", "gcg", 'g', "ingotGold", 'c',
          ModItems.craftingMaterial.chaosEssenceEnriched, 's', gem.getItemOreName()));
    }
  }

  @Override
  protected void clGetSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {

    for (EnumGem gem : EnumGem.values()) {
      ItemStack stack = new ItemStack(item);
      stack.setItemDamage(stack.getMaxDamage());
      stack.setTagCompound(new NBTTagCompound());
      stack.getTagCompound().setShort(NBT_GEM_ID, (short) gem.ordinal());
      list.add(stack);
    }
  }
}
