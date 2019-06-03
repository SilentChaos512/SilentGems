package net.silentchaos512.gems.config;

import com.google.common.base.Predicate;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.SilentGems;

import java.util.*;

@Getter(AccessLevel.PUBLIC)
public class ConfigOptionOreGen extends ConfigOption {
    private static final float VEIN_COUNT_MIN = 0.0f;
    private static final float VEIN_COUNT_MAX = 1000.0f;
    private static final int VEIN_SIZE_MIN = 0;
    private static final int VEIN_SIZE_MAX = 1000;
    private static final int Y_MIN = 0;
    private static final int Y_MAX = 255;

    private final String name;
    private float veinCount;
    private int veinSize;
    private int minY;
    private int maxY;
    private final int dimension;

    @Getter(AccessLevel.NONE)
    private final Set<Integer> dimensionList = new HashSet<>();
    private boolean dimensionListIsWhitelist = false;

    private final Set<Block> blockList = new HashSet<>();
    private Predicate<IBlockState> blockPredicate;

    public ConfigOptionOreGen(String name, int dimension, float veinCount, int veinSize, int minY, int maxY) {

        this.name = name;
        this.veinCount = veinCount;
        this.veinSize = veinSize;
        this.minY = minY;
        this.maxY = maxY;
        this.dimension = dimension;
    }

    public int getVeinCount(Random random) {
        // Decimal part of veinCount is a chance to spawn an extra vein.
        float diff = veinCount - (int) veinCount;
        return (int) veinCount + (random.nextFloat() < diff ? 1 : 0);
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category) {
        return loadValue(c, category + c.CATEGORY_SPLITTER + name, "World generation for " + name);
    }

    @Override
    public ConfigOption loadValue(Configuration c, String category, String comment) {
        c.setCategoryComment(category, comment);
        veinCount = (float) c.get(category, "Vein Count", veinCount).getDouble();
        veinSize = c.get(category, "Vein Size", veinSize).getInt();
        minY = c.get(category, "Min Y", minY).getInt();
        maxY = c.get(category, "Max Y", maxY).getInt();

        // Dimension blacklist/whitelist
        dimensionList.clear();
        int[] dimList = c.get(category, "Dimension Blacklist", new int[0]).getIntList();
        for (int dim : dimList) dimensionList.add(dim);
        dimensionListIsWhitelist = c.get(category, "Dimension List Is Whitelist", false).getBoolean();

        // Block replacements
        blockList.clear();
        String[] blocksDefault = new String[]{dimension == -1 ? "minecraft:netherrack" : dimension == 1 ? "minecraft:end_stone" : "minecraft:stone"};
        String[] blocks = c.get(category, "Blocks To Replace", blocksDefault).getStringList();
        Arrays.stream(blocks).map(Block::getBlockFromName).filter(Objects::nonNull).forEach(blockList::add);
        if (blockList.isEmpty()) blockList.add(Blocks.STONE);
        blockPredicate = state -> state != null && blockList.contains(state.getBlock());

        return this.validate();
    }

    @Override
    public ConfigOption validate() {
        veinCount = MathHelper.clamp(veinCount, VEIN_COUNT_MIN, VEIN_COUNT_MAX);
        veinSize = MathHelper.clamp(veinSize, VEIN_SIZE_MIN, VEIN_SIZE_MAX);
        minY = MathHelper.clamp(minY, Y_MIN, Y_MAX);
        maxY = MathHelper.clamp(maxY, Y_MIN, Y_MAX);

        // Sanity check: max Y must be greater than min Y.
        if (maxY <= minY) {
            SilentGems.logHelper.warn("Ore config \"" + name + "\": Min Y is greater than Max Y!");
            maxY = minY + 1;
        }

        return this;
    }

    public boolean isEnabled() {
        // Ore will be disabled if either the vein count or vein size is set to 0.
        return veinCount > 0 && veinSize > 0;
    }

    public BlockPos getRandomPos(Random random, int posX, int posZ) {
        return new BlockPos(
                posX + random.nextInt(16),
                minY + random.nextInt(maxY - minY),
                posZ + random.nextInt(16));
    }

    public boolean canSpawnInDimension(int dim) {
        return dimensionList.contains(dim) == dimensionListIsWhitelist;
    }
}
