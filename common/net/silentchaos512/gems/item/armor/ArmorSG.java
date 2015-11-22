package net.silentchaos512.gems.item.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.registry.IHasVariants;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class ArmorSG extends ItemArmor implements IAddRecipe, IHasVariants {

  public static final ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton",
      "cotton", 4, new int[] { 1, 2, 2, 1 }, 17);

  private String itemName;
  private String textureName;
  private ItemStack craftingItem;

  public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name) {

    this(material, renderIndex, armorType, name, "FluffyArmor",
        CraftingMaterial.getStack(Names.FLUFFY_FABRIC));
  }

  public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name,
      String textureName, ItemStack craftingItem) {

    super(material, renderIndex, armorType);

    this.textureName = textureName;
    this.craftingItem = craftingItem;
    this.setCreativeTab(SilentGems.tabSilentGems);
    this.setUnlocalizedName(name);
  }

  @Override
  public void addRecipes() {

    ItemStack result = new ItemStack(this);
    switch (this.armorType) {
      case 0:
        GameRegistry.addShapedRecipe(result, "mmm", "m m", 'm', craftingItem);
        break;
      case 1:
        GameRegistry.addShapedRecipe(result, "m m", "mmm", "mmm", 'm', craftingItem);
        break;
      case 2:
        GameRegistry.addShapedRecipe(result, "mmm", "m m", "m m", 'm', craftingItem);
        break;
      case 3:
        GameRegistry.addShapedRecipe(result, "m m", "m m", 'm', craftingItem);
        break;
    }
  }

  @Override
  public void addOreDict() {

  }

  @Override
  public String getName() {

    return itemName;
  }

  @Override
  public String getFullName() {

    return Names.convert(SilentGems.MOD_ID + ":" + itemName);
  }

  @Override
  public String[] getVariantNames() {

    return new String[] { getFullName() };
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {

    return LocalizationHelper.ITEM_PREFIX + itemName;
  }

  @Override
  public Item setUnlocalizedName(String name) {

    this.itemName = name;
    return super.setUnlocalizedName(name);
  }

  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {

    return Strings.RESOURCE_PREFIX + "textures/armor/" + textureName + "_"
        + (armorType == 2 ? "2" : "1") + ".png";
  }
}
