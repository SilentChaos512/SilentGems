package silent.gems.item.block;

import silent.gems.lib.Names;


public class GemBlockItemBlock extends ItemBlockSG {

    public GemBlockItemBlock(int id) {

        super(id);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_BLOCK);
    }
}
