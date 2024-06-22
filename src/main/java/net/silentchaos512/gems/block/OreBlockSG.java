package net.silentchaos512.gems.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.silentchaos512.gems.GemsBase;

import javax.annotation.Nullable;
import java.util.List;

public class OreBlockSG extends DropExperienceBlock {
    @Nullable
    private final ItemLike droppedItemForTooltip;
    private final int harvestLevelForTooltip; // TODO: Change to harvest tier

    /**
     * Constructor. If the ore drops itself, {@code droppedItem} should be null.
     *
     * @param droppedItemForTooltip  The item dropped when mined without silk touch, or null if the block
     *                     should always drop itself.
     * @param harvestLevelForTooltip The harvest level required
     * @param xpDrop       The XP dropped by the ore when mined
     * @param builder      The block properties
     */
    public OreBlockSG(@Nullable ItemLike droppedItemForTooltip, int harvestLevelForTooltip, IntProvider xpDrop, Properties builder) {
        super(xpDrop, builder);
        this.droppedItemForTooltip = droppedItemForTooltip;
        this.harvestLevelForTooltip = harvestLevelForTooltip;
    }

    /**
     * Gets the item dropped when mined with a non-silk touch tool
     */
    public ItemLike getDroppedItemForTooltip() {
        return this.droppedItemForTooltip != null ? this.droppedItemForTooltip : this;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Component itemName = this.getDroppedItemForTooltip().asItem().getDescription();
        tooltip.add(Component.translatable("misc.silentgems.dropFromOre", itemName)
                .withStyle(ChatFormatting.GRAY));
        // Harvest level tips
        Component harvestLevelName = GemsBase.TEXT.misc("harvestLevel." + this.harvestLevelForTooltip);
        tooltip.add(GemsBase.TEXT.misc("harvestLevel", this.harvestLevelForTooltip, harvestLevelName));
    }
}
