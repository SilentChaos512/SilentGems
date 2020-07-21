package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;
import net.silentchaos512.gems.lib.Gems;

public class GemGeodeFeatureConfig implements IFeatureConfig {
    public static final Codec<GemGeodeFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                Gems.Set.CODEC.fieldOf("gem_set").forGetter(config -> config.gemSet),
                BlockState.field_235877_b_.fieldOf("shell_block").forGetter(config -> config.shellBlock),
                TagMatchRuleTest.field_237161_a_.fieldOf("target_block").forGetter(config -> config.target)
        ).apply(instance, (GemGeodeFeatureConfig::new)));

    public final Gems.Set gemSet;
    public final BlockState shellBlock;
    public final TagMatchRuleTest target;
    public final boolean replaceAir = true;
    public final float gemDensity = 0.75f;

    public GemGeodeFeatureConfig(Gems.Set gemSet, BlockState shellBlock, ITag<Block> target) {
        this(gemSet, shellBlock, new TagMatchRuleTest(target));
    }

    public GemGeodeFeatureConfig(Gems.Set gemSet, BlockState shellBlock, TagMatchRuleTest target) {
        this.gemSet = gemSet;
        this.shellBlock = shellBlock;
        this.target = target;
    }
}
