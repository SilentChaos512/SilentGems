package silent.gems.material;

import net.minecraft.item.EnumToolMaterial;
import net.minecraftforge.common.EnumHelper;

public class ModMaterials {
    
    public final static int FISH_GEM_ID = 42;

    public final static EnumToolMaterial toolRegRuby        = EnumHelper.addToolMaterial("GemRegRuby",       2, 768,  8.0f, 3, 10);
    public final static EnumToolMaterial toolRegGarnet      = EnumHelper.addToolMaterial("GemRegGarnet",     2, 512,  8.0f, 3, 10);
    public final static EnumToolMaterial toolRegTopaz       = EnumHelper.addToolMaterial("GemRegTopaz",      2, 512, 10.0f, 4, 10);
    public final static EnumToolMaterial toolRegHeliodor    = EnumHelper.addToolMaterial("GemRegHeliodor",   2, 384, 12.0f, 5, 10);
    public final static EnumToolMaterial toolRegPeridot     = EnumHelper.addToolMaterial("GemRegPeridot",    2, 512,  7.0f, 4, 10);
    public final static EnumToolMaterial toolRegEmerald     = EnumHelper.addToolMaterial("GemRegEmerald",    2, 512,  8.0f, 4, 10);
    public final static EnumToolMaterial toolRegAquamarine  = EnumHelper.addToolMaterial("GemRegAquamarine", 2, 384, 10.0f, 3, 10);
    public final static EnumToolMaterial toolRegSapphire    = EnumHelper.addToolMaterial("GemRegSapphire",   2, 768,  8.0f, 3, 10);
    public final static EnumToolMaterial toolRegIolite      = EnumHelper.addToolMaterial("GemRegIolite",     2, 768,  7.0f, 2, 10);
    public final static EnumToolMaterial toolRegAmethyst    = EnumHelper.addToolMaterial("GemRegAmethyst",   2, 512,  8.0f, 3, 10);
    public final static EnumToolMaterial toolRegMorganite   = EnumHelper.addToolMaterial("GemRegMorganite",  2, 512, 10.0f, 4, 10);
    public final static EnumToolMaterial toolRegOnyx        = EnumHelper.addToolMaterial("GemRegOnyx",       2, 384, 10.0f, 5, 10);
    
    private final static int dm = 4;
    private final static float ea = 4.0f;
    private final static float da = 2.0f;
    private final static int ml = 4;
    
    public final static EnumToolMaterial toolSupRuby        = EnumHelper.addToolMaterial("GemSupRuby",       ml, 768 * dm,  8.0f + ea, 3 + da, 10);
    public final static EnumToolMaterial toolSupGarnet      = EnumHelper.addToolMaterial("GemSupGarnet",     ml, 512 * dm,  8.0f + ea, 3 + da, 10);
    public final static EnumToolMaterial toolSupTopaz       = EnumHelper.addToolMaterial("GemSupTopaz",      ml, 512 * dm, 10.0f + ea, 4 + da, 10);
    public final static EnumToolMaterial toolSupHeliodor    = EnumHelper.addToolMaterial("GemSupHeliodor",   ml, 384 * dm, 12.0f + ea, 5 + da, 10);
    public final static EnumToolMaterial toolSupPeridot     = EnumHelper.addToolMaterial("GemSupPeridot",    ml, 512 * dm,  7.0f + ea, 4 + da, 10);
    public final static EnumToolMaterial toolSupEmerald     = EnumHelper.addToolMaterial("GemSupEmerald",    ml, 512 * dm,  8.0f + ea, 4 + da, 10);
    public final static EnumToolMaterial toolSupAquamarine  = EnumHelper.addToolMaterial("GemSupAquamarine", ml, 384 * dm, 10.0f + ea, 3 + da, 10);
    public final static EnumToolMaterial toolSupSapphire    = EnumHelper.addToolMaterial("GemSupSapphire",   ml, 768 * dm,  8.0f + ea, 3 + da, 10);
    public final static EnumToolMaterial toolSupIolite      = EnumHelper.addToolMaterial("GemSupIolite",     ml, 768 * dm,  7.0f + ea, 2 + da, 10);
    public final static EnumToolMaterial toolSupAmethyst    = EnumHelper.addToolMaterial("GemSupAmethyst",   ml, 512 * dm,  8.0f + ea, 3 + da, 10);
    public final static EnumToolMaterial toolSupMorganite   = EnumHelper.addToolMaterial("GemSupMorganite",  ml, 512 * dm, 10.0f + ea, 4 + da, 10);
    public final static EnumToolMaterial toolSupOnyx        = EnumHelper.addToolMaterial("GemSupOnyx",       ml, 384 * dm, 10.0f + ea, 5 + da, 10);
    
    public final static EnumToolMaterial toolFish           = EnumHelper.addToolMaterial("GemFish", 3, 64, 3.0f, 2.0f, 20);
}
