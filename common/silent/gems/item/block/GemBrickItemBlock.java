package silent.gems.item.block;

import silent.gems.lib.Names;


public class GemBrickItemBlock extends ItemBlockSG {

    public GemBrickItemBlock(int id) {

        super(id);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_BRICK);
    }

}
