package net.silentchaos512.gems.lib.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.silentchaos512.gems.api.lib.ArmorPartPosition;
import net.silentchaos512.gems.api.lib.IPartPosition;
import net.silentchaos512.gems.api.tool.part.ToolPart;
import net.silentchaos512.gems.util.ArmorHelper;

public class ArmorModelData implements IModelData {

  int[] colors;

  public ArmorModelData(ItemStack armor) {

    colors = new int[ArmorPartPosition.values().length];

    for (ArmorPartPosition pass : ArmorPartPosition.values()) {
      ToolPart part = ArmorHelper.getRenderPart(armor, pass);
      if (part != null) {
        colors[pass.getRenderPass()] = part.getColor(armor);
      } else {
        colors[pass.getRenderPass()] = 0xFFFFFF;
      }
    }
  }

  @Override
  public ModelResourceLocation getModel(IPartPosition pos, int frame) {

    // Not using different models for armor, just store color data.
    return null;
  }

  @Override
  public int getColor(IPartPosition pos, int frame) {

    int pass = pos.getRenderPass();
    if (pass < 0 || pass >= colors.length) {
      return 0xFF00FF;
    }
    return colors[pass];
  }

}
