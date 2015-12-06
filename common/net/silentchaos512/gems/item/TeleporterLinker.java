package net.silentchaos512.gems.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.silentchaos512.gems.core.util.DimensionalPosition;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class TeleporterLinker extends ItemSG {

  public TeleporterLinker() {

    setMaxStackSize(1);
    setUnlocalizedName(Names.TELEPORTER_LINKER);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    // Verify NBT
    if (stack.stackTagCompound == null) {
      stack.stackTagCompound = new NBTTagCompound();
    }
    if (!stack.stackTagCompound.hasKey(Strings.TELEPORTER_LINKER_STATE)) {
      stack.stackTagCompound.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    }

    // Display state and coords (if active)
    if (isLinked(stack)) {
      list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, "Active"));
      DimensionalPosition pos = getLinkedPosition(stack);
      String posStr = LocalizationHelper.getMiscText("DimensionalPosition");
      posStr = String.format(posStr, pos.x, pos.y, pos.z, pos.d);
      list.add(posStr);
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, "Inactive"));
    }

    list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 0));
  }

  @Override
  public void addRecipes() {

    GameRegistry.addShapedRecipe(new ItemStack(this), "c", "s", 'c',
        CraftingMaterial.getStack(Names.CHAOS_ESSENCE_PLUS), 's',
        CraftingMaterial.getStack(Names.ORNATE_STICK));
  }

  @Override
  public boolean hasEffect(ItemStack stack, int pass) {

    return isLinked(stack);
  }
  
  public boolean isLinked(ItemStack stack) {
    
    NBTTagCompound tags = stack.getTagCompound();
    if (tags != null) {
      return tags.getBoolean(Strings.TELEPORTER_LINKER_STATE);
    }
    return false;
  }
  
  public DimensionalPosition getLinkedPosition(ItemStack stack) {
    
    if (!stack.hasTagCompound()) {
      return new DimensionalPosition(0, 0, 0, 0);
    }
    return DimensionalPosition.fromNBT(stack.getTagCompound());
  }
}
