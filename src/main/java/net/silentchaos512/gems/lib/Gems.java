package net.silentchaos512.gems.lib;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.block.teleporter.GemTeleporter;
import net.silentchaos512.gems.block.teleporter.GemTeleporterRedstone;
import net.silentchaos512.gems.item.ChaosGem;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.item.GemShard;
import net.silentchaos512.gems.item.ReturnHomeCharm;
import net.silentchaos512.utils.Lazy;

import javax.annotation.Nullable;
import java.util.*;

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
    final int color;

    // Blocks
    final Lazy<GemOre> ore;
    final Lazy<GemBlock> block;
    final Lazy<GemBricks> bricks;
    final Lazy<GemGlass> glass;
    final Lazy<GemLamp> lampUnlit;
    final Lazy<GemLamp> lampLit;
    final Lazy<GemLamp> lampInvertedLit;
    final Lazy<GemLamp> lampInvertedUnlit;
    final Lazy<GemTeleporter> teleporter;
    final Lazy<GemTeleporterRedstone> redstoneTeleporter;
    final Lazy<Glowrose> glowrose;
    final Lazy<BlockFlowerPot> pottedGlowrose;

    // Items
    final Lazy<GemItem> item;
    final Lazy<GemShard> shard;
    final Lazy<ReturnHomeCharm> returnHomeCharm;
    final Lazy<ChaosGem> chaosGem;

    // Tags
    final Tag<Block> blockTag;
    final Tag<Block> glowroseTag;
    final Tag<Item> itemTag;
    final Tag<Item> shardTag;

    Gems(Set set, int color) {
        this.set = set;
        this.set.gems.add(this);
        this.color = color;

        // Blocks
        this.ore = Lazy.of(() -> new GemOre(this));
        this.block = Lazy.of(() -> new GemBlock(this));
        this.bricks = Lazy.of(() -> new GemBricks(this));
        this.glass = Lazy.of(() -> new GemGlass(this));
        this.lampUnlit = Lazy.of(() -> new GemLamp(this, GemLamp.State.UNLIT));
        this.lampLit = Lazy.of(() -> new GemLamp(this, GemLamp.State.LIT));
        this.lampInvertedLit = Lazy.of(() -> new GemLamp(this, GemLamp.State.INVERTED_LIT));
        this.lampInvertedUnlit = Lazy.of(() -> new GemLamp(this, GemLamp.State.INVERTED_UNLIT));
        this.teleporter = Lazy.of(() -> new GemTeleporter(this, false));
        this.redstoneTeleporter = Lazy.of(() -> new GemTeleporterRedstone(this, false));
        this.glowrose = Lazy.of(() -> new Glowrose(this));
        this.pottedGlowrose = Lazy.of(() -> new PottedGlowrose(this.glowrose.get()));

        // Items
        this.item = Lazy.of(() -> new GemItem(this));
        this.shard = Lazy.of(() -> new GemShard(this));
        this.returnHomeCharm = Lazy.of(() -> new ReturnHomeCharm(this));
        this.chaosGem = Lazy.of(() -> new ChaosGem(this));

        // Tags
        this.blockTag = new BlockTags.Wrapper(new ResourceLocation("forge", "storage_blocks/" + this.getName()));
        this.glowroseTag = new BlockTags.Wrapper(new ResourceLocation(SilentGems.MOD_ID, "glowroses/" + this.getName()));
        this.itemTag = new ItemTags.Wrapper(new ResourceLocation("forge", "gems/" + this.getName()));
        this.shardTag = new ItemTags.Wrapper(new ResourceLocation("forge", "nuggets/" + this.getName()));
    }

    /**
     * @return The IStringSerializable name: All lowercase with underscores, good for block states.
     */
    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int getColor() {
        return color;
    }

    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("gem.silentgems." + this.getName());
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

    public GemBricks getBricks() {
        return bricks.get();
    }

    public GemGlass getGlass() {
        return glass.get();
    }

    public GemLamp getLamp(GemLamp.State state) {
        if (state == GemLamp.State.UNLIT) return lampUnlit.get();
        if (state == GemLamp.State.LIT) return lampLit.get();
        if (state == GemLamp.State.INVERTED_LIT) return lampInvertedLit.get();
        if (state == GemLamp.State.INVERTED_UNLIT) return lampInvertedUnlit.get();
        throw new IllegalArgumentException("Unknown GemLamp.State: " + state);
    }

    /**
     * @return The gem ore block.
     */
    public GemOre getOre() {
        return ore.get();
    }

    public Glowrose getGlowrose() {
        return glowrose.get();
    }

    public BlockFlowerPot getPottedGlowrose() {
        return pottedGlowrose.get();
    }

    public GemTeleporter getTeleporter() {
        return teleporter.get();
    }

    public GemTeleporterRedstone getRedstoneTeleporter() {
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

    public Tag<Item> getItemTag() {
        return itemTag;
    }

    public Tag<Item> getShardTag() {
        return shardTag;
    }

    /**
     * @return The gem shard (nugget) item.
     */
    public GemShard getShard() {
        return shard.get();
    }

    public ReturnHomeCharm getReturnHomeCharm() {
        return returnHomeCharm.get();
    }

    public ChaosGem getChaosGem() {
        return chaosGem.get();
    }

    public Set getSet() {
        return set;
    }

    public enum Set implements Iterable<Gems> {
        CLASSIC(0), DARK(16), LIGHT(32); // Overworld, Nether, and the End

        private final int startMeta; // TODO: Should probably do away with this... but works for now
        private final MultiGemOre multiOre;
        private final Collection<Gems> gems = new ArrayList<>();

        Set(int startMeta) {
            this.startMeta = startMeta;
            multiOre = new MultiGemOre(this);
        }

        public static Set forDimension(int dimension) {
            if (dimension == -1) return DARK;
            if (dimension == 1) return LIGHT;
            return CLASSIC;
        }

        public MultiGemOre getMultiOre() {
            return multiOre;
        }

        public Gems selectRandom(Random random) {
            int id = random.nextInt(16) + startMeta;
            return Gems.values()[id];
        }

        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public ITextComponent getDisplayName() {
            TextComponentTranslation textSet = new TextComponentTranslation("gem.silentgems.set." + getName());
            return new TextComponentTranslation("gem.silentgems.set", textSet).applyTextStyle(TextFormatting.ITALIC);
        }

        @Override
        public Iterator<Gems> iterator() {
            return gems.iterator();
        }
    }
}
