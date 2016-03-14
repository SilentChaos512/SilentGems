package net.silentchaos512.gems.client.render.models;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelPylonPlates {

    private IFlexibleBakedModel pylonPlatesPassiveModel;
    private IFlexibleBakedModel pylonPlatesBurnerModel;

    public ModelPylonPlates()
    {
        //load
        OBJModel model = ModelHelper.loadModel("ChaosPylonPlates");
        //retexture
        IModel pylonPlatesPassive = ModelHelper.retexture(model,"#skin.001","ChaosPylonPassive");
        IModel pylonPlatesBurner = ModelHelper.retexture(model,"#skin.001","ChaosPylonBurner");
        //activate
        pylonPlatesPassiveModel = ModelHelper.bake(pylonPlatesPassive);
        pylonPlatesBurnerModel = ModelHelper.bake(pylonPlatesBurner);
    }

    public void renderPylonPlates(int pylonType)
    {
        switch (pylonType)
        {
            case 1:
                renderModel(pylonPlatesBurnerModel);
                break;
            default:
                renderModel(pylonPlatesPassiveModel);
        }
    }

    private void renderModel(IFlexibleBakedModel model)
    {
        renderModel(model, -1);
    }

    private void renderModel(IFlexibleBakedModel model, int color)
    {
        ModelHelper.renderModel(model,color);
    }
}
