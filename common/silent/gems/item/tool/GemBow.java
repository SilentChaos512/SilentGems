package silent.gems.item.tool;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import silent.gems.SilentGems;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.lib.Names;
import silent.gems.lib.Reference;

public class GemBow extends ItemBow implements IHasVariants {

  private final int gemId;

  public GemBow(ToolMaterial toolMaterial, int gemId) {

    super();
    this.gemId = gemId;
    this.setMaxDamage(toolMaterial.getMaxUses());
    addRecipe(new ItemStack(this), gemId);
    this.setCreativeTab(SilentGems.tabSilentGems);
  }
  
  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public String getName() {

//    return "Bow" + gemId + (supercharged ? "Plus" : "");
    return "Bow" + gemId;
  }

  @Override
  public String getFullName() {

    return Reference.MOD_ID + ":" + getName();
  }

  public static void addRecipe(ItemStack tool, int gemId) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ROD), 1, gemId);
    GameRegistry.addShapedRecipe(tool, " rs", "r s", " rs", 'r', material, 's',
        CraftingMaterial.getStack(Names.GILDED_STRING));
  }

//  @Override
//  public IIcon getIcon(ItemStack stack, int pass) {
//
//    return super.getIcon(stack, pass);
//    // TODO Fancy decorations!
//  }

//  @Override
//  public int getRenderPasses(int meta) {
//
//    // TODO: How many?
//    return 6;
//  }

//  @Override
//  public boolean requiresMultipleRenderPasses() {
//
//    return true;
//  }

  @Override
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    ItemStack material = new ItemStack(SRegistry.getItem(Names.GEM_ITEM), 1, gemId);
    if (material.getItem() == stack2.getItem()
        && material.getItemDamage() == stack2.getItemDamage()) {
      return true;
    } else {
      return super.getIsRepairable(stack1, stack2);
    }
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Bow" + gemId;
  }

//  @Override
//  public void registerIcons(IIconRegister reg) {
//
//    // TODO register icons!
//
//    String s = Strings.RESOURCE_PREFIX + "Bow";
//
//    super.registerIcons(reg); // temp
//  }

  public int getGemId() {

    return gemId;
  }
}
