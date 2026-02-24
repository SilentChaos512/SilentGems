package net.silentchaos512.gems.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class PetSummonerItem extends ItemWithFlavorText {
    private final Function<Level, ? extends TamableAnimal> petFactory;

    public PetSummonerItem(Function<Level, ? extends TamableAnimal> petFactory, Properties properties) {
        super(properties);
        this.petFactory = petFactory;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (!(level instanceof ServerLevel serverLevel) || player == null) {
            return InteractionResult.SUCCESS;
        } else if (!player.mayUseItemAt(pos.relative(facing), facing, stack)) {
            return InteractionResult.PASS;
        } else {
            pos = pos.relative(facing);

            TamableAnimal pet = this.petFactory.apply(level);

            // Set position
            pet.snapTo(pos.getX(), pos.getY(), pos.getZ(),
                    Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);
            pet.yHeadRot = pet.getYRot();
            pet.yBodyRot = pet.getYRot();

            // Calling finalizeSpawn should set variant data
            pet.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pet.blockPosition()), EntitySpawnReason.MOB_SUMMONED, null);
            level.addFreshEntity(pet);
            pet.playAmbientSound();

            if (stack.has(DataComponents.CUSTOM_NAME)) {
                pet.setCustomName(stack.getHoverName());
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Make it tame and set master.
            pet.setTame(true, true);
            pet.setOwner(player);
            level.broadcastEntityEvent(pet, (byte) 7);

            // Heal to full health (because wolves)
            pet.setHealth(pet.getMaxHealth());

            return InteractionResult.SUCCESS;
        }
    }

    public static Cat getCat(Level level) {
        return new Cat(EntityType.CAT, level);
    }

    public static Wolf getDog(Level level) {
        return new Wolf(EntityType.WOLF, level);
    }
}
