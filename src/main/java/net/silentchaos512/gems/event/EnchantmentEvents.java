package net.silentchaos512.gems.event;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.enchantment.EnchantmentIceAspect;
import net.silentchaos512.gems.enchantment.EnchantmentLifeSteal;
import net.silentchaos512.gems.enchantment.EnchantmentLightningAspect;
import net.silentchaos512.gems.init.GemsEnchantments;

@Mod.EventBusSubscriber(modid = SilentGems.MOD_ID)
public final class EnchantmentEvents {
    private EnchantmentEvents() {}

    @SubscribeEvent
    public static void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        ItemStack mainHand = player.getHeldItem(Hand.MAIN_HAND);

        if (!mainHand.isEmpty()) {
            // Gravity enchantment.
            int gravityLevel = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.gravity, mainHand);
            if (gravityLevel > 0) {
                GemsEnchantments.gravity.onGetBreakSpeed(event, mainHand, gravityLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            ItemStack mainHand = player.getHeldItemMainhand();
            ItemStack offHand = player.getHeldItemOffhand();

            int lifeStealLevel = 0;
            int iceAspectLevel = 0;
            int lightningAspectLevel = 0;

            // Get levels of relevant enchantments.
            if (!mainHand.isEmpty()) {
                lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.lifeSteal, mainHand);
                iceAspectLevel = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.iceAspect, mainHand);
                lightningAspectLevel = EnchantmentHelper
                        .getEnchantmentLevel(GemsEnchantments.lightningAspect, mainHand);
            }
            // If not, is it on off hand?
            if (lifeStealLevel < 1 && !offHand.isEmpty()) {
                lifeStealLevel = EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.lifeSteal, offHand);
            }

            // Do life steal?
            if (lifeStealLevel > 0) {
                float amount = Math.min(event.getAmount(), event.getEntityLiving().getHealth());
                float healAmount = EnchantmentLifeSteal.getAmountHealed(lifeStealLevel, amount);
                player.heal(healAmount);
            }

            // Ice Aspect
            if (iceAspectLevel > 0) {
                EnchantmentIceAspect.applyTo(event.getEntityLiving(), iceAspectLevel);
            }

            // Lightning Aspect
            if (lightningAspectLevel > 0) {
                EnchantmentLightningAspect.applyTo(event.getEntityLiving(), lightningAspectLevel);
            }
        }
    }
}
