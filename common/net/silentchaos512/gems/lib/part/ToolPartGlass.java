package net.silentchaos512.gems.lib.part;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartGlass extends ToolPartMain {

  public ToolPartGlass() {

    super(SilentGems.MOD_ID + ":glass", new ItemStack(Blocks.GLASS));
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos, int frame) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MOD_ID + ":" + name.toLowerCase() + "/" + name;

    switch (pos) {
      case HEAD_LEFT:
        name += "GlassL";
        break;
      case HEAD_MIDDLE:
        name += "Glass";
        break;
      case HEAD_RIGHT:
        name += "GlassR";
        break;
      case ROD_DECO:
        name += "DecoGlass";
        break;
      default:
        return null;
    }

    return new ModelResourceLocation(name, "inventory");
  }

  @Override
  public int getDurability() {

    return 92;
  }

  @Override
  public float getHarvestSpeed() {

    return 6.0f;
  }

  @Override
  public int getHarvestLevel() {

    return 1;
  }

  @Override
  public float getMeleeDamage() {

    return 2.5f;
  }

  @Override
  public float getMagicDamage() {

    return 0.0f;
  }

  @Override
  public int getEnchantability() {

    return 11;
  }

  @Override
  public float getMeleeSpeed() {

    return 0.8f;
  }

  @Override
  public float getChargeSpeed() {

    return 1.0f;
  }

  @Override
  public EnumMaterialTier getTier() {

    return EnumMaterialTier.MUNDANE;
  }
}
