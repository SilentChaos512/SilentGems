package silent.gems.block;

import silent.gems.lib.Names;


public class TeleporterItemBlock extends ItemBlockSG {

    public TeleporterItemBlock(int id) {

        super(id);
        setHasSubtypes(true);
        setHasGemSubtypes(true);
        setUnlocalizedName(Names.TELEPORTER);
    }

}
