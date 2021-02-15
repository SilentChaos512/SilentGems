package net.silentchaos512.gemschaos.compat.tokenenchanter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.silentchaos512.tokenenchanter.api.xp.XpStorageCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

final class TokenEnchanterCompatProxy {
    private TokenEnchanterCompatProxy() {}

    static ICapabilityProvider getChaosXpStorageProvider(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                if (cap == XpStorageCapability.INSTANCE) {
                    return LazyOptional.of(() -> new ChaosXpStorage(stack)).cast();
                }
                return LazyOptional.empty();
            }
        };
    }
}
