package silent.gems.item.tool;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;
import silent.gems.material.ModMaterials;

public class GemSword extends ItemSword implements IHasVariants {

  private final int gemId;
  private final boolean supercharged;
  private final ToolMaterial toolMaterial;
//  private IIcon iconRod;
//  private IIcon iconHead;

//  public static IIcon iconBlank = null;
//  public static IIcon[] iconToolDeco = null;
//  public static IIcon[] iconToolRod = null;
//  public static IIcon[] iconToolHeadL = null;
//  public static IIcon[] iconToolHeadM = null;
//  public static IIcon[] iconToolHeadR = null;

  public GemSword(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);
    this.gemId = gemId;
    this.supercharged = supercharged;
    this.toolMaterial = toolMaterial;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId, supercharged);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }
  
  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public String getName() {

    return "Sword" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public String getFullName() {

    return Reference.MOD_ID + ":" + getName();
  }

  public static void addRecipe(ItemStack tool, int gemId, boolean supercharged) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId
        + (supercharged ? 16 : 0));

    // Fish tools
    if (gemId == ModMaterials.FISH_GEM_ID) {
      material = new ItemStack(Items.fish);
    }

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "g", "g", "s", 'g',
          material, 's', new ItemStack(SRegistry.getItem(Names.CRAFTING_MATERIALS), 1, 0) }));
    } else {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "g", "g", "s", 'g',
          material, 's', "stickWood" }));
    }
  }

  public int getGemId() {

    return gemId;
  }

//  @Override
//  public IIcon getIcon(ItemStack stack, int pass) {
//
//    if (pass == 0) {
//      // Rod
//      return iconRod;
//    } else if (pass == 1) {
//      // HeadM
//      if (stack.stackTagCompound != null
//          && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_MIDDLE)) {
//        byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_MIDDLE);
//        if (b >= 0 && b < iconToolHeadM.length) {
//          return iconToolHeadM[b];
//        }
//      }
//      return iconHead;
//    } else if (pass == 2) {
//      // HeadL
//      if (stack.stackTagCompound != null
//          && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_LEFT)) {
//        byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_LEFT);
//        if (b >= 0 && b < iconToolHeadL.length) {
//          return iconToolHeadL[b];
//        }
//      }
//      return iconBlank;
//    } else if (pass == 3) {
//      // HeadR
//      if (stack.stackTagCompound != null
//          && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_HEAD_RIGHT)) {
//        byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_HEAD_RIGHT);
//        if (b >= 0 && b < iconToolHeadR.length) {
//          return iconToolHeadR[b];
//        }
//      }
//      return iconBlank;
//    } else if (pass == 4) {
//      // Rod decoration
//      if (supercharged) {
//        if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_DECO)) {
//          byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_DECO);
//          if (b >= 0 && b < iconToolDeco.length - 1) {
//            return iconToolDeco[b];
//          }
//        }
//        return iconToolDeco[iconToolDeco.length - 1];
//      }
//      return iconBlank;
//    } else if (pass == 5) {
//      // Rod wool
//      if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey(Strings.TOOL_ICON_ROD)) {
//        byte b = stack.stackTagCompound.getByte(Strings.TOOL_ICON_ROD);
//        if (b >= 0 && b < iconToolRod.length) {
//          return iconToolRod[b];
//        }
//      }
//      return iconBlank;
//    } else {
//      return iconBlank;
//    }
//  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId
        + (supercharged ? 16 : 0));
    if (material.getItem() == stack2.getItem()
        && material.getItemDamage() == stack2.getItemDamage()) {
      return true;
    } else {
      return super.getIsRepairable(stack1, stack2);
    }
  }

//  @Override
//  public int getRenderPasses(int meta) {
//
//    return 6;
//  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Sword" + gemId + (supercharged ? "Plus" : "");
  }

//  @Override
//  public void registerIcons(IIconRegister iconRegister) {
//
//    String s = Strings.RESOURCE_PREFIX + "Sword";
//
//    if (supercharged) {
//      iconRod = iconRegister.registerIcon(s + "_RodOrnate");
//    } else {
//      iconRod = iconRegister.registerIcon(s + "_RodNormal");
//    }
//
//    s += gemId;
//
//    iconHead = iconRegister.registerIcon(s);
//
//    // Deco
//    String str = Strings.RESOURCE_PREFIX + "SwordDeco";
//    iconToolDeco = new IIcon[EnumGem.all().length + 1];
//    for (int i = 0; i < EnumGem.all().length; ++i) {
//      iconToolDeco[i] = iconRegister.registerIcon(str + i);
//    }
//    iconToolDeco[iconToolDeco.length - 1] = iconRegister.registerIcon(str);
//
//    // Rod
//    str = Strings.RESOURCE_PREFIX + "SwordWool";
//    iconToolRod = new IIcon[16];
//    for (int i = 0; i < 16; ++i) {
//      iconToolRod[i] = iconRegister.registerIcon(str + i);
//    }
//
//    // Blank texture
//    iconBlank = iconRegister.registerIcon(Strings.RESOURCE_PREFIX + "Blank");
//
//    // HeadL
//    str = Strings.RESOURCE_PREFIX + "Sword";
//    iconToolHeadL = new IIcon[EnumGem.all().length];
//    for (int i = 0; i < EnumGem.all().length; ++i) {
//      iconToolHeadL[i] = iconRegister.registerIcon(str + i + "L");
//    }
//    // HeadM
//    iconToolHeadM = new IIcon[EnumGem.all().length];
//    for (int i = 0; i < EnumGem.all().length; ++i) {
//      iconToolHeadM[i] = iconRegister.registerIcon(str + i);
//    }
//    // HeadR
//    iconToolHeadR = new IIcon[EnumGem.all().length];
//    for (int i = 0; i < EnumGem.all().length; ++i) {
//      iconToolHeadR[i] = iconRegister.registerIcon(str + i + "R");
//    }
//  }

//  @Override
//  public boolean requiresMultipleRenderPasses() {
//
//    return true;
//  }
}
