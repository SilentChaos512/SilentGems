package net.silentchaos512.gems.api.tool.part;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;

public abstract class ToolPart {

  @SideOnly(Side.CLIENT)
  protected ModelResourceLocation model;
  protected String key;
  protected ItemStack craftingStack;
  protected String craftingOreDictName;
  protected EnumMaterialTier tier;

  public ToolPart(String key, ItemStack craftingStack) {

    this(key, craftingStack, "");
  }

  public ToolPart(String key, String craftingOreDictName) {

    this(key, null, craftingOreDictName);
  }

  protected ToolPart(String key, ItemStack craftingStack, String craftingOreDictName) {

    this.key = key;
    this.craftingStack = craftingStack;
    this.craftingOreDictName = craftingOreDictName;
    this.tier = EnumMaterialTier.REGULAR;
  }

  public String getKey() {

    return key;
  }

  public ItemStack getCraftingStack() {

    return craftingStack;
  }

  public String getCraftingOreDictName() {

    return craftingOreDictName;
  }

  public EnumMaterialTier getTier() {

    return tier;
  }

  public int getRepairAmount(ItemStack tool) {

    return 0;
  }

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    return model;
  }

  @SideOnly(Side.CLIENT)
  public void setModel(String name) {

    model = new ModelResourceLocation(name, "inventory");
  }

  public abstract int getDurability();

  public abstract float getHarvestSpeed();

  public abstract int getHarvestLevel();

  public abstract float getMeleeDamage();

  public abstract float getMagicDamage();

  public abstract int getEnchantability();

  public abstract float getMeleeSpeed();

  public abstract float getChargeSpeed();

  public abstract boolean validForToolOfTier(EnumMaterialTier toolTier);

  public abstract boolean validForPosition(EnumPartPosition pos);
}
