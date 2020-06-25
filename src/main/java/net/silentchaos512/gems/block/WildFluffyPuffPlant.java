package net.silentchaos512.gems.block;

import net.minecraft.block.BushBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class WildFluffyPuffPlant extends BushBlock {
    public WildFluffyPuffPlant() {
        super(Properties.create(Material.PLANTS)
                .hardnessAndResistance(0)
                .doesNotBlockMovement()
                .sound(SoundType.CROP)
        );
    }
}
