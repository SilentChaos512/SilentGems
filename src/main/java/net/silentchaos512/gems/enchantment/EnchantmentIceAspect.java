package net.silentchaos512.gems.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.silentchaos512.gems.event.EnchantmentEvents;
import net.silentchaos512.gems.init.GemsEffects;

public class EnchantmentIceAspect extends Enchantment {
    public static final int EFFECT_DURATION = 80;

    public static boolean ENABLED = true;

    public EnchantmentIceAspect() {
        super(Rarity.RARE, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return !(ench instanceof FireAspectEnchantment)
                && !(ench instanceof EnchantmentLightningAspect)
                && super.canApplyTogether(ench);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        if (!ENABLED) return false;
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 10 + 20 * (level - 1);
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    /**
     * Apply effect to mob. Called in {@link EnchantmentEvents#onLivingAttack}.
     */
    public static void applyTo(LivingEntity entityLiving, int enchLevel) {
        int duration = getEffectDuration(entityLiving, enchLevel);
        int amplifier = enchLevel - 1;
        entityLiving.addPotionEffect(new EffectInstance(GemsEffects.FREEZING.get(), duration, amplifier, true, false));
    }

    private static int getEffectDuration(LivingEntity entityLiving, int enchLevel) {
        int ret = EFFECT_DURATION + (enchLevel - 1) * EFFECT_DURATION / 2;
        if (entityLiving instanceof PlayerEntity)
            ret /= 2;
        return ret;
    }
}
