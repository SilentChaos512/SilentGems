package net.silentchaos512.gems.world.feature.structure;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.AlwaysTrueRuleTest;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleEntry;
import net.minecraft.world.gen.feature.template.RuleStructureProcessor;
import net.silentchaos512.gems.SilentGems;

public class ShrineTest {
    public static void init() {
        JigsawManager.REGISTRY.register(new JigsawPattern(SilentGems.getId("shrine"), SilentGems.getId("shrine/test"),
                ImmutableList.of(
                        new Pair<>(new SingleJigsawPiece("silentgems:shrine", ImmutableList.of(
                                new RuleStructureProcessor(ImmutableList.of(
                                        new RuleEntry(new BlockMatchRuleTest(Blocks.GRASS_BLOCK), AlwaysTrueRuleTest.INSTANCE, Blocks.MOSSY_COBBLESTONE.getDefaultState())
                                ))
                        ), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING), 4)
                ), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING
        ));
    }
}
