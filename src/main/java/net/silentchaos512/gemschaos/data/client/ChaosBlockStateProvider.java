package net.silentchaos512.gemschaos.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gemschaos.ChaosMod;
import net.silentchaos512.gemschaos.setup.ChaosBlocks;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.registry.BlockRegistryObject;
import net.silentchaos512.lib.util.NameUtils;

public class ChaosBlockStateProvider extends BlockStateProvider {
    public ChaosBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ChaosMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockRegistryObject<? extends Block> blockRegistryObject : ChaosBlocks.getSimpleBlocks()) {
            simpleBlock(blockRegistryObject);
        }
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
}
