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

/**
 * Controls the rendering of tools.
 * 
 * In 1.7, tools used multiple IIcons for different parts of the tool. IIcons are no more, so we use multiple
 * IBakedModels instead. Technically, these "part models" are registered for different metavalues of the
 * ToolRenderHelper fake item.
 * 
 * In spite of the radical underlying changes, this is fairly similar to 1.7 on the surface. ToolRenderHelper registers,
 * stores, and retrieves the models (or their resource locations, specifically). This class utilizes the models,
 * combining the quads from each model to create a new, dynamic model.
 * 
 * @author SilentChaos512
 *
 */
@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class ToolSmartModel implements ISmartItemModel, IPerspectiveAwareModel {

  /**
   * The base model of the tool. This may not have any meaningful function.
   */
  private final IBakedModel baseModel;
//  /**
//   * Used by bows to determine which frame of the animation it should display.
//   */
//  public final int animationFrame;
  /**
   * The tool being rendered. The NBT of the tool determines the models used.
   */
  private ItemStack tool;

  public ToolSmartModel(IBakedModel baseModel/*, int animationFrame*/) {

    this.baseModel = baseModel;
//    this.animationFrame = animationFrame;
  }

  /**
   * Gives the smart model a chance to set its state (variables, etc) before rendering.
   * 
   * @param stack
   *          The tool.
   * @return The modified smart model.
   */
  @Override
  public IBakedModel handleItemState(ItemStack stack) {

    if (InventoryHelper.isGemTool(stack)) {
      tool = stack;
//      if (animationFrame != 0) {
//        LogHelper.debug(animationFrame);
//      }
    }
    return this;
  }

  /**
   * Handles transformations for various perspectives. Not sure what the returned values are for. Returning a
   * transformation matrix has strange effects, so I don't recommend it.
   * 
   * @param cameraTransformType
   *          The perspective (ie first person, third person, etc)
   * @return A pair of the baked model (this) and a Matrix4f (always null)
   */
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

  /**
   * Gets the quads of all the part models combined. This does NOT include the base model quads, as they would be
   * redundant.
   * 
   * @return A list of quads
   */
  @Override
  public List getGeneralQuads() {

    if (tool == null) {
      return new ArrayList<BakedQuad>();
    }

    // The base tool specs are used if the tool part hasn't been modified (ie, is -1)
    int gemId = ToolHelper.getToolGemId(tool);
    boolean supercharged = ToolHelper.getToolIsSupercharged(tool);

    List<BakedQuad> quads = Lists.newArrayList();
    ModelResourceLocation modelLocation;
    IBakedModel model;

    // Assigning the model manager to a variable for readability.
    ModelManager manager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
        .getModelManager();

    // We're using "render passes". Basically just adapting the 1.7 code wherever possible.
    for (int pass = 0; pass < ToolRenderHelper.RENDER_PASS_COUNT; ++pass) {
      // Get resource location from ToolRenderHelper.
      modelLocation = ToolRenderHelper.instance.getModel(tool, pass, gemId, supercharged,
          ToolHelper.getAnimationFrame(tool));
      // Get the actual tool part model.
      model = manager.getModel(modelLocation);
      // Some safety checks...
      // LogHelper.debug(animationFrame);
      if (model != null && !(model instanceof ToolSmartModel)) {
        // Add the quads from the part to the tool.
        quads.addAll(model.getGeneralQuads());
        // if (animationFrame != 0) {
        // LogHelper.debug(model.getGeneralQuads().size());
        // }
      }
    }

    return quads;
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

  /**
   * Gets information on the transformations that should be applied in various perspectives. This is used in
   * handlePerspective! These should be identical to the transformations applied to vanilla tools. Transformations in
   * the JSON files are ignored, unfortunately.
   * 
   * @return
   */
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
}
