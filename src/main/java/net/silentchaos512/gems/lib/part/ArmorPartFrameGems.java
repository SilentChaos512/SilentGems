package net.silentchaos512.gems.lib.part;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ArmorPartFrame;
import net.silentchaos512.gems.init.ModItems;

public class ArmorPartFrameGems extends ArmorPartFrame {

  public ArmorPartFrameGems(String key, EntityEquipmentSlot slot, EnumMaterialTier tier) {

    super(SilentGems.RESOURCE_PREFIX + key, slot,
        ModItems.armorFrame.getFrameForArmorPiece(slot, tier));
    this.tier = tier;
  }
}
