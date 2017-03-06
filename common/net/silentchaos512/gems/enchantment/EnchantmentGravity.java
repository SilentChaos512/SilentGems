package net.silentchaos512.gems.enchantment;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.silentchaos512.lib.util.StackHelper;

public class EnchantmentGravity extends Enchantment {

  public static final String NAME = "Gravity";

  protected EnchantmentGravity() {

    super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER,
        new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
    setName(NAME);
    setRegistryName(NAME);
  }

  @Override
  public int getMinEnchantability(int level) {

    return 20 + (level - 1) * 8;
  }

  @Override
  public int getMaxEnchantability(int level) {

    return getMinEnchantability(level) + 50;
  }

  @Override
  public int getMaxLevel() {

    return 3;
  }

  @Override
  public String getName() {

    return "enchantment.silentgems:" + NAME;
  }

  public void onGetBreakSpeed(PlayerEvent.BreakSpeed event, ItemStack tool, int enchLevel) {

    if (StackHelper.isEmpty(tool) || enchLevel <= 0)
      return;

    EntityPlayer player = event.getEntityPlayer();
    float speedMulti = 5f / (getMaxLevel() - enchLevel + 1);

    // In air or flying?
    if (!player.onGround || player.capabilities.isFlying)
      event.setNewSpeed(event.getNewSpeed() * speedMulti);

    // Underwater?
    if (player.isInsideOfMaterial(Material.WATER))
      event.setNewSpeed(event.getNewSpeed() * speedMulti);
  }
}
