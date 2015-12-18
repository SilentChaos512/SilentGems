package net.silentchaos512.gems.client.renderers.tool;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ToolModelCollection {

  public ModelResourceLocation[] rod = new ModelResourceLocation[ToolRenderHelper.ROD_TYPE_COUNT];
  public ModelResourceLocation[] rodDeco = new ModelResourceLocation[ToolRenderHelper.ROD_DECO_TYPE_COUNT];
  public ModelResourceLocation[] rodWool = new ModelResourceLocation[ToolRenderHelper.ROD_WOOL_TYPE_COUNT];
  public ModelResourceLocation[] headL = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];
  public ModelResourceLocation[] headM = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];
  public ModelResourceLocation[] headR = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];
  public ModelResourceLocation[] tip = new ModelResourceLocation[ToolRenderHelper.TIP_TYPE_COUNT];
}
