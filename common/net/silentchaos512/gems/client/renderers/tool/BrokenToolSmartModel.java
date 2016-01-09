package net.silentchaos512.gems.client.renderers.tool;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IFlexibleBakedModel;
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
  public TextureAtlasSprite getParticleTexture() {

    return baseModel.getParticleTexture();
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {

    Vector3f rotation, translation, scale;

    // Third Person
    rotation = new Vector3f(0f, (float) Math.PI / 2f, (float) -Math.PI * 7f / 36f); // (0, 90, -35)
    translation = new Vector3f(0f, 1.25f, -3.5f);
    translation.scale(0.0625f);
    scale = new Vector3f(0.85f, 0.85f, 0.85f);
    ItemTransformVec3f thirdPerson = new ItemTransformVec3f(rotation, translation, scale);

    // First Person
    rotation = new Vector3f(0f, (float) -Math.PI * 3f / 4f, (float) Math.PI * 5f / 36f); // (0, -135, 25)
    translation = new Vector3f(0f, 4f, 2f);
    translation.scale(0.0625f);
    scale = new Vector3f(1.7f, 1.7f, 1.7f);
    ItemTransformVec3f firstPerson = new ItemTransformVec3f(rotation, translation, scale);

    // Head and GUI are default.
    return new ItemCameraTransforms(thirdPerson, firstPerson, ItemTransformVec3f.DEFAULT,
        ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
  }

  @Override
  public Pair<IFlexibleBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {

    Matrix4f matrix = new Matrix4f();
    switch (cameraTransformType) {
      case FIRST_PERSON:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().firstPerson);
        break;
      case GUI:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().gui);
        break;
      case HEAD:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().head);
        break;
      case THIRD_PERSON:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().thirdPerson);
        break;
      case GROUND:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().ground);
        break;
      case FIXED:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().fixed);
      default:
        break;
    }
    return Pair.of((IFlexibleBakedModel) this, matrix);
  }

  @Override
  public IBakedModel handleItemState(ItemStack stack) {

    if (stack.getItem() instanceof ItemBrokenTool) {
      brokenTool = stack;
    }
    return this;
  }

  @Override
  public VertexFormat getFormat() {

    // TODO Auto-generated method stub
    return DefaultVertexFormats.ITEM;
  }

}
