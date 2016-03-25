package net.silentchaos512.gems.lib.part;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartTip;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.lib.EnumTipUpgrade;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartTipGems extends ToolPartTip {

  Map<String, ModelResourceLocation> modelMap = Maps.newHashMap();

  public final String tipName;

  public ToolPartTipGems(String name, EnumTipUpgrade upgrade) {

    super(SilentGems.MOD_ID + ":" + name, new ItemStack(ModItems.tipUpgrade, 1, upgrade.ordinal() - 1),
        upgrade.getMiningLevel(), upgrade.getDurabilityBoost(), upgrade.getSpeedBoost(),
        upgrade.getMeleeBoost(), upgrade.getMagicBoost());
    this.tipName = name;
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MOD_ID + ":" + name.toLowerCase() + "/" + name + tipName;

    if (modelMap.containsKey(name)) {
      return modelMap.get(name);
    }

    ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
    modelMap.put(name, model);
    return model;
  }
}
