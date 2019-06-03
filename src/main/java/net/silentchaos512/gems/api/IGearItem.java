package net.silentchaos512.gems.api;

import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.config.ConfigOptionToolClass;
import net.silentchaos512.gems.lib.EnumToolType;

import java.util.Set;

public interface IGearItem {
    ConfigOptionToolClass getConfig();

    EnumToolType getToolType();

    default Set<EnumMaterialTier> getValidTiers() {
        return getConfig().validTiers;
    }
}
