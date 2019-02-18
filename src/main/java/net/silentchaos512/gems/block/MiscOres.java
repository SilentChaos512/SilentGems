package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.math.MathHelper;
import net.silentchaos512.gems.item.CraftingItems;

import java.util.Locale;

public enum MiscOres implements IItemProvider, IStringSerializable {
    CHAOS(CraftingItems.CHAOS_CRYSTAL, 3),
    ENDER(CraftingItems.ENDER_CRYSTAL, 4);

    private final LazyLoadBase<MiscOreBlock> block;

    MiscOres(IItemProvider droppedItem, int harvestLevel) {
        block = new LazyLoadBase<>(() -> new MiscOreBlock(droppedItem, harvestLevel,
                Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(4, 20)));
    }

    public MiscOreBlock getBlock() {
        return block.getValue();
    }

    @Override
    public Item asItem() {
        return getBlock().asItem();
    }

    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT) + "_ore";
    }

    public static class MiscOreBlock extends OreBlockSG {
        MiscOreBlock(IItemProvider droppedItem, int harvestLevel, Properties builder) {
            super(droppedItem, harvestLevel, builder);
        }

        @Override
        public int getExpRandom() {
            return MathHelper.nextInt(RANDOM, 2, 7);
        }
    }
}
