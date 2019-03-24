package net.silentchaos512.gems.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.silentchaos512.gems.block.FluffyPuffPlant;
import net.silentchaos512.utils.Lazy;

public final class FluffyPuffSeeds extends ItemSeeds {
    public static final Lazy<FluffyPuffSeeds> INSTANCE = Lazy.of(FluffyPuffSeeds::new);

    private FluffyPuffSeeds() {
        super(FluffyPuffPlant.NORMAL.get(), new Properties());
        // Super broken, let's take a different approach... dangit Forge
//        MinecraftForge.addGrassSeed(new ItemStack(this), 2);
    }

    @Override
    public IBlockState getPlant(IBlockReader world, BlockPos pos) {
        return FluffyPuffPlant.NORMAL.get().getDefaultState();
    }
}
