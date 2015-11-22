package net.silentchaos512.gems.client.renderers.tool;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ToolSpriteCollection {

  public TextureAtlasSprite[] rod;
  public TextureAtlasSprite[] rodDeco;
  public TextureAtlasSprite[] rodWool;
  public TextureAtlasSprite[] headL;
  public TextureAtlasSprite[] headM;
  public TextureAtlasSprite[] headR;
  public TextureAtlasSprite[] tip;

  public ToolSpriteCollection() {

    rod = new TextureAtlasSprite[ToolRenderHelper.ROD_TYPE_COUNT];
    rodDeco = new TextureAtlasSprite[ToolRenderHelper.ROD_DECO_TYPE_COUNT];
    rodWool = new TextureAtlasSprite[ToolRenderHelper.ROD_WOOL_TYPE_COUNT];
    headL = new TextureAtlasSprite[ToolRenderHelper.HEAD_TYPE_COUNT];
    headM = new TextureAtlasSprite[ToolRenderHelper.HEAD_TYPE_COUNT];
    headR = new TextureAtlasSprite[ToolRenderHelper.HEAD_TYPE_COUNT];
    tip = new TextureAtlasSprite[ToolRenderHelper.TIP_TYPE_COUNT];
  }
}
