package net.silentchaos512.gems.util;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.GemOreBlock;
import net.silentchaos512.gems.item.GemBlockItem;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.setup.Registration;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.utils.Color;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("NonFinalFieldInEnum")
public enum Gems {
    RUBY(0xE61D1D),
    CARNELIAN(0xE4331D),
    TOPAZ(0xE6711D),
    CITRINE(0xC78B03),
    HELIODOR(0xE6C51D),
    MOLDAVITE(0xA6D923),
    PERIDOT(0x29DB18),
    TURQUOISE(0x3DF4BD),
    KYANITE(0x41C4F3),
    SAPPHIRE(0x1D1DE6),
    IOLITE(0x7543F5),
    ALEXANDRITE(0xBE36E8),
    AMMOLITE(0xDB2BFF),
    ROSE_QUARTZ(0xFF4EAB),
    BLACK_DIAMOND(0x5F524C),
    WHITE_DIAMOND(0xD1C1CE);

    private final Color color;

    // Blocks
    BlockRegistryObject<GemOreBlock> ore;
    BlockRegistryObject<GemOreBlock> netherOre;
    BlockRegistryObject<GemOreBlock> endOre;
//    BlockRegistryObject<GemBlock> block;
//    BlockRegistryObject<GlowroseBlock> glowrose;
    BlockRegistryObject<FlowerPotBlock> pottedGlowrose;

    // Items
    ItemRegistryObject<GemItem> item;
    ItemRegistryObject<GemItem> shard;

    // Tags
//    final ITag.INamedTag<Block> blockTag;
//    final ITag.INamedTag<Block> glowroseTag;
    final ITag.INamedTag<Block> oreTag;
//    final ITag.INamedTag<Item> blockItemTag;
//    final ITag.INamedTag<Item> glowroseItemTag;
    final ITag.INamedTag<Item> oreItemTag;
    final ITag.INamedTag<Item> itemTag;
//    final ITag.INamedTag<Item> shardTag;

    Gems(int colorIn) {
        this.color = new Color(colorIn);

//        this.blockTag = BlockTags.makeWrapperTag(new ResourceLocation("forge", "storage_blocks/" + this.getName()).toString());
//        this.glowroseTag = BlockTags.makeWrapperTag(GemsBase.getId("glowroses/" + this.getName()).toString());
        this.oreTag = BlockTags.makeWrapperTag(new ResourceLocation("forge", "ores/" + this.getName()).toString());
//        this.blockItemTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "storage_blocks/" + this.getName()).toString());
//        this.glowroseItemTag = ItemTags.makeWrapperTag(GemsBase.getId("glowroses/" + this.getName()).toString());
        this.oreItemTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "ores/" + this.getName()).toString());
        this.itemTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "gems/" + this.getName()).toString());
//        this.shardTag = ItemTags.makeWrapperTag(new ResourceLocation("forge", "nuggets/" + this.getName()).toString());
    }

    public static void registerBlocks() {
        for (Gems gem : values())
            gem.ore = registerBlock(gem.getName() + "_ore", () ->
                    new GemOreBlock(gem, 2, AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(3)
                            .harvestTool(ToolType.PICKAXE)
                            .setRequiresTool()
                            .sound(SoundType.STONE)));
        for (Gems gem : values())
            gem.netherOre = registerBlock(gem.getName() + "_nether_ore", () ->
                    new GemOreBlock(gem, 3, AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(4)
                            .harvestTool(ToolType.PICKAXE)
                            .setRequiresTool()
                            .sound(SoundType.NETHER_ORE)));
        for (Gems gem : values())
            gem.endOre = registerBlock(gem.getName() + "_end_ore", () ->
                    new GemOreBlock(gem, 4, AbstractBlock.Properties.create(Material.ROCK)
                            .hardnessAndResistance(6)
                            .harvestTool(ToolType.PICKAXE)
                            .setRequiresTool()
                            .sound(SoundType.STONE)));
    }

    public static void registerItems() {
        for (Gems gem : values())
            gem.item = registerItem(gem.getName(), () ->
                    new GemItem(gem, "gem", new Item.Properties().group(GemsBase.ITEM_GROUP)));

//        for (Gems gem : values())
//            gem.shard = registerItem(gem.getName() + "_shard", () ->
//                    new GemItem(gem, "gem_shard", new Item.Properties().group(GemsBase.ITEM_GROUP)));
    }

    private static <T extends Block> BlockRegistryObject<T> registerBlockNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, Gems::defaultBlockItem);
    }

    private static <T extends Block> BlockRegistryObject<T> registerBlock(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<BlockItem>> item) {
        BlockRegistryObject<T> ret = registerBlockNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static <T extends Item> ItemRegistryObject<T> registerItem(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }

    private static Supplier<BlockItem> defaultBlockItem(BlockRegistryObject<?> block) {
        return () -> new GemBlockItem(block.get(), new Item.Properties().group(GemsBase.ITEM_GROUP));
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getColor() {
        return color.getColor();
    }

    public float[] getColorArray() {
        return new float[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gem.silentgems." + this.getName());
    }

    //region Block, Item, and Tag getters

    public GemOreBlock getOre(RegistryKey<World> world) {
        if (world.equals(World.THE_NETHER))
            return netherOre.get();
        if (world.equals(World.THE_END))
            return endOre.get();
        return ore.get();
    }

    public GemItem getItem() {
        return this.item.get();
    }

    @Deprecated
    public GemItem getShard() {
        return this.shard.get();
    }

    public ITag.INamedTag<Block> getOreTag() {
        return oreTag;
    }

    public ITag.INamedTag<Item> getOreItemTag() {
        return oreItemTag;
    }

    public ITag.INamedTag<Item> getItemTag() {
        return itemTag;
    }

    //endregion
}
