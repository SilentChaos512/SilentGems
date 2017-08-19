package net.silentchaos512.gems.compat.tconstruct;

import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.lib.EnumGem;
import slimeknights.mantle.util.RecipeMatch.Match;
import slimeknights.tconstruct.library.materials.Material;

public class TConstructMaterialGem extends Material {

  EnumGem gem;
  EnumMaterialTier tier;

  public TConstructMaterialGem(EnumGem gem, EnumMaterialTier tier) {

    super("silentgems:" + gem.name().toLowerCase() + (tier == EnumMaterialTier.SUPER ? "_super" : ""), gem.getColor());
    this.gem = gem;
    this.tier = tier;

//    setCraftable(true);
//    addCommonItems(tier == EnumMaterialTier.SUPER ? gem.getItemSuperOreName() : gem.getItemOreName());
//    addItem(tier == EnumMaterialTier.SUPER ? gem.getItemSuper() : gem.getItem(), VALUE_Gem, VALUE_Gem);
//    setRepresentativeItem(tier == EnumMaterialTier.SUPER ? gem.getItemSuper() : gem.getItem());
  }

  @Override
  public String getLocalizedName() {

    int meta = gem.ordinal() + (tier == EnumMaterialTier.SUPER ? 32 : 0); 
    return SilentGems.localizationHelper.getLocalizedString("item", "Gem" + meta + ".name");
  }
}
