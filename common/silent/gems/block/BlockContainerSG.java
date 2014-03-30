package silent.gems.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockContainerSG extends BlockSG implements ITileEntityProvider {

    public BlockContainerSG(int id, Material material) {

        super(id, material);
        this.isBlockContainer = true;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        
        super.breakBlock(world, x, y, z, blockId, meta);
        world.removeBlockTileEntity(x, y, z);
    }
    
    @Override
    public boolean onBlockEventReceived(World world, int x, int y, int z, int blockId, int eventId) {
        
        super.onBlockEventReceived(world, x, y, z, blockId, eventId);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        return tile != null ? tile.receiveClientEvent(blockId, eventId) : false;
    }
}
