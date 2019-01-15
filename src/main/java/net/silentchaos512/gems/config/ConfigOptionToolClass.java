package net.silentchaos512.gems.config;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraftforge.common.config.Configuration;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;

import java.util.List;
import java.util.Set;

public class ConfigOptionToolClass extends ConfigOption {

  public final ITool tool;
  public final String toolName;
  public Set<EnumMaterialTier> validTiers;
  public boolean isDisabled;

  public ConfigOptionToolClass(ITool tool, String toolName) {

    this.tool = tool;
    this.toolName = toolName;
  }

  public ConfigOption loadValue(Configuration c) {

    return loadValue(c, GemsConfig.CAT_TOOLS + GemsConfig.split + toolName);
  }

  @Override
  public ConfigOption loadValue(Configuration c, String category) {

    return loadValue(c, category, "Configure tool class " + toolName);
  }

  @Override
  public ConfigOption loadValue(Configuration c, String category, String comment) {

    c.setCategoryComment(category, comment);
    c.setCategoryRequiresMcRestart(category, true);

    // Valid tiers
    List<EnumMaterialTier> tierList = Lists.newArrayList();
    for (EnumMaterialTier tier : EnumMaterialTier.values()) {
      boolean _default = !tool.isSuperTool() || tier.ordinal() >= EnumMaterialTier.SUPER.ordinal();
      boolean allowed = c.get(category, "Tier " + tier.name() + " Allowed", _default).getBoolean();
      if (allowed) {
        tierList.add(tier);
      }
    }
    validTiers = ImmutableSet.copyOf(tierList);

    // Disable option
    isDisabled = c.get(category, "Disable", false).getBoolean();

    return this.validate();
  }

  @Override
  public ConfigOption validate() {

    return this;
  }

}
