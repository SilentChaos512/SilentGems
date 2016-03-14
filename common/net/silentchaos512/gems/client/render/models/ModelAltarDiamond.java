package net.silentchaos512.gems.client.render.models;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelAltarDiamond {

    private IFlexibleBakedModel diamondModel;

    public ModelAltarDiamond()
    {
        //load
        OBJModel model = ModelHelper.loadModel("ChaosAltarDiamond");
        //texture
        IModel diamond = ModelHelper.retexture(model,"#skin","ChaosAltar");
        //bake
        diamondModel = ModelHelper.bake(diamond);

    }

    public void renderDiamond()
    {
        renderModel(diamondModel);
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
