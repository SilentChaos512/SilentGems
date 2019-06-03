package net.silentchaos512.gems.client.render.models;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.silentchaos512.lib.client.model.ModelHelperSL;

public class ModelPylon {
    private IBakedModel pylonPassiveModel;
    private IBakedModel pylonBurnerModel;

    public ModelPylon() {
        // loading the model
        ResourceLocation resource = new ResourceLocation("silentgems:models/block/chaospylon.obj");
        OBJModel model = ModelHelperSL.loadModel(resource);

        // apply textures and orient correctly
        IModel pylonPassive = ModelHelperSL.retexture(model, "#chaospylon", "silentgems:blocks/chaospylonpassive");
        IModel pylonBurner = ModelHelperSL.retexture(model, "#chaospylon", "silentgems:blocks/chaospylonburner");

        // "turn on" the models
        pylonPassiveModel = ModelHelperSL.bake(pylonPassive);
        pylonBurnerModel = ModelHelperSL.bake(pylonBurner);
    }

    public void renderPylon(int pylonType) {
        switch (pylonType) {
            case 1:
                renderModel(pylonBurnerModel);
                break;
            default:
                renderModel(pylonPassiveModel);
        }
    }

    private void renderModel(IBakedModel model) {
        renderModel(model, -1);
    }

    private void renderModel(IBakedModel model, int color) {
        ModelHelperSL.renderModel(model, color);
    }
}
