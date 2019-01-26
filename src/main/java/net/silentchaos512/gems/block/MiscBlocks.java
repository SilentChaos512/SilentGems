package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyLoadBase;
import net.silentchaos512.gems.item.CraftingItems;

import java.util.Locale;

public enum MiscBlocks implements IItemProvider, IStringSerializable {
    CHAOS_CRYSTAL(
            CraftingItems.CHAOS_CRYSTAL,
            builder(Material.IRON)),
    ENRICHED_CHAOS_CRYSTAL(
            CraftingItems.ENRICHED_CHAOS_CRYSTAL,
            builder(Material.IRON)),
    ENDER_CRYSTAL(
            CraftingItems.ENDER_CRYSTAL,
            builder(Material.IRON)),
    CHAOS_COAL(
            CraftingItems.CHAOS_COAL,
            builder(Material.ROCK)
                    .sound(SoundType.STONE)),
    CHAOS_IRON(
            CraftingItems.CHAOS_IRON,
            builder(Material.IRON));

    private final LazyLoadBase<Block> block;
    // The item this block is made from
    private final IItemProvider storedItem;

    MiscBlocks(IItemProvider storedItem, Block.Builder builder) {
        block = new LazyLoadBase<>(() -> new Block(builder));
        this.storedItem = storedItem;
    }

    private static Block.Builder builder(Material material) {
        return Block.Builder.create(material)
                .hardnessAndResistance(4, 30);
    }

    public Block getBlock() {
        return block.getValue();
    }

    @Override
    public Item asItem() {
        return getBlock().asItem();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT) + "_block";
    }

    /**
     * Gets the item used to craft this block, not the item of the block itself.
     *
     * @return The item this block is made from
     */
    public IItemProvider getStoredItem() {
        return storedItem;
    }
}
