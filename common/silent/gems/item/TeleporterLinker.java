package silent.gems.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.block.ChaosEssenceBlock;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class TeleporterLinker extends ItemSG {

  public TeleporterLinker() {

    super(1);
    setMaxStackSize(1);
    setUnlocalizedName(Names.TELEPORTER_LINKER);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {

    // Verify NBT
    if (stack.getTagCompound() == null) {
      stack.setTagCompound(new NBTTagCompound());
    }
    if (!stack.getTagCompound().hasKey(Strings.TELEPORTER_LINKER_STATE)) {
      stack.getTagCompound().setBoolean(Strings.TELEPORTER_LINKER_STATE, false);
    }

    // Display state and coords (if active)
    if (stack.getTagCompound().getBoolean(Strings.TELEPORTER_LINKER_STATE)) {
      list.add(EnumChatFormatting.GREEN + LocalizationHelper.getOtherItemKey(itemName, "Active"));
      list.add(LogHelper.coordFromNBT(stack.getTagCompound()));
    } else {
      list.add(EnumChatFormatting.RED + LocalizationHelper.getOtherItemKey(itemName, "Inactive"));
    }

    list.add(EnumChatFormatting.DARK_GRAY + LocalizationHelper.getItemDescription(itemName, 0));
  }

  @Override
  public void addRecipes() {

    ItemStack chaosEssence = ChaosEssence.getByType(ChaosEssenceBlock.EnumType.REFINED);
    ItemStack ornateStick = CraftingMaterial.getStack(Names.ORNATE_STICK);
    GameRegistry
        .addShapedRecipe(new ItemStack(this), "c", "s", 'c', chaosEssence, 's', ornateStick);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {

    return stack.getTagCompound() != null
        && stack.getTagCompound().hasKey(Strings.TELEPORTER_LINKER_STATE)
        && stack.getTagCompound().getBoolean(Strings.TELEPORTER_LINKER_STATE);
  }
}
