package net.silentchaos512.gems.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IPedestalItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PedestalItemCapability implements IPedestalItem, ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IPedestalItem.class)
    public static Capability<IPedestalItem> INSTANCE = null;
    public static final ResourceLocation NAME = SilentGems.getId("pedestal_item");

    private final LazyOptional<IPedestalItem> holder = LazyOptional.of(() -> this);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return INSTANCE.orEmpty(cap, holder);
    }

    @Override
    public CompoundNBT serializeNBT() {
        return new CompoundNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
    }

    @Override
    public boolean pedestalPowerChange(ItemStack stack, World world, BlockPos pos, boolean powered) {
        if (stack.getItem() instanceof IPedestalItem) {
            return ((IPedestalItem) stack.getItem()).pedestalPowerChange(stack, world, pos, powered);
        }
        return false;
    }

    @Override
    public void pedestalTick(ItemStack stack, World world, BlockPos pos) {
        if (stack.getItem() instanceof IPedestalItem) {
            ((IPedestalItem) stack.getItem()).pedestalTick(stack, world, pos);
        }
    }
}
