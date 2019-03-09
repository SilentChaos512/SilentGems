package net.silentchaos512.gems.block.tokenenchanter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.silentchaos512.gems.client.gui.GuiTypes;
import net.silentchaos512.utils.Lazy;

public final class TokenEnchanterBlock extends BlockContainer {
    public static final Lazy<TokenEnchanterBlock> INSTANCE = Lazy.of(TokenEnchanterBlock::new);

    private TokenEnchanterBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(5, 50)
                .sound(SoundType.METAL));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new TokenEnchanterTileEntity();
    }

    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof IInventory) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tile);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof TokenEnchanterTileEntity) {
                GuiTypes.TOKEN_ENCHANTER.display(player, worldIn, pos);
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
