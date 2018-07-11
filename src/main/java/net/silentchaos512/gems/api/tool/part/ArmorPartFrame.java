package net.silentchaos512.gems.api.tool.part;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.EnumMaterialGrade;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.stats.ItemStat;
import net.silentchaos512.gems.api.stats.ItemStatModifier;

public class ArmorPartFrame extends ToolPart {

  protected EntityEquipmentSlot armorSlot;

  public ArmorPartFrame(String key, EntityEquipmentSlot slot, ItemStack craftingStack) {

    this(key, slot, craftingStack, "");
  }

  protected ArmorPartFrame(String key, EntityEquipmentSlot slot, ItemStack craftingStack,
      String craftingOreDictName) throws IllegalArgumentException {

    super(key, craftingStack, craftingOreDictName);

    if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR) {
      throw new IllegalArgumentException("Cannot register armor frame for slot " + slot.getName());
    }
    this.armorSlot = slot;
  }

  @Override
  public ItemStatModifier getStatModifier(ItemStat stat, EnumMaterialGrade grade) {

    float val = stats.getStat(stat);
    val += 1f;
    return new ItemStatModifier(getUnlocalizedName(), val,
        ItemStatModifier.Operation.MULTIPLY);
  }

  public EntityEquipmentSlot getSlot() {

    return armorSlot;
  }

  @Override
  public boolean matchesForDecorating(ItemStack partRep, boolean matchOreDict) {

    return false;
  }

  @Override
  public ModelResourceLocation getModel(ItemStack toolOrArmor, ToolPartPosition pos, int frame) {

    return null;
  }

  @Override
  public int getDurability() {

    return 0;
  }

  @Override
  public float getHarvestSpeed() {

    return 0;
  }

  @Override
  public int getHarvestLevel() {

    return 0;
  }

  @Override
  public float getMeleeDamage() {

    return 0;
  }

  @Override
  public float getMagicDamage() {

    return 0;
  }

  @Override
  public int getEnchantability() {

    return 0;
  }

  @Override
  public float getMeleeSpeed() {

    return 0;
  }

  @Override
  public float getChargeSpeed() {

    return 0;
  }

  @Override
  public float getProtection() {

    return 0;
  }

  @Override
  public boolean validForToolOfTier(EnumMaterialTier targetTier) {

    return getTier() == targetTier;
  }

  @Override
  public boolean validForPosition(IPartPosition pos) {

    return pos == ArmorPartPosition.FRAME;
  }

}
