package net.silentchaos512.gems.compat.tconstruct;

import net.minecraftforge.oredict.OreDictionary;
import net.silentchaos512.lib.util.StackHelper;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerTools;


public class MaterialIntegrationGems extends MaterialIntegration {

  private boolean integrated = false;
  private boolean toolforge;

  public MaterialIntegrationGems(Material material, String oreSuffix, String oreRequirement) {

    super(material, null, oreSuffix, oreRequirement);
  }

  @Override
  public void integrate() {

    integrate(false);
  }

  public void integrate(boolean force) {

    if (integrated) {
      return;
    }

    boolean forceRegisterAll = false; // TODO: Config?

    if (!force) {
      if (oreRequirement != null && oreRequirement.length > 0 && !forceRegisterAll) {
        int found = 0;
        for (String ore : OreDictionary.getOreNames()) {
          for (int i = 0; i < oreRequirement.length; ++i) {
            if (oreRequirement[i].equals(ore)) {
              if (StackHelper.getOres(ore).size() > 0) {
                if (++found == oreRequirement.length) {
                  break;
                }
              }
            }
          }
        }

        if (found < oreRequirement.length) {
          return;
        }
      }
    }

    integrated = true;

    // No fluids!

    // Register the material.
    if (material != null) {
      TinkerRegistry.addMaterial(material);
      material.setCraftable(true);
    }

    // Add toolforge recipe.
    if (toolforge && oreSuffix != null && !oreSuffix.isEmpty()) {
      TinkerTools.registerToolForgeBlock("block" + oreSuffix);
    }
  }

  @Override
  public boolean isIntegrated() {

    return integrated;
  }

  @Override
  public void integrateRecipes() {

    if (!integrated) {
      return;
    }

    // TODO: Need anything here? I think this is just for smeltery support.
  }

  @Override
  public MaterialIntegration toolforge() {

    super.toolforge();
    this.toolforge = true;
    return this;
  }
}
