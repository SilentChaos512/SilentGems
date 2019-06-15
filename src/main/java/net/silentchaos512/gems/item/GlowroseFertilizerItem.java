package net.silentchaos512.gems.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.silentchaos512.gems.block.GlowroseBlock;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.utils.Lazy;

public class GlowroseFertilizerItem extends Item {
    private static final int SPREAD = 3;

    public static final Lazy<GlowroseFertilizerItem> INSTANCE = Lazy.of(GlowroseFertilizerItem::new);

    private GlowroseFertilizerItem() {
        super(new Properties().group(ModItemGroups.UTILITY));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos center = context.getPos().offset(Direction.UP);
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (world.isAirBlock(center)) {
            // Always one at position used.
            Gems gem = Gems.Set.forDimension(world.dimension.getType().getId()).selectRandom(random);
            GlowroseBlock blockGlowrose = gem.getGlowrose();
            BlockState stateToPlace = blockGlowrose.getDefaultState();

            if (!blockGlowrose.isValidPosition(stateToPlace, world, center) || !world.isAirBlock(center) || !world.isAirBlock(center.up())) {
                // Fail to use if glow rose can't be placed at center.
                return ActionResultType.FAIL;
            } else if (world.isRemote) {
                // Only make changes on the server side!
                return ActionResultType.SUCCESS;
            }

            world.setBlockState(center, stateToPlace, 2);

            // Random extras
            int extraCount = random.nextInt(3);
            for (int i = 0; i < extraCount; ++i) {
                tryPlaceExtra(world, center);
            }

            if (player != null && !player.abilities.isCreativeMode) {
                context.getItem().shrink(1);
            }

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }

    private static void tryPlaceExtra(World world, BlockPos center) {
        for (int y = center.getY() + 1; y > center.getY() - 2; --y) {
            BlockPos target = new BlockPos(
                    center.getX() + MathHelper.nextInt(random, -SPREAD, SPREAD),
                    y,
                    center.getZ() + MathHelper.nextInt(random, -SPREAD, SPREAD));

            Gems gem = Gems.Set.forDimension(world.dimension.getType().getId()).selectRandom(random);
            GlowroseBlock block = gem.getGlowrose();
            BlockState state = block.getDefaultState();

            if (block.isValidPosition(state, world, target) && world.isAirBlock(center) && world.isAirBlock(center.up())) {
                world.setBlockState(target, state, 2);
                return;
            }
        }
    }
}
