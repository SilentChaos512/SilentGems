package net.silentchaos512.gems.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.init.GemsEnchantments;
import net.silentchaos512.gems.lib.soul.GearSoul;
import net.silentchaos512.gems.lib.soul.SoulTraits;
import net.silentchaos512.lib.util.PlayerUtils;
import net.silentchaos512.lib.util.TimeUtils;

public final class SoulEvents {
    public static final SoulEvents INSTANCE = new SoulEvents();

    private static final int CLEAR_DELAY = TimeUtils.ticksFromMinutes(15);
    private static final int SOUL_WRITE_DELAY = TimeUtils.ticksFromSeconds(30);

    private static int ticks;

    private SoulEvents() { }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player instanceof ServerPlayerEntity) {
            ItemStack mainHand = player.getHeldItemMainhand();
            GearSoul soul = SoulManager.getSoul(mainHand);

            if (soul != null) {
                soul.onBreakBlock(player, mainHand, event.getWorld(), event.getPos(), event.getState());
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        PlayerEntity player = event.getPlayer();
        ItemStack mainHand = player.getHeldItemMainhand();

        // Overridden by the Gravity enchantment.
        if (EnchantmentHelper.getEnchantmentLevel(GemsEnchantments.GRAVITY.get(), mainHand) > 0) {
            return;
        }

        GearSoul soul = SoulManager.getSoul(mainHand);
        if (soul != null) {
            // Aquatic or Aerial?
            if (player.isInWater()) {
                int aquatic = TraitHelper.getTraitLevel(mainHand, SoulTraits.AQUATIC);
                if (aquatic > 0) {
                    event.setNewSpeed(event.getNewSpeed() * (5f / (5 - aquatic + 1)));
                }
            } else if (!player.onGround || player.abilities.isFlying) {
                int aerial = TraitHelper.getTraitLevel(mainHand, SoulTraits.AERIAL);
                if (aerial > 0) {
                    event.setNewSpeed(event.getNewSpeed() * (5f / (5 - aerial + 1)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getTrueSource() instanceof ServerPlayerEntity) {
            LivingEntity hurt = event.getEntityLiving();
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            ItemStack mainHand = player.getHeldItemMainhand();
            GearSoul soul = SoulManager.getSoul(mainHand);

            if (soul != null) {
                float damageAmount = Math.min(event.getAmount(), hurt.getMaxHealth());
                soul.onAttackedWith(player, mainHand, hurt, damageAmount);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDamage(LivingHurtEvent event) {
        // Aerial - fall damage protection
        if (event.getSource() == DamageSource.FALL && event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            SoulTraits.getHighestLevelEitherHand(player, SoulTraits.AERIAL).ifPresent(hand -> {
                ItemStack stack = player.getHeldItem(hand);
                int level = TraitHelper.getTraitLevel(stack, SoulTraits.AERIAL);
                // Reduce by 2 + 2 * level, or 15% per level, whichever is greater
                float amountToReduce = Math.max(2 + 2 * level, 0.15f * level * event.getAmount());
                event.setAmount(event.getAmount() - amountToReduce);
                GearHelper.attemptDamage(stack, 2, player, hand);
            });
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        SoulManager.writeSoulsToNBT(event.getPlayer(), true);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        // Aquatic - drowning protection (max air is 300, so 30 per bubble)
        if (!player.world.isRemote && player.getAir() < 5) {
            SoulTraits.getHighestLevelEitherHand(player, SoulTraits.AQUATIC).ifPresent(hand -> {
                ItemStack stack = player.getHeldItem(hand);
                int level = TraitHelper.getTraitLevel(stack, SoulTraits.AQUATIC);
                int amountToRestore = 60 * level;
                player.setAir(player.getAir() + amountToRestore);
                GearHelper.attemptDamage(stack, 2, player, hand);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_HURT_DROWN, SoundCategory.PLAYERS, 1f, 1f);
            });
        }

        if (!player.world.isRemote && player.ticksExisted % SOUL_WRITE_DELAY == 0) {
            SoulManager.queueSoulsForWrite(player);
            SoulManager.writeSoulsToNBT(player, false);
        }

        // TODO: Maybe make this less frequent? Or avoid using PlayerUtils.
        for (ItemStack stack : PlayerUtils.getNonEmptyStacks(player, stack -> stack.getItem() instanceof ICoreItem)) {
            GearSoul soul = SoulManager.getSoul(stack);
            if (soul != null) {
                soul.updateTick(stack, player);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (event.phase == TickEvent.Phase.END && (event instanceof TickEvent.ClientTickEvent || event instanceof TickEvent.ServerTickEvent)) {
            if (++ticks % CLEAR_DELAY == 0) {
                SoulManager.SOULS.clear();
            }
        }
    }

    public static final class Client {
        public static final Client INSTANCE = new Client();

        private Client() { }

        @SubscribeEvent
        public void onTooltip(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            GearSoul soul = SoulManager.getSoul(stack);
            if (soul != null) {
                soul.addInformation(stack, null, event.getToolTip(), event.getFlags().isAdvanced());
            }
        }
    }
}
