package silent.gems.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import silent.gems.SilentGems;
import silent.gems.core.util.LogHelper;
import silent.gems.lib.GuiIds;
import silent.gems.lib.Names;
import silent.gems.tileentity.TileShinyCrafter;


public class ShinyCrafter extends BlockContainerSG {

    public ShinyCrafter(int id) {

        super(id, Material.iron);
        setStepSound(Block.soundMetalFootstep);
        setCreativeTab(CreativeTabs.tabBlock);
        setUnlocalizedName(Names.SHINY_CRAFTER);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world) {
        
        return new TileShinyCrafter();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {

        if (player.isSneaking()) {
            return false;
        }
        else {
            if (!world.isRemote) {
                if (world.getBlockTileEntity(x, y, z) instanceof TileShinyCrafter) {
                    //LogHelper.derp();
                    player.openGui(SilentGems.instance, GuiIds.SHINY_CRAFTER, world, x, y, z);
                }
            }
            
            return true;
        }
    }
    
    @Override
    public void addRecipes() {
        
        // TODO
    }
}
