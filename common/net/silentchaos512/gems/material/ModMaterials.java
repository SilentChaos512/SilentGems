package net.silentchaos512.gems.material;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.silentchaos512.gems.configuration.Config;

public class ModMaterials {

	public final static int FISH_GEM_ID = 42;
	private static int mlr = Config.miningLevelRegular;
	
	public final static ToolMaterial toolRegRuby        = EnumHelper.addToolMaterial("GemRegRuby",       mlr, 768,  8.0f, 3.0f, 10);
  public final static ToolMaterial toolRegGarnet      = EnumHelper.addToolMaterial("GemRegGarnet",     mlr, 512,  8.0f, 3.0f, 10);
  public final static ToolMaterial toolRegTopaz       = EnumHelper.addToolMaterial("GemRegTopaz",      mlr, 512, 10.0f, 4.0f, 10);
  public final static ToolMaterial toolRegHeliodor    = EnumHelper.addToolMaterial("GemRegHeliodor",   mlr, 384, 12.0f, 5.0f, 10);
  public final static ToolMaterial toolRegPeridot     = EnumHelper.addToolMaterial("GemRegPeridot",    mlr, 512,  7.0f, 4.0f, 10);
  public final static ToolMaterial toolRegEmerald     = EnumHelper.addToolMaterial("GemRegEmerald",    mlr, 512,  8.0f, 4.0f, 10);
  public final static ToolMaterial toolRegAquamarine  = EnumHelper.addToolMaterial("GemRegAquamarine", mlr, 384, 10.0f, 3.0f, 10);
  public final static ToolMaterial toolRegSapphire    = EnumHelper.addToolMaterial("GemRegSapphire",   mlr, 768,  8.0f, 3.0f, 10);
  public final static ToolMaterial toolRegIolite      = EnumHelper.addToolMaterial("GemRegIolite",     mlr, 768,  7.0f, 2.0f, 10);
  public final static ToolMaterial toolRegAmethyst    = EnumHelper.addToolMaterial("GemRegAmethyst",   mlr, 512,  8.0f, 3.0f, 10);
  public final static ToolMaterial toolRegMorganite   = EnumHelper.addToolMaterial("GemRegMorganite",  mlr, 512, 10.0f, 4.0f, 10);
  public final static ToolMaterial toolRegOnyx        = EnumHelper.addToolMaterial("GemRegOnyx",       mlr, 384, 10.0f, 6.0f, 10);

  private final static int dm = 4;
  private final static float ea = 4.0f;
  private final static float da = 2.0f;
  private final static int mls = Config.miningLevelSuper;

  public final static ToolMaterial toolSupRuby        = EnumHelper.addToolMaterial("GemSupRuby",       mls, 768 * dm,  8.0f + ea, 3.0f + da, 14);
  public final static ToolMaterial toolSupGarnet      = EnumHelper.addToolMaterial("GemSupGarnet",     mls, 512 * dm,  8.0f + ea, 3.0f + da, 14);
  public final static ToolMaterial toolSupTopaz       = EnumHelper.addToolMaterial("GemSupTopaz",      mls, 512 * dm, 10.0f + ea, 4.0f + da, 14);
  public final static ToolMaterial toolSupHeliodor    = EnumHelper.addToolMaterial("GemSupHeliodor",   mls, 384 * dm, 12.0f + ea, 5.0f + da, 14);
  public final static ToolMaterial toolSupPeridot     = EnumHelper.addToolMaterial("GemSupPeridot",    mls, 512 * dm,  7.0f + ea, 4.0f + da, 14);
  public final static ToolMaterial toolSupEmerald     = EnumHelper.addToolMaterial("GemSupEmerald",    mls, 512 * dm,  8.0f + ea, 4.0f + da, 14);
  public final static ToolMaterial toolSupAquamarine  = EnumHelper.addToolMaterial("GemSupAquamarine", mls, 384 * dm, 10.0f + ea, 3.0f + da, 14);
  public final static ToolMaterial toolSupSapphire    = EnumHelper.addToolMaterial("GemSupSapphire",   mls, 768 * dm,  8.0f + ea, 3.0f + da, 14);
  public final static ToolMaterial toolSupIolite      = EnumHelper.addToolMaterial("GemSupIolite",     mls, 768 * dm,  7.0f + ea, 2.0f + da, 14);
  public final static ToolMaterial toolSupAmethyst    = EnumHelper.addToolMaterial("GemSupAmethyst",   mls, 512 * dm,  8.0f + ea, 3.0f + da, 14);
  public final static ToolMaterial toolSupMorganite   = EnumHelper.addToolMaterial("GemSupMorganite",  mls, 512 * dm, 10.0f + ea, 4.0f + da, 14);
  public final static ToolMaterial toolSupOnyx        = EnumHelper.addToolMaterial("GemSupOnyx",       mls, 384 * dm, 10.0f + ea, 6.0f + da, 14);

  public final static ToolMaterial toolFish           = EnumHelper.addToolMaterial("GemFish", Config.miningLevelFish, 64, 3.0f, 1.0f, 20);
}
