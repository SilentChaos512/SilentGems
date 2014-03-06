package silent.gems.block;

import silent.gems.lib.Names;


public class GlowRoseItemBlock extends ItemBlockSG {

    public GlowRoseItemBlock(int id) {

        super(id);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.GLOW_ROSE);
    }

}
