package net.silentchaos512.gems.lib.part;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartGem extends ToolPartMain {

  EnumGem gem;
  Map<String, ModelResourceLocation> modelMap = Maps.newHashMap();

  public ToolPartGem(EnumGem gem, boolean supercharged) {

    super(SilentGems.MOD_ID + ":" + gem.name().toLowerCase() + (supercharged ? "_super" : ""),
        supercharged ? gem.getItemSuper() : gem.getItem());
    this.craftingOreDictName = supercharged ? gem.getItemSuperOreName() : gem.getItemOreName();
    this.gem = gem;
    this.tier = supercharged ? EnumMaterialTier.SUPER : EnumMaterialTier.REGULAR;
  }

  public EnumGem getGem() {

    return gem;
  }

  @Override
  public int getColor(ItemStack toolOrArmor) {

    Item item = toolOrArmor.getItem();
    if (item instanceof IArmor || item instanceof ItemGemShield || item instanceof ItemGemBow)
      return gem.getColor();
    else
      return gem.ordinal() > 15 ? ToolRenderHelper.DARK_GEM_SHADE : 0xFFFFFF;
  }

  @Override
  public String getDisplayName(ItemStack stack) {

    if (stack.hasDisplayName() || stack.getItem() != ModItems.gem)
      return stack.getDisplayName();

    return SilentGems.localizationHelper.getLocalizedString("item",
        Names.GEM + (stack.getItemDamage() & 0x1F) + ".name");
  }

  @Override
  public String getDisplayNamePrefix(ItemStack stack) {

    return tier == EnumMaterialTier.SUPER
        ? SilentGems.instance.localizationHelper.getItemSubText(Names.GEM, "superPrefix") : "";
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos, int frame) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MOD_ID + ":" + name.toLowerCase() + "/" + name;
    String gemNum = tool.getItem() instanceof ItemGemBow ? "" : "" + gem.ordinal();
    String frameNum = frame == 3 ? "_3" : "";

    switch (pos) {
      case HEAD_LEFT:
        name += gemNum + "L" + frameNum;
        break;
      case HEAD_MIDDLE:
        name += gemNum + frameNum;
        break;
      case HEAD_RIGHT:
        name += gemNum + "R" + frameNum;
        break;
      case ROD_DECO:
        name += "Deco" + gemNum;
        break;
      default:
        return null;
    }

    if (modelMap.containsKey(name)) {
      return modelMap.get(name);
    }

    ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
    modelMap.put(name, model);
    return model;
  }

  @Override
  public int getDurability() {

    return gem.getDurability(tier);
  }

  @Override
  public float getHarvestSpeed() {

    return gem.getMiningSpeed(tier);
  }

  @Override
  public int getHarvestLevel() {

    // TODO: Configs!
    return tier == EnumMaterialTier.SUPER ? 4 : 2;
  }

  @Override
  public float getMeleeDamage() {

    return gem.getMeleeDamage(tier);
  }

  @Override
  public float getMagicDamage() {

    return gem.getMagicDamage(tier);
  }

  @Override
  public int getEnchantability() {

    return gem.getEnchantability(tier);
  }

  @Override
  public float getMeleeSpeed() {

    return gem.getMeleeSpeed(tier);
  }

  @Override
  public float getChargeSpeed() {

    return gem.getChargeSpeed(tier);
  }

  @Override
  public float getProtection() {

    return gem.getProtection(tier);
  }

  @Override
  public EnumMaterialTier getTier() {

    return tier;
  }
}
