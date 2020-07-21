package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.GemsTags;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.utils.MathUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.function.Predicate;

public enum CorruptedBlocks implements IBlockProvider {
    STONE(Blocks.STONE, GemsTags.Blocks.CORRUPTABLE_STONE),
    DIRT(Blocks.DIRT, GemsTags.Blocks.CORRUPTABLE_DIRT);

    @SuppressWarnings("NonFinalFieldInEnum")
    private BlockRegistryObject<Block> block;
    @SuppressWarnings("NonFinalFieldInEnum")
    private ItemRegistryObject<Item> pile;
    private final Block purifyBlock;
    private final Predicate<Block> canReplace;

    CorruptedBlocks(Block purifyBlock, ITag<Block> replaces) {
        this.purifyBlock = purifyBlock;
        this.canReplace = block -> block.isIn(replaces);
    }

    public static void registerBlocks() {
        for (CorruptedBlocks type : values()) {
            type.block = new BlockRegistryObject<>(Registration.BLOCKS.register(type.getName(), () ->
                    new CorruptedBlock(type)));
            Registration.ITEMS.register(type.getName(), GemsBlocks.defaultItem(type.block));
        }
    }

    public static void registerItems() {
        for (CorruptedBlocks type : values()) {
            type.pile = new ItemRegistryObject<>(Registration.ITEMS.register(type.getName() + "_pile", () ->
                    new Item(new Item.Properties().group(GemsItemGroups.MATERIALS))));
        }
    }

    @Override
    public Block asBlock() {
        return block.get();
    }

    @Override
    public Item asItem() {
        return asBlock().asItem();
    }

    public Item getPile() {
        return pile.get();
    }

    public Block getPurifyBlock() {
        return purifyBlock;
    }

    public String getName() {
        return "corrupted_" + name().toLowerCase(Locale.ROOT);
    }

    public boolean canReplace(Block block) {
        return canReplace.test(block);
    }

    private static class CorruptedBlock extends Block {
        private final CorruptedBlocks type;

        CorruptedBlock(CorruptedBlocks type) {
            super(Block.Properties.create(Material.CLAY)
                    .hardnessAndResistance(1)
                    .sound(SoundType.GROUND)
                    .setLightLevel(state -> 7)
                    .tickRandomly()
            );
            this.type = type;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
            // Small chance of turning back
            if (MathUtils.tryPercentage(rand, 0.01)) {
                worldIn.setBlockState(pos, type.purifyBlock.getDefaultState(), 3);
            }
        }

        @Override
        public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
            tooltip.add(new TranslationTextComponent("misc.silentgems.dropFromOre", type.pile.get().getName()));
        }
    }
}
