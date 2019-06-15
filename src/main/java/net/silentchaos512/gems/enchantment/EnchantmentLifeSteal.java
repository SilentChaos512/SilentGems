package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.MathHelper;

public class EnchantmentLifeSteal extends Enchantment {
    private static final float HEAL_AMOUNT_MULTI = 1f / 40f;
    private static final float HEAL_AMOUNT_CAP = 1f;

    public static boolean ENABLED = true;

    public EnchantmentLifeSteal() {
        super(Rarity.RARE, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    public static float getAmountHealed(int level, float damageDealt) {
        return MathHelper.clamp(level * damageDealt * HEAL_AMOUNT_MULTI, 0f, HEAL_AMOUNT_CAP);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (!ENABLED) return false;
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 15 + (level - 1) * 9;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
