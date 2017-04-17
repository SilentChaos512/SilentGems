package net.silentchaos512.gems.api.lib;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.lib.util.StackHelper;

public enum EnumMaterialTier {

  MUNDANE, REGULAR, SUPER/*, HYPER*/;

  public String getLocalizedName() {

    return SilentGems.localizationHelper.getMiscText("ToolTier." + name());
  }

  public static EnumMaterialTier fromStack(ItemStack stack) {

    ToolPart part = ToolPartRegistry.fromStack(stack);
    if (part != null)
      return part.getTier();
    return null;
  }

  public ItemStack getFiller() {

    switch (this) {
      case MUNDANE:
        return new ItemStack(Items.STRING);
      case REGULAR:
        return new ItemStack(Items.IRON_INGOT);
      case SUPER:
        return new ItemStack(Items.GOLD_INGOT);
      default:
        SilentGems.logHelper.warning("EnumMaterialTier " + this + ": undefined filler stack!");
        return StackHelper.empty();
    }
  }
}
