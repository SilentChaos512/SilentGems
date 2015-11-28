package net.silentchaos512.gems.item.armor;

import org.lwjgl.opengl.ARBTextureMirroredRepeat;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.EnumHelper;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.renderers.tool.ToolRenderHelper;
import net.silentchaos512.gems.core.registry.IAddRecipe;
import net.silentchaos512.gems.core.util.LocalizationHelper;
import net.silentchaos512.gems.item.CraftingMaterial;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.lib.Strings;

public class ArmorSG extends ItemArmor implements IAddRecipe {

  public static final ArmorMaterial materialCotton = EnumHelper.addArmorMaterial("gemsCotton", 6,
      new int[] { 1, 3, 2, 2 }, 17);

  private String itemName;
  private String textureName;
  private ItemStack craftingItem;
  private IIcon extraIcon = null;

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
  public boolean hasColor(ItemStack stack) {

    return getArmorMaterial() != materialCotton ? false
        : (!stack.hasTagCompound() ? false
            : (!stack.getTagCompound().hasKey("display", 10) ? false
                : stack.getTagCompound().getCompoundTag("display").hasKey("color", 3)));
  }

  @Override
  public int getColor(ItemStack stack) {

    if (getArmorMaterial() != materialCotton) {
      return -1;
    } else {
      NBTTagCompound nbttagcompound = stack.getTagCompound();

      if (nbttagcompound == null) {
        return 0xFFFF;
      } else {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
        return nbttagcompound1 == null ? 0xFFFFFF
            : (nbttagcompound1.hasKey("color", 3) ? nbttagcompound1.getInteger("color") : 0xFFFFFF);
      }
    }
  }

  @Override
  public void removeColor(ItemStack stack) {

    if (getArmorMaterial() == materialCotton) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();

      if (nbttagcompound != null) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

        if (nbttagcompound1.hasKey("color")) {
          nbttagcompound1.removeTag("color");
        }
      }
    }
  }

  @Override
  public void func_82813_b(ItemStack stack, int color) {

    if (getArmorMaterial() != materialCotton) {
      return;
    } else {
      NBTTagCompound nbttagcompound = stack.getTagCompound();

      if (nbttagcompound == null) {
        nbttagcompound = new NBTTagCompound();
        stack.setTagCompound(nbttagcompound);
      }

      NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

      if (!nbttagcompound.hasKey("display", 10)) {
        nbttagcompound.setTag("display", nbttagcompound1);
      }

      nbttagcompound1.setInteger("color", color);
    }
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
  public boolean requiresMultipleRenderPasses() {

    return extraIcon != null || getArmorMaterial() == materialCotton;
  }

  @Override
  public int getRenderPasses(int meta) {

    return extraIcon == null || getArmorMaterial() != materialCotton ? 1 : 2;
  }

  @Override
  public IIcon getIcon(ItemStack stack, int pass) {
    
    if (pass == 1) {
      return extraIcon != null ? extraIcon : ToolRenderHelper.instance.iconBlank;
    } else {
      return itemIcon;
    }
  }

  @Override
  public void registerIcons(IIconRegister reg) {

    String s = itemName.replaceFirst("Plus$", "");
    itemIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + s);
    if (itemName.contains("Plus")) {
      switch (armorType) {
        case 0:
          extraIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + "HelmetExtra");
          break;
        case 1:
          extraIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + "ChestplateExtra");
          break;
        case 2:
          extraIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + "LeggingsExtra");
          break;
        case 3:
          extraIcon = reg.registerIcon(Strings.RESOURCE_PREFIX + "BootsExtra");
          break;
      }
    }
  }

  @Override
  public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {

    return Strings.RESOURCE_PREFIX + "textures/armor/" + textureName + "_"
        + (armorType == 2 ? "2" : "1") + ".png";
  }
}
