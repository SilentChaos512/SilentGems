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
import net.silentchaos512.gems.item.Gem;
import net.silentchaos512.gems.lib.EnumMaterialClass;
import net.silentchaos512.gems.lib.IGemItem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class ArmorSG extends ItemArmor implements IAddRecipe, IHasVariants, IGemItem {

  public static final ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton",
      "cotton", 6, new int[] { 1, 3, 2, 2 }, 17);

  private String itemName;
  private String textureName;
  private ItemStack craftingItem;
  private int gemId;
  private boolean supercharged;

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

    this.gemId = craftingItem.getItem() instanceof Gem ? craftingItem.getItemDamage() & 0xF : -1;
    this.supercharged = craftingItem.getItem() instanceof Gem ? craftingItem.getItemDamage() > 15
        : false;
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
  public boolean getIsRepairable(ItemStack stack1, ItemStack stack2) {

    return stack2.getItem() == craftingItem.getItem()
        && stack2.getItemDamage() == craftingItem.getItemDamage();
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

  @Override
  public int getGemId(ItemStack stack) {

    return gemId;
  }

  @Override
  public boolean isSupercharged(ItemStack stack) {

    return supercharged;
  }

  @Override
  public EnumMaterialClass getGemMaterialClass(ItemStack stack) {

    return gemId < 0 ? EnumMaterialClass.MUNDANE
        : (supercharged ? EnumMaterialClass.SUPERCHARGED : EnumMaterialClass.REGULAR);
  }
}
