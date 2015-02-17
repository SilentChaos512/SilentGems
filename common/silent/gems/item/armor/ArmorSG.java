package silent.gems.item.armor;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import silent.gems.SilentGems;
import silent.gems.core.registry.IAddRecipe;
import silent.gems.core.registry.IHasVariants;
import silent.gems.core.util.LocalizationHelper;

public class ArmorSG extends ItemArmor implements IAddRecipe, IHasVariants {

  public final static ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton",
      "cotton", 4, new int[] { 1, 2, 2, 1 }, 17);
  // public final static ArmorMaterial materialHeadphones = EnumHelper.addArmorMaterial("headphones", 12, new int[] { 5,
  // 1, 2, 0 }, 20);

  private final String itemName;

  public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name) {

    super(material, renderIndex, armorType);

    itemName = name;
    setCreativeTab(SilentGems.tabSilentGems);
    setUnlocalizedName(name);
  }

  @Override
  public String getName() {

    return itemName;
  }

  @Override
  public String getFullName() {

    return SilentGems.MOD_ID + ":" + itemName;
  }

  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public void addRecipes() {

    if (this.getArmorMaterial() == materialCotton) {
      addArmorRecipe("materialCotton", this.armorType);
    }
  }

  private void addArmorRecipe(Object material, int armorType) {

    if (armorType == 0) {
      GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "mmm", "m m", 'm',
          material }));
    } else if (armorType == 1) {
      GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "m m", "mmm", "mmm",
          'm', material }));
    } else if (armorType == 2) {
      GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "mmm", "m m", "m m",
          'm', material }));
    } else if (armorType == 3) {
      GameRegistry.addRecipe(new ShapedOreRecipe(this, true, new Object[] { "m m", "m m", 'm',
          material }));
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.ITEM_PREFIX + itemName;
  }
}
