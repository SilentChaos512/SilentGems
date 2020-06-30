package net.silentchaos512.gems.lib;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.teleporter.GemTeleporterBlock;
import net.silentchaos512.gems.block.teleporter.RedstoneGemTeleporterBlock;
import net.silentchaos512.gems.init.GemsItemGroups;
import net.silentchaos512.gems.init.Registration;
import net.silentchaos512.gems.item.*;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import net.silentchaos512.utils.Color;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("NonFinalFieldInEnum")
public enum Gems implements IStringSerializable {
    // Classic Gems
    RUBY(Set.CLASSIC, 0xE61D1D),
    GARNET(Set.CLASSIC, 0xE64F1D),
    TOPAZ(Set.CLASSIC, 0xE6711D),
    AMBER(Set.CLASSIC, 0xE6A31D),
    HELIODOR(Set.CLASSIC, 0xE6C51D),
    PERIDOT(Set.CLASSIC, 0xA3E61D),
    GREEN_SAPPHIRE(Set.CLASSIC, 0x1DE61D),
    PHOSPHOPHYLLITE(Set.CLASSIC, 0x1DE682),
    AQUAMARINE(Set.CLASSIC, 0x1DE6E6),
    SAPPHIRE(Set.CLASSIC, 0x1D1DE6),
    TANZANITE(Set.CLASSIC, 0x601DE6),
    AMETHYST(Set.CLASSIC, 0xA31DE6),
    AGATE(Set.CLASSIC, 0xE61DE6),
    MORGANITE(Set.CLASSIC, 0xFF88FE),
    ONYX(Set.CLASSIC, 0x2F2F2F),
    OPAL(Set.CLASSIC, 0xE4E4E4),
    // Dark Gems
    CARNELIAN(Set.DARK, 0xA30E00),
    SPINEL(Set.DARK, 0xA34400),
    CITRINE(Set.DARK, 0xA35F00),
    JASPER(Set.DARK, 0xA38800),
    ZIRCON(Set.DARK, 0xA3A300),
    MOLDAVITE(Set.DARK, 0x88A300),
    MALACHITE(Set.DARK, 0x00A336),
    TURQUOISE(Set.DARK, 0x00A388),
    EUCLASE(Set.DARK, 0x006DA3),
    BENITOITE(Set.DARK, 0x001BA3),
    IOLITE(Set.DARK, 0x5F00A3),
    ALEXANDRITE(Set.DARK, 0x9500A3),
    LEPIDOLITE(Set.DARK, 0xA3007A),
    AMETRINE(Set.DARK, 0xA30052),
    BLACK_DIAMOND(Set.DARK, 0x1E1E1E),
    MOONSTONE(Set.DARK, 0x898989),
    // Light Gems
    PYROPE(Set.LIGHT, 0xFF4574),
    CORAL(Set.LIGHT, 0xFF5545),
    SUNSTONE(Set.LIGHT, 0xFF7445),
    CATS_EYE(Set.LIGHT, 0xFFC145),
    YELLOW_DIAMOND(Set.LIGHT, 0xFFFF45),
    JADE(Set.LIGHT, 0xA2FF45),
    CHRYSOPRASE(Set.LIGHT, 0x64FF45),
    APATITE(Set.LIGHT, 0x45FFD1),
    FLUORITE(Set.LIGHT, 0x45D1FF),
    KYANITE(Set.LIGHT, 0x4583FF),
    SODALITE(Set.LIGHT, 0x5445FF),
    AMMOLITE(Set.LIGHT, 0xE045FF),
    KUNZITE(Set.LIGHT, 0xFF45E0),
    ROSE_QUARTZ(Set.LIGHT, 0xFF78B6),
    TEKTITE(Set.LIGHT, 0x8F7C6B),
    PEARL(Set.LIGHT, 0xE2E8F1);

    final Set set;
    final Color color;

    // Blocks
    BlockRegistryObject<GemOreBlock> ore;
    BlockRegistryObject<GemBlock> block;
    BlockRegistryObject<GemBricksBlock> bricks;
    BlockRegistryObject<GemGlassBlock> glass;
    BlockRegistryObject<GemLampBlock> lampUnlit;
    BlockRegistryObject<GemLampBlock> lampLit;
    BlockRegistryObject<GemLampBlock> lampInvertedLit;
    BlockRegistryObject<GemLampBlock> lampInvertedUnlit;
    BlockRegistryObject<GemTeleporterBlock> teleporter;
    BlockRegistryObject<RedstoneGemTeleporterBlock> redstoneTeleporter;
    BlockRegistryObject<GlowroseBlock> glowrose;
    BlockRegistryObject<FlowerPotBlock> pottedGlowrose;

    // Items
    ItemRegistryObject<GemItem> item;
    ItemRegistryObject<GemShardItem> shard;
    ItemRegistryObject<ReturnHomeCharmItem> returnHomeCharm;
    ItemRegistryObject<ChaosGemItem> chaosGem;

    // Tags
    final Tag<Block> blockTag;
    final Tag<Block> glowroseTag;
    final Tag<Block> oreTag;
    final Tag<Item> itemTag;
    final Tag<Item> shardTag;

    Gems(Set set, int color) {
        this.set = set;
        this.set.gems.add(this);
        this.color = new Color(color);

        this.blockTag = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/" + this.getName()));
        this.glowroseTag = new BlockTags.Wrapper(new ResourceLocation(SilentGems.MOD_ID, "glowroses/" + this.getName()));
        this.oreTag = new BlockTags.Wrapper(new ResourceLocation("forge", "ores/" + this.getName()));
        this.itemTag = new ItemTags.Wrapper(new ResourceLocation("forge", "gems/" + this.getName()));
        this.shardTag = new ItemTags.Wrapper(new ResourceLocation("forge", "nuggets/" + this.getName()));
    }

    public static void registerBlocks() {
        for (Gems gem : values())
            gem.ore = registerBlock(gem.getName() + "_ore", () -> new GemOreBlock(gem));
        for (Gems gem : values())
            gem.block = registerBlock(gem.getName() + "_block", () -> new GemBlock(gem));
        for (Gems gem : values())
            gem.bricks = registerBlock(gem.getName() + "_bricks", () -> new GemBricksBlock(gem));
        for (Gems gem : values())
            gem.glass = registerBlock(gem.getName() + "_glass", () -> new GemGlassBlock(gem));
        for (Gems gem : values())
            gem.lampUnlit = registerBlock(gem.getName() + "_lamp", () -> new GemLampBlock(gem, GemLampBlock.State.UNLIT));
        for (Gems gem : values())
            gem.lampLit = registerBlockNoItem(gem.getName() + "_lamp_lit", () -> new GemLampBlock(gem, GemLampBlock.State.LIT));
        for (Gems gem : values())
            gem.lampInvertedLit = registerBlock(gem.getName() + "_lamp_inverted_lit", () -> new GemLampBlock(gem, GemLampBlock.State.INVERTED_LIT));
        for (Gems gem : values())
            gem.lampInvertedUnlit = registerBlockNoItem(gem.getName() + "_lamp_inverted", () -> new GemLampBlock(gem, GemLampBlock.State.INVERTED_UNLIT));
        for (Gems gem : values())
            gem.glowrose = registerBlock(gem.getName() + "_glowrose", () -> new GlowroseBlock(gem));
        for (Gems gem : values()) {
            gem.pottedGlowrose = registerBlockNoItem("potted_" + gem.getName() + "_glowrose", () -> new PottedGlowroseBlock(gem, gem.glowrose));
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(SilentGems.getId(gem.getName() + "_glowrose"), gem.pottedGlowrose);
        }
        for (Gems gem : values())
            gem.teleporter = registerBlock(gem.getName() + "_teleporter", () -> new GemTeleporterBlock(gem));
        for (Gems gem : values())
            gem.redstoneTeleporter = registerBlock(gem.getName() + "_redstone_teleporter", () -> new RedstoneGemTeleporterBlock(gem));
    }

    public static void registerItems() {
        for (Gems gem : values())
            gem.item = registerItem(gem.getName(), () -> new GemItem(gem));
        for (Gems gem : values())
            gem.shard = registerItem(gem.getName() + "_shard", () -> new GemShardItem(gem));
        for (Gems gem : values())
            gem.returnHomeCharm = registerItem(gem.getName() + "_return_home_charm", () -> new ReturnHomeCharmItem(gem));
        for (Gems gem : values())
            gem.chaosGem = registerItem("chaos_" + gem.getName(), () -> new ChaosGemItem(gem));
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
        return () -> new GemBlockItem(block.get(), new Item.Properties().group(GemsItemGroups.BLOCKS));
    }

    /**
     * @return The IStringSerializable name: All lowercase with underscores, good for block states.
     */
    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getColor() {
        return color.getColor();
    }

    public Color getColorObj() {
        return color;
    }

    public float[] getColorArray() {
        return new float[]{color.getRed(), color.getGreen(), color.getBlue()};
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gem.silentgems." + this.getName());
    }

    /**
     * Gets the gem associated with the item. Also checks item tags. See {@link #from(ItemStack,
     * boolean)}.
     *
     * @param stack The item
     * @return The gem, or null if it cannot be determined
     */
    @Nullable
    public static Gems from(ItemStack stack) {
        return from(stack, true);
    }

    /**
     * Gets the gem associated with the item.
     *
     * @param stack    The item
     * @param matchTag Should we also match item tags?
     * @return The gem, or null if it cannot be determined
     */
    @Nullable
    public static Gems from(ItemStack stack, boolean matchTag) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();

            // Gems and shards, maybe others in the future?
            if (item instanceof IGem) {
                return ((IGem) item).getGem();
            }

            // Match item tag?
            if (matchTag) {
                for (Gems gem : values()) {
                    if (gem.itemTag.contains(item)) {
                        return gem;
                    }
                }
            }
        }

        return null;
    }

    @Nullable
    public static Gems fromName(String name) {
        for (Gems gem : values()) {
            if (name.equalsIgnoreCase(gem.name())) {
                return gem;
            }
        }
        return null;
    }

    public static Gems selectRandom() {
        return selectRandom(SilentGems.random);
    }

    public static Gems selectRandom(Random random) {
        return values()[random.nextInt(values().length)];
    }

    // ======================
    // Block and Item getters
    // ======================

    /**
     * @return The gem block.
     */
    public GemBlock getBlock() {
        return block.get();
    }

    public GemBricksBlock getBricks() {
        return bricks.get();
    }

    public GemGlassBlock getGlass() {
        return glass.get();
    }

    public GemLampBlock getLamp(GemLampBlock.State state) {
        if (state == GemLampBlock.State.UNLIT) return lampUnlit.get();
        if (state == GemLampBlock.State.LIT) return lampLit.get();
        if (state == GemLampBlock.State.INVERTED_LIT) return lampInvertedLit.get();
        if (state == GemLampBlock.State.INVERTED_UNLIT) return lampInvertedUnlit.get();
        throw new IllegalArgumentException("Unknown GemLampBlock.State: " + state);
    }

    /**
     * @return The gem ore block.
     */
    public GemOreBlock getOre() {
        return ore.get();
    }

    public GlowroseBlock getGlowrose() {
        return glowrose.get();
    }

    public FlowerPotBlock getPottedGlowrose() {
        return pottedGlowrose.get();
    }

    public GemTeleporterBlock getTeleporter() {
        return teleporter.get();
    }

    public RedstoneGemTeleporterBlock getRedstoneTeleporter() {
        return redstoneTeleporter.get();
    }

    /**
     * @return The gem item.
     */
    public GemItem getItem() {
        return item.get();
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.item.get());
    }

    public Tag<Block> getBlockTag() {
        return blockTag;
    }

    public Tag<Block> getGlowroseTag() {
        return glowroseTag;
    }

    public Tag<Block> getOreTag() {
        return oreTag;
    }

    public Tag<Item> getItemTag() {
        return itemTag;
    }

    public Tag<Item> getShardTag() {
        return shardTag;
    }

    /**
     * @return The gem shard (nugget) item.
     */
    public GemShardItem getShard() {
        return shard.get();
    }

    public ReturnHomeCharmItem getReturnHomeCharm() {
        return returnHomeCharm.get();
    }

    public ChaosGemItem getChaosGem() {
        return chaosGem.get();
    }

    public Set getSet() {
        return set;
    }

    public enum Set implements Iterable<Gems> {
        CLASSIC(0, HardenedRock.STONE), // Overworld
        DARK(16, HardenedRock.NETHERRACK), // Nether
        LIGHT(32, HardenedRock.END_STONE); // The End

        private final int startMeta; // TODO: Should probably do away with this... but works for now
        private final IBlockProvider geodeShell;
        private final Collection<Gems> gems = new ArrayList<>();

        Set(int startMeta, IBlockProvider geodeShell) {
            this.startMeta = startMeta;
            this.geodeShell = geodeShell;
        }

        public static Set forDimension(int dimension) {
            if (dimension == -1) return DARK;
            if (dimension == 1) return LIGHT;
            return CLASSIC;
        }

        public static Set deserialize(Dynamic<?> dynamic) {
            String setName = dynamic.get("gem_set").asString("classic");
            return "light".equals(setName) ? Gems.Set.LIGHT : "dark".equals(setName) ? Gems.Set.DARK : Gems.Set.CLASSIC;
        }

        public IBlockProvider getGeodeShell() {
            return geodeShell;
        }

        public Gems selectRandom(Random random) {
            int id = random.nextInt(16) + startMeta;
            return Gems.values()[id];
        }

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public ITextComponent getDisplayName() {
            TranslationTextComponent textSet = new TranslationTextComponent("gem.silentgems.set." + getName());
            return new TranslationTextComponent("gem.silentgems.set", textSet).applyTextStyle(TextFormatting.ITALIC);
        }

        @Override
        public Iterator<Gems> iterator() {
            return gems.iterator();
        }
    }
}
