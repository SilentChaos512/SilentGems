package net.silentchaos512.gems.item.armor;

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
import cpw.mods.fml.common.registry.GameRegistry;

public class ArmorSG extends ItemArmor implements IAddRecipe {

  public final static ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton", 4,
      new int[] { 1, 2, 2, 1 }, 17);

  private String itemName;
  private ItemStack craftingItem;

  public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name) {

    this(material, renderIndex, armorType, name, new ItemStack(ModItems.fluffyPuff));
  }

  public ArmorSG(ArmorMaterial material, int renderIndex, int armorType, String name,
      ItemStack craftingItem) {

    super(material, renderIndex, armorType);

    this.craftingItem = craftingItem;
    this.setCreativeTab(SilentGems.tabSilentGems);
    this.setUnlocalizedName(name);
  }

  @Override
  public void addRecipes() {

    ItemStack result = new ItemStack(this);
    switch (this.armorType) {
      case 0:
        GameRegistry.addShapedRecipe(result, "mmm", "m m", 'm', this.craftingItem);
        break;
      case 1:
        GameRegistry.addShapedRecipe(result, "m m", "mmm", "mmm", 'm', this.craftingItem);
        break;
      case 2:
        GameRegistry.addShapedRecipe(result, "mmm", "m m", "m m", 'm', this.craftingItem);
        break;
      case 3:
        GameRegistry.addShapedRecipe(result, "m m", "m m", 'm', this.craftingItem);
        break;
    }
  }

  @Override
  public void addOreDict() {

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
  public void registerIcons(IIconRegister reg) {

    itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + this.itemName);
  }

  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {

    return Strings.RESOURCE_PREFIX + "textures/armor/" + this.itemName + "_"
        + (this.armorType == 2 ? "2" : "1") + ".png";
  }
}
