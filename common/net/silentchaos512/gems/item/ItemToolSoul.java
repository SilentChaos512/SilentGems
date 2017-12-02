package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.init.ModRecipes;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.soul.ToolSoul;
import net.silentchaos512.lib.item.ItemSL;
import net.silentchaos512.lib.registry.RecipeMaker;
import net.silentchaos512.lib.util.StackHelper;

public class ItemToolSoul extends ItemSL {

  private static final String NBT_KEY = "ToolSoul";

  public IRecipe recipe;

  public ItemToolSoul() {

    super(1, SilentGems.MODID, Names.TOOL_SOUL);
    setMaxStackSize(1);
  }

  public ToolSoul getSoul(ItemStack stack) {

    if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(NBT_KEY)) {
      return null;
    }
    return ToolSoul.readFromNBT(stack.getTagCompound().getCompoundTag(NBT_KEY));
  }

  public void setSoul(ItemStack stack, ToolSoul soul) {

    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    if (!stack.getTagCompound().hasKey(NBT_KEY)) {
      stack.getTagCompound().setTag(NBT_KEY, new NBTTagCompound());
    }
    soul.writeToNBT(stack.getTagCompound().getCompoundTag(NBT_KEY));
  }

  @Override
  public void clAddInformation(ItemStack stack, World world, List<String> list, boolean advanced) {

    super.clAddInformation(stack, world, list, advanced);

    ToolSoul soul = getSoul(stack);
    if (soul != null) {
      soul.addInformation(stack, world, list, advanced);
    }
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot,
      boolean isSelected) {

    if (StackHelper.isValid(stack) && !stack.hasTagCompound()) {
      // Randomize souls with no data!
      ToolSoul soul = ToolSoul.randomSoul();
      setSoul(stack, soul);
    }
  }

  @Override
  public void addRecipes(RecipeMaker recipes) {

    if (ModRecipes.ADD_SOUL_RECIPES) {
      // FIXME? JEI only shows wheat souls?
      NonNullList<ItemStack> listSouls = NonNullList.create();
      ModItems.soulGem.getSubItems(ModItems.soulGem.getCreativeTab(), listSouls);
      Ingredient ingSouls = Ingredient
          .fromStacks(listSouls.toArray(new ItemStack[listSouls.size()]));

      recipe = recipes.addShaped("tool_soul", new ItemStack(this), " s ", "scs", " s ", 's',
          ingSouls, 'c', new ItemStack(Items.DIAMOND));
    }
  }
}
