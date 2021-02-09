package net.silentchaos512.gems.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.World;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.setup.GemsBlocks;
import net.silentchaos512.gems.util.Gems;
import net.silentchaos512.lib.block.IBlockProvider;
import net.silentchaos512.lib.util.NameUtils;

public class GemsBlockStateProvider extends BlockStateProvider {
    public GemsBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, GemsBase.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Gems gem : Gems.values()) {
            simpleBlock(gem.getOre(World.OVERWORLD));
            simpleBlock(gem.getOre(World.THE_NETHER));
            simpleBlock(gem.getOre(World.THE_END));
            simpleBlock(gem.getBlock());
            simpleBlock(gem.getBricks());
        }

        simpleBlock(GemsBlocks.SILVER_BLOCK);
        simpleBlock(GemsBlocks.SILVER_ORE);
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
