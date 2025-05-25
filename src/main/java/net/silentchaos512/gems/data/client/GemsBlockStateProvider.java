package net.silentchaos512.gems.data.client;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.block.teleporter.GemTeleporterBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

public class GemsBlockStateProvider extends BlockStateProvider {
    public GemsBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen.getPackOutput(), SilentGems.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation flowerPotCross = mcLoc("block/flower_pot_cross");

        for (Gems gem : Gems.values()) {
            simpleBlock(gem.getOre());
            simpleBlock(gem.getDeepslateOre());
            simpleBlock(gem.getNetherOre());
            simpleBlock(gem.getEndOre());
            simpleBlock(gem.getBlock());
            simpleBlock(gem.getBricks());
            simpleBlock(gem.getTiles());
            simpleBlock(gem.getSmallBricks());
            simpleBlock(gem.getPolishedStone());
            simpleBlock(gem.getSmoothStone());
            simpleBlock(gem.getChiseledStone());
            var glass = gem.getName() + "_glass";
            simpleBlock(gem.getGlass(),
                    models()
                            .cubeAll(glass, modLoc("block/" + glass))
                            .renderType("translucent")
            );

            for (GemLampBlock.State state : GemLampBlock.State.values()) {
                simpleBlock(gem.getLamp(state), "block/" + gem.getName() + "_lamp" + (state.lit() ? "_on" : ""));
            }

            String glowroseName = gem.getName() + "_glowrose";
            simpleBlock(gem.getGlowrose(),
                    models()
                            .cross(glowroseName, modLoc("block/" + glowroseName))
                            .renderType("cutout")
            );
            simpleBlock(gem.getPottedGlowrose(),
                    models()
                            .withExistingParent(NameUtils.fromBlock(gem.getPottedGlowrose()).getPath(), flowerPotCross)
                            .texture("plant", modLoc("block/" + glowroseName))
                            .renderType("cutout")
            );

            teleporterBlock(gem, false);
            teleporterBlock(gem, true);
        }

        simpleBlock(GemsBlocks.CHAOS_ESSENCE_BLOCK.get());
        simpleBlock(GemsBlocks.CHAOS_ORE.get());
        simpleBlock(GemsBlocks.DEEPSLATE_CHAOS_ORE.get());
        simpleBlock(GemsBlocks.SILVER_BLOCK.get());
        simpleBlock(GemsBlocks.DEEPSLATE_SILVER_ORE.get());
        simpleBlock(GemsBlocks.SILVER_ORE.get());
    }

    private void simpleBlock(IBlockProvider block) {
        simpleBlock(block.asBlock());
    }

    private void simpleBlock(IBlockProvider block, String texture) {
        simpleBlock(block.asBlock(), texture);
    }

    private void simpleBlock(Block block, String texture) {
        String name = NameUtils.fromBlock(block).getPath();
        simpleBlock(block, models().cubeAll(name, modLoc(texture)));
    }

    private void teleporterBlock(Gems gem, boolean redstone) {
        var teleporterBlock = redstone ? gem.getRedstoneTeleporter().get() : gem.getTeleporter().get();
        var frameTexture = modLoc("block/" + (redstone ? "redstone_teleporter_frame" : "teleporter_frame"));
        var model = models()
                .withExistingParent(NameUtils.fromBlock(teleporterBlock).getPath(), modLoc("block/teleporter"))
                .texture("gem", modLoc("block/" + gem.getName() + "_block"))
                .texture("frame", frameTexture);
        getVariantBuilder(teleporterBlock).forAllStates(state -> {
            Direction facing = state.getValue(GemTeleporterBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY((int) facing.getOpposite().toYRot())
                    .build();
        });
    }
}
