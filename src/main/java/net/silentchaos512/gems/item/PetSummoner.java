package net.silentchaos512.gems.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.silentchaos512.gems.lib.ModItemGroups;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class PetSummoner extends Item {
    private final Function<World, ? extends EntityTameable> petFactory;

    public PetSummoner(Function<World, ? extends EntityTameable> petFactory) {
        super(new Builder().group(ModItemGroups.UTILITY));
        this.petFactory = petFactory;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ResourceLocation registryName = Objects.requireNonNull(getRegistryName());
        String key = String.format("item.%s.%s.desc", registryName.getNamespace(), registryName.getPath());
        tooltip.add(new TextComponentTranslation(key));
    }

    @Override
    public EnumActionResult onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        World world = context.getWorld();
        EntityPlayer player = context.getPlayer();
        BlockPos pos = context.getPos();
        EnumFacing facing = context.getFace();

        if (world.isRemote || player == null) {
            return EnumActionResult.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            return EnumActionResult.PASS;
        } else {
            pos = pos.offset(facing);

            EntityTameable pet = this.petFactory.apply(world);

            // Set position
            pet.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(),
                    MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
            pet.rotationYawHead = pet.rotationYaw;
            pet.renderYawOffset = pet.rotationYaw;
            world.spawnEntity(pet);
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

            return EnumActionResult.SUCCESS;
        }
    }

    public static EntityOcelot getCat(World world) {
        EntityOcelot cat = new EntityOcelot(world);
        cat.setTameSkin(1 + world.rand.nextInt(3));
        return cat;
    }

    public static EntityWolf getDog(World world) {
        return new EntityWolf(world);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
