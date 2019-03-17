package net.silentchaos512.gems.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.lib.Gems;

import java.util.function.Supplier;

public final class ModItemGroups {
    public static final ItemGroup BLOCKS = create("blocks", () -> Gems.selectRandom().getBlock());
    public static final ItemGroup MATERIALS = create("materials", () -> Gems.selectRandom().getItem());
    public static final ItemGroup UTILITY = create("utility", () -> Gems.selectRandom().getChaosGem());

    private ModItemGroups() {}

    private static ItemGroup create(String name, Supplier<IItemProvider> icon) {
        return new ItemGroup(SilentGems.MOD_ID + "." + name) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(icon.get());
            }
        };
    }
}
