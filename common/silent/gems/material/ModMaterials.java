package silent.gems.material;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class ModMaterials {

	  public final static int FISH_GEM_ID = 42;
	
	  public final static ToolMaterial toolRegRuby        = EnumHelper.addToolMaterial("GemRegRuby",       2, 768,  8.0f, 3.0f, 10);
    public final static ToolMaterial toolRegGarnet      = EnumHelper.addToolMaterial("GemRegGarnet",     2, 512,  8.0f, 3.0f, 10);
    public final static ToolMaterial toolRegTopaz       = EnumHelper.addToolMaterial("GemRegTopaz",      2, 512, 10.0f, 4.0f, 10);
    public final static ToolMaterial toolRegHeliodor    = EnumHelper.addToolMaterial("GemRegHeliodor",   2, 384, 12.0f, 5.0f, 10);
    public final static ToolMaterial toolRegPeridot     = EnumHelper.addToolMaterial("GemRegPeridot",    2, 512,  7.0f, 4.0f, 10);
    public final static ToolMaterial toolRegEmerald     = EnumHelper.addToolMaterial("GemRegEmerald",    2, 512,  8.0f, 4.0f, 10);
    public final static ToolMaterial toolRegAquamarine  = EnumHelper.addToolMaterial("GemRegAquamarine", 2, 384, 10.0f, 3.0f, 10);
    public final static ToolMaterial toolRegSapphire    = EnumHelper.addToolMaterial("GemRegSapphire",   2, 768,  8.0f, 3.0f, 10);
    public final static ToolMaterial toolRegIolite      = EnumHelper.addToolMaterial("GemRegIolite",     2, 768,  7.0f, 2.0f, 10);
    public final static ToolMaterial toolRegAmethyst    = EnumHelper.addToolMaterial("GemRegAmethyst",   2, 512,  8.0f, 3.0f, 10);
    public final static ToolMaterial toolRegMorganite   = EnumHelper.addToolMaterial("GemRegMorganite",  2, 512, 10.0f, 4.0f, 10);
    public final static ToolMaterial toolRegOnyx        = EnumHelper.addToolMaterial("GemRegOnyx",       2, 384, 10.0f, 6.0f, 10);
    
    private final static int dm = 4;
    private final static float ea = 4.0f;
    private final static float da = 2.0f;
    private final static int ml = 4;
    
    public final static ToolMaterial toolSupRuby        = EnumHelper.addToolMaterial("GemSupRuby",       ml, 768 * dm,  8.0f + ea, 3.0f + da, 14);
    public final static ToolMaterial toolSupGarnet      = EnumHelper.addToolMaterial("GemSupGarnet",     ml, 512 * dm,  8.0f + ea, 3.0f + da, 14);
    public final static ToolMaterial toolSupTopaz       = EnumHelper.addToolMaterial("GemSupTopaz",      ml, 512 * dm, 10.0f + ea, 4.0f + da, 14);
    public final static ToolMaterial toolSupHeliodor    = EnumHelper.addToolMaterial("GemSupHeliodor",   ml, 384 * dm, 12.0f + ea, 5.0f + da, 14);
    public final static ToolMaterial toolSupPeridot     = EnumHelper.addToolMaterial("GemSupPeridot",    ml, 512 * dm,  7.0f + ea, 4.0f + da, 14);
    public final static ToolMaterial toolSupEmerald     = EnumHelper.addToolMaterial("GemSupEmerald",    ml, 512 * dm,  8.0f + ea, 4.0f + da, 14);
    public final static ToolMaterial toolSupAquamarine  = EnumHelper.addToolMaterial("GemSupAquamarine", ml, 384 * dm, 10.0f + ea, 3.0f + da, 14);
    public final static ToolMaterial toolSupSapphire    = EnumHelper.addToolMaterial("GemSupSapphire",   ml, 768 * dm,  8.0f + ea, 3.0f + da, 14);
    public final static ToolMaterial toolSupIolite      = EnumHelper.addToolMaterial("GemSupIolite",     ml, 768 * dm,  7.0f + ea, 2.0f + da, 14);
    public final static ToolMaterial toolSupAmethyst    = EnumHelper.addToolMaterial("GemSupAmethyst",   ml, 512 * dm,  8.0f + ea, 3.0f + da, 14);
    public final static ToolMaterial toolSupMorganite   = EnumHelper.addToolMaterial("GemSupMorganite",  ml, 512 * dm, 10.0f + ea, 4.0f + da, 14);
    public final static ToolMaterial toolSupOnyx        = EnumHelper.addToolMaterial("GemSupOnyx",       ml, 384 * dm, 10.0f + ea, 6.0f + da, 14);
    
    public final static ToolMaterial toolFish           = EnumHelper.addToolMaterial("GemFish", 3, 64, 3.0f, 1.0f, 20);
}
