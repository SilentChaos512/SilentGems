package net.silentchaos512.gems.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gear.api.item.ICoreTool;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.chaos.Chaos;
import net.silentchaos512.gems.init.GemsTraits;
import net.silentchaos512.utils.MathUtils;

public final class TraitEvents {
    public static final TraitEvents INSTANCE = new TraitEvents();

    private TraitEvents() { }

    @SubscribeEvent
    public void criticalHit(CriticalHitEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack weapon = player.getHeldItemMainhand();
        if (GearHelper.isGear(weapon)) {
            int critical = TraitHelper.getTraitLevel(weapon, GemsTraits.CRITICAL);
            if (critical > 0 && MathUtils.tryPercentage(0.1 * critical)) {
                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerAttack(AttackEntityEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack tool = player.getHeldItemMainhand();

        if (!tool.isEmpty() && tool.getItem() instanceof ICoreTool) {
            // Chaotic: generates chaos when used
            int chaotic = TraitHelper.getTraitLevel(tool, GemsTraits.CHAOTIC);
            if (chaotic > 0) {
                Chaos.generate(player, 100 * chaotic, true);
            }
        }
    }

    @SubscribeEvent
    public void onLivingAttack(LivingHurtEvent event) {
        Entity attacker = event.getSource().getTrueSource();
        if (!(attacker instanceof ServerPlayerEntity)) return;

        PlayerEntity player = (PlayerEntity) attacker;
        ItemStack stack = player.getHeldItemMainhand();
        if (!stack.isEmpty() && stack.getItem() instanceof ICoreTool) {
            // Luna: extra damage at night
            int luna = TraitHelper.getTraitLevel(stack, GemsTraits.LUNA);
            if (luna > 0 && !player.world.isDaytime() && MathUtils.tryPercentage(0.25)) {
                event.setAmount(event.getAmount() + 4 * luna);
                knockback(player, event.getEntityLiving(), 0.3f * luna);
            }
            // Sol: extra damage during the day
            int sol = TraitHelper.getTraitLevel(stack, GemsTraits.SOL);
            if (sol > 0 && player.world.isDaytime() && MathUtils.tryPercentage(0.25)) {
                event.setAmount(event.getAmount() + 4 * sol);
                knockback(player, event.getEntityLiving(), 0.3f * sol);
            }
        }
    }

    private static void knockback(LivingEntity attacker, LivingEntity target, float strength) {
        double x = attacker.posX - target.posX;
        double z;
        for(z = attacker.posZ - target.posZ; x * x + z * z < 1.0E-4D; z = (Math.random() - Math.random()) * 0.01D) {
            x = (Math.random() - Math.random()) * 0.01D;
        }
        target.knockBack(attacker, strength, x, z);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ItemStack tool = player.getHeldItemMainhand();

        if (!tool.isEmpty() && tool.getItem() instanceof ICoreTool) {
            // Chaotic: generates chaos when used
            int chaotic = TraitHelper.getTraitLevel(tool, GemsTraits.CHAOTIC);
            if (chaotic > 0) {
                Chaos.generate(player, 100 * chaotic, true);
            }
        }
    }

    @SubscribeEvent
    public void onGetBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        ItemStack tool = player.getHeldItemMainhand();

        if (!tool.isEmpty() && GearHelper.isGear(tool)) {
            // Entropy: extra speed based on chaos
            int entropy = TraitHelper.getTraitLevel(tool, GemsTraits.ENTROPY);
            if (entropy > 0) {
                float chaosRatio = (Chaos.getChaos(player) + Chaos.getChaos(player.world)) / 5_000_000;
                float ratioClamped = MathHelper.clamp(chaosRatio, 0f, 1f);
                event.setNewSpeed(event.getNewSpeed() + 3 * entropy * ratioClamped);
            }
        }
    }
}
