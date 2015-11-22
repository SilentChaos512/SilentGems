package net.silentchaos512.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
    if (!stack.hasTagCompound()) {
      stack.setTagCompound(new NBTTagCompound());
    }
    NBTTagCompound root = stack.getTagCompound();
    if (!root.hasKey(Strings.TELEPORTER_LINKER_STATE)) {
      root.setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    }

    // Display state and coords (if active)
    if (root.getBoolean(Strings.TELEPORTER_LINKER_STATE)) {
      list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, "Active"));
      list.add(LogHelper.coordFromNBT(root));
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
  public boolean hasEffect(ItemStack stack) {

    NBTTagCompound root = stack.getTagCompound();
    return root != null
        && root.hasKey(Strings.TELEPORTER_LINKER_STATE)
        && root.getBoolean(Strings.TELEPORTER_LINKER_STATE);
  }
}
