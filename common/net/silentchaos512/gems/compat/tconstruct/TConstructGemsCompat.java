package net.silentchaos512.gems.compat.tconstruct;

import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.lib.EnumGem;
import slimeknights.tconstruct.TinkerIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;

public class TConstructGemsCompat {

  public static void init() {

    SilentGems.logHelper.info("Loading TConstruct compatibility module...");

    for (EnumGem gem : EnumGem.values())
      register(gem, EnumMaterialTier.REGULAR);
    for (EnumGem gem : EnumGem.values())
      register(gem, EnumMaterialTier.SUPER);
  }

  private static void register(EnumGem gem, EnumMaterialTier tier) {

    TConstructMaterialGem mat = new TConstructMaterialGem(gem, tier);
    TinkerIntegration.integrate(mat,
        tier == EnumMaterialTier.SUPER ? gem.getItemSuperOreName() : gem.getItemOreName());

    int durability = gem.getDurability(tier);
    float miningSpeed = gem.getMiningSpeed(tier);
    float meleeDamage = gem.getMeleeDamage(tier);
    float meleeSpeed = gem.getMeleeSpeed(tier);
    int harvestLevel = gem.getHarvestLevel(tier);
    float drawDelay = Math.max(38.4f - 1.4f * meleeSpeed * miningSpeed, 10);

    TinkerRegistry.addMaterialStats(mat, new HeadMaterialStats(durability, miningSpeed, meleeDamage, harvestLevel));
    TinkerRegistry.addMaterialStats(mat, new HandleMaterialStats(0.875f, durability / 8));
    TinkerRegistry.addMaterialStats(mat, new ExtraMaterialStats(durability / 8));
    TinkerRegistry.addMaterialStats(mat, new BowMaterialStats(20f / drawDelay, 1f, 0.4f * meleeDamage - 1));
    //TinkerRegistry.addMaterialStats(mat, new BowStringMaterialStats(1f));
    TinkerRegistry.addMaterialStats(mat, new ArrowShaftMaterialStats(1.0f, 0));
    //TinkerRegistry.addMaterialStats(mat, new FletchingMaterialStats(1f, 1f));
    //TinkerRegistry.addMaterialStats(mat, new ProjectileMaterialStats());
  }
}
