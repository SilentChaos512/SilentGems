package net.silentchaos512.gems.enchantment;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.silentchaos512.gems.SilentGems;

public class EnchantmentGravity extends Enchantment {
    public static boolean ENABLED = true;

    public EnchantmentGravity() {
        super(Rarity.VERY_RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setName(SilentGems.MODID + ".gravity");
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
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return ENABLED && super.canApplyAtEnchantingTable(stack);
    }

    public void onGetBreakSpeed(PlayerEvent.BreakSpeed event, ItemStack tool, int enchLevel) {
        if (tool.isEmpty() || enchLevel <= 0) return;

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
