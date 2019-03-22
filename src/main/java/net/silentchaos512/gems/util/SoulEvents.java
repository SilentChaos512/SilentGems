package net.silentchaos512.gems.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.silentchaos512.gear.api.item.ICoreItem;
import net.silentchaos512.gear.util.GearHelper;
import net.silentchaos512.gear.util.TraitHelper;
import net.silentchaos512.gems.init.ModEnchantments;
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
        EntityPlayer player = event.getPlayer();
        if (player instanceof EntityPlayerMP) {
            ItemStack mainHand = player.getHeldItemMainhand();
            GearSoul soul = SoulManager.getSoul(mainHand);

            if (soul != null) {
                soul.onBreakBlock(player, mainHand, event.getWorld(), event.getPos(), event.getState());
            }
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack mainHand = player.getHeldItemMainhand();

        // Overridden by the Gravity enchantment.
        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.gravity, mainHand) > 0) {
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
        if (event.getSource().getTrueSource() instanceof EntityPlayerMP) {
            EntityLivingBase hurt = event.getEntityLiving();
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
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
        if (event.getSource() == DamageSource.FALL && event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            SoulTraits.getHighestLevelEitherHand(player, SoulTraits.AERIAL).ifPresent(tuple -> {
                // Reduce by 2 + 2 * level, or 15% per level, whichever is greater
                float amountToReduce = Math.max(
                        2 + 2 * tuple.getB(),
                        0.15f * tuple.getB() * event.getAmount()
                );
                event.setAmount(event.getAmount() - amountToReduce);
                GearHelper.attemptDamage(tuple.getA(), 2, player);
            });
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent event) {
        SoulManager.writeSoulsToNBT(event.getPlayer(), true);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;

        // Aquatic - drowning protection (max air is 300, so 30 per bubble)
        if (!player.world.isRemote && player.getAir() < 5) {
            SoulTraits.getHighestLevelEitherHand(player, SoulTraits.AQUATIC).ifPresent(tuple -> {
                int amountToRestore = 60 * tuple.getB();
                player.setAir(player.getAir() + amountToRestore);
                GearHelper.attemptDamage(tuple.getA(), 2, player);
                player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_PLAYER_HURT_DROWN, SoundCategory.PLAYERS, 1f, 1f);
            });
        }

        if (ticks % SOUL_WRITE_DELAY == 0) {
            SoulManager.queueSoulsForWrite(player);
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
            if (GearHelper.isGear(stack)) {
                GearSoul soul = SoulManager.getSoul(stack);
                if (soul != null) {
                    soul.addInformation(stack, null, event.getToolTip(), event.getFlags().isAdvanced());
                }
            }
        }
    }
}
