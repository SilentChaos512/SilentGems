package silent.gems.item.block;

import silent.gems.lib.Names;


public class MushroomBlockItemBlock extends ItemBlockSG {

    public MushroomBlockItemBlock(int id) {
        
        super(id);
        setHasSubtypes(true);
        setUnlocalizedName(Names.MUSHROOM_BLOCK);
    }
}
