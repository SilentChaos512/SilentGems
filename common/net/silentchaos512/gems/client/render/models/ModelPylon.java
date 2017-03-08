package net.silentchaos512.gems.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelPylon {

    private IBakedModel pylonPassiveModel;
    private IBakedModel pylonBurnerModel;


    public ModelPylon()
    {
        //loading the model
        OBJModel model = ModelHelper.loadModel("chaospylon");

        //apply textures and orient correctly
        IModel pylonPassive = ModelHelper.retexture(model,"#chaospylon","chaospylonpassive");
        IModel pylonBurner = ModelHelper.retexture(model,"#chaospylon","chaospylonburner");

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

    private void renderModel(IBakedModel model)
    {
        renderModel(model,-1);
    }

    private void renderModel(IBakedModel model, int color)
    {
        ModelHelper.renderModel(model,color);
    }
}