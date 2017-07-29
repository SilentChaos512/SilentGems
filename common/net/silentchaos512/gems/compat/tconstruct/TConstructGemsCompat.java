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
import slimeknights.tconstruct.library.materials.Material;

public class TConstructGemsCompat {

  public static void init() {

    SilentGems.logHelper.info("Loading TConstruct compatibility module...");

    try {
      for (EnumGem gem : EnumGem.values())
        register(gem, EnumMaterialTier.REGULAR);
      for (EnumGem gem : EnumGem.values())
        register(gem, EnumMaterialTier.SUPER);
    } catch (NoSuchMethodError ex) {
      SilentGems.logHelper.info("Failed to load TConstruct module. Are Tinkers tools disabled?");
      ex.printStackTrace();
    } catch (Exception ex) {
      SilentGems.logHelper.info("Unknown error while loading TConstruct module.");
      ex.printStackTrace();
    }
  }

  private static Material register(EnumGem gem, EnumMaterialTier tier) {

    Material material = new TConstructMaterialGem(gem, tier);

    String oreRequirement = tier == EnumMaterialTier.SUPER ? gem.getItemSuperOreName()
        : gem.getItemOreName();
    String oreSuffix = gem.getGemName() + (tier == EnumMaterialTier.SUPER ? "Super" : "");
    MaterialIntegrationGems integration = new MaterialIntegrationGems(material, oreSuffix,
        oreRequirement);

    integration.setRepresentativeItem(oreRequirement);
    integration.integrate();
    TinkerRegistry.integrate(integration);

    material.addItemIngot(oreRequirement); // FIXME?
    material.setCraftable(true);

    // TConstructMaterialGem mat = new TConstructMaterialGem(gem, tier);
    // TinkerRegistry.addMaterial(mat);
    // TinkerRegistry.integrate(mat,
    // tier == EnumMaterialTier.SUPER ? gem.getItemSuperOreName() : gem.getItemOreName());

    int durability = gem.getDurability(tier);
    float miningSpeed = gem.getMiningSpeed(tier);
    float meleeDamage = gem.getMeleeDamage(tier);
    float meleeSpeed = gem.getMeleeSpeed(tier);
    int harvestLevel = gem.getHarvestLevel(tier);
    float drawDelay = Math.max(38.4f - 1.4f * meleeSpeed * miningSpeed, 10);

    TinkerRegistry.addMaterialStats(material,
        new HeadMaterialStats(durability, miningSpeed, meleeDamage, harvestLevel),
        new HandleMaterialStats(0.875f, durability / 8), new ExtraMaterialStats(durability / 8),
        new BowMaterialStats(20f / drawDelay, 1f, 0.4f * meleeDamage - 1),
        new ArrowShaftMaterialStats(1.0f, 0));
    // TinkerRegistry.addMaterialStats(mat, new BowStringMaterialStats(1f));
    // TinkerRegistry.addMaterialStats(mat, new FletchingMaterialStats(1f, 1f));
    // TinkerRegistry.addMaterialStats(mat, new ProjectileMaterialStats());

    return material;
  }
}
