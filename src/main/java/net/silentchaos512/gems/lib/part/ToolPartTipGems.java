package net.silentchaos512.gems.lib.part;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.ToolPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.init.ModItems;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartTipGems extends ToolPartTip {

  Map<String, ModelResourceLocation> modelMap = Maps.newHashMap();

  public final String tipName;

  public ToolPartTipGems(String name, EnumTipUpgrade upgrade) {

    super(SilentGems.MODID + ":" + name,
        new ItemStack(ModItems.tipUpgrade, 1, upgrade.ordinal() - 1), upgrade.getMiningLevel(),
        upgrade.getDurabilityBoost(), upgrade.getSpeedBoost(), upgrade.getMeleeBoost(),
        upgrade.getMagicBoost());
    this.tipName = name.toLowerCase().replaceFirst("^tip", "tip_");
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, ToolPartPosition pos, int frame) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MODID + ":" + name.toLowerCase() + "/" + name + "_" + tipName
        + (frame == 3 ? "_3" : "");

    if (modelMap.containsKey(name)) {
      return modelMap.get(name);
    }

    name = name.toLowerCase();
    ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
    modelMap.put(name, model);
    return model;
  }
}
