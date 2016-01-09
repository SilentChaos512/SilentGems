package net.silentchaos512.gems.client.renderers;

import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.silentchaos512.gems.item.ModItems;

@SuppressWarnings("deprecation")
public class SmartModelReturnHome implements ISmartItemModel, IPerspectiveAwareModel {

  private final IBakedModel baseModel;
  private ItemStack charm;

  public SmartModelReturnHome(IBakedModel baseModel) {

    this.baseModel = baseModel;
  }

  @Override
  public List getFaceQuads(EnumFacing facing) {

    return baseModel.getFaceQuads(facing);
  }

  @Override
  public List getGeneralQuads() {

    ModelResourceLocation modelLocation = charm.getItem().getModel(charm, null, 0);
    IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
        .getModelManager().getModel(modelLocation);
    return model.getGeneralQuads();
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
    rotation = new Vector3f(-90f, 0f, 0f);
    translation = new Vector3f(0f, 1f, -3f);
    translation.scale(0.0625f);
    scale = new Vector3f(0.55f, 0.55f, 0.55f);
    ItemTransformVec3f thirdPerson = new ItemTransformVec3f(rotation, translation, scale);

    // First Person
    rotation = new Vector3f(0f, -135f, 25f);
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
//        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().firstPerson);
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().firstPerson);
        break;
      case GUI:
//        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().gui);
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().gui);
        break;
      case HEAD:
//        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().head);
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().head);
        break;
      case THIRD_PERSON:
//        RenderItem.applyVanillaTransform(this.getItemCameraTransforms().thirdPerson);
        matrix = ForgeHooksClient.getMatrix(getItemCameraTransforms().thirdPerson);
        break;
      default:
        break;
    }
    return Pair.of((IFlexibleBakedModel) this, matrix);
  }

  @Override
  public IBakedModel handleItemState(ItemStack stack) {

    if (stack != null && stack.getItem() == ModItems.returnHome) {
      this.charm = stack;
    }
    return this;
  }

  @Override
  public VertexFormat getFormat() {

 // TODO Auto-generated method stub
    return DefaultVertexFormats.ITEM;
  }

}
