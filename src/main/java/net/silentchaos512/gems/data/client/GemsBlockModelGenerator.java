package net.silentchaos512.gems.data.client;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.resources.Identifier;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.GemLampBlock;
import net.silentchaos512.gems.block.teleporter.TeleporterAnchorBlock;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.setup.Gems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GemsBlockModelGenerator extends BlockModelGenerators {
    public GemsBlockModelGenerator(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(blockStateOutput, itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        for (Gems gem : Gems.values()) {
            createModelsForGem(gem);
        }

        createTeleporterAnchor(GemsBlocks.TELEPORTER_ANCHOR.get());

        createTrivialCube(GemsBlocks.CHAOS_ESSENCE_BLOCK.get());
        createTrivialCube(GemsBlocks.CHAOS_ORE.get());
        createTrivialCube(GemsBlocks.DEEPSLATE_CHAOS_ORE.get());
        createTrivialCube(GemsBlocks.SILVER_BLOCK.get());
        createTrivialCube(GemsBlocks.DEEPSLATE_SILVER_ORE.get());
        createTrivialCube(GemsBlocks.SILVER_ORE.get());
    }

    private void createModelsForGem(Gems gem) {
        createTrivialCube(gem.getOre());
        createTrivialCube(gem.getDeepslateOre());
        createTrivialCube(gem.getNetherOre());
        createTrivialCube(gem.getEndOre());
        createTrivialCube(gem.getBlock());
        createTrivialCube(gem.getBricks());
        createTrivialCube(gem.getTiles());
        createTrivialCube(gem.getSmallBricks());
        createTrivialCube(gem.getPolishedStone());
        createTrivialCube(gem.getSmoothStone());
        createTrivialCube(gem.getChiseledStone());
        createTrivialBlock(gem.getGlass(),
                TexturedModel.CUBE.updateTemplate(template ->
                        template.extend().renderType("minecraft:translucent").build()
                )
        );

        for (GemLampBlock.State state : GemLampBlock.State.values()) {
            createTrivialBlock(gem.getLamp(state),
                    TexturedModel.CUBE.updateTexture(mapping ->
                            mapping.put(TextureSlot.ALL, modId("block/" + gem.getName() + "_lamp" + (state.lit() ? "_on" : "")))
                    )
            );
        }

        createPlantWithDefaultItem(gem.getGlowrose(), gem.getPottedGlowrose(), PlantType.NOT_TINTED);

        createTeleporter(gem, false);
        createTeleporter(gem, true);
    }

    private void createTeleporter(Gems gem, boolean redstone) {
        var teleporterBlock = redstone ? gem.getRedstoneTeleporter().get() : gem.getTeleporter().get();
        var frameTexture = modId("block/" + (redstone ? "redstone_teleporter_frame" : "teleporter_frame"));
        var gemBlockTexture = modId("block/" + gem.getName() + "_block");
        var model = GemsModelTemplates.TELEPORTER.create(
                gem.getTeleporter().get(),
                new TextureMapping()
                        .put(GemsTextureSlots.FRAME, frameTexture)
                        .put(GemsTextureSlots.GEM, gemBlockTexture),
                modelOutput
        );
        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(teleporterBlock, plainVariant(model))
                        .with(ROTATION_HORIZONTAL_FACING)
        );
    }

    private void createTeleporterAnchor(TeleporterAnchorBlock teleporterAnchorBlock) {
        var frameTexture = modId("block/teleporter_frame");
        var gemBlockTexture = modId("block/silver_block");
        var particleTexture = modId("block/chaos_essence_block");
        var model = GemsModelTemplates.TELEPORTER.create(
                teleporterAnchorBlock,
                new TextureMapping()
                        .put(GemsTextureSlots.FRAME, frameTexture)
                        .put(GemsTextureSlots.GEM, gemBlockTexture)
                        .put(TextureSlot.PARTICLE, particleTexture),
                modelOutput
        );
        this.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(teleporterAnchorBlock, plainVariant(model))
                        .with(ROTATION_HORIZONTAL_FACING)
        );
    }

    private Identifier modId(String path) {
        return SilentGems.getId(path);
    }
}
