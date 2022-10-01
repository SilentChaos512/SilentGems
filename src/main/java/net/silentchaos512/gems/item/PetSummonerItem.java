package net.silentchaos512.gems.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.util.TextUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class PetSummonerItem extends Item {
    private final Function<Level, ? extends TamableAnimal> petFactory;

    public PetSummonerItem(Function<Level, ? extends TamableAnimal> petFactory, Properties properties) {
        super(properties);
        this.petFactory = petFactory;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(TextUtil.itemSub(this, "desc"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level world = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (world.isClientSide || player == null) {
            return InteractionResult.SUCCESS;
        } else if (!player.mayUseItemAt(pos.relative(facing), facing, stack)) {
            return InteractionResult.PASS;
        } else {
            pos = pos.relative(facing);

            TamableAnimal pet = this.petFactory.apply(world);

            // Set position
            pet.moveTo(pos.getX(), pos.getY(), pos.getZ(),
                    Mth.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
            pet.yHeadRot = pet.getYRot();
            pet.yBodyRot = pet.getYRot();

            world.addFreshEntity(pet);
            pet.playAmbientSound();

            if (stack.hasCustomHoverName()) {
                pet.setCustomName(stack.getHoverName());
            }

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            // Make it tame and set master.
            pet.setTame(true);
            pet.setOwnerUUID(player.getUUID());
            world.broadcastEntityEvent(pet, (byte) 7);

            // Heal to full health (because wolves)
            pet.setHealth(pet.getMaxHealth());

            return InteractionResult.SUCCESS;
        }
    }

    public static Cat getCat(Level world) {
        Cat cat = new Cat(EntityType.CAT, world);
        Registry.CAT_VARIANT.getRandom(GemsBase.RANDOM_SOURCE).ifPresent(variant ->
                cat.setCatVariant(variant.get()));
        return cat;
    }

    public static Wolf getDog(Level world) {
        return new Wolf(EntityType.WOLF, world);
    }
}
