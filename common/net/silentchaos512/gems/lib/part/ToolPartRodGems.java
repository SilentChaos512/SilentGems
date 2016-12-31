package net.silentchaos512.gems.lib.part;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumMaterialTier;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPartRod;
import net.silentchaos512.gems.item.tool.ItemGemShield;
import net.silentchaos512.lib.registry.IRegistryObject;

public class ToolPartRodGems extends ToolPartRod {

  Map<String, ModelResourceLocation> modelMap = Maps.newHashMap();

  public final String rodName;
  public final int color;

  public ToolPartRodGems(String name, EnumMaterialTier tier, ItemStack stack, int color) {

    super(SilentGems.MODID + ":" + name, stack);
    this.rodName = name;
    this.tier = tier;
    this.color = color;
  }

  public ToolPartRodGems(String name, EnumMaterialTier tier, ItemStack stack, int color, String oreName) {

    super(SilentGems.MODID + ":" + name, stack, oreName);
    this.rodName = name;
    this.tier = tier;
    this.color = color;
  }

  @Override
  public ModelResourceLocation getModel(ItemStack tool, EnumPartPosition pos, int frame) {

    String name = ((IRegistryObject) tool.getItem()).getName();
    name = SilentGems.MODID + ":" + name.toLowerCase() + "/" + name + rodName;

    if (modelMap.containsKey(name)) {
      return modelMap.get(name);
    }

    ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
    modelMap.put(name, model);
    return model;
  }

  @Override
  public int getColor(ItemStack toolOrArmor) {

    if (toolOrArmor.getItem() instanceof ItemGemShield)
      return color;
    return 0xFFFFFF;
  }
}
