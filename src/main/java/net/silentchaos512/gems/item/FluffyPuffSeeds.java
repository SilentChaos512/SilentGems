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

    // TODO: Is this still necessary?
//    @Override
//    public EnumActionResult onItemUse(ItemUseContext p_195939_1_) {
//        // Identical to ItemSeeds.onItemUse, except using getPlant instead of this.crops, which does not work.
//        IBlockState state = worldIn.getBlockState(pos);
//        ItemStack stack = playerIn.getHeldItem(hand);
//        if (facing == EnumFacing.UP && playerIn.canPlayerEdit(pos.offset(facing), facing, stack)
//                && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this)
//                && worldIn.isAirBlock(pos.up())) {
//            worldIn.setBlockState(pos.up(), getPlant(worldIn, pos));
//            if (playerIn instanceof EntityPlayerMP)
//                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) playerIn, pos.up(), stack);
//            stack.shrink(1);
//            return EnumActionResult.SUCCESS;
//        } else {
//            return EnumActionResult.FAIL;
//        }
//    }
}
