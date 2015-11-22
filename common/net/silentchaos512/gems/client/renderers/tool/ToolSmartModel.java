package net.silentchaos512.gems.client.renderers.tool;

import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.silentchaos512.gems.SilentGems;
import net.silentchaos512.gems.core.util.InventoryHelper;

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

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List getFaceQuads(EnumFacing p_177551_1_) {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List getGeneralQuads() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isAmbientOcclusion() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isGui3d() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isBuiltInRenderer() {

    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public TextureAtlasSprite getTexture() {

    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ItemCameraTransforms getItemCameraTransforms() {

    // TODO Auto-generated method stub
    return null;
  }
}
