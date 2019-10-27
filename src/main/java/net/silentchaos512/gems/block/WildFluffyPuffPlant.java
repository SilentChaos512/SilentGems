package net.silentchaos512.gems.block;

import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.silentchaos512.lib.util.Lazy;

public final class WildFluffyPuffPlant extends BushBlock {
    public static final Lazy<WildFluffyPuffPlant> INSTANCE = Lazy.of(WildFluffyPuffPlant::new);

    private WildFluffyPuffPlant() {
        super(Properties.create(Material.PLANTS)
                .hardnessAndResistance(0)
                .doesNotBlockMovement()
                .sound(SoundType.CROP)
        );
    }
}
