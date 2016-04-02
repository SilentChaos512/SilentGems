package net.silentchaos512.gems.lib.part;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartRodGems extends ToolPartRod {

  Map<String, ModelResourceLocation> modelMap = Maps.newHashMap();

  public final String rodName;

  public ToolPartRodGems(String name, EnumMaterialTier tier, ItemStack stack) {

    super(SilentGems.MOD_ID + ":" + name, stack);
    this.rodName = name;
    this.tier = tier;
  }

  public ToolPartRodGems(String name, EnumMaterialTier tier, ItemStack stack, String oreName) {

    super(SilentGems.MOD_ID + ":" + name, stack, oreName);
    this.rodName = name;
    this.tier = tier;
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MOD_ID + ":" + name.toLowerCase() + "/" + name + rodName;

    if (modelMap.containsKey(name)) {
      return modelMap.get(name);
    }

    ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
    modelMap.put(name, model);
    return model;
  }
}
