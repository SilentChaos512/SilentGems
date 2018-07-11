package net.silentchaos512.gems.compat.tconstruct;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import gnu.trove.map.hash.THashMap;
import net.minecraft.init.Items;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.lib.EnumGem;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;

public class TConstructGemsCompat {

  static final Map<String, Material> materials = new THashMap<>();
  static final Map<String, MaterialIntegration> materialIntegrations = new THashMap<>();
  static final Map<String, CompletionStage<?>> materialIntegrationStages = new THashMap<>();

  public static void preInit() {

    SilentGems.logHelper.info("Loading TConstruct compatibility module...");

    try {
      for (EnumGem gem : EnumGem.values())
        register(gem, EnumMaterialTier.REGULAR);
      for (EnumGem gem : EnumGem.values())
        register(gem, EnumMaterialTier.SUPER);

      preIntegrate(materials, materialIntegrations, materialIntegrationStages);
    } catch (NoSuchMethodError ex) {
      SilentGems.logHelper.info("Failed to load TConstruct module. Are Tinkers tools disabled?");
      ex.printStackTrace();
    } catch (Exception ex) {
      SilentGems.logHelper.info("Unknown error while loading TConstruct module.");
      ex.printStackTrace();
    }
  }

  // Copied from PlusTiC
  private static void preIntegrate(Map<String, Material> materials,
      Map<String, MaterialIntegration> materialIntegrations,
      Map<String, CompletionStage<?>> materialIntegrationStages) {

    materials.forEach((k, v) -> {
      if (!materialIntegrations.containsKey(k)) {
        materialIntegrationStages.getOrDefault(k, CompletableFuture.completedFuture(null))
            .thenRun(() -> {
              MaterialIntegration mi;
              if (v.getRepresentativeItem().getItem() == Items.EMERALD) {
                mi = new MaterialIntegration(v, v.getFluid());
              } else if (v.getFluid() != null) {
                mi = new MaterialIntegration(v, v.getFluid(), k).toolforge();
              } else {
                mi = new MaterialIntegration(v);
              }
              TinkerRegistry.integrate(mi).preInit();
              materialIntegrations.put(k, mi);
            });
      }
    });
  }

  private static void register(EnumGem gem, EnumMaterialTier tier) {

    Material material = new TConstructMaterialGem(gem, tier);
    String itemOreName = tier == EnumMaterialTier.SUPER ? gem.getItemSuperOreName() : gem.getItemOreName();
    material.addItem(itemOreName, 1, Material.VALUE_Ingot);
    material.setCraftable(true);
    SilentGems.proxy.setTinkersRenderColor(material, gem.getColor());

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

    materials.put(material.identifier, material);
  }
}
