package net.silentchaos512.gems.client.renderers.tool;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.client.RenderHelperQ;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ToolSmartModel implements ISmartItemModel, IPerspectiveAwareModel {

  private final IBakedModel baseModel;
  private ItemStack tool;
  public IBakedModel test;

  public ToolSmartModel(IBakedModel baseModel) {

    this.baseModel = baseModel;
  }

  @Override
  public IBakedModel handleItemState(ItemStack stack) {

    if (InventoryHelper.isGemTool(stack)) {
      tool = stack;
    }
    return this;
  }

  @Override
  public Pair<IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {

    // TODO: Finish this!
    switch (cameraTransformType) {
      case FIRST_PERSON:
        RenderItem.applyVanillaTransform(baseModel.getItemCameraTransforms().firstPerson);
        return Pair.of(baseModel, null);
      case GUI:
        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().gui);
        break;
      case HEAD:
        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().head);
        break;
      case THIRD_PERSON:
        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().thirdPerson);
        break;
      default:
        break;
    }
    return Pair.of(this, null);
  }

  @Override
  public List getFaceQuads(EnumFacing face) {

    return baseModel.getFaceQuads(face);
  }

  @Override
  public List getGeneralQuads() {

    return test.getGeneralQuads();
//    int gemId = ToolHelper.getToolGemId(tool);
//    boolean supercharged = ToolHelper.getToolIsSupercharged(tool);
//    List<BakedQuad> quads = Lists.newArrayList(baseModel.getGeneralQuads());
//    TextureAtlasSprite sprite;
//    for (int pass = 0; pass < ToolRenderHelper.RENDER_PASS_COUNT; ++pass) {
//      sprite = ToolRenderHelper.instance.getIcon(tool, pass, gemId, supercharged);
////      LogHelper.info(pass + ", " + sprite);
//      quads.add(makeQuad(sprite, pass));
//    }
//    return quads;
  }
  
  private BakedQuad makeQuad(TextureAtlasSprite sprite, int pass) {

    return RenderHelperQ.createBakedQuadForFace(0.5f, 1, 0.5f, 1, 0.0001f * pass, 0, sprite, EnumFacing.SOUTH);
  }

  @Override
  public boolean isAmbientOcclusion() {

    return baseModel.isAmbientOcclusion();
  }

  @Override
  public boolean isGui3d() {

    return baseModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {

    return baseModel.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getTexture() {

    return baseModel.getTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {

    return baseModel.getItemCameraTransforms();
  }
}
