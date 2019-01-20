package net.silentchaos512.gems.lib;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.item.GemItem;
import net.silentchaos512.gems.item.GemShard;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

public enum Gems implements IStringSerializable {
    // Classic Gems
    RUBY(Set.CLASSIC, "Ruby", 0xE61D1D),
    GARNET(Set.CLASSIC, "Garnet", 0xE64F1D),
    TOPAZ(Set.CLASSIC, "Topaz", 0xE6711D),
    AMBER(Set.CLASSIC, "Amber", 0xE6A31D),
    HELIODOR(Set.CLASSIC, "Heliodor", 0xE6C51D),
    PERIDOT(Set.CLASSIC, "Peridot", 0xA3E61D, "gemOlivine"),
    GREEN_SAPPHIRE(Set.CLASSIC, "GreenSapphire", 0x1DE61D),
    PHOSPHOPHYLLITE(Set.CLASSIC, "Phosphophyllite", 0x1DE682),
    AQUAMARINE(Set.CLASSIC, "Aquamarine", 0x1DE6E6),
    SAPPHIRE(Set.CLASSIC, "Sapphire", 0x1D1DE6),
    TANZANITE(Set.CLASSIC, "Tanzanite", 0x601DE6),
    AMETHYST(Set.CLASSIC, "Amethyst", 0xA31DE6),
    AGATE(Set.CLASSIC, "Agate", 0xE61DE6),
    MORGANITE(Set.CLASSIC, "Morganite", 0xFF88FE),
    ONYX(Set.CLASSIC, "Onyx", 0x2F2F2F),
    OPAL(Set.CLASSIC, "Opal", 0xE4E4E4),
    // Dark Gems
    CARNELIAN(Set.DARK, "Carnelian", 0xA30E00),
    SPINEL(Set.DARK, "Spinel", 0xA34400),
    CITRINE(Set.DARK, "Citrine", 0xA35F00),
    JASPER(Set.DARK, "Jasper", 0xA38800),
    ZIRCON(Set.DARK, "Zircon", 0xA3A300),
    MOLDAVITE(Set.DARK, "Moldavite", 0x88A300),
    MALACHITE(Set.DARK, "Malachite", 0x00A336),
    TURQUOISE(Set.DARK, "Turquoise", 0x00A388),
    EUCLASE(Set.DARK, "Euclase", 0x006DA3),
    BENITOITE(Set.DARK, "Benitoite", 0x001BA3),
    IOLITE(Set.DARK, "Iolite", 0x5F00A3, "gemCordierite"),
    ALEXANDRITE(Set.DARK, "Alexandrite", 0x9500A3),
    LEPIDOLITE(Set.DARK, "Lepidolite", 0xA3007A),
    AMETRINE(Set.DARK, "Ametrine", 0xA30052),
    BLACK_DIAMOND(Set.DARK, "BlackDiamond", 0x1E1E1E),
    MOONSTONE(Set.DARK, "Moonstone", 0x898989),
    // Light Gems
    PYROPE(Set.LIGHT, "Pyrope", 0xFF4574),
    CORAL(Set.LIGHT, "Coral", 0xFF5545),
    SUNSTONE(Set.LIGHT, "Sunstone", 0xFF7445),
    CATS_EYE(Set.LIGHT, "CatsEye", 0xFFC145),
    YELLOW_DIAMOND(Set.LIGHT, "YellowDiamond", 0xFFFF45),
    JADE(Set.LIGHT, "Jade", 0xA2FF45),
    CHRYSOPRASE(Set.LIGHT, "Chrysoprase", 0x64FF45),
    APATITE(Set.LIGHT, "Apatite", 0x45FFD1),
    FLUORITE(Set.LIGHT, "Fluorite", 0x45D1FF),
    KYANITE(Set.LIGHT, "Kyanite", 0x4583FF),
    SODALITE(Set.LIGHT, "Sodalite", 0x5445FF),
    AMMOLITE(Set.LIGHT, "Ammolite", 0xE045FF),
    KUNZITE(Set.LIGHT, "Kunzite", 0xFF45E0),
    ROSE_QUARTZ(Set.LIGHT, "RoseQuartz", 0xFF78B6),
    TEKTITE(Set.LIGHT, "Tektite", 0x8F7C6B),
    PEARL(Set.LIGHT, "Pearl", 0xE2E8F1);

    final Set set;
    final String name;
    final int color;
    // Additional ore dictionary keys, which apply only to standard gem items.
    final String[] extraOreKeys;

    // Blocks
    final GemOre ore;
    final GemBlock block;
    final GemBricks bricks;
    final GemGlass glass;
    final GemLamp lampUnlit;
    final GemLamp lampLit;
    final GemLamp lampInvertedLit;
    final GemLamp lampInvertedUnlit;
    final Glowrose glowrose;

    // Items
    final GemItem item;
    final GemShard shard;

    // Tags
    final Tag<Item> itemTag;

    Gems(Set set, String name, int color, String... extraOreKeys) {
        this.set = set;
        this.name = name;
        this.color = color;
        this.extraOreKeys = extraOreKeys; // FIXME

        this.ore = new GemOre(this);
        this.block = new GemBlock(this);
        this.bricks = new GemBricks(this);
        this.glass = new GemGlass(this);
        this.lampUnlit = new GemLamp(this, GemLamp.State.UNLIT);
        this.lampLit = new GemLamp(this, GemLamp.State.LIT);
        this.lampInvertedLit = new GemLamp(this, GemLamp.State.INVERTED_LIT);
        this.lampInvertedUnlit = new GemLamp(this, GemLamp.State.INVERTED_UNLIT);
        glowrose = new Glowrose(this);

        this.item = new GemItem(this);
        this.shard = new GemShard(this);

        this.itemTag = new ItemTags.Wrapper(new ResourceLocation(SilentGems.MOD_ID, this.name));
    }

    /**
     * @return The IStringSerializable name: All lowercase with underscores, good for block states.
     */
    @Override
    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    /**
     * @return A localization-friendly version of the name, capital case with no spaces or
     * underscores.
     */
    @Deprecated // TODO Is this needed?
    public String getGemName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    @Nullable
    public static Gems fromStack(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IGem) {
            return ((IGem) stack.getItem()).getGem();
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
        return block;
    }

    public GemBricks getBricks() {
        return bricks;
    }

    public GemGlass getGlass() {
        return glass;
    }

    public GemLamp getLamp(GemLamp.State state) {
        if (state == GemLamp.State.UNLIT) return lampUnlit;
        if (state == GemLamp.State.LIT) return lampLit;
        if (state == GemLamp.State.INVERTED_LIT) return lampInvertedLit;
        if (state == GemLamp.State.INVERTED_UNLIT) return lampInvertedUnlit;
        throw new IllegalArgumentException("Unknown GemLamp.State: " + state);
    }

    /**
     * @return The gem ore block.
     */
    public GemOre getOre() {
        return ore;
    }

    public Glowrose getGlowrose() {
        return glowrose;
    }

    /**
     * @return The gem item.
     */
    public GemItem getItem() {
        return item;
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.item);
    }

    public Tag<Item> getItemTag() {
        return itemTag;
    }

    /**
     * @return The gem shard (nugget) item.
     */
    public GemShard getShard() {
        return shard;
    }

    public Set getSet() {
        return set;
    }

    public enum Set {
        CLASSIC(0), DARK(16), LIGHT(32); // Overworld, Nether, and the End

        private final int startMeta; // TODO: Should probably do away with this... but works for now
        private final MultiGemOre multiOre;

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
    }
}
