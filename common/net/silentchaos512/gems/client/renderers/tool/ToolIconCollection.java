package net.silentchaos512.gems.client.renderers.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class ToolIconCollection {

  public IIcon[] rod;
  public IIcon[] rodDeco;
  public IIcon[] rodWool;
  public IIcon[] headL;
  public IIcon[] headM;
  public IIcon[] headR;
  public IIcon[] tip;

  public ToolIconCollection() {

    rod = new IIcon[ToolRenderHelper.ROD_TYPE_COUNT];
    rodDeco = new IIcon[ToolRenderHelper.ROD_DECO_TYPE_COUNT];
    rodWool = new IIcon[ToolRenderHelper.ROD_WOOL_TYPE_COUNT];
    headL = new IIcon[ToolRenderHelper.HEAD_TYPE_COUNT];
    headM = new IIcon[ToolRenderHelper.HEAD_TYPE_COUNT];
    headR = new IIcon[ToolRenderHelper.HEAD_TYPE_COUNT];
    tip = new IIcon[ToolRenderHelper.TIP_TYPE_COUNT];
  }
}
