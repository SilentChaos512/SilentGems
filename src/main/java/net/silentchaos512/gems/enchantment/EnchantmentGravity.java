package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EnchantmentGravity extends Enchantment {
    public static boolean ENABLED = true;

    public EnchantmentGravity() {
        super(Rarity.VERY_RARE, EnchantmentType.DIGGER, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMinEnchantability(int level) {
        return 20 + (level - 1) * 8;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return ENABLED && super.canApplyAtEnchantingTable(stack);
    }

    public void onGetBreakSpeed(PlayerEvent.BreakSpeed event, ItemStack tool, int enchLevel) {
        if (tool.isEmpty() || enchLevel <= 0) return;

        PlayerEntity player = event.getEntityPlayer();
        float speedMulti = 5f / (getMaxLevel() - enchLevel + 1);

        // In air or flying?
        if (!player.onGround || player.abilities.isFlying) {
            event.setNewSpeed(event.getNewSpeed() * speedMulti);
        }

        // Underwater?
        if (player.isInWater()) {
            event.setNewSpeed(event.getNewSpeed() * speedMulti);
        }
    }
}
