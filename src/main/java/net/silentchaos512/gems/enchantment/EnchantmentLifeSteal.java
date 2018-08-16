package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.SilentGems;

public class EnchantmentLifeSteal extends Enchantment {
    public static final float HEAL_AMOUNT_MULTI = 1f / 40f;
    public static final float HEAL_AMOUNT_CAP = 1f;

    public static boolean ENABLED = true;

    public EnchantmentLifeSteal() {
        super(Rarity.RARE, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        setName(SilentGems.MODID + ".lifesteal");
    }

    public float getAmountHealed(int level, float damageDealt) {
        return MathHelper.clamp(level * damageDealt * HEAL_AMOUNT_MULTI, 0f, HEAL_AMOUNT_CAP);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (!ENABLED) return false;
        Item item = stack.getItem();
        return item instanceof ItemSword || item instanceof ItemAxe;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 15 + (level - 1) * 9;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return getMinEnchantability(level) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
