package net.silentchaos512.gemschaos.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.silentchaos512.gemschaos.ChaosMod;

import javax.annotation.Nullable;
import java.util.List;

public class ChaosOrbItem extends Item {
    public static final ResourceLocation CRACK_STAGE = ChaosMod.getId("crack_stage");

    private final int maxAbsorb;
    private final int crackStages;
    private final float leakage;

    public ChaosOrbItem(int crackStages, int maxAbsorb, float leakage) {
        super(new Properties()
                .group(ChaosMod.ITEM_GROUP)
                .maxStackSize(1)
                .defaultMaxDamage(maxAbsorb)
                .setNoRepair()
        );

        this.maxAbsorb = maxAbsorb;
        this.crackStages = crackStages;
        this.leakage = leakage;
    }

    public static int absorbChaos(LivingEntity entity, ItemStack stack, int amount) {
        if (!(stack.getItem() instanceof ChaosOrbItem)) return 0;

        ChaosOrbItem item = (ChaosOrbItem) stack.getItem();
        int toAbsorb = getChaosToAbsorb(amount, item);
        int crackStage = getCrackStage(stack);

        // Absorb chaos amount minus leakage, destroy if durability depleted
        if (stack.attemptDamageItem(toAbsorb, entity.getRNG(), entity instanceof ServerPlayerEntity ? (ServerPlayerEntity) entity : null)) {
            destroyOrb(entity, stack);
        }

        // If not destroyed, check crack stage and notify player
        if (!stack.isEmpty()) {
            int newCrackStage = getCrackStage(stack);
            if (newCrackStage != crackStage) {
                notifyOrbCracked(entity, stack);
            }
        }

        return amount - toAbsorb;
    }

    public static int absorbChaos(IWorld world, BlockPos pos, ItemStack stack, int amount) {
        if (!(stack.getItem() instanceof ChaosOrbItem)) return 0;

        ChaosOrbItem item = (ChaosOrbItem) stack.getItem();
        int toAbsorb = getChaosToAbsorb(amount, item);
        int crackStage = getCrackStage(stack);

        // Absorb chaos amount minus leakage, destroy if durability depleted
        if (stack.attemptDamageItem(toAbsorb, ChaosMod.RANDOM, null)) {
            destroyOrb(world, pos, stack);
        }

        // If not destroyed, check crack stage and play sound
        if (!stack.isEmpty()) {
            int newCrackStage = getCrackStage(stack);
            if (newCrackStage != crackStage) {
                playBreakSound(world, pos);
            }
        }

        return amount - toAbsorb;
    }

    private static int getChaosToAbsorb(int amount, ChaosOrbItem item) {
        return (int) (amount * (1 - item.leakage));
    }

    private static void notifyOrbCracked(LivingEntity entity, ItemStack stack) {
        entity.sendMessage(ChaosMod.TEXT.translate("item", "chaos_orb.crack", stack.getDisplayName()), Util.DUMMY_UUID);
        playCrackSound(entity.world, entity.getPosition());
    }

    private static void destroyOrb(LivingEntity entity, ItemStack stack) {
        // Display name will be Air after we shrink, so get it now
        ITextComponent displayName = stack.getDisplayName();
//        entity.renderBrokenItemStack(stack);
        entity.world.playSound(entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, entity.getSoundCategory(), 0.8F, 0.8F + entity.world.rand.nextFloat() * 0.4F, false);
        if (entity instanceof PlayerEntity) {
            ((PlayerEntity) entity).addStat(Stats.ITEM_BROKEN.get(stack.getItem()));
        }
        destroyOrb(entity.world, entity.getPosition(), stack);

        int pieceCount = entity.getRNG().nextInt(99000) + 1000;
        String piecesFormatted = String.format("%,d", pieceCount);
        entity.sendMessage(ChaosMod.TEXT.translate("item", "chaos_orb.break", displayName, piecesFormatted), Util.DUMMY_UUID);
    }

    private static void destroyOrb(IWorld world, BlockPos pos, ItemStack stack) {
        stack.shrink(1);
        stack.setDamage(0);
        playBreakSound(world, pos);
    }

    private static void playCrackSound(IWorld world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT, 0.6f, 1.5f);
    }

    private static void playBreakSound(IWorld world, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT, 0.7f, -2.5f);
    }

    public static int getChaosAbsorbed(ItemStack stack) {
        return stack.getDamage();
    }

    public static int getCrackStage(ItemStack stack) {
        if (!(stack.getItem() instanceof ChaosOrbItem)) return 0;

        ChaosOrbItem item = (ChaosOrbItem) stack.getItem();
        float ratio = (float) getChaosAbsorbed(stack) / item.maxAbsorb;
        return MathHelper.clamp((int) (ratio * (item.crackStages + 1)), 0, item.crackStages);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.silentgems.chaos_orb.leakage", (int) (100 * this.leakage)));
    }
}
