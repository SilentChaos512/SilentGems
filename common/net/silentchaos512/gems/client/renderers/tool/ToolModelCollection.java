package net.silentchaos512.gems.client.renderers.tool;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolModelCollection {

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] rod = new ModelResourceLocation[ToolRenderHelper.ROD_TYPE_COUNT];

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] rodDeco = new ModelResourceLocation[ToolRenderHelper.ROD_DECO_TYPE_COUNT];

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] rodWool = new ModelResourceLocation[ToolRenderHelper.ROD_WOOL_TYPE_COUNT];

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] headL = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] headM = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] headR = new ModelResourceLocation[ToolRenderHelper.HEAD_TYPE_COUNT];

  @SideOnly(Side.CLIENT)
  public ModelResourceLocation[] tip = new ModelResourceLocation[ToolRenderHelper.TIP_TYPE_COUNT];
}
