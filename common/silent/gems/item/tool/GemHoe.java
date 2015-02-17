package silent.gems.item.tool;

import net.minecraft.init.Items;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.registry.SRegistry;
import silent.gems.core.util.LocalizationHelper;
import silent.gems.item.CraftingMaterial;
import silent.gems.item.ModItems;
import silent.gems.lib.Names;
import silent.gems.material.ModMaterials;

public class GemHoe extends ItemHoe implements IAddRecipe, IHasVariants {

  private int gemId;
  private boolean supercharged;

  public GemHoe(ToolMaterial toolMaterial, int gemId, boolean supercharged) {

    super(toolMaterial);

    this.gemId = gemId;
    this.supercharged = supercharged;
    this.setMaxDamage(toolMaterial.getMaxUses());
    this.setCreativeTab(SilentGems.tabSilentGems);
  }

  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public String getName() {

    return "Hoe" + gemId + (supercharged ? "Plus" : "");
  }

  @Override
  public String getFullName() {

    return SilentGems.MOD_ID + ":" + getName();
  }

  @Override
  public void addRecipes() {

    ItemStack tool = new ItemStack(this);
    ItemStack material = new ItemStack(ModItems.gem, 1, gemId + (supercharged ? 16 : 0));

    // Fish tools
    if (gemId == ModMaterials.FISH_GEM_ID) {
      material = new ItemStack(Items.fish);
    }

    if (supercharged) {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg", " s", " s", 'g',
          material, 's', CraftingMaterial.getStack(Names.ORNATE_STICK) }));
    } else {
      GameRegistry.addRecipe(new ShapedOreRecipe(tool, true, new Object[] { "gg", " s", " s", 'g',
          material, 's', "stickWood" }));
    }
  }

  @Override
  public void addOreDict() {

  }

  public int getGemId() {

    return gemId;
  }

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

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.TOOL_PREFIX + "Hoe" + gemId + (supercharged ? "Plus" : "");
  }
}
