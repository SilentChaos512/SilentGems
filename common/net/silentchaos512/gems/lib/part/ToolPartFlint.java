package net.silentchaos512.gems.lib.part;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.lib.registry.IRegistryObject;
import slimeknights.tconstruct.library.utils.ToolHelper;

public class ToolPartFlint extends ToolPartMain {

  public static final int COLOR = 0x424242;

  public ToolPartFlint() {

    super(SilentGems.MODID + ":flint", new ItemStack(Items.FLINT));
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos, int frame) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.RESOURCE_PREFIX + name + "/" + name;
    String mat = tool.getItem() instanceof ItemGemBow ? "" : "flint";
    String frameNum = frame == 3 ? "_3" : "";

    switch (pos) {
      case HEAD:
        name += mat + frameNum;
        break;
      case ROD_DECO:
        name += "deco" + mat;
        break;
      default:
        return null;
    }

    name = name.toLowerCase();
    return new ModelResourceLocation(name, "inventory");
  }

  @Override
  public int getColor(ItemStack toolOrArmor) {

    Item item = toolOrArmor.getItem();
    return item instanceof IArmor || item instanceof ItemGemBow || item instanceof ItemGemShield || ToolHelper.isBroken(toolOrArmor)
        ? COLOR : 0xFFFFFF;
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
  public float getProtection() {

    return 3;
  }

  @Override
  public EnumMaterialTier getTier() {

    return EnumMaterialTier.MUNDANE;
  }
}
