package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.lib.advancements.LibTriggers;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.registry.BlockRegistryObject;

import java.util.Locale;

public enum MiscBlocks implements IBlockProvider {
    CHAOS_CRYSTAL(CraftingItems.CHAOS_CRYSTAL,
            builder(Material.IRON)
    ),
    ENRICHED_CHAOS_CRYSTAL(CraftingItems.ENRICHED_CHAOS_CRYSTAL,
            builder(Material.IRON)
    ),
    ENDER_CRYSTAL(CraftingItems.ENDER_CRYSTAL,
            builder(Material.IRON)
    ),
    CHAOS_COAL(CraftingItems.CHAOS_COAL,
            builder(Material.ROCK)
                    .sound(SoundType.STONE)
    ),
    SILVER(CraftingItems.SILVER_INGOT,
            builder(Material.IRON)
    ),
    CHAOS_IRON(CraftingItems.CHAOS_IRON_INGOT,
            builder(Material.IRON)
    ),
    CHAOS_GOLD(CraftingItems.CHAOS_GOLD_INGOT,
            builder(Material.IRON)
    ),
    CHAOS_SILVER(CraftingItems.CHAOS_SILVER_INGOT,
               builder(Material.IRON)
    );

    public static final ResourceLocation WALK_ON_ENDER_CRYSTAL = SilentGems.getId("walk_on_ender_crystal");

    private final Block.Properties builder;
    @SuppressWarnings("NonFinalFieldInEnum")
    private BlockRegistryObject<MiscBlock> block;
    // The item this block is made from
    private final IItemProvider storedItem;

    MiscBlocks(IItemProvider storedItem, Block.Properties builder) {
        this.builder = builder;
        this.storedItem = storedItem;
    }

    private static Block.Properties builder(Material material) {
        return Block.Properties.create(material)
                .hardnessAndResistance(4, 30)
                .sound(SoundType.METAL);
    }

    public static void registerBlocks() {
        for (MiscBlocks block : values()) {
            block.block = new BlockRegistryObject<>(Registration.BLOCKS.register(block.getName(), () ->
                    new MiscBlock(block, block.builder)));
            Registration.ITEMS.register(block.getName(), () ->
                    new MiscBlockItem(block, new Item.Properties().group(GemsItemGroups.BLOCKS)));
        }
    }

    @Override
    public MiscBlock asBlock() {
        return block.get();
    }

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

    static final class MiscBlock extends Block {
        private final MiscBlocks type;

        MiscBlock(MiscBlocks type, Properties properties) {
            super(properties);
            this.type = type;
        }

        @Override
        public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
            if (this.type == ENDER_CRYSTAL) {
                // Levitation effect from ender crystal blocks
                if (entityIn instanceof LivingEntity && worldIn.getBlockState(pos).getBlock() == this) {
                    // Stack up to 5 blocks, each increasing the levitation level
                    final int stackedBlocks = getStackedBlocks(worldIn, pos);
                    EffectInstance effect = new EffectInstance(Effects.LEVITATION, 100, stackedBlocks - 1);
                    LivingEntity entityLiving = (LivingEntity) entityIn;
                    entityLiving.addPotionEffect(effect);

                    // Advancement triggers?
                    if (entityIn instanceof ServerPlayerEntity) {
                        LibTriggers.GENERIC_INT.trigger((ServerPlayerEntity) entityIn, WALK_ON_ENDER_CRYSTAL, stackedBlocks);
                    }
                }
            }
        }

        private int getStackedBlocks(IBlockReader world, BlockPos pos) {
            // Count the number of identical blocks below this one
            int result = 1;
            BlockState state = world.getBlockState(pos.down());
            while (state.getBlock() == this && result < 5) {
                ++result;
                state = world.getBlockState(pos.down(result));
            }
            return result;
        }
    }

    static final class MiscBlockItem extends BlockItem {
        private final MiscBlocks type;

        MiscBlockItem(MiscBlocks type, Properties builder) {
            super(type.block.get(), builder);
            this.type = type;
        }

        @Override
        public int getBurnTime(ItemStack itemStack) {
            return this.type == CHAOS_COAL ? 64000 : 0;
        }
    }
}
