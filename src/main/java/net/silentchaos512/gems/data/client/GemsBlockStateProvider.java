package net.silentchaos512.gems.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.block.*;
import net.silentchaos512.gems.init.GemsBlocks;
import net.silentchaos512.gems.lib.Gems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

import java.util.Arrays;

public class GemsBlockStateProvider extends BlockStateProvider {
    public GemsBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SilentGems.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation flowerPotCross = mcLoc("block/flower_pot_cross");

        for (Gems gem : Gems.values()) {
            String name = gem.getName();
            simpleBlock(gem.getOre(), "block/ore/gem/" + name);
            simpleBlock(gem.getBlock(), "block/gem/" + name + "_block");
            simpleBlock(gem.getBricks(), "block/bricks/" + name);
            simpleBlock(gem.getGlass(), "block/glass/" + name);
            simpleBlock(gem.getLamp(GemLampBlock.State.UNLIT), "block/lamp/" + name);
            simpleBlock(gem.getLamp(GemLampBlock.State.LIT), "block/lamp/" + name + "_lit");
            simpleBlock(gem.getLamp(GemLampBlock.State.INVERTED_LIT), "block/lamp/" + name + "_lit");
            simpleBlock(gem.getLamp(GemLampBlock.State.INVERTED_UNLIT), "block/lamp/" + name);
            simpleBlock(gem.getGlowrose(), models()
                    .cross(name + "_glowrose", modLoc("block/glowrose/" + name)));
            simpleBlock(gem.getPottedGlowrose(), models()
                    .withExistingParent(NameUtils.from(gem.getPottedGlowrose()).getPath(), flowerPotCross)
                    .texture("plant", modLoc("block/glowrose/" + name)));
            simpleBlock(gem.getTeleporter(), "block/teleporter/standard/" + name);
            simpleBlock(gem.getRedstoneTeleporter(), "block/teleporter/redstone/" + name);
        }

        simpleBlock(GemsBlocks.TELEPORTER_ANCHOR, "block/teleporter/anchor");
        simpleBlock(GemsBlocks.MULTI_ORE_CLASSIC, "block/ore/multi_classic");
        simpleBlock(GemsBlocks.MULTI_ORE_DARK, "block/ore/multi_dark");
        simpleBlock(GemsBlocks.MULTI_ORE_LIGHT, "block/ore/multi_light");

        Arrays.stream(MiscBlocks.values()).forEach(this::simpleBlock);
        Arrays.stream(MiscOres.values()).forEach(block -> simpleBlock(block, "block/ore/" + block.getName()));
        Arrays.stream(CorruptedBlocks.values()).forEach(this::simpleBlock);
        Arrays.stream(HardenedRock.values()).forEach(this::simpleBlock);

        simpleBlock(GemsBlocks.WHITE_FLUFFY_BLOCK, "block/fluffy/white");
        simpleBlock(GemsBlocks.ORANGE_FLUFFY_BLOCK, "block/fluffy/orange");
        simpleBlock(GemsBlocks.MAGENTA_FLUFFY_BLOCK, "block/fluffy/magenta");
        simpleBlock(GemsBlocks.LIGHT_BLUE_FLUFFY_BLOCK, "block/fluffy/light_blue");
        simpleBlock(GemsBlocks.YELLOW_FLUFFY_BLOCK, "block/fluffy/yellow");
        simpleBlock(GemsBlocks.LIME_FLUFFY_BLOCK, "block/fluffy/lime");
        simpleBlock(GemsBlocks.PINK_FLUFFY_BLOCK, "block/fluffy/pink");
        simpleBlock(GemsBlocks.GRAY_FLUFFY_BLOCK, "block/fluffy/gray");
        simpleBlock(GemsBlocks.LIGHT_GRAY_FLUFFY_BLOCK, "block/fluffy/light_gray");
        simpleBlock(GemsBlocks.CYAN_FLUFFY_BLOCK, "block/fluffy/cyan");
        simpleBlock(GemsBlocks.PURPLE_FLUFFY_BLOCK, "block/fluffy/purple");
        simpleBlock(GemsBlocks.BLUE_FLUFFY_BLOCK, "block/fluffy/blue");
        simpleBlock(GemsBlocks.BROWN_FLUFFY_BLOCK, "block/fluffy/brown");
        simpleBlock(GemsBlocks.GREEN_FLUFFY_BLOCK, "block/fluffy/green");
        simpleBlock(GemsBlocks.RED_FLUFFY_BLOCK, "block/fluffy/red");
        simpleBlock(GemsBlocks.BLACK_FLUFFY_BLOCK, "block/fluffy/black");

        threeQuartersBlock(GemsBlocks.SUPERCHARGER);
        threeQuartersBlock(GemsBlocks.TOKEN_ENCHANTER);
        threeQuartersBlock(GemsBlocks.TRANSMUTATION_ALTAR);
        threeQuartersBlock(GemsBlocks.PURIFIER);

        pedestal(GemsBlocks.ANDESITE_PEDESTAL.get(), mcLoc("block/andesite"));
        pedestal(GemsBlocks.DIORITE_PEDESTAL.get(), mcLoc("block/diorite"));
        pedestal(GemsBlocks.GRANITE_PEDESTAL.get(), mcLoc("block/granite"));
        pedestal(GemsBlocks.STONE_PEDESTAL.get(), mcLoc("block/stone"));
        pedestal(GemsBlocks.OBSIDIAN_PEDESTAL.get(), mcLoc("block/obsidian"));

        getVariantBuilder(GemsBlocks.FLUFFY_PUFF_PLANT.get()).forAllStates(state -> {
            int i = cropAgeToIndex(state.get(FluffyPuffPlant.AGE));
            return ConfiguredModel.builder()
                    .modelFile(models().cross("fluffy_puff_plant" + i, modLoc("block/fluffy_plant" + i)))
                    .build();
        });
        simpleBlock(GemsBlocks.WILD_FLUFFY_PUFF_PLANT.get(), models()
                .cross(NameUtils.from(GemsBlocks.WILD_FLUFFY_PUFF_PLANT.get()).getPath(),
                        modLoc("block/fluffy_plant3")));
    }

    private int cropAgeToIndex(int age) {
        if (age > 6)
            return 3;
        if (age > 3)
            return 2;
        if (age > 1)
            return 1;
        return 0;
    }

    private void simpleBlock(IBlockProvider block) {
        simpleBlock(block.asBlock());
    }

    private void simpleBlock(IBlockProvider block, String texture) {
        simpleBlock(block.asBlock(), texture);
    }

    private void simpleBlock(Block block, String texture) {
        String name = NameUtils.from(block).getPath();
        simpleBlock(block, models().cubeAll(name, modLoc(texture)));
    }

    private void threeQuartersBlock(IBlockProvider block) {
        String name = NameUtils.from(block.asBlock()).getPath();
        threeQuartersBlock(block.asBlock(),
                "block/" + name + "_bottom",
                "block/" + name + "_side",
                "block/" + name + "_top");
    }

    private void threeQuartersBlock(Block block, String bottomTexture, String sideTexture, String topTexture) {
        String name = NameUtils.from(block).getPath();
        simpleBlock(block, models().withExistingParent(name, mcLoc("block/block"))
                .texture("bottom", bottomTexture)
                .texture("side", sideTexture)
                .texture("top", topTexture)
                .texture("particle", bottomTexture)
                .element()
                .from(0, 0, 0)
                .to(16, 12, 16)
                .face(Direction.NORTH).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.EAST).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.SOUTH).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.WEST).uvs(0, 4, 16, 16).texture("#side").end()
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom").end()
                .end()
        );
    }

    private void pedestal(Block block, ResourceLocation stone) {
        String name = NameUtils.from(block).getPath();
        simpleBlock(block, models()
                .withExistingParent(name, modLoc("block/pedestal"))
                .texture("stone", stone));
    }
}
