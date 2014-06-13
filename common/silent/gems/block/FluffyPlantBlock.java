package silent.gems.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import silent.gems.core.registry.SRegistry;
import silent.gems.lib.Names;
import silent.gems.lib.Strings;

public class FluffyPlantBlock extends BlockCrops {

    private IIcon[] icons;

    public FluffyPlantBlock() {

        this.setBlockName(Names.FLUFFY_PLANT);
        this.setBlockTextureName(Strings.RESOURCE_PREFIX + Names.FLUFFY_PLANT);
    }

    @Override
    protected Item func_149866_i() {

        return SRegistry.getItem(Names.FLUFFY_SEED);
    }

    @Override
    protected Item func_149865_P() {

        return SRegistry.getItem(Names.FLUFFY_SEED);
    }

    @Override
    public IIcon getIcon(int side, int meta) {

        if (meta < 7) {
            if (meta == 6) {
                meta = 5;
            }

            return this.icons[meta >> 1];
        }
        else {
            return this.icons[3];
        }
    }
    
    @Override
    public int getRenderType() {
        
        return 1;
    }
    
    @Override
    public String getUnlocalizedName() {
        
        return "tile." + Names.FLUFFY_PLANT;
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {

        this.icons = new IIcon[4];
        
        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = reg.registerIcon(this.getTextureName() + i);
        }
    }
}
