package net.silentchaos512.gems.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.silentchaos512.gems.GemsBase;

import javax.annotation.Nullable;
import java.util.List;

public abstract class OreBlockSG extends DropExperienceBlock {
    @Nullable private final ItemLike droppedItem;
    private final int harvestLevel;

    /**
     * Constructor. If the ore drops itself, {@code droppedItem} should be null.
     *
     * @param droppedItem  The item dropped when mined without silk touch, or null if the block
     *                     should always drop itself.
     * @param harvestLevel The harvest level required
     * @param builder      The block properties
     */
    public OreBlockSG(@Nullable ItemLike droppedItem, int harvestLevel, Properties builder) {
        super(builder);
        this.droppedItem = droppedItem;
        this.harvestLevel = harvestLevel;
    }

    /**
     * Gets the item dropped when mined with a non-silk touch tool
     */
    public ItemLike getDroppedItem() {
        return this.droppedItem != null ? this.droppedItem : this;
    }

    /**
     * Get a random amount of XP to drop, assuming XP will be dropped.
     * @param random
     */
    public abstract int getExpRandom(RandomSource random);

    @Override
    public int getExpDrop(BlockState state, LevelReader level, RandomSource randomSource, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        return silkTouchLevel == 0 ? getExpRandom(randomSource) : 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Component itemName = this.getDroppedItem().asItem().getDescription();
        tooltip.add(Component.translatable("misc.silentgems.dropFromOre", itemName)
                .withStyle(ChatFormatting.GRAY));
        // Harvest level tips
        Component harvestLevelName = GemsBase.TEXT.misc("harvestLevel." + this.harvestLevel);
        tooltip.add(GemsBase.TEXT.misc("harvestLevel", this.harvestLevel, harvestLevelName));
    }
}
