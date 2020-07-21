package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;

public class SGOreFeatureConfig implements IFeatureConfig {
    public static final Codec<SGOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockState.field_235877_b_.fieldOf("block").forGetter(config -> config.state),
                    Codec.INT.fieldOf("size").forGetter(config -> config.size),
                    TagMatchRuleTest.field_237161_a_.fieldOf("target_block").forGetter(config -> config.target)
            ).apply(instance, (SGOreFeatureConfig::new)));

    public final BlockState state;
    public final int size;
    public final TagMatchRuleTest target;

    public SGOreFeatureConfig(BlockState state, int size, ITag<Block> target) {
        this(state, size, new TagMatchRuleTest(target));
    }

    public SGOreFeatureConfig(BlockState state, int size, TagMatchRuleTest target) {
        this.state = state;
        this.size = size;
        this.target = target;
    }
}
