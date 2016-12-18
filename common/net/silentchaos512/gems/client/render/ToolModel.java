package net.silentchaos512.gems.client.render;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.api.lib.EnumPartPosition;
import net.silentchaos512.gems.item.ModItems;
import net.silentchaos512.gems.item.ToolRenderHelper;
import net.silentchaos512.gems.item.tool.ItemGemScepter;
import net.silentchaos512.gems.util.ToolHelper;
import net.silentchaos512.lib.registry.IRegistryObject;
import net.silentchaos512.lib.util.LogHelper;

@SuppressWarnings("deprecation")
public class ToolModel implements IPerspectiveAwareModel {

  private static ModelManager modelManager = null;
  private final IBakedModel baseModel;

  private ItemStack tool;
  private boolean isGui = false;

  public ToolModel(IBakedModel baseModel) {

    this.baseModel = baseModel;
  }

  public IBakedModel handleItemState(ItemStack stack) {

    // TODO: Check that it's a tool? Is that necessary?
    tool = stack;
    return this;
  }

  @Override
  public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {

    LogHelper log = SilentGems.instance.logHelper;

    if (tool == null) {
      return new ArrayList<BakedQuad>();
    }

    if (modelManager == null) {
      modelManager = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
          .getModelManager();
    }

    List<BakedQuad> quads = Lists.newArrayList();
    ModelResourceLocation location;
    IBakedModel model;
    IBakedModel rodModel = null;

    boolean isBroken = ToolHelper.isBroken(tool);
    Item item = tool.getItem();
//    SilentGems.instance.logHelper.debug(tool.getItemDamage(), isBroken);

    // Invalid tools models.
    if (ToolHelper.getMaxDamage(tool) <= 0 && tool.getItem() instanceof IRegistryObject) {
      String name = ((IRegistryObject) tool.getItem()).getName();
      location = new ModelResourceLocation(SilentGems.MOD_ID + ":" + name.toLowerCase() + "/_error", "inventory");
      model = modelManager.getModel(location);
      if (model != null)
        quads.addAll(model.getQuads(state, side, rand));

      if (isGui)
        quads.addAll(modelManager.getModel(ToolRenderHelper.getInstance().modelError).getQuads(state, side, rand));

      return quads;
    }

    for (EnumPartPosition partPos : EnumPartPosition.values()) {
      if (isBroken) {
        if ((partPos == EnumPartPosition.HEAD_LEFT && item != ModItems.sword && item != ModItems.bow)
            || (partPos == EnumPartPosition.HEAD_MIDDLE && item != ModItems.bow)
            || (partPos == EnumPartPosition.HEAD_RIGHT && item != ModItems.bow)
            || partPos == EnumPartPosition.TIP) {
//          SilentGems.instance.logHelper.debug(tool.getDisplayName(), isBroken, partPos);
          continue;
        }
      }

      // Scepter rods on top of head.
      if (tool.getItem() instanceof ItemGemScepter) {
        if (partPos == EnumPartPosition.ROD) {
          location = ToolRenderHelper.getInstance().getModel(tool, partPos);
          rodModel = modelManager.getModel(location);
          continue;
        } else if (partPos == EnumPartPosition.ROD_DECO) {
          quads.addAll(rodModel.getQuads(state, side, rand));
        }
      }

      // Normal logic.
      location = ToolRenderHelper.getInstance().getModel(tool, partPos);
      if (location != null) {
        model = modelManager.getModel(location);
        if (model != null) {
          quads.addAll(model.getQuads(state, side, rand));
        }
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
  public TextureAtlasSprite getParticleTexture() {

    return baseModel.getParticleTexture();
  }

  private static ItemTransformVec3f thirdPersonLeft = null;
  private static ItemTransformVec3f thirdPersonRight = null;
  private static ItemTransformVec3f firstPersonLeft = null;
  private static ItemTransformVec3f firstPersonRight = null;

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {

    // if (thirdPersonLeft == null) {
    Vector3f rotation = new Vector3f();
    Vector3f translation = new Vector3f();
    Vector3f scale = new Vector3f(1f, 1f, 1f);

    // Third Person
    rotation = new Vector3f(0f, (float) -Math.PI / 2f, (float) Math.PI * 7f / 36f); // (0, 90, -35)
    translation = new Vector3f(0f, 5.5f, 2.5f);
    if (tool != null && tool.getItem() == ModItems.katana) {
      translation.y += 2.0f;
    }
    translation.scale(0.0625f);
    // scale = new Vector3f(0.85f, 0.85f, 0.85f);
    thirdPersonRight = new ItemTransformVec3f(rotation, translation, scale);

    rotation = new Vector3f(0f, (float) Math.PI / 2f, (float) -Math.PI * 7f / 36f); // (0, 90, -35)
    // translation = new Vector3f(0f, 5.5f, 2.5f);
    // if (tool != null && tool.getItem() == ModItems.katana) {
    // translation.y += 2.0f;
    // }
    // translation.scale(0.0625f);
    // scale = new Vector3f(0.85f, 0.85f, 0.85f);
    thirdPersonLeft = new ItemTransformVec3f(rotation, translation, scale);

    // First Person
    // rotation = new Vector3f(0f, (float) -Math.PI * 3f / 4f, (float) Math.PI * 5f / 36f); // (0, -135, 25)
    rotation = new Vector3f(0f, (float) -Math.PI * 1f / 2f, (float) Math.PI * 5f / 36f);
    translation = new Vector3f(1.13f, 3.2f, 1.13f);
    if (tool != null && tool.getItem() == ModItems.katana) {
      translation.y += 1.5f;
    }
    translation.scale(0.0625f);
    scale = new Vector3f(0.68f, 0.68f, 0.68f);
    firstPersonRight = new ItemTransformVec3f(rotation, translation, scale);

    rotation = new Vector3f(0f, (float) Math.PI * 1f / 2f, (float) -Math.PI * 5f / 36f);
    firstPersonLeft = new ItemTransformVec3f(rotation, translation, scale);
    // }

    // Head and GUI are default.
    return new ItemCameraTransforms(thirdPersonLeft, thirdPersonRight, firstPersonLeft,
        firstPersonRight, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT,
        ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
  }

  // @Override
  // public VertexFormat getFormat() {
  //
  // return DefaultVertexFormats.ITEM;
  // }

  @Override
  public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
      TransformType cameraTransformType) {

    Matrix4f matrix = new Matrix4f();
    switch (cameraTransformType) {
      case FIRST_PERSON_RIGHT_HAND:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().firstperson_right);
        break;
      case FIRST_PERSON_LEFT_HAND:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().firstperson_left);
        break;
      case GUI:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().gui);
        isGui = true;
        break;
      case HEAD:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().head);
        break;
      case THIRD_PERSON_RIGHT_HAND:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().thirdperson_right);
        break;
      case THIRD_PERSON_LEFT_HAND:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().thirdperson_left);
        break;
      case GROUND:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().ground);
        matrix.setScale(matrix.getScale() * 0.5f);
        break;
      case FIXED:
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().fixed);
        break;
      default:
        break;
    }
    // TODO: Getter function for scale?
    if (tool != null && tool.getItem() == ModItems.katana) {
      if (cameraTransformType != TransformType.GUI) {
        matrix.setScale(matrix.getScale() * 1.3f);
      }
    }
    else if (tool != null && tool.getItem() == ModItems.scepter) {
      if (cameraTransformType != TransformType.GUI) {
        matrix.setScale(matrix.getScale() * 1.2f);
      }
    }
    return Pair.of((IBakedModel) this, matrix);
  }

  @Override
  public ItemOverrideList getOverrides() {

    return ToolItemOverrideHandler.INSTANCE;
  }
}
