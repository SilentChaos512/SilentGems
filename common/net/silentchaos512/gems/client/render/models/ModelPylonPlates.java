package net.silentchaos512.gems.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelPylonPlates {

  private IBakedModel pylonPlatesPassiveModel;
  private IBakedModel pylonPlatesBurnerModel;

  public ModelPylonPlates() {
    // load
    OBJModel model = ModelHelper.loadModel("chaospylonplates");
    // retexture
    IModel pylonPlatesPassive = ModelHelper.retexture(model, "#skin.001", "chaospylonpassive");
    IModel pylonPlatesBurner = ModelHelper.retexture(model, "#skin.001", "chaospylonburner");
    // activate
    pylonPlatesPassiveModel = ModelHelper.bake(pylonPlatesPassive);
    pylonPlatesBurnerModel = ModelHelper.bake(pylonPlatesBurner);
  }

  public void renderPylonPlates(int pylonType) {

    switch (pylonType) {
      case 1:
        renderModel(pylonPlatesBurnerModel);
        break;
      default:
        renderModel(pylonPlatesPassiveModel);
    }
  }

  private void renderModel(IBakedModel model) {

    renderModel(model, -1);
  }

  private void renderModel(IBakedModel model, int color) {

    ModelHelper.renderModel(model, color);
  }
}
