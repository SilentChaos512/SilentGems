package net.silentchaos512.gems.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tags.ITag;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.feature.template.TagMatchRuleTest;

public class SGOreFeatureConfig extends OreFeatureConfig {
    public static final Codec<SGOreFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RuleTest.field_237127_c_.fieldOf("target").forGetter(config -> config.target),
                    BlockState.CODEC.fieldOf("state").forGetter(config -> config.state),
                    Codec.INT.fieldOf("size").forGetter(config -> config.size)
            ).apply(instance, SGOreFeatureConfig::new));

    public SGOreFeatureConfig(ITag<Block> target, BlockState state, int size) {
        this(new TagMatchRuleTest(target), state, size);
    }

    public SGOreFeatureConfig(RuleTest target, BlockState state, int size) {
        super(target, state, size);
    }
}
