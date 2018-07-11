package net.silentchaos512.gems.lib.part;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.IArmor;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartMain;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.tool.ItemGemBow;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.gems.lib.EnumGem;
import net.silentchaos512.gems.lib.Names;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.StackHelper;

public class ToolPartGem extends ToolPartMain {

  EnumGem gem;
  Map<String, ModelResourceLocation> modelMap = Maps.newHashMap();

  public ToolPartGem(EnumGem gem, boolean supercharged) {

    super(SilentGems.RESOURCE_PREFIX + gem.name().toLowerCase() + (supercharged ? "_super" : ""),
        supercharged ? gem.getItemSuper() : gem.getItem());
    this.craftingOreDictName = supercharged ? gem.getItemSuperOreName() : gem.getItemOreName();
    this.gem = gem;
    this.tier = supercharged ? EnumMaterialTier.SUPER : EnumMaterialTier.REGULAR;
  }

  public EnumGem getGem() {

    return gem;
  }

  @Override
  public boolean matchesForDecorating(ItemStack partRep, boolean matchOreDict) {

    // Allow shards to be used for decorating.
    if (partRep.getItem() == ModItems.gemShard && partRep.getItemDamage() == gem.ordinal())
      return true;
    if (matchOreDict && StackHelper.matchesOreDict(partRep, gem.getShardOreName()))
      return true;

    return super.matchesForDecorating(partRep, matchOreDict);
  }

  @Override
  public int getRepairAmount(ItemStack toolOrArmor, ItemStack partRep) {

    if (partRep.getItem() == ModItems.gemShard)
      return super.getRepairAmount(toolOrArmor, gem.getItem()) / 10;

    return super.getRepairAmount(toolOrArmor, partRep);
  }

  @Override
  public int getColor(ItemStack toolOrArmor, IPartPosition position, int animationFrame) {

    Item item = toolOrArmor.getItem();
    boolean isTextureUncolored = position == ToolPartPosition.ROD_DECO || item instanceof IArmor
        || item instanceof ItemGemShield || item instanceof ItemGemBow;
    if (isTextureUncolored || ToolHelper.isBroken(toolOrArmor)) {
      return gem.getColor();
    }
    return 0xFFFFFF;
  }

  @Override
  public String getDisplayName(ItemStack stack) {

    if (stack.hasDisplayName()
        || (stack.getItem() != ModItems.gem && stack.getItem() != ModItems.gemSuper)) {
      return stack.getDisplayName();
    }

    return SilentGems.localizationHelper.getLocalizedString("item",
        Names.GEM + stack.getItemDamage() + ".name");
  }

  @Override
  public String getDisplayNamePrefix(ItemStack stack) {

    return tier == EnumMaterialTier.SUPER
        ? SilentGems.instance.localizationHelper.getItemSubText(Names.GEM, "superPrefix") : "";
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, ToolPartPosition pos, int frame) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.RESOURCE_PREFIX + name + "/" + name;
    String gemNum = tool.getItem() instanceof ItemGemBow ? "" : "" + gem.ordinal();
    String frameNum = frame == 3 ? "_3" : "";

    switch (pos) {
      case HEAD:
        name += gemNum + frameNum;
        break;
      case ROD_DECO:
        name += "_deco";
        break;
      default:
        return null;
    }

    if (modelMap.containsKey(name)) {
      return modelMap.get(name);
    }

    name = name.toLowerCase();
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
