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
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
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
import net.silentchaos512.gems.core.util.ToolHelper;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.tool.GemBow;
import net.silentchaos512.gems.item.tool.GemSword;
import net.silentchaos512.gems.item.tool.ItemBrokenTool;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class BrokenToolSmartModel implements ISmartItemModel, IPerspectiveAwareModel {

  private final IBakedModel baseModel;
  private ItemStack brokenTool;
  
  public BrokenToolSmartModel(IBakedModel baseModel) {

    this.baseModel = baseModel;
  }

  public ItemStack getTool() {

    return brokenTool == null ? null : ModItems.brokenTool.getTool(brokenTool);
  }

  @Override
  public List getFaceQuads(EnumFacing p_177551_1_) {

    return Lists.newArrayList();
  }

  @Override
  public List getGeneralQuads() {

    if (brokenTool == null) {
      return new ArrayList<BakedQuad>();
    }

    ItemStack tool = getTool();
    List<BakedQuad> quads = Lists.newArrayList();
    ModelResourceLocation modelLocation;
    IBakedModel model;

    ModelManager manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
        .getModelManager();
    ToolRenderHelper renderHelper = ToolRenderHelper.getInstance();

    // No tool?
    if (tool == null) {
      modelLocation = renderHelper.modelError;
      model = manager.getModel(modelLocation);
      if (model != null) {
        quads.addAll(model.getGeneralQuads());
      }
      return quads;
    }

    int gemId = ToolHelper.getToolGemId(tool);
    boolean supercharged = ToolHelper.getToolIsSupercharged(tool);

    for (int pass = 0; pass < renderHelper.RENDER_PASS_COUNT; ++pass) {
      modelLocation = getModelForPass(tool, pass);
      model = manager.getModel(modelLocation);
      if (model != null) {
        quads.addAll(model.getGeneralQuads());
      }
    }
    
    return quads;
  }

  private ModelResourceLocation getModelForPass(ItemStack tool, int pass) {

    ToolRenderHelper helper = ToolRenderHelper.getInstance();
    if (tool.getItem() instanceof GemBow) {
      switch (pass) {
        case ToolRenderHelper.PASS_HEAD_L:
        case ToolRenderHelper.PASS_HEAD_M:
        case ToolRenderHelper.PASS_HEAD_R:
          return helper.getModel(tool, pass);
        default:
          return helper.modelBlank;
      }
    }

    switch (pass) {
      case ToolRenderHelper.PASS_ROD:
      case ToolRenderHelper.PASS_ROD_DECO:
      case ToolRenderHelper.PASS_ROD_WOOL:
        return helper.getModel(tool, pass);
      case ToolRenderHelper.PASS_HEAD_L:
        if (tool.getItem() instanceof GemSword) {
          return helper.getModel(tool, pass);
        }
        break;
    }
    
    return helper.modelBlank;
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

    Vector3f rotation, translation, scale;

    // Third Person
    rotation = new Vector3f(0f, 90f, -35f);
    translation = new Vector3f(0f, 1.25f, -3.5f);
    translation.scale(0.0625f);
    scale = new Vector3f(0.85f, 0.85f, 0.85f);
    ItemTransformVec3f thirdPerson = new ItemTransformVec3f(rotation, translation, scale);

    // First Person
    rotation = new Vector3f(0f, -135f, 25f);
    translation = new Vector3f(0f, 4f, 2f);
    translation.scale(0.0625f);
    scale = new Vector3f(1.7f, 1.7f, 1.7f);
    ItemTransformVec3f firstPerson = new ItemTransformVec3f(rotation, translation, scale);

    // Head and GUI are default.
    return new ItemCameraTransforms(thirdPerson, firstPerson, ItemTransformVec3f.DEFAULT,
        ItemTransformVec3f.DEFAULT);
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
  public IBakedModel handleItemState(ItemStack stack) {

    if (stack.getItem() instanceof ItemBrokenTool) {
      brokenTool = stack;
    }
    return this;
  }

}
