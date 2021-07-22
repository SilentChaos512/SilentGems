package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.util.TextUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class PetSummonerItem extends Item {
    private final Function<World, ? extends TameableEntity> petFactory;

    public PetSummonerItem(Function<World, ? extends TameableEntity> petFactory, Properties properties) {
        super(properties);
        this.petFactory = petFactory;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextUtil.itemSub(this, "desc"));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();

        if (world.isClientSide || player == null) {
            return ActionResultType.SUCCESS;
        } else if (!player.mayUseItemAt(pos.relative(facing), facing, stack)) {
            return ActionResultType.PASS;
        } else {
            pos = pos.relative(facing);

            TameableEntity pet = this.petFactory.apply(world);

            // Set position
            pet.moveTo(pos.getX(), pos.getY(), pos.getZ(),
                    MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
            pet.yHeadRot = pet.yRot;
            pet.yBodyRot = pet.yRot;

            world.addFreshEntity(pet);
            pet.playAmbientSound();

            if (stack.hasCustomHoverName()) {
                pet.setCustomName(stack.getHoverName());
            }

            if (!player.abilities.instabuild) {
                stack.shrink(1);
            }

            // Make it tame and set master.
            pet.setTame(true);
            pet.setOwnerUUID(player.getUUID());
            world.broadcastEntityEvent(pet, (byte) 7);

            // Heal to full health (because wolves)
            pet.setHealth(pet.getMaxHealth());

            return ActionResultType.SUCCESS;
        }
    }

    public static CatEntity getCat(World world) {
        CatEntity cat = new CatEntity(EntityType.CAT, world);
        cat.setCatType( world.random.nextInt(11));
        return cat;
    }

    public static WolfEntity getDog(World world) {
        return new WolfEntity(EntityType.WOLF, world);
    }
}
