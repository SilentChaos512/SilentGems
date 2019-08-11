package net.silentchaos512.gems.chaos;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.chaos.IChaosSource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChaosSourceCapability implements IChaosSource, ICapabilitySerializable<CompoundNBT> {
    @CapabilityInject(IChaosSource.class)
    public static Capability<IChaosSource> INSTANCE = null;
    public static ResourceLocation NAME = new ResourceLocation(SilentGems.MOD_ID, "chaos_source");

    private static final String NBT_CHAOS = "Chaos";

    private final LazyOptional<IChaosSource> holder = LazyOptional.of(() -> this);

    private int chaos;

    @Override
    public int getChaos() {
        return chaos;
    }

    @Override
    public void setChaos(int amount) {
        chaos = amount;
        if (chaos < 0) chaos = 0;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return INSTANCE.orEmpty(cap, holder);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(NBT_CHAOS, chaos);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        chaos = nbt.getInt(NBT_CHAOS);
    }

    public static boolean canAttachTo(ICapabilityProvider obj) {
        return (obj instanceof PlayerEntity || obj instanceof World) && !obj.getCapability(INSTANCE).isPresent();
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(IChaosSource.class, new Storage(), ChaosSourceCapability::new);
    }

    private static class Storage implements Capability.IStorage<IChaosSource> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IChaosSource> capability, IChaosSource instance, Direction side) {
            if (instance instanceof ChaosSourceCapability) {
                return ((ChaosSourceCapability) instance).serializeNBT();
            }
            return new CompoundNBT();
        }

        @Override
        public void readNBT(Capability<IChaosSource> capability, IChaosSource instance, Direction side, INBT nbt) {
            if (instance instanceof ChaosSourceCapability) {
                ((ChaosSourceCapability) instance).deserializeNBT((CompoundNBT) nbt);
            }
        }
    }
}
