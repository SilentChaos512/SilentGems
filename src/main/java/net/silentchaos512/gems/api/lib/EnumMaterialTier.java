package net.silentchaos512.gems.api.lib;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.api.tool.part.ToolPartRegistry;

public enum EnumMaterialTier {
    MUNDANE, REGULAR, SUPER;

    public String getLocalizedName() {
        return SilentGems.i18n.miscText("tooltier." + name());
    }

    public static EnumMaterialTier fromStack(ItemStack stack) {
        ToolPart part = ToolPartRegistry.fromStack(stack);
        if (part != null)
            return part.getTier();
        return null;
    }
}
