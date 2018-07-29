package net.silentchaos512.gems.block;

import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.config.GemsConfig;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.RecipeMaker;

import java.util.List;

public class BlockTeleporterAnchor extends BlockTeleporter {
    public BlockTeleporterAnchor() {
        super(Names.TELEPORTER_ANCHOR);
        setDefaultState(blockState.getBaseState());
    }


    @Override
    public List<String> getWitLines(IBlockState state, BlockPos pos, EntityPlayer player, boolean advanced) {
        return null;
    }

    @Override
    public void addRecipes(RecipeMaker recipes) {
        if (!GemsConfig.RECIPE_TELEPORTER_ANCHOR_DISABLE) {
            recipes.addShapedOre(Names.TELEPORTER_ANCHOR, new ItemStack(this, 4), "cec", " i ", "cec", 'c',
                    "gemChaos", 'e', Items.ENDER_PEARL, 'i', "blockIron");
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this);
    }

    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(SilentGems.RESOURCE_PREFIX + Names.TELEPORTER_ANCHOR, "inventory"));
    }
}
