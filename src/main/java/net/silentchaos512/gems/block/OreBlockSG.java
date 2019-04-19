package net.silentchaos512.gems.block;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public abstract class OreBlockSG extends BlockOre {
    @Nullable private final IItemProvider droppedItem;
    private final int harvestLevel;

    /**
     * Constructor. If the ore drops itself, {@code droppedItem} should be null.
     *
     * @param droppedItem  The item dropped when mined without silk touch, or null if the block
     *                     should always drop itself.
     * @param harvestLevel The harvest level required
     * @param builder      The block properties
     */
    public OreBlockSG(@Nullable IItemProvider droppedItem, int harvestLevel, Properties builder) {
        super(builder);
        this.droppedItem = droppedItem;
        this.harvestLevel = harvestLevel;
    }

    /**
     * Gets the item dropped when mined with a non-silk touch tool
     */
    public IItemProvider getDroppedItem() {
        return this.droppedItem != null ? this.droppedItem : this;
    }

    /**
     * Get a random amount of XP to drop, assuming XP will be dropped.
     */
    public abstract int getExpRandom();

    @Override
    public int getExpDrop(IBlockState state, IWorldReader reader, BlockPos pos, int fortune) {
        World world = reader instanceof World ? (World) reader : null;
        if (world == null || this.getItemDropped(state, world, pos, fortune) != this) {
            return getExpRandom();
        }
        return 0;
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return this.harvestLevel;
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return this.getDroppedItem();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        ITextComponent itemName = this.getDroppedItem().asItem().getName();
        tooltip.add(new TextComponentTranslation("misc.silentgems.dropFromOre", itemName)
                .applyTextStyle(TextFormatting.GRAY));
    }
}
