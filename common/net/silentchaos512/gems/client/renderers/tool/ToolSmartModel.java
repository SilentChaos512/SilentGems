package net.silentchaos512.gems.client.renderers.tool;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.core.util.InventoryHelper;
import net.silentchaos512.gems.core.util.LogHelper;
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.ModItems;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ToolSmartModel implements ISmartItemModel, IPerspectiveAwareModel {

  private final IBakedModel baseModel;
  private ItemStack tool;

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

    switch (cameraTransformType) {
      case FIRST_PERSON:
        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().firstPerson);
        break;
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
    return Pair.of((IBakedModel) this, (Matrix4f) null);
  }

  @Override
  public List getFaceQuads(EnumFacing face) {

    return baseModel.getFaceQuads(face);
  }

  @Override
  public List getGeneralQuads() {

    if (tool == null) {
      return new ArrayList<BakedQuad>();
    }

    int gemId = ToolHelper.getToolGemId(tool);
    boolean supercharged = ToolHelper.getToolIsSupercharged(tool);

//    List<BakedQuad> quads = Lists.newArrayList(baseModel.getGeneralQuads());
    List<BakedQuad> quads = Lists.newArrayList();
    ModelResourceLocation modelLocation;
    IBakedModel model;

    ModelManager manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
        .getModelManager();
    for (int pass = 0; pass < ToolRenderHelper.RENDER_PASS_COUNT; ++pass) {
      modelLocation = ToolRenderHelper.instance.getModel(tool, pass, gemId, supercharged);
      model = manager.getModel(modelLocation);
      if (model != null && !(model instanceof ToolSmartModel)) {
//        LogHelper.debug(model.getGeneralQuads().size() + " " + modelLocation);
        if (model.getGeneralQuads().size() == 0) {
          LogHelper.warning("Model has no quads!: " + modelLocation);
        }
        quads.addAll(model.getGeneralQuads());
        // LogHelper.debug(model.getGeneralQuads().size());
      }
    }

    // Test
//    model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
//        .getItemModel(new ItemStack(ModItems.toolRenderHelper));
//    quads.addAll(model.getGeneralQuads());
    // LogHelper.debug(ModItems.toolRenderHelper.modelKeys);
    // LogHelper.debug(model.getGeneralQuads().size());

    return quads;
  }

  // private BakedQuad makeQuad(TextureAtlasSprite sprite, int pass) {
  //
  // return RenderHelperQ.createBakedQuadForFace(0.5f, 1, 0.5f, 1, 0.0001f * pass, 0, sprite,
  // EnumFacing.SOUTH);
  // }

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

    Vector3f rotation, translation, scale;

    rotation = new Vector3f(0f, 90f, -35f);
    translation = new Vector3f(0f, 1.25f, -3.5f);
    translation.scale(0.0625f);
    scale = new Vector3f(0.85f, 0.85f, 0.85f);
    ItemTransformVec3f thirdPerson = new ItemTransformVec3f(rotation, translation, scale);

    rotation = new Vector3f(0f, -135f, 25f);
    translation = new Vector3f(0f, 4f, 2f);
    translation.scale(0.0625f);
    scale = new Vector3f(1.7f, 1.7f, 1.7f);
    ItemTransformVec3f firstPerson = new ItemTransformVec3f(rotation, translation, scale);

    return new ItemCameraTransforms(thirdPerson, firstPerson, ItemTransformVec3f.DEFAULT,
        ItemTransformVec3f.DEFAULT);
  }
}
