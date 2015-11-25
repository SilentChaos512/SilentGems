package net.silentchaos512.gems.client.renderers.tool;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ToolModelCollection {

  public ModelResourceLocation[] rod;
  public ModelResourceLocation[] rodDeco;
  public ModelResourceLocation[] rodWool;
  public ModelResourceLocation[] headL;
  public ModelResourceLocation[] headM;
  public ModelResourceLocation[] headR;
  public ModelResourceLocation[] tip;

  public ToolModelCollection() {

    rod = new ModelResourceLocation[ToolRenderHelper.ROD_TYPE_COUNT];
    rodDeco = new ModelResourceLocation[ToolRenderHelper.ROD_DECO_TYPE_COUNT];
    rodWool = new ModelResourceLocation[ToolRenderHelper.ROD_WOOL_TYPE_COUNT];
    headL = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];
    headM = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];
    headR = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];
    tip = new ModelResourceLocation[ToolRenderHelper.TIP_TYPE_COUNT];
  }
}
