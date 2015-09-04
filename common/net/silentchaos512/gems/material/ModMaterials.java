package net.silentchaos512.gems.material;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.silentchaos512.gems.configuration.Config;

public class ModMaterials {

	public static final int FISH_GEM_ID = 42;
	private static int mlr = Config.miningLevelRegular;
	
	public static final ToolMaterial toolRegRuby        = EnumHelper.addToolMaterial("GemRegRuby",       mlr, 768,  8.0f, 3.0f, 10);
  public static final ToolMaterial toolRegGarnet      = EnumHelper.addToolMaterial("GemRegGarnet",     mlr, 512,  8.0f, 3.0f, 13);
  public static final ToolMaterial toolRegTopaz       = EnumHelper.addToolMaterial("GemRegTopaz",      mlr, 512, 10.0f, 4.0f, 11);
  public static final ToolMaterial toolRegHeliodor    = EnumHelper.addToolMaterial("GemRegHeliodor",   mlr, 384, 12.0f, 5.0f, 10);
  public static final ToolMaterial toolRegPeridot     = EnumHelper.addToolMaterial("GemRegPeridot",    mlr, 512,  7.0f, 4.0f, 14);
  public static final ToolMaterial toolRegEmerald     = EnumHelper.addToolMaterial("GemRegEmerald",    mlr, 512,  8.0f, 4.0f, 12);
  public static final ToolMaterial toolRegAquamarine  = EnumHelper.addToolMaterial("GemRegAquamarine", mlr, 384, 10.0f, 3.0f, 12);
  public static final ToolMaterial toolRegSapphire    = EnumHelper.addToolMaterial("GemRegSapphire",   mlr, 768,  8.0f, 3.0f, 10);
  public static final ToolMaterial toolRegIolite      = EnumHelper.addToolMaterial("GemRegIolite",     mlr, 768,  7.0f, 2.0f, 11);
  public static final ToolMaterial toolRegAmethyst    = EnumHelper.addToolMaterial("GemRegAmethyst",   mlr, 512,  8.0f, 3.0f, 12);
  public static final ToolMaterial toolRegMorganite   = EnumHelper.addToolMaterial("GemRegMorganite",  mlr, 512, 10.0f, 4.0f, 12);
  public static final ToolMaterial toolRegOnyx        = EnumHelper.addToolMaterial("GemRegOnyx",       mlr, 384, 10.0f, 6.0f, 8);

  private static final int dm = 4;
  private static final float ea = 4.0f;
  private static final float da = 2.0f;
  private static final int mls = Config.miningLevelSuper;

  public static final ToolMaterial toolSupRuby        = EnumHelper.addToolMaterial("GemSupRuby",       mls, 768 * dm,  8.0f + ea, 3.0f + da, 16);
  public static final ToolMaterial toolSupGarnet      = EnumHelper.addToolMaterial("GemSupGarnet",     mls, 512 * dm,  8.0f + ea, 3.0f + da, 19);
  public static final ToolMaterial toolSupTopaz       = EnumHelper.addToolMaterial("GemSupTopaz",      mls, 512 * dm, 10.0f + ea, 4.0f + da, 17);
  public static final ToolMaterial toolSupHeliodor    = EnumHelper.addToolMaterial("GemSupHeliodor",   mls, 384 * dm, 12.0f + ea, 5.0f + da, 16);
  public static final ToolMaterial toolSupPeridot     = EnumHelper.addToolMaterial("GemSupPeridot",    mls, 512 * dm,  7.0f + ea, 4.0f + da, 20);
  public static final ToolMaterial toolSupEmerald     = EnumHelper.addToolMaterial("GemSupEmerald",    mls, 512 * dm,  8.0f + ea, 4.0f + da, 18);
  public static final ToolMaterial toolSupAquamarine  = EnumHelper.addToolMaterial("GemSupAquamarine", mls, 384 * dm, 10.0f + ea, 3.0f + da, 18);
  public static final ToolMaterial toolSupSapphire    = EnumHelper.addToolMaterial("GemSupSapphire",   mls, 768 * dm,  8.0f + ea, 3.0f + da, 16);
  public static final ToolMaterial toolSupIolite      = EnumHelper.addToolMaterial("GemSupIolite",     mls, 768 * dm,  7.0f + ea, 2.0f + da, 17);
  public static final ToolMaterial toolSupAmethyst    = EnumHelper.addToolMaterial("GemSupAmethyst",   mls, 512 * dm,  8.0f + ea, 3.0f + da, 18);
  public static final ToolMaterial toolSupMorganite   = EnumHelper.addToolMaterial("GemSupMorganite",  mls, 512 * dm, 10.0f + ea, 4.0f + da, 18);
  public static final ToolMaterial toolSupOnyx        = EnumHelper.addToolMaterial("GemSupOnyx",       mls, 384 * dm, 10.0f + ea, 6.0f + da, 14);

  public static final ToolMaterial toolFish           = EnumHelper.addToolMaterial("GemFish", Config.miningLevelFish, 64, 3.0f, 1.0f, 20);
  
  /*
   * Armor
   */
  
  private static final int [] armorRegReduction = {2, 6, 5, 2};
  private static final int [] armorSupReduction = {3, 8, 6, 3};
  
  public static final ArmorMaterial armorRegRuby = EnumHelper.addArmorMaterial("GemArmorRegRuby", 24, armorRegReduction, 10);
  public static final ArmorMaterial armorRegGarnet = EnumHelper.addArmorMaterial("GemArmorRegGarnet", 20, armorRegReduction, 13);
  public static final ArmorMaterial armorRegTopaz = EnumHelper.addArmorMaterial("GemArmorRegTopaz", 20, armorRegReduction, 11);
  public static final ArmorMaterial armorRegHeliodor = EnumHelper.addArmorMaterial("GemArmorRegHeliodor", 16, armorRegReduction, 10);
  public static final ArmorMaterial armorRegPeridot = EnumHelper.addArmorMaterial("GemArmorRegPeridot", 20, armorRegReduction, 14);
  public static final ArmorMaterial armorRegEmerald = EnumHelper.addArmorMaterial("GemArmorRegEmerald", 20, armorRegReduction, 12);
  public static final ArmorMaterial armorRegAquamarine = EnumHelper.addArmorMaterial("GemArmorRegAquamarine", 16, armorRegReduction, 12);
  public static final ArmorMaterial armorRegSapphire = EnumHelper.addArmorMaterial("GemArmorRegSapphire", 24, armorRegReduction, 10);
  public static final ArmorMaterial armorRegIolite = EnumHelper.addArmorMaterial("GemArmorRegIolite", 24, armorRegReduction, 11);
  public static final ArmorMaterial armorRegAmethyst = EnumHelper.addArmorMaterial("GemArmorRegAmethyst", 20, armorRegReduction, 12);
  public static final ArmorMaterial armorRegMorganite = EnumHelper.addArmorMaterial("GemArmorRegMorganite", 20, armorRegReduction, 12);
  public static final ArmorMaterial armorRegOnyx = EnumHelper.addArmorMaterial("GemArmorRegOnyx", 16, armorRegReduction, 8);
  
  public static final ArmorMaterial armorSupRuby = EnumHelper.addArmorMaterial("GemArmorSupRuby", 48, armorSupReduction, 16);
  public static final ArmorMaterial armorSupGarnet = EnumHelper.addArmorMaterial("GemArmorSupGarnet", 40, armorSupReduction, 19);
  public static final ArmorMaterial armorSupTopaz = EnumHelper.addArmorMaterial("GemArmorSupTopaz", 40, armorSupReduction, 17);
  public static final ArmorMaterial armorSupHeliodor = EnumHelper.addArmorMaterial("GemArmorSupHeliodor", 32, armorSupReduction, 16);
  public static final ArmorMaterial armorSupPeridot = EnumHelper.addArmorMaterial("GemArmorSupPeridot", 40, armorSupReduction, 20);
  public static final ArmorMaterial armorSupEmerald = EnumHelper.addArmorMaterial("GemArmorSupEmerald", 40, armorSupReduction, 18);
  public static final ArmorMaterial armorSupAquamarine = EnumHelper.addArmorMaterial("GemArmorSupAquamarine", 32, armorSupReduction, 18);
  public static final ArmorMaterial armorSupSapphire = EnumHelper.addArmorMaterial("GemArmorSupSapphire", 48, armorSupReduction, 16);
  public static final ArmorMaterial armorSupIolite = EnumHelper.addArmorMaterial("GemArmorSupIolite", 48, armorSupReduction, 17);
  public static final ArmorMaterial armorSupAmethyst = EnumHelper.addArmorMaterial("GemArmorSupAmethyst", 40, armorSupReduction, 18);
  public static final ArmorMaterial armorSupMorganite = EnumHelper.addArmorMaterial("GemArmorSupMorganite", 40, armorSupReduction, 18);
  public static final ArmorMaterial armorSupOnyx = EnumHelper.addArmorMaterial("GemArmorSupOnyx", 32, armorSupReduction, 14);
}
