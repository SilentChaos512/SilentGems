package net.silentchaos512.gems.data.client;

import net.minecraft.world.level.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.silentchaos512.gems.GemsBase;
import net.silentchaos512.gems.block.GemLampBlock;
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
        ResourceLocation flowerPotCross = mcLoc("block/flower_pot_cross");

        for (Gems gem : Gems.values()) {
            simpleBlock(gem.getOre(Level.OVERWORLD));
            simpleBlock(gem.getOre(Level.NETHER));
            simpleBlock(gem.getOre(Level.END));
            simpleBlock(gem.getBlock());
            simpleBlock(gem.getBricks());
            simpleBlock(gem.getGlass());

            for (GemLampBlock.State state : GemLampBlock.State.values()) {
                simpleBlock(gem.getLamp(state), "block/" + gem.getName() + "_lamp" + (state.lit() ? "_on" : ""));
            }

            String glowroseName = gem.getName() + "_glowrose";
            simpleBlock(gem.getGlowrose(), models()
                    .cross(glowroseName, modLoc("block/" + glowroseName)));
            simpleBlock(gem.getPottedGlowrose(), models()
                    .withExistingParent(NameUtils.from(gem.getPottedGlowrose()).getPath(), flowerPotCross)
                    .texture("plant", modLoc("block/" + glowroseName)));
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
