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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.silentchaos512.gems.init.ModItemGroups;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PetSummonerItem extends Item {
    private final Function<World, ? extends TameableEntity> petFactory;

    public PetSummonerItem(Function<World, ? extends TameableEntity> petFactory) {
        super(new Properties().group(ModItemGroups.UTILITY));
        this.petFactory = petFactory;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ResourceLocation registryName = Objects.requireNonNull(getRegistryName());
        String key = String.format("item.%s.%s.desc", registryName.getNamespace(), registryName.getPath());
        tooltip.add(new TranslationTextComponent(key));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getPos();
        Direction facing = context.getFace();

        if (world.isRemote || player == null) {
            return ActionResultType.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return ActionResultType.PASS;
        } else {
            pos = pos.offset(facing);

            TameableEntity pet = this.petFactory.apply(world);

            // Set position
            pet.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(),
                    MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.addEntity(pet);
            pet.playAmbientSound();

            if (stack.hasDisplayName()) {
                pet.setCustomName(stack.getDisplayName());
            }

            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }

            // Make it tame and set master.
            pet.setTamed(true);
            pet.setOwnerId(player.getUniqueID());
            world.setEntityState(pet, (byte) 7);

            // Heal to full health (because wolves)
            pet.setHealth(pet.getMaxHealth());

            return ActionResultType.SUCCESS;
        }
    }

    public static CatEntity getCat(World world) {
        CatEntity cat = new CatEntity(EntityType.CAT, world);
        cat.setCatType( world.rand.nextInt(11));
        return cat;
    }

    public static WolfEntity getDog(World world) {
        return new WolfEntity(EntityType.WOLF, world);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
