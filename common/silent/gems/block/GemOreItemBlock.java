package silent.gems.block;

import silent.gems.lib.Names;


public class GemOreItemBlock extends ItemBlockSG {

    public GemOreItemBlock(int id) {

        super(id);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GEM_ORE);
    }

}
