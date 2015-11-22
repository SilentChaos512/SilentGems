package net.silentchaos512.gems.client.renderers.tool;

import net.minecraft.client.resources.model.IBakedModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("deprecation")
public class ToolModelCollection {

  public IBakedModel[] rod;
  public IBakedModel[] rodDeco;
  public IBakedModel[] rodWool;
  public IBakedModel[] headL;
  public IBakedModel[] headM;
  public IBakedModel[] headR;
  public IBakedModel[] tip;

  public ToolModelCollection() {

    rod = new IBakedModel[ToolRenderHelper.ROD_TYPE_COUNT];
    rodDeco = new IBakedModel[ToolRenderHelper.ROD_DECO_TYPE_COUNT];
    rodWool = new IBakedModel[ToolRenderHelper.ROD_WOOL_TYPE_COUNT];
    headL = new IBakedModel[ToolRenderHelper.HEAD_TYPE_COUNT];
    headM = new IBakedModel[ToolRenderHelper.HEAD_TYPE_COUNT];
    headR = new IBakedModel[ToolRenderHelper.HEAD_TYPE_COUNT];
    tip = new IBakedModel[ToolRenderHelper.TIP_TYPE_COUNT];
  }
}
