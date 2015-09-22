package net.silentchaos512.gems.item.armor;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Strings;

public class ArmorSG extends ItemArmor implements IAddRecipe {

  public static final ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton", 4,
      new int[] { 1, 2, 2, 1 }, 17);

  private String itemName;
  private String textureName;
  private ItemStack craftingItem;

  public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name) {

    this(material, renderIndex, armorType, name, "FluffyArmor", new ItemStack(ModItems.fluffyPuff));
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

//  @Override
//  public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
//
//    if (this.craftingItem.getItem() instanceof Gem) {
//      list.add(LocalizationHelper.getMiscText("ArmorTextures"));
//    }
//  }

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
  public void registerIcons(IIconRegister reg) {

    String s = this.itemName.replaceFirst("Plus$", "");
    itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + s);
  }

  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {

    return Strings.RESOURCE_PREFIX + "textures/armor/" + this.textureName + "_"
        + (this.armorType == 2 ? "2" : "1") + ".png";
  }
}
