package net.silentchaos512.gems.lib.part;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartFlint extends ToolPartMain {

  public ToolPartFlint() {

    super(SilentGems.MOD_ID + ":flint", new ItemStack(Items.FLINT));
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MOD_ID + ":" + name.toLowerCase() + "/" + name;

    switch (pos) {
      case HEAD_LEFT:
        name += "FlintL";
        break;
      case HEAD_MIDDLE:
        name += "Flint";
        break;
      case HEAD_RIGHT:
        name += "FlintR";
        break;
      case ROD_DECO:
        name += "DecoFlint";
        break;
      default:
        return null;
    }

    return new ModelResourceLocation(name, "inventory");
  }

  @Override
  public int getDurability() {

    return 128;
  }

  @Override
  public float getHarvestSpeed() {

    return 5.0f;
  }

  @Override
  public int getHarvestLevel() {

    return 1;
  }

  @Override
  public float getMeleeDamage() {

    return 1.5f;
  }

  @Override
  public float getMagicDamage() {

    return 0.0f;
  }

  @Override
  public int getEnchantability() {

    return 8;
  }

  @Override
  public float getMeleeSpeed() {

    return 1.1f;
  }

  @Override
  public float getChargeSpeed() {

    return 0.5f;
  }

  @Override
  public int getProtection() {

    return 3;
  }

  @Override
  public EnumMaterialTier getTier() {

    return EnumMaterialTier.MUNDANE;
  }
}
