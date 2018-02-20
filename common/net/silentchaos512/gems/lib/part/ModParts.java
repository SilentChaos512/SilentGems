package net.silentchaos512.gems.lib.part;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.EnumTipUpgrade;

public class ModParts {

  public static void init() {

    // Head and deco
    ToolPartRegistry.putPart(new ToolPartFlint());
    // ToolPartRegistry.putPart(new ToolPartGlass());
    for (EnumGem gem : EnumGem.values())
      ToolPartRegistry.putPart(new ToolPartGem(gem, false));
    for (EnumGem gem : EnumGem.values())
      ToolPartRegistry.putPart(new ToolPartGem(gem, true));

    // Rods @formatter:off
    ToolPartRegistry.putPart(new ToolPartRodGems("RodWood", EnumMaterialTier.MUNDANE,
        new ItemStack(Items.STICK), 0x896727, "stickWood",
        1.0f, 1.0f, 1.0f, 1.0f, 0.8f));
    ToolPartRegistry.putPart(new ToolPartRodGems("RodBone", EnumMaterialTier.MUNDANE,
        new ItemStack(Items.BONE), 0xFFFDE9,
        1.1f, 0.9f, 1.1f, 0.9f, 1.0f));
    ToolPartRegistry.putPart(new ToolPartRodGems("RodStone", EnumMaterialTier.MUNDANE,
        ModItems.craftingMaterial.toolRodStone, 0x777777, "stickStone",
        0.9f, 1.1f, 1.0f, 1.0f, 0.7f));
    ToolPartRegistry.putPart(new ToolPartRodGems("RodIron", EnumMaterialTier.REGULAR,
        ModItems.craftingMaterial.toolRodIron, 0xA2A2A2, "stickIron",
        1.25f, 1.0f, 1.0f, 1.0f, 0.9f));
    ToolPartRegistry.putPart(new ToolPartRodGems("RodGold", EnumMaterialTier.SUPER,
        ModItems.craftingMaterial.toolRodGold, 0xC8AE09, "stickGold",
        1.15f, 1.0f, 1.15f, 1.0f, 1.0f));
    ToolPartRegistry.putPart(new ToolPartRodGems("RodSilver", EnumMaterialTier.SUPER,
        ModItems.craftingMaterial.toolRodSilver, 0xF2F2F2, "stickSilver",
        1.0f, 1.15f, 1.0f, 1.15f, 1.0f)); //@formatter:on

    // Tips
    ToolPartRegistry.putPart(new ToolPartTipGems("TipIron", EnumTipUpgrade.IRON));
    ToolPartRegistry.putPart(new ToolPartTipGems("TipGold", EnumTipUpgrade.GOLD));
    ToolPartRegistry.putPart(new ToolPartTipGems("TipDiamond", EnumTipUpgrade.DIAMOND));
    ToolPartRegistry.putPart(new ToolPartTipGems("TipEmerald", EnumTipUpgrade.EMERALD));

    // Armor Frames
    for (EnumMaterialTier tier : EnumMaterialTier.values()) {
      for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
        if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
          String key = "frame_" + slot.name().toLowerCase() + "_" + tier.name().toLowerCase();
          ToolPartRegistry.putPart(new ArmorPartFrameGems(key, slot, tier));
        }
      }
    }
  }
}
