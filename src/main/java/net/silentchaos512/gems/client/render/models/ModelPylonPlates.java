package net.silentchaos512.gems.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.silentchaos512.lib.client.model.ModelHelperSL;

public class ModelPylonPlates {

  private IBakedModel pylonPlatesPassiveModel;
  private IBakedModel pylonPlatesBurnerModel;

  public ModelPylonPlates() {
    // load
    ResourceLocation resource = new ResourceLocation("silentgems:models/block/chaospylonplates.obj");
    OBJModel model = ModelHelperSL.loadModel(resource);
    // retexture
    IModel pylonPlatesPassive = ModelHelperSL.retexture(model, "#skin.001", "silentgems:blocks/chaospylonpassive");
    IModel pylonPlatesBurner = ModelHelperSL.retexture(model, "#skin.001", "silentgems:blocks/chaospylonburner");
    // activate
    pylonPlatesPassiveModel = ModelHelperSL.bake(pylonPlatesPassive);
    pylonPlatesBurnerModel = ModelHelperSL.bake(pylonPlatesBurner);
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

    ModelHelperSL.renderModel(model, color);
  }
}
