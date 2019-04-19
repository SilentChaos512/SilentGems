package net.silentchaos512.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.init.ModItemGroups;
import net.silentchaos512.gems.item.CraftingItems;
import net.silentchaos512.lib.advancements.LibTriggers;
import net.silentchaos512.utils.Lazy;

import java.util.Locale;

public enum MiscBlocks implements IItemProvider, IStringSerializable {
    CHAOS_CRYSTAL(
            CraftingItems.CHAOS_CRYSTAL,
            builder(Material.IRON)
    ),
    ENRICHED_CHAOS_CRYSTAL(
            CraftingItems.ENRICHED_CHAOS_CRYSTAL,
            builder(Material.IRON)
    ),
    ENDER_CRYSTAL(
            CraftingItems.ENDER_CRYSTAL,
            builder(Material.IRON)
    ),
    CHAOS_COAL(
            CraftingItems.CHAOS_COAL,
            builder(Material.ROCK)
                    .sound(SoundType.STONE)
    ),
    SILVER(
            CraftingItems.SILVER_INGOT,
            builder(Material.IRON)
    ),
    CHAOS_IRON(
            CraftingItems.CHAOS_IRON,
            builder(Material.IRON)
    );

    private final Lazy<MiscBlock> block;
    // The item this block is made from
    private final IItemProvider storedItem;

    MiscBlocks(IItemProvider storedItem, Block.Properties builder) {
        block = Lazy.of(() -> new MiscBlock(this, builder));
        this.storedItem = storedItem;
    }

    private static Block.Properties builder(Material material) {
        return Block.Properties.create(material)
                .hardnessAndResistance(4, 30)
                .sound(SoundType.METAL);
    }

    public MiscBlock getBlock() {
        return block.get();
    }

    public MiscBlockItem getBlockItem() {
        return new MiscBlockItem(this, new Item.Properties().group(ModItemGroups.BLOCKS));
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
                if (entityIn instanceof EntityLivingBase && worldIn.getBlockState(pos).getBlock() == this) {
                    // Stack up to 5 blocks, each increasing the levitation level
                    final int stackedBlocks = getStackedBlocks(worldIn, pos);
                    PotionEffect effect = new PotionEffect(MobEffects.LEVITATION, 100, stackedBlocks - 1);
                    EntityLivingBase entityLiving = (EntityLivingBase) entityIn;
                    entityLiving.addPotionEffect(effect);

                    // Advancement triggers?
                    if (entityIn instanceof EntityPlayerMP) {
                        ResourceLocation triggerType = new ResourceLocation(SilentGems.MOD_ID, "walk_on_" + this.type.getName());
                        LibTriggers.GENERIC_INT.trigger((EntityPlayerMP) entityIn, triggerType, stackedBlocks);
                    }
                }
            }
        }

        private int getStackedBlocks(IBlockReader world, BlockPos pos) {
            // Count the number of identical blocks below this one
            int result = 1;
            IBlockState state = world.getBlockState(pos.down());
            while (state.getBlock() == this && result < 5) {
                ++result;
                state = world.getBlockState(pos.down(result));
            }
            return result;
        }
    }

    static final class MiscBlockItem extends ItemBlock {
        private final MiscBlocks type;

        MiscBlockItem(MiscBlocks type, Properties builder) {
            super(type.getBlock(), builder);
            this.type = type;
        }

        @Override
        public int getBurnTime(ItemStack itemStack) {
            return this.type == CHAOS_COAL ? 64000 : 0;
        }
    }
}
