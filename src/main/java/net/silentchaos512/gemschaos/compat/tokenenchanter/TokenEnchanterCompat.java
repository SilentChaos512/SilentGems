package net.silentchaos512.gemschaos.compat.tokenenchanter;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import net.silentchaos512.gems.util.Const;

import javax.annotation.Nullable;

public final class TokenEnchanterCompat {
    private TokenEnchanterCompat() {}

    @Nullable
    public static ICapabilityProvider getChaosXpStorageProvider(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (ModList.get().isLoaded(Const.TOKEN_ENCHANTER_MOD_ID)) {
            return TokenEnchanterCompatProxy.getChaosXpStorageProvider(stack, nbt);
        }
        return null;
    }
}
