package net.silentchaos512.gems.client.render.models;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelPylon {

    private IFlexibleBakedModel pylonPassiveModel;
    private IFlexibleBakedModel pylonBurnerModel;


    public ModelPylon()
    {
        //loading the model
        OBJModel model = ModelHelper.loadModel("ChaosPylon");

        //apply textures and orient correctly
        IModel pylonPassive = ModelHelper.retexture(model,"#ChaosPylon","ChaosPylonPassive");
        IModel pylonBurner = ModelHelper.retexture(model,"#ChaosPylon","ChaosPylonBurner");

        //"turn on" the models
        pylonPassiveModel = ModelHelper.bake(pylonPassive);
        pylonBurnerModel = ModelHelper.bake(pylonBurner);
    }

    public void renderPylon(int pylonType)
    {
        switch (pylonType)
        {
            case 1:
                renderModel(pylonBurnerModel);
                break;
            default:
                renderModel(pylonPassiveModel);
        }
    }

    private void renderModel(IFlexibleBakedModel model)
    {
        renderModel(model,-1);
    }

    private void renderModel(IFlexibleBakedModel model, int color)
    {
        ModelHelper.renderModel(model,color);
    }
}
